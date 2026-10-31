package com.drawupdateprobabilities.application.services.calculators.ifaces;

import com.drawupdateprobabilities.application.ports.driven.MatrixOccurrenceDatasourcePort;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.MatrixOccurrence;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.domain.constans.CalculatorTypeCode;
import com.drawupdateprobabilities.domain.constans.MathConstants;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TNumberOccurrences;
import com.drawupdateprobabilities.domain.types.TOccurrences;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class HigherWhenLastNDraws extends CalculatorBase {
    private final CalculatorTypeCode calculatorTypeCode;
    public final String typeCode;
    public final String typeDescription;
    private final Short drawsToCount;

    private final MatrixOccurrenceDatasourcePort matrixOccurrenceDatasourcePort;
    protected CalculatorTypeCode getCalculatorTypeCode() {
        return calculatorTypeCode;
    }

    protected Short getDrawsToCount() {
        return drawsToCount;
    }

    @Override
    protected String getProbabilityTypeDescription() {
        return typeDescription;
    }

    @Override
    protected String getProbabilityTypeCode() {
        return typeCode;
    }

    public HigherWhenLastNDraws(ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort, MatrixOccurrenceDatasourcePort matrixOccurrenceDatasourcePort, CalculatorTypeCode calculatorTypeCode) {
        super(probabilityTypeDatasourcePort);
        this.matrixOccurrenceDatasourcePort = matrixOccurrenceDatasourcePort;
        this.calculatorTypeCode = calculatorTypeCode;
        typeCode = calculatorTypeCode.getTypeCode();
        typeDescription = "Probability depending on the last " + calculatorTypeCode.getNumDraws().toString() + " draws";
        drawsToCount = calculatorTypeCode.getNumDraws();
    }


    @Override
    public ProbabilityTypeByDraw calculateAllFromDate(DrawList drawList, TDateInteger calculationDrawDate, Short drawTypeId) {
        Optional<MatrixOccurrence> matrixOccurrence = matrixOccurrenceDatasourcePort.getByDrawTypeIdAndDrawsToCount(drawTypeId, getDrawsToCount());
        if (matrixOccurrence.isPresent()) {
            // Get the occurrences matrix of all numbers in the last N draws
            TOccurrences occurrences = getOccurrencesByDateAndNumDraws(drawList, calculationDrawDate, getDrawsToCount());

            // Calculate probabilities by occurrences in the last n draws and the occurrences matrix
            List<BigDecimal> probabilitiesByNumber = getProbabilitiesByNumberNew(occurrences, matrixOccurrence.get());

            ProbabilityTypeByDraw result = ProbabilityTypeByDraw.builder()
                    .drawTypeId(drawTypeId)
                    .drawDate(calculationDrawDate)
                    .probabilityType(getProbabilityType())
                    .numberList(probabilitiesByNumber)
                    .build();
            return correctStatistics(result);
        }

        return null;
    }

    private List<BigDecimal> getProbabilitiesByNumberNew(TOccurrences numberOccurrences, MatrixOccurrence matrixOccurrence) {
        List<Integer> occurrencesByNumberAux = new ArrayList<>();
        long total = 0;
        for (byte cont=0; cont<49; cont++) {
            int numOccurrences = matrixOccurrence.getOccurrences().getOccurrenceValues().get(numberOccurrences.getOccurrenceValues().get(cont));
            total = total + numOccurrences;
            occurrencesByNumberAux.add(cont, numOccurrences);
        }

        BigDecimal totalAsBigDecimal = BigDecimal.valueOf(total);

        List<BigDecimal> result = new ArrayList<>();
        for (byte cont=0; cont<49; cont++) {
            result.add(BigDecimal.valueOf(occurrencesByNumberAux.get(cont)).divide(totalAsBigDecimal, MathConstants.DIVIDE_SCALE, MathConstants.ROUNDING_MODE));
        }

        return result;
    }

    private TOccurrences getOccurrencesByDateAndNumDraws(DrawList drawList, TDateInteger fromDrawDate, Short numDraws) {
        TDateInteger finalDrawDate = drawList.getPreviousDate(drawList.getDraw(fromDrawDate).getDrawDate());
        List<Integer> result = new ArrayList<>(Collections.nCopies(49, 0));
        for (byte cont=0; cont<49; cont++) {
            result.set(cont, findNumberOccurrencesLastNDraws((byte) (cont + 1), drawList, finalDrawDate, numDraws));
        }
        return new TNumberOccurrences(result);
    }

    private Integer findNumberOccurrencesLastNDraws(byte number, DrawList drawList, TDateInteger finalDrawDate, Short numDraws) {
        int result = 0;
        TDateInteger currentDrawDate = finalDrawDate;
        for (int cont = 0; cont < numDraws; cont++) {
            Draw draw = drawList.getDraw(currentDrawDate);
            if (draw.getNumberCombination().contains(number, true)) {
                result = result + (1 << cont);
            }
            currentDrawDate = drawList.getPreviousDate(currentDrawDate);
        }
        return result;
    }

}
