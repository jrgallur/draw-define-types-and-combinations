package com.drawupdateprobabilities.application.usecases;

import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeCombinationDatasourcePort;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeCombinationWeightDatasourcePort;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.application.services.ProbabilityTypeCombinationService;
import com.drawupdateprobabilities.domain.ProbabilityType;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombination;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationWeight;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CalculateCombinationsUseCase {
    private final ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort;
    private final ProbabilityTypeCombinationDatasourcePort probabilityTypeCombinationDatasourcePort;
    private final ProbabilityTypeCombinationWeightDatasourcePort probabilityTypeCombinationWeightDatasourcePort;
    private final ProbabilityTypeCombinationService probabilityTypeCombinationService;

    /**
     * Calculate all possible combinations following the rules of CombinationTypeService.calculateCombinationsFromTypeList and save the new ones into the database
     */
    public void calculateAndSaveNotExistentCombinations() {
        log.info("calculateAndSaveNotExistentCombinations - Inicio");
        List<ProbabilityTypeCombination> probabilityTypeCombinationList = getCalculatedCombinationTypeList();
        // Save all not existent combinations
        saveNotExistentProbabilityTypeCombination(probabilityTypeCombinationList);
        log.info("calculateAndSaveNotExistentCombinations - Fin");
    }

    // Calculate all the combinations and return them as a list
    private List<ProbabilityTypeCombination> getCalculatedCombinationTypeList() {
        List<ProbabilityType> probabilityTypeList = probabilityTypeDatasourcePort.findAll();

        List<ProbabilityTypeCombination> probabilityTypeCombinationList = probabilityTypeCombinationService.calculateCombinationsFromTypeList(probabilityTypeList);

        logProbabilityTypeCombinationList(probabilityTypeCombinationList);

        return probabilityTypeCombinationList;
    }

    private void logProbabilityTypeCombinationList(List<ProbabilityTypeCombination> probabilityTypeCombinationList) {
        log.info("#######");
        for (ProbabilityTypeCombination probabilityTypeCombination : probabilityTypeCombinationList) {
            StringBuilder result = new StringBuilder(probabilityTypeCombination.getCode()).append(": ");
            List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList = probabilityTypeCombination.getProbabilityTypeCombinationWeightList();
            for (ProbabilityTypeCombinationWeight probabilityTypeCombinationWeight : probabilityTypeCombinationWeightList) {
                result.append(probabilityTypeCombinationWeight.getProbabilityType().getCode()).append("-").append(probabilityTypeCombinationWeight.getPrettyWeight()).append(" ");
            }
            log.info(result.toString());
        }
        log.info("#######");
    }

    private void saveNotExistentProbabilityTypeCombination(List<ProbabilityTypeCombination> probabilityTypeCombinationList) {
        List<String> probabilityTypeCombinationReceivedCodeList = probabilityTypeCombinationList.stream().map(ProbabilityTypeCombination::getCode).toList();
        List<String> probabilityTypeCombinationExistingCodeList = probabilityTypeCombinationDatasourcePort.findByCodeList(probabilityTypeCombinationReceivedCodeList);
        if (probabilityTypeCombinationExistingCodeList == null || probabilityTypeCombinationExistingCodeList.isEmpty()) {
            saveProbabilityTypeCombinationList(probabilityTypeCombinationList);
        } else {
            Set<String> probabilityTypeCombinationExistingCodeSet = new HashSet<>(probabilityTypeCombinationExistingCodeList);
            List<ProbabilityTypeCombination> probabilityTypeCombinationUnexistingList = probabilityTypeCombinationList.stream().filter(
                    probabilityTypeCombination -> !probabilityTypeCombinationExistingCodeSet.contains(probabilityTypeCombination.getCode())
            ).toList();
            saveProbabilityTypeCombinationList(probabilityTypeCombinationUnexistingList);
        }
    }

    private void saveProbabilityTypeCombinationList(List<ProbabilityTypeCombination> probabilityTypeCombinationList) {
        for (ProbabilityTypeCombination probabilityTypeCombination : probabilityTypeCombinationList) {
            ProbabilityTypeCombination probabilityTypeCombinationSaved = probabilityTypeCombinationDatasourcePort.upsert(probabilityTypeCombination);
            setProbabilityTypeCombinationToWeightList(probabilityTypeCombination, probabilityTypeCombinationSaved);
            probabilityTypeCombinationWeightDatasourcePort.saveAll(probabilityTypeCombination.getProbabilityTypeCombinationWeightList());
        }
    }

    private void setProbabilityTypeCombinationToWeightList(ProbabilityTypeCombination probabilityTypeCombination, ProbabilityTypeCombination probabilityTypeCombinationSaved) {
        probabilityTypeCombination.getProbabilityTypeCombinationWeightList().forEach(probabilityTypeCombinationWeight -> probabilityTypeCombinationWeight.setProbabilityTypeCombinationId(probabilityTypeCombinationSaved.getId()));
    }
}
