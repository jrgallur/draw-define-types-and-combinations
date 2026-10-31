package com.drawupdateprobabilities.application.services.calculators.ifaces;

import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.ProbabilityType;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.domain.constans.MathConstants;
import com.drawupdateprobabilities.domain.types.TDateInteger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CalculatorBase {
    private ProbabilityType probabilityType;

    protected abstract String getProbabilityTypeCode();
    protected abstract String getProbabilityTypeDescription();

    private final ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort;

    protected CalculatorBase(ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort) {
        this.probabilityTypeDatasourcePort = probabilityTypeDatasourcePort;
    }

    public abstract ProbabilityTypeByDraw calculateAllFromDate(DrawList drawList, TDateInteger calculationDrawDate, Short drawTypeId);

    public ProbabilityType getProbabilityType() {
        if (probabilityType == null) {
            probabilityType = probabilityTypeDatasourcePort.getByCode(getProbabilityTypeCode());
            if (probabilityType == null) {
                probabilityType = ProbabilityType.builder().code(getProbabilityTypeCode()).description(getProbabilityTypeDescription()).active(true).build();
                probabilityTypeDatasourcePort.upsert(probabilityType);
                probabilityType = probabilityTypeDatasourcePort.getByCode(getProbabilityTypeCode());
            }
        }
        return probabilityType;
    }

    /**
     * Force to the sum of each number probabilities to be 1
     * To do that, it should substract one value from the numbers with lower values
     * I.E:
     * 1 = 0.0002, 2 = 0.0003, 3 = 0.0002, 4 = 0.1000, 5 = 0.8994 should become in 1 = 0.0001, 2 = 0.0003, 3 = 0.0002, 4 = 0.1000, 5 = 0.8994
     * 1 = 0.0002, 2 = 0.0003, 3 = 0.0002, 4 = 0.1000, 5 = 0.8998 should become in 1 = 0.0001, 2 = 0.0002, 3 = 0.0001, 4 = 0.1000, 5 = 0.8997
     */
    protected ProbabilityTypeByDraw correctStatistics(ProbabilityTypeByDraw probabilityTypeByDraw) {
        BigDecimal sum = probabilityTypeByDraw.getNumberList().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sum.compareTo(BigDecimal.ONE) > 0) {
            return correctGreaterOneStatistics(probabilityTypeByDraw, sum);
        } else if (sum.compareTo(BigDecimal.ONE) < 0) {
            return correctLowerOneStatistics(probabilityTypeByDraw, sum);
        }
        return probabilityTypeByDraw;
    }

    protected ProbabilityTypeByDraw correctGreaterOneStatistics(ProbabilityTypeByDraw probabilityTypeByDraw, final BigDecimal initialSum) {
        BigDecimal partialSum = new BigDecimal(initialSum.toString());
        BigDecimal unit = BigDecimal.ONE.movePointLeft(MathConstants.DIVIDE_SCALE);
        List<Integer> sortedIndex = getLowerToGreaterSortedIndex(probabilityTypeByDraw);
        while (partialSum.compareTo(BigDecimal.ONE) > 0) {
            BigDecimal difference = partialSum.subtract(BigDecimal.ONE);
            int numberOfValuesToChange = difference.divide(unit, MathConstants.DIVIDE_SCALE, MathConstants.ROUNDING_MODE).intValue();
            if (numberOfValuesToChange > 49) {
                numberOfValuesToChange = 49;
            }
            numberOfValuesToChange--;
            for (int indexesIndex = 0; indexesIndex <= numberOfValuesToChange; indexesIndex++) {
                int valuesIndex = sortedIndex.get(indexesIndex) + 1;

                probabilityTypeByDraw.setNumberProbability(valuesIndex, probabilityTypeByDraw.getNumberProbability(valuesIndex).subtract(unit));
            }
            partialSum = probabilityTypeByDraw.getNumberList().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return probabilityTypeByDraw;
    }

    protected ProbabilityTypeByDraw correctLowerOneStatistics(ProbabilityTypeByDraw probabilityTypeByDraw, final BigDecimal initialSum) {
        BigDecimal partialSum = new BigDecimal(initialSum.toString());
        BigDecimal unit = BigDecimal.ONE.movePointLeft(MathConstants.DIVIDE_SCALE);
        List<Integer> sortedIndex = getGreaterToLowerSortedIndex(probabilityTypeByDraw);
        while (partialSum.compareTo(BigDecimal.ONE) < 0) {
            BigDecimal difference = BigDecimal.ONE.subtract(partialSum);
            int numberOfValuesToChange = difference.divide(unit, MathConstants.DIVIDE_SCALE, MathConstants.ROUNDING_MODE).intValue();
            if (numberOfValuesToChange > 49) {
                numberOfValuesToChange = 49;
            }
            numberOfValuesToChange--;
            for (int indexesIndex = 0; indexesIndex <= numberOfValuesToChange; indexesIndex++) {
                int valuesIndex = sortedIndex.get(indexesIndex) + 1;

                probabilityTypeByDraw.setNumberProbability(valuesIndex, probabilityTypeByDraw.getNumberProbability(valuesIndex).add(unit));
            }
            partialSum = probabilityTypeByDraw.getNumberList().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return probabilityTypeByDraw;
    }

    protected List<Integer> getLowerToGreaterSortedIndex(ProbabilityTypeByDraw probabilityTypeByDraw) {
        return IntStream.range(0, probabilityTypeByDraw.getNumberList().size())  // Range generate indexes as int [0, 1, 2, ...]
                // Convert from int to Integer
                .boxed()
                .sorted(Comparator
                        // Sort bye the probability value
                        .comparing(probabilityTypeByDraw.getNumberList()::get)
                        // When the probability value is equal, then sort by index
                        .thenComparingInt(i -> i)
                )
                .toList();
    }

    protected List<Integer> getGreaterToLowerSortedIndex(ProbabilityTypeByDraw probabilityTypeByDraw) {
        List<Integer> result = IntStream.range(0, probabilityTypeByDraw.getNumberList().size())  // Range generate indexes as int [0, 1, 2, ...]
                // Convert from int to Integer
                .boxed()
                .sorted(Comparator
                        // Sort bye the probability value
                        .comparing(probabilityTypeByDraw.getNumberList()::get)
                        // When the probability value is equal, then sort by index
                        .thenComparingInt(i -> i)
                )
                .collect(Collectors.toList());
        Collections.reverse(result);

        return result;
    }

}
