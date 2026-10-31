package com.drawupdateprobabilities.application.services.occurrences.ifaces;

import com.drawupdateprobabilities.application.ports.driven.MatrixOccurrenceDatasourcePort;
import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.MatrixOccurrence;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TDrawOccurrences;
import com.drawupdateprobabilities.domain.types.TNumberList;
import com.drawupdateprobabilities.domain.types.TOccurrences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class ByLastNDraws {
    private final MatrixOccurrenceDatasourcePort matrixOccurrenceDatasourcePort;

    protected ByLastNDraws(MatrixOccurrenceDatasourcePort matrixOccurrenceDatasourcePort) {
        this.matrixOccurrenceDatasourcePort = matrixOccurrenceDatasourcePort;
    }

    public abstract Short getDrawsToCount();
    public MatrixOccurrence calculateFromDate(DrawList drawList, TDateInteger calculationDrawDate, Short drawTypeId) {
        return calculateOccurrencesMatrix(drawList, calculationDrawDate, drawTypeId);
    }
    public static Map<String, ByLastNDraws> getImplementations(ApplicationContext applicationContext) {
        return applicationContext.getBeansOfType(ByLastNDraws.class);
    }
    private MatrixOccurrence calculateOccurrencesMatrix(DrawList drawList, TDateInteger calculationDrawDate, Short drawTypeId) {
        // Initialize occurrences list
        TOccurrences occurrences = new TDrawOccurrences(getDrawsToCount());
        // Get the first draw
        Draw draw = drawList.getDraw(calculationDrawDate);
        // While is not null (we don't arrive at the end)
        while (draw!=null) {
            // Add to the occurrences list the current draw occurrences
            addDrawOccurrences(draw, drawList, occurrences);

            // Get next draw
            draw = drawList.getDraw(drawList.getNextDate(draw.getDrawDate()));
        }
        printOccurrences(occurrences);
        return MatrixOccurrence.builder()
                .drawTypeId(drawTypeId)
                .drawsToCount(getDrawsToCount())
                .untilDate(drawList.getLastDrawDate())
                .occurrences(occurrences)
                .build();
    }

    private void addDrawOccurrences(Draw currentDraw, DrawList drawList, TOccurrences occurrences) {
        TDateInteger currentDrawDate = drawList.getPreviousDate(currentDraw.getDrawDate());
        TNumberList numberList = currentDraw.getNumbersWC();

        for (int cont = 1; cont<8; cont++) {
            byte number = numberList.get(cont);

            Integer index = findNumberOccurrencesLastNDraws(number, drawList, currentDrawDate);
            occurrences.increment(index);
        }
    }

    private Integer findNumberOccurrencesLastNDraws(byte number, DrawList drawList, TDateInteger finalDrawDate) {
        int result = 0;
        TDateInteger currentDrawDate = finalDrawDate;
        for (int cont = 0; cont < getDrawsToCount(); cont++) {
            Draw draw = drawList.getDraw(currentDrawDate);
            if (draw.getNumberCombination().contains(number, true)) {
                result = result + (1 << cont);
            }
            currentDrawDate = drawList.getPreviousDate(currentDrawDate);
        }
        return result;
    }

    private void printOccurrences(TOccurrences occurrences) {
        log.info(occurrences.getNonZeroValues() + ": " + occurrences);
    }

    public void recalculateMatrixOccurrencesByDrawTypeIdAndByLastNDraws(Short drawTypeId, DrawList drawList, TDateInteger initialDrawDate) {
        Optional<MatrixOccurrence> matrixOccurrenceDB = matrixOccurrenceDatasourcePort.getByDrawTypeIdAndDrawsToCount(drawTypeId, getDrawsToCount());
        if (matrixOccurrenceDB.isPresent()) {
            if (datesAreDifferenteInMoreThanNDays(drawList.getLastDrawDate(), matrixOccurrenceDB.get().getUntilDate(), 30)) {
                matrixOccurrenceDatasourcePort.deleteByDrawTypeId(drawTypeId);
                calculateAndSaveMatrixOccurrencesByDrawTypeIdAndByLastNDraws(drawTypeId, drawList, initialDrawDate);
            }
        } else {
            calculateAndSaveMatrixOccurrencesByDrawTypeIdAndByLastNDraws(drawTypeId, drawList, initialDrawDate);
        }
    }

    private void calculateAndSaveMatrixOccurrencesByDrawTypeIdAndByLastNDraws(Short drawTypeId, DrawList drawList, TDateInteger initialDrawDate) {
        MatrixOccurrence matrixOccurrence = calculateFromDate(drawList, initialDrawDate, drawTypeId);
        matrixOccurrenceDatasourcePort.upsert(matrixOccurrence);
    }

    /**
     * Returns true if the firstDate is daysBetween days greater or lower than secondDate
     * @param firstDate The first date to compare
     * @param secondDate The second date to compare. It can be null
     * @param daysBetween Days to compare the first and the second date
     * @return True if the firstDate is daysBetween days greater or lower than secondDate
     */
    private boolean datesAreDifferenteInMoreThanNDays(TDateInteger firstDate, TDateInteger secondDate, int daysBetween) {
        // Recalculate if the matrix stored has more than 1 month
        if (secondDate==null) {
            return true;
        }
        if (secondDate.isGreaterThan(firstDate)) {
            TDateInteger auxDate = firstDate;
            firstDate = secondDate;
            secondDate = auxDate;
        }
        GregorianCalendar secondDateGC = secondDate.toGregorianCalendar();
        GregorianCalendar firstDateGC = firstDate.toGregorianCalendar();

        long differenceDays = (secondDateGC.getTimeInMillis() - firstDateGC.getTimeInMillis() ) / ( 1000 * 60 * 60 * 24) ;
        return differenceDays > daysBetween;
    }
}
