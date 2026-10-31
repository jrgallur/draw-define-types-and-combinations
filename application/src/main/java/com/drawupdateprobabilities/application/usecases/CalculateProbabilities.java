package com.drawupdateprobabilities.application.usecases;

import com.drawupdateprobabilities.application.ports.driven.DrawDatasourcePort;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeByDrawDatasourcePort;
import com.drawupdateprobabilities.application.services.calculators.*;
import com.drawupdateprobabilities.application.services.occurrences.ifaces.ByLastNDraws;
import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CalculateProbabilities {
    @Autowired
    private final ApplicationContext applicationContext;

    private final DrawDatasourcePort drawDatasourcePort;
    private final ProbabilityTypeByDrawDatasourcePort probabilityTypeByDrawDatasourcePort;


    private final HigherWhenLastAppearanceIsOlder calculatorHigherWhenLastAppearanceIsOlder;
    private final HigherWhenAppearsMoreTimesByLast06Months calculatorHigherWhenAppearsMoreTimesByLast06Months;
    private final HigherWhenAppearsMoreTimesByLast12Months calculatorHigherWhenAppearsMoreTimesByLast12Months;
    private final HigherWhenAppearsMoreTimesByLast24Months calculatorHigherWhenAppearsMoreTimesByLast24Months;
    private final HigherWhenAppearsMoreTimesByLast99Months calculatorHigherWhenAppearsMoreTimesByLast99Months;

    private final HigherWhenLast01Draws calculatorHigherWhenLast01Draws;
    private final HigherWhenLast02Draws calculatorHigherWhenLast02Draws;
    private final HigherWhenLast03Draws calculatorHigherWhenLast03Draws;
    private final HigherWhenLast04Draws calculatorHigherWhenLast04Draws;
    private final HigherWhenLast05Draws calculatorHigherWhenLast05Draws;
    private final HigherWhenLast06Draws calculatorHigherWhenLast06Draws;
    private final HigherWhenLast07Draws calculatorHigherWhenLast07Draws;
    private final HigherWhenLast08Draws calculatorHigherWhenLast08Draws;
    private final HigherWhenLast09Draws calculatorHigherWhenLast09Draws;
    private final HigherWhenLast10Draws calculatorHigherWhenLast10Draws;
    private final HigherWhenLast11Draws calculatorHigherWhenLast11Draws;
    private final HigherWhenLast12Draws calculatorHigherWhenLast12Draws;
    private final HigherWhenLast13Draws calculatorHigherWhenLast13Draws;
    private final HigherWhenLast14Draws calculatorHigherWhenLast14Draws;

    public void executeAll() {
        List<Short> drawTypeIdList = drawDatasourcePort.getDrawTypeIdList();
        for (Short drawTypeId : drawTypeIdList) {
            DrawList drawList = drawDatasourcePort.getDrawListByDrawTypeId(drawTypeId);
            TDateInteger initialDrawDate = drawList.findFirstDrawDateFromDate(new TDateInteger(19910101)); // Descartamos los anteriores como irrelevantes

            recalculateMatrixOccurrencesByDrawTypeId(drawList, initialDrawDate, drawTypeId);

            processProbabilityTypes(drawList, initialDrawDate, drawTypeId);
        }
    }

    private void processProbabilityTypes(DrawList drawList, TDateInteger initialDrawDate, Short drawTypeId) {
        higherWhenLastAppearanceIsOlder(drawList, initialDrawDate, drawTypeId);
        higherWhenAppearsMoreTimesByLastNMonths(drawList, initialDrawDate, drawTypeId);
        higherWhenLastNDraws(drawList, initialDrawDate, drawTypeId);
    }

    private void higherWhenLastAppearanceIsOlder(DrawList drawList, TDateInteger initialDrawDate, Short drawTypeId) {
        TDateInteger executionDrawDate = initialDrawDate;
        Set<String> probabilityTypeByDrawKeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLastAppearanceIsOlder.getProbabilityType().getId(), drawTypeId)
        );

        // While last draw date is greater than execution draw date
        TDateInteger finalDate = drawList.getLastDrawDate().add(TDateInteger.addTypes.DAY,1);
        while (executionDrawDate!=null && executionDrawDate.compareTo(finalDate)<0) {
            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLastAppearanceIsOlder.getProbabilityType().getId(), drawTypeId, probabilityTypeByDrawKeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLastAppearanceIsOlder.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }
            executionDrawDate = drawList.getNextDate(executionDrawDate);
        }
    }

    private Set<String> getKeysFromProbabilityTypeByDrawList(List<ProbabilityTypeByDraw> probabilityTypeByDrawExistentList) {
        return probabilityTypeByDrawExistentList.stream()
                .map(e -> e.getDrawDate() + "#" + e.getDrawTypeId() + "#" + e.getProbabilityType().getId())
                .collect(Collectors.toSet());
    }

    private boolean isProbabilityTypeByDrawMissing(TDateInteger drawDate, Short probabilityTypeId, Short drawTypeId, Set<String> keySet) {
        return !keySet.contains(drawDate + "#" + drawTypeId + "#" + probabilityTypeId);
    }

    private void higherWhenAppearsMoreTimesByLastNMonths(DrawList drawList, TDateInteger initialDrawDate, Short drawTypeId) {
        TDateInteger executionDrawDate = initialDrawDate;

        Set<String> probabilityTypeByDraw06KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenAppearsMoreTimesByLast06Months.getProbabilityType().getId(), drawTypeId)
        );
        Set<String> probabilityTypeByDraw12KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenAppearsMoreTimesByLast12Months.getProbabilityType().getId(), drawTypeId)
        );
        Set<String> probabilityTypeByDraw24KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenAppearsMoreTimesByLast24Months.getProbabilityType().getId(), drawTypeId)
        );
        Set<String> probabilityTypeByDraw99KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenAppearsMoreTimesByLast99Months.getProbabilityType().getId(), drawTypeId)
        );

        // While last draw date is greater than execution draw date
        TDateInteger finalDate = drawList.getLastDrawDate().add(TDateInteger.addTypes.DAY,1);
        while (executionDrawDate!=null && executionDrawDate.compareTo(finalDate)<0) {
            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenAppearsMoreTimesByLast06Months.getProbabilityType().getId(), drawTypeId, probabilityTypeByDraw06KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenAppearsMoreTimesByLast06Months.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenAppearsMoreTimesByLast12Months.getProbabilityType().getId(), drawTypeId, probabilityTypeByDraw12KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenAppearsMoreTimesByLast12Months.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenAppearsMoreTimesByLast24Months.getProbabilityType().getId(), drawTypeId, probabilityTypeByDraw24KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenAppearsMoreTimesByLast24Months.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenAppearsMoreTimesByLast99Months.getProbabilityType().getId(), drawTypeId, probabilityTypeByDraw99KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenAppearsMoreTimesByLast99Months.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }
            executionDrawDate = drawList.getNextDate(executionDrawDate);
        }
    }

    private void higherWhenLastNDraws(DrawList drawList, TDateInteger initialDrawDate, Short drawTypeId) {
        TDateInteger executionDrawDate = initialDrawDate;

        Set<String> probabilityTypeLast01KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast01Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast02KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast02Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast03KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast03Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast04KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast04Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast05KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast05Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast06KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast06Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast07KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast07Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast08KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast08Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast09KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast09Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast10KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast10Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast11KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast11Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast12KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast12Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast13KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast13Draws.getProbabilityType().getId(), drawTypeId)
        );

        Set<String> probabilityTypeLast14KeySet = getKeysFromProbabilityTypeByDrawList(
                probabilityTypeByDrawDatasourcePort.findByProbabilityTypeAndDrawTypeId(calculatorHigherWhenLast14Draws.getProbabilityType().getId(), drawTypeId)
        );
        // While last draw date is greater than execution draw date
        TDateInteger finalDate = drawList.getLastDrawDate().add(TDateInteger.addTypes.DAY,1);
        while (executionDrawDate!=null && executionDrawDate.compareTo(finalDate)<0) {
            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast01Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast01KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast01Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast02Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast02KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast02Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast03Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast03KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast03Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast04Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast04KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast04Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast05Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast05KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast05Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast06Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast06KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast06Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast07Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast07KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast07Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast08Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast08KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast08Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast09Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast09KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast09Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast10Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast10KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast10Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast11Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast11KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast11Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast12Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast12KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast12Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast13Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast13KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast13Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            // Only calculate if not exists
            if (isProbabilityTypeByDrawMissing(executionDrawDate, calculatorHigherWhenLast14Draws.getProbabilityType().getId(), drawTypeId, probabilityTypeLast14KeySet)) {
                ProbabilityTypeByDraw probabilityList = calculatorHigherWhenLast14Draws.calculateAllFromDate(drawList, executionDrawDate, drawTypeId);
                probabilityTypeByDrawDatasourcePort.upsert(probabilityList);
            }

            executionDrawDate = drawList.getNextDate(executionDrawDate);
        }
    }

    private void recalculateMatrixOccurrencesByDrawTypeId(DrawList drawList, TDateInteger initialDrawDate, Short drawTypeId) {
        Map<String, ByLastNDraws> byLastNDrawsImplementationList = ByLastNDraws.getImplementations(applicationContext);
        byLastNDrawsImplementationList.values().forEach(
            byLastNDrawsMethod -> byLastNDrawsMethod.recalculateMatrixOccurrencesByDrawTypeIdAndByLastNDraws(drawTypeId, drawList, initialDrawDate)
        );
    }
}
