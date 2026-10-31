package com.drawupdateprobabilities.services.calculators;

import com.drawupdateprobabilities.application.services.calculators.HigherWhenAppearsMoreTimesByLast06Months;
import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.domain.ProbabilityType;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TNumberList;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HigherWhenAppearsMoreTimesByLast06MonthsTest {
    private HigherWhenAppearsMoreTimesByLast06Months service;

    @Mock
    private ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort;

    @BeforeEach
    void setUp() {
        service = new HigherWhenAppearsMoreTimesByLast06Months(probabilityTypeDatasourcePort);
        when(probabilityTypeDatasourcePort.getByCode(any())).thenReturn(new ProbabilityType());
    }

    private BigDecimal sumProbabilitiesValuesFromProbabilityTypeByDraw(ProbabilityTypeByDraw list) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal probability : list.getNumberList()) {
            result = result.add(probability);
        }
        return result;
    }

    private boolean hasAllNumbersProbability(ProbabilityTypeByDraw list, BigDecimal probability, int ... numbers) {
        for (int number : numbers) {
            if (probability.compareTo(list.getNumberProbability(number))!=0) {
                return false;
            }
        }

        return true;
    }

    @Test
    void test_1() {
        List<Draw> draws = new ArrayList<>();
        draws.add(new Draw(TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));          // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-12-30", "yyyy-MM-dd"), new TNumberList(new byte[]{8, 9, 10, 11, 12, 13}), 14, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-29", "yyyy-MM-dd"), new TNumberList(new byte[]{15, 16, 17, 18, 19, 20}), 21, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-28", "yyyy-MM-dd"), new TNumberList(new byte[]{22, 23, 24, 25, 26, 27}), 28, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-27", "yyyy-MM-dd"), new TNumberList(new byte[]{29, 30, 31, 32, 33, 34}), 35, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-26", "yyyy-MM-dd"), new TNumberList(new byte[]{36, 37, 38, 39, 40, 41}), 42, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-25", "yyyy-MM-dd"), new TNumberList(new byte[]{43, 44, 45, 46, 47, 48}), 49, (short) 1));   // 0,99 / 7

        DrawList drawList = new DrawList(draws);

        ProbabilityTypeByDraw result = service.calculateAllFromDate(drawList, TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), (short) 1);
        assertEquals(TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), result.getDrawDate());
        assertEquals(0, BigDecimal.ONE.compareTo(sumProbabilitiesValuesFromProbabilityTypeByDraw(result)));

        assertEquals(0, BigDecimal.valueOf(0.0204081632).compareTo(result.getNumberProbability(1)));
        assertEquals(0, BigDecimal.valueOf(0.0204081632).compareTo(result.getNumberProbability(17)));
        assertEquals(0, BigDecimal.valueOf(0.0204081633).compareTo(result.getNumberProbability(18)));
        assertEquals(0, BigDecimal.valueOf(0.0204081633).compareTo(result.getNumberProbability(49)));
    }
    @Test
    void test_2() {
        List<Draw> draws = new ArrayList<>();
        draws.add(new Draw(TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));          // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-12-30", "yyyy-MM-dd"), new TNumberList(new byte[]{8, 9, 10, 11, 12, 13}), 14, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-29", "yyyy-MM-dd"), new TNumberList(new byte[]{15, 16, 17, 18, 19, 20}), 21, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-28", "yyyy-MM-dd"), new TNumberList(new byte[]{22, 23, 24, 25, 26, 27}), 28, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-27", "yyyy-MM-dd"), new TNumberList(new byte[]{29, 30, 31, 32, 33, 34}), 35, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-26", "yyyy-MM-dd"), new TNumberList(new byte[]{36, 37, 38, 39, 40, 41}), 42, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-25", "yyyy-MM-dd"), new TNumberList(new byte[]{43, 44, 45, 46, 47, 48}), 49, (short) 1));   // 0,99 / 7

        draws.add(new Draw(TDateInteger.valueOf("2010-11-28", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));          // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-11-27", "yyyy-MM-dd"), new TNumberList(new byte[]{8, 9, 10, 11, 12, 13}), 14, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-26", "yyyy-MM-dd"), new TNumberList(new byte[]{15, 16, 17, 18, 19, 20}), 21, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-25", "yyyy-MM-dd"), new TNumberList(new byte[]{22, 23, 24, 25, 26, 27}), 28, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-24", "yyyy-MM-dd"), new TNumberList(new byte[]{29, 30, 31, 32, 33, 34}), 35, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-23", "yyyy-MM-dd"), new TNumberList(new byte[]{36, 37, 38, 39, 40, 41}), 42, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-22", "yyyy-MM-dd"), new TNumberList(new byte[]{43, 44, 45, 46, 47, 48}), 49, (short) 1));   // 0,99 / 7

        DrawList drawList = new DrawList(draws);

        ProbabilityTypeByDraw result = service.calculateAllFromDate(drawList, TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), (short) 1);
        assertEquals(TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), result.getDrawDate());
        assertEquals(0, BigDecimal.ONE.compareTo(sumProbabilitiesValuesFromProbabilityTypeByDraw(result)));

        assertEquals(0, BigDecimal.valueOf(0.0204081632).compareTo(result.getNumberProbability(1)));
        assertEquals(0, BigDecimal.valueOf(0.0204081632).compareTo(result.getNumberProbability(17)));
        assertEquals(0, BigDecimal.valueOf(0.0204081633).compareTo(result.getNumberProbability(18)));
        assertEquals(0, BigDecimal.valueOf(0.0204081633).compareTo(result.getNumberProbability(49)));
    }

    @Test
    void test_3() {
        List<Draw> draws = new ArrayList<>();
        draws.add(new Draw(TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));          // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-12-30", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 9, 10, 11, 12, 13}), 14, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-29", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 16, 17, 18, 19, 20}), 21, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-28", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 23, 24, 25, 26, 27}), 28, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-27", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 30, 31, 32, 33, 34}), 35, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-26", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 37, 38, 39, 40, 41}), 42, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-12-25", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 44, 45, 46, 47, 48}), 49, (short) 1));   // 0,99 / 7

        draws.add(new Draw(TDateInteger.valueOf("2010-11-28", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));          // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-11-27", "yyyy-MM-dd"), new TNumberList(new byte[]{8, 2, 10, 11, 12, 13}), 14, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-26", "yyyy-MM-dd"), new TNumberList(new byte[]{15, 2, 17, 18, 19, 20}), 21, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-25", "yyyy-MM-dd"), new TNumberList(new byte[]{22, 2, 24, 25, 26, 27}), 28, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-24", "yyyy-MM-dd"), new TNumberList(new byte[]{29, 2, 31, 32, 33, 34}), 35, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-23", "yyyy-MM-dd"), new TNumberList(new byte[]{36, 2, 38, 39, 40, 41}), 42, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-11-22", "yyyy-MM-dd"), new TNumberList(new byte[]{43, 2, 45, 46, 47, 48}), 49, (short) 1));   // 0,99 / 7

        DrawList drawList = new DrawList(draws);

        ProbabilityTypeByDraw result = service.calculateAllFromDate(drawList, TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), (short) 1);
        assertEquals(TDateInteger.valueOf("2010-12-31", "yyyy-MM-dd"), result.getDrawDate());
        assertEquals(0, BigDecimal.ONE.compareTo(sumProbabilitiesValuesFromProbabilityTypeByDraw(result)));

        assertTrue(hasAllNumbersProbability(result, BigDecimal.valueOf(0.0816326531), 1, 2));
        assertTrue(hasAllNumbersProbability(result, BigDecimal.valueOf(0.0102040815), 8, 9, 15, 16, 22, 23, 29, 30, 36));
        assertTrue(hasAllNumbersProbability(result, BigDecimal.valueOf(0.0102040816), 37, 43, 44));
        assertTrue(hasAllNumbersProbability(result, BigDecimal.valueOf(0.0204081633), 3, 4, 5, 6, 7, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 38, 39, 40, 41, 42, 45, 46, 47 ,48, 49));

    }
}
