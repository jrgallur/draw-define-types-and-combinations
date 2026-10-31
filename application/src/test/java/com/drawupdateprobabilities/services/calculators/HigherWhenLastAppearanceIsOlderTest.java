package com.drawupdateprobabilities.services.calculators;

import com.drawupdateprobabilities.application.services.calculators.HigherWhenLastAppearanceIsOlder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HigherWhenLastAppearanceIsOlderTest {
    private HigherWhenLastAppearanceIsOlder service;

    @Mock
    private ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort;

    @BeforeEach
    void setUp() {
        service = new HigherWhenLastAppearanceIsOlder(probabilityTypeDatasourcePort);
        when(probabilityTypeDatasourcePort.getByCode(any())).thenReturn(new ProbabilityType());
    }

    private BigDecimal sumNumberProbabilityList(ProbabilityTypeByDraw list) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal probability : list.getNumberList()) {
            result = result.add(probability);
        }
        return result;
    }

    @Test
    void test_1() {
        List<Draw> draws = new ArrayList<>();
        draws.add(new Draw(TDateInteger.valueOf("2010-12-01", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));          // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-11-01", "yyyy-MM-dd"), new TNumberList(new byte[]{8, 9, 10, 11, 12, 13}), 14, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-10-01", "yyyy-MM-dd"), new TNumberList(new byte[]{15, 16, 17, 18, 19, 20}), 21, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-09-01", "yyyy-MM-dd"), new TNumberList(new byte[]{22, 23, 24, 25, 26, 27}), 28, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-08-01", "yyyy-MM-dd"), new TNumberList(new byte[]{29, 30, 31, 32, 33, 34}), 35, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-07-01", "yyyy-MM-dd"), new TNumberList(new byte[]{36, 37, 38, 39, 40, 41}), 42, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-06-01", "yyyy-MM-dd"), new TNumberList(new byte[]{43, 44, 45, 46, 47, 48}), 49, (short) 1));   // 0,99 / 7

        DrawList drawList = new DrawList(draws);

        ProbabilityTypeByDraw result = service.calculateAllFromDate(drawList, TDateInteger.valueOf("2010-12-01", "yyyy-MM-dd"), (short) 1);
        assertEquals(TDateInteger.valueOf("2010-12-01", "yyyy-MM-dd"), result.getDrawDate());
        assertEquals(0, BigDecimal.ONE.compareTo(sumNumberProbabilityList(result)));

        // Ese valor se obtiene como 0.01 / (0.01 + [...] + 0.99) / 7
        assertEquals(0, BigDecimal.valueOf(0.0004024145).compareTo(result.getNumberProbability(1)));
        assertEquals(0, BigDecimal.valueOf(0.0004024145).compareTo(result.getNumberProbability(2)));
        assertEquals(0, BigDecimal.valueOf(0.0071093226).compareTo(result.getNumberProbability(8)));
        assertEquals(0, BigDecimal.valueOf(0.0138162307).compareTo(result.getNumberProbability(15)));
        assertEquals(0, BigDecimal.valueOf(0.0205231388).compareTo(result.getNumberProbability(22)));
        assertEquals(0, BigDecimal.valueOf(0.0272300469).compareTo(result.getNumberProbability(29)));
        assertEquals(0, BigDecimal.valueOf(0.0339369551).compareTo(result.getNumberProbability(36)));
        assertEquals(0, BigDecimal.valueOf(0.0398390343).compareTo(result.getNumberProbability(48)));
        assertEquals(0, BigDecimal.valueOf(0.0398390343).compareTo(result.getNumberProbability(49)));
    }

    @Test
    void test_2() {
        List<Draw> draws = new ArrayList<>();
        draws.add(new Draw(TDateInteger.valueOf("2010-12-01", "yyyy-MM-dd"), new TNumberList(new byte[]{1, 2, 3, 4, 5, 6}), 7, (short) 1));        // 0,01 / 7
        draws.add(new Draw(TDateInteger.valueOf("2010-11-01", "yyyy-MM-dd"), new TNumberList(new byte[]{8, 9, 10, 11, 12, 13}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-10-01", "yyyy-MM-dd"), new TNumberList(new byte[]{14, 15, 16, 17, 18, 19}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-09-01", "yyyy-MM-dd"), new TNumberList(new byte[]{20, 21, 22, 23, 24, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-08-01", "yyyy-MM-dd"), new TNumberList(new byte[]{25, 26, 27, 28, 29, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-07-01", "yyyy-MM-dd"), new TNumberList(new byte[]{30, 31, 32, 33, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-06-01", "yyyy-MM-dd"), new TNumberList(new byte[]{34, 35, 36, 37, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-05-01", "yyyy-MM-dd"), new TNumberList(new byte[]{38, 39, 40, 4, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-04-01", "yyyy-MM-dd"), new TNumberList(new byte[]{41, 42, 43, 4, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-03-01", "yyyy-MM-dd"), new TNumberList(new byte[]{44, 45, 3, 4, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-02-01", "yyyy-MM-dd"), new TNumberList(new byte[]{46, 47, 3, 4, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2010-01-01", "yyyy-MM-dd"), new TNumberList(new byte[]{48, 2, 3, 4, 5, 6}), 7, (short) 1));
        draws.add(new Draw(TDateInteger.valueOf("2009-12-01", "yyyy-MM-dd"), new TNumberList(new byte[]{49, 2, 3, 4, 5, 6}), 7, (short) 1));      // 0,99 / 1

        DrawList drawList = new DrawList(draws);

        ProbabilityTypeByDraw result = service.calculateAllFromDate(drawList, TDateInteger.valueOf("2010-12-01", "yyyy-MM-dd"), (short) 1);
        assertEquals(0, BigDecimal.ONE.compareTo(sumNumberProbabilityList(result)));

        // Ese valor se obtiene como 0.01 / (0.01 + [...] + 0.99) / 7
        assertEquals(0, BigDecimal.valueOf(0.0005751533).compareTo(result.getNumberProbability(1)));
        assertEquals(0, BigDecimal.valueOf(0.0005751534).compareTo(result.getNumberProbability(2)));
        assertEquals(0, BigDecimal.valueOf(0.0005751534).compareTo(result.getNumberProbability(7)));
        assertEquals(0, BigDecimal.valueOf(0.0053680982).compareTo(result.getNumberProbability(8)));
        assertEquals(0, BigDecimal.valueOf(0.0053680982).compareTo(result.getNumberProbability(13)));
        assertEquals(0, BigDecimal.valueOf(0.0101610429).compareTo(result.getNumberProbability(14)));
        assertEquals(0, BigDecimal.valueOf(0.0101610429).compareTo(result.getNumberProbability(19)));
        assertEquals(0, BigDecimal.valueOf(0.0149539877).compareTo(result.getNumberProbability(20)));
        assertEquals(0, BigDecimal.valueOf(0.0149539877).compareTo(result.getNumberProbability(24)));
        assertEquals(0, BigDecimal.valueOf(0.0197469325).compareTo(result.getNumberProbability(25)));
        assertEquals(0, BigDecimal.valueOf(0.0197469325).compareTo(result.getNumberProbability(29)));
        assertEquals(0, BigDecimal.valueOf(0.0245398773).compareTo(result.getNumberProbability(33)));
        assertEquals(0, BigDecimal.valueOf(0.0293328221).compareTo(result.getNumberProbability(34)));
        assertEquals(0, BigDecimal.valueOf(0.0293328221).compareTo(result.getNumberProbability(37)));
        assertEquals(0, BigDecimal.valueOf(0.0341257669).compareTo(result.getNumberProbability(38)));
        assertEquals(0, BigDecimal.valueOf(0.0341257669).compareTo(result.getNumberProbability(40)));
        assertEquals(0, BigDecimal.valueOf(0.0389187117).compareTo(result.getNumberProbability(41)));
        assertEquals(0, BigDecimal.valueOf(0.0389187117).compareTo(result.getNumberProbability(43)));
        assertEquals(0, BigDecimal.valueOf(0.0437116564).compareTo(result.getNumberProbability(44)));
        assertEquals(0, BigDecimal.valueOf(0.0437116564).compareTo(result.getNumberProbability(45)));
        assertEquals(0, BigDecimal.valueOf(0.0485046012).compareTo(result.getNumberProbability(46)));
        assertEquals(0, BigDecimal.valueOf(0.0485046012).compareTo(result.getNumberProbability(47)));
        assertEquals(0, BigDecimal.valueOf(0.0532975460).compareTo(result.getNumberProbability(48)));
        assertEquals(0, BigDecimal.valueOf(0.0569401841).compareTo(result.getNumberProbability(49)));
    }
}
