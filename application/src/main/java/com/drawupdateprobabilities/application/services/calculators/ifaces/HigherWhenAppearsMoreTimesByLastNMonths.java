package com.drawupdateprobabilities.application.services.calculators.ifaces;

import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.domain.constans.MathConstants;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;

import java.math.BigDecimal;

import static com.drawupdateprobabilities.domain.types.TDateInteger.addTypes.MONTH;


public abstract class HigherWhenAppearsMoreTimesByLastNMonths extends CalculatorBase {
    public static final String TYPE_CODE = "0002";
    public static final String TYPE_DESCRIPTION = "Higher probability when the number appears more times during the last N months";

    @Override
    protected String getProbabilityTypeCode() {
        return TYPE_CODE;
    }

    @Override
    protected String getProbabilityTypeDescription() {
        return TYPE_DESCRIPTION;
    }

    protected HigherWhenAppearsMoreTimesByLastNMonths(ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort) {
        super(probabilityTypeDatasourcePort);
    }

    public abstract int getNumberOfMonthsToCalculate();

    @Override
    public ProbabilityTypeByDraw calculateAllFromDate(DrawList drawList, TDateInteger calculationDrawDate, Short drawTypeId) {
        int[] numberAppearanceArray = countNumberOccurrences(drawList, calculationDrawDate, getNumberOfMonthsToCalculate());

        ProbabilityTypeByDraw result = getProbabilityTypeByDraw(numberAppearanceArray, calculationDrawDate, drawTypeId);

        return correctStatistics(result);
    }

    // Returns the occurences of the numbers in the las N months from calculationDrawDate
    private int[] countNumberOccurrences(DrawList drawList, TDateInteger calculationDrawDate, int monthNumber) {
        int[] numberAppearanceArray = new int[49];

        TDateInteger currentDrawDate = calculationDrawDate;
        TDateInteger endDrawDate = calculationDrawDate.copy().add(MONTH, -monthNumber);

        while (currentDrawDate!=null && drawList.getFirstDrawDate().isLowerOrEqualsThan(currentDrawDate) && currentDrawDate.isGreaterOrEqualsThan(endDrawDate)) {
            for (int index = 1; index < 7; index++) {
                numberAppearanceArray[drawList.getDraw(currentDrawDate).getNumber(index)-1]++;
            }
            numberAppearanceArray[drawList.getDraw(currentDrawDate).getComplementary()-1]++;

            currentDrawDate = drawList.getPreviousDate(currentDrawDate);
        }

        return numberAppearanceArray;
    }

    protected ProbabilityTypeByDraw getProbabilityTypeByDraw(int[] numberAppearanceArray, TDateInteger calculateDrawDate, Short drawTypeId) {
        ProbabilityTypeByDraw probabilityTypeByDraw = ProbabilityTypeByDraw.builder()
                .drawDate(calculateDrawDate)
                .drawTypeId(drawTypeId)
                .build();
        BigDecimal totalPercentagesNew = BigDecimal.valueOf(countAllAppearanceNumbers(numberAppearanceArray));

        for (int index = 0; index < 49; index++) {
            probabilityTypeByDraw.setNumberProbability(index + 1,
                    BigDecimal.valueOf(numberAppearanceArray[index])
                            .divide(totalPercentagesNew, MathConstants.DIVIDE_SCALE, MathConstants.ROUNDING_MODE)
            );
        }

        probabilityTypeByDraw.setProbabilityType(getProbabilityType());

        return probabilityTypeByDraw;
    }

    protected long countAllAppearanceNumbers(int[] numberAppearanceArray) {
        long result = 0;
        for (byte number = 0; number<=48; number++) {
            result+=numberAppearanceArray[number];
        }
        return result;
    }

}
