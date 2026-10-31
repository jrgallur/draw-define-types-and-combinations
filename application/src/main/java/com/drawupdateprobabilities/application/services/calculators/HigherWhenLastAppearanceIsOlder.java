package com.drawupdateprobabilities.application.services.calculators;

import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.domain.constans.CalculatorTypeCode;
import com.drawupdateprobabilities.domain.constans.MathConstants;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TDrawsNumberAndFirstDrawDate;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.application.services.calculators.ifaces.CalculatorBase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class HigherWhenLastAppearanceIsOlder extends CalculatorBase {
    public static final String TYPE_CODE = CalculatorTypeCode.LAST_APPEARANCE_IS_OLDER.getTypeCode();
    public static final String TYPE_DESCRIPTION = "Higher probability when last appearance is older";

    public HigherWhenLastAppearanceIsOlder(ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort) {
        super(probabilityTypeDatasourcePort);
    }

    @Override
    protected String getProbabilityTypeCode() {
        return TYPE_CODE;
    }

    @Override
    protected String getProbabilityTypeDescription() {
        return TYPE_DESCRIPTION;
    }

    @Override
    public ProbabilityTypeByDraw calculateAllFromDate(DrawList drawList, TDateInteger calculationDrawDate, Short drawTypeId) {
        TDrawsNumberAndFirstDrawDate drawsNumberAndFirstDrawDate = getDrawsCountContainingAllNumbers(drawList, calculationDrawDate);

        byte[] numberAppearance = fillNumberAppearance(drawList, drawsNumberAndFirstDrawDate, calculationDrawDate);

        ProbabilityTypeByDraw result = getProbabilityTypeByDraw(numberAppearance, drawsNumberAndFirstDrawDate, calculationDrawDate, drawTypeId);

        return correctStatistics(result);
    }

    private ProbabilityTypeByDraw getProbabilityTypeByDraw(byte[] numberAppearanceArray, TDrawsNumberAndFirstDrawDate drawsNumberAndFirstDrawDate, TDateInteger calculateDrawDate, Short drawTypeId) {
        byte drawsNumber = drawsNumberAndFirstDrawDate.getDrawsNumber();

        ProbabilityTypeByDraw probabilityTypeByDraw = ProbabilityTypeByDraw.builder()
                .drawDate(calculateDrawDate)
                .drawTypeId(drawTypeId)
                .build();

        BigDecimal[] probabilitiesOnlyByAppearanceOrder = getProbabilitiesOnlyByAppearanceOrder(drawsNumber);
        BigDecimal[] countNumbersByAppearance = getCountNumbersByAppearance(drawsNumber, numberAppearanceArray);
        BigDecimal[] probabilitiesByAppearanceOrderAndNumbersOccurrence = getProbabilitiesByAppearanceOrderAndNumbersOccurrence(probabilitiesOnlyByAppearanceOrder, countNumbersByAppearance,drawsNumber);
        BigDecimal totalPercentagesNew = sumPercentagesByAppearance(drawsNumber, probabilitiesByAppearanceOrderAndNumbersOccurrence);

        for (int index = 0; index < 49; index++) {
            probabilityTypeByDraw.setNumberProbability(index + 1,
                    probabilitiesOnlyByAppearanceOrder[numberAppearanceArray[index]-1]
                            .divide(totalPercentagesNew, MathConstants.DIVIDE_SCALE, MathConstants.ROUNDING_MODE)
            );
        }

        probabilityTypeByDraw.setProbabilityType(getProbabilityType());

        return probabilityTypeByDraw;
    }

    private BigDecimal[] getProbabilitiesByAppearanceOrderAndNumbersOccurrence(BigDecimal[] probabilitiesOnlyByAppearanceOrder, BigDecimal[] countNumbersByAppearance, byte drawsNumber) {
        BigDecimal[] result = new BigDecimal[drawsNumber];
        for (int index = 0; index < drawsNumber; index++) {
            result[index] = probabilitiesOnlyByAppearanceOrder[index].multiply(countNumbersByAppearance[index]);
        }
        return result;
    }

    private BigDecimal[] getProbabilitiesOnlyByAppearanceOrder(byte drawsNumber) {
        BigDecimal[] percentagesByAppearance = new BigDecimal[drawsNumber];
        percentagesByAppearance[0] = BigDecimal.valueOf(0.01);
        percentagesByAppearance[drawsNumber - 1] = BigDecimal.valueOf(0.99);
        BigDecimal drawsNumberLessOne = new BigDecimal(drawsNumber - 1);
        for (int index = 1; index < drawsNumber - 1; index++) {
            percentagesByAppearance[index] = percentagesByAppearance[index-1].add(BigDecimal.ONE.divide(drawsNumberLessOne, MathConstants.DIVIDE_SCALE, MathConstants.ROUNDING_MODE));
        }
        return percentagesByAppearance;
    }

    private BigDecimal sumPercentagesByAppearance(byte drawsNumber, BigDecimal[] percentagesByAppearance) {
        BigDecimal totalPercentages = BigDecimal.ZERO;
        for (int index = 0; index < drawsNumber; index++) {
            totalPercentages = totalPercentages.add(percentagesByAppearance[index]);
        }
        return totalPercentages;
    }

    /**
     * Get an array where each index has the count of numbers who appear in that draw
     * @param drawsNumber The total elements of the array
     * @param numberAppearanceArray The array who contains the appearance of the numbers
     * @return An array where each index has the count of numbers who appear in that draw
     */
    private BigDecimal[] getCountNumbersByAppearance(byte drawsNumber, byte[] numberAppearanceArray) {
        BigDecimal[] countNumbersByAppearance = new BigDecimal[drawsNumber];
        for (int index = 0; index < drawsNumber; index++) {
            countNumbersByAppearance[index] = countNumberByApperance(index+1, numberAppearanceArray);
        }
        return countNumbersByAppearance;
    }

    /**
     * Counts the numbers from numberApperanceArray where have the apperanceIndex value
     * @param appearanceIndex The value to match
     * @param numberAppearanceArray The array who contains the appearanceIndex
     * @return The numbers from numberApperanceArray where have the apperanceIndex value
     */
    private BigDecimal countNumberByApperance(int appearanceIndex, byte[] numberAppearanceArray) {
        BigDecimal total = BigDecimal.ZERO;
        for (int index = 0; index < 49; index++) {
            if (numberAppearanceArray[index]==appearanceIndex) {
                total = total.add(BigDecimal.ONE);
            }
        }
        return total;
    }

    private TDrawsNumberAndFirstDrawDate getDrawsCountContainingAllNumbers(DrawList drawList, TDateInteger calculationDrawDate) {
        byte drawsNumber = 1;
        byte[] numberAppearance = new byte[49];
        Arrays.fill(numberAppearance, (byte) 0);
        TDateInteger currentDrawDate = new TDateInteger(calculationDrawDate.toInteger());
        boolean allNumbersAppeared = false;

        while (!allNumbersAppeared) {
            byte[] currentDrawNumbers = drawList.getDraw(currentDrawDate).getNumbersWC().getAllNumbers();
            updateNumbersAppearance(numberAppearance, currentDrawNumbers, (byte) 1);

            allNumbersAppeared = allNumbersAppeared(numberAppearance);
            if (!allNumbersAppeared) {
                currentDrawDate = drawList.getPreviousDate(currentDrawDate);
                drawsNumber++;
            }
        }

        return TDrawsNumberAndFirstDrawDate.builder().drawsNumber(drawsNumber).firstDrawDate(currentDrawDate).build();
    }

    private byte[] fillNumberAppearance(DrawList drawList, TDrawsNumberAndFirstDrawDate drawsNumberAndFirstDrawDate, TDateInteger calculationDrawDate) {
        TDateInteger localCurrentDrawDate = new TDateInteger(drawsNumberAndFirstDrawDate.getFirstDrawDate().toInteger());
        TDateInteger endDrawDate = new TDateInteger(calculationDrawDate.toInteger());
        byte[] numberAppearance = new byte[49];
        byte occurrenceOfNumber = drawsNumberAndFirstDrawDate.getDrawsNumber();
        Arrays.fill(numberAppearance, occurrenceOfNumber);
        while (localCurrentDrawDate!=null && localCurrentDrawDate.toInteger()<=endDrawDate.toInteger()) {
            byte[] currentDrawNumbers = drawList.getDraw(localCurrentDrawDate).getNumbersWC().getAllNumbers();
            updateNumbersAppearance(numberAppearance, currentDrawNumbers, occurrenceOfNumber);
            localCurrentDrawDate = drawList.getNextDate(localCurrentDrawDate);
            occurrenceOfNumber--;
        }

        return numberAppearance;
    }

    private boolean allNumbersAppeared(byte[] numberAppearance) {
        for (int index = 0; index < 49; index ++) {
            if (numberAppearance[index]==0) {
                return false;
            }
        }
        return true;
    }

    private void updateNumbersAppearance(byte[] numberAppearance, byte[] currentDrawNumbers, byte value) {
        for (int index = 0; index < 7; index ++) {
            numberAppearance[currentDrawNumbers[index] - 1] = value;
        }
    }

}
