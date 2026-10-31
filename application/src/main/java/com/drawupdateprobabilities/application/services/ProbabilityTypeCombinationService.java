package com.drawupdateprobabilities.application.services;


import com.drawupdateprobabilities.domain.ProbabilityType;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombination;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationWeight;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@NoArgsConstructor
public class ProbabilityTypeCombinationService {
    private static final BigDecimal STEP = BigDecimal.valueOf(0.2);

    public List<ProbabilityTypeCombination> calculateCombinationsFromTypeList(List<ProbabilityType> probabilityTypeList) {
        return new ArrayList<>(getCombinationsOrderN(probabilityTypeList));
    }

    /**
     * Retrieves combinations of different types with the next conditions:
     * The sum of all of them must be 1
     * The vales must be STEP multiples (0, 0.1, 0.2, ..., 0.9, 1.0)
     */
    public List<ProbabilityTypeCombination> getCombinationsOrderN(List<ProbabilityType> types) {
        List<ProbabilityTypeCombination> result = new ArrayList<>();

        generateCombinations(types, new ArrayList<>(), BigDecimal.ZERO, STEP, result);
        setCombinationsCodes(result);
        return result;
    }

    private void setCombinationsCodes(List<ProbabilityTypeCombination> probabilityTypeCombinationList) {
        for (ProbabilityTypeCombination probabilityTypeCombination : probabilityTypeCombinationList) {
            probabilityTypeCombination.setProbabilityTypeCombinationWeightList(deleteEmptyProbabilityTypeWeightListItems(probabilityTypeCombination.getProbabilityTypeCombinationWeightList()));
            sortProbabilityTypeWeightList(probabilityTypeCombination.getProbabilityTypeCombinationWeightList());
            probabilityTypeCombination.setCode(getCodeFromCombinationTypeWeightList(probabilityTypeCombination.getProbabilityTypeCombinationWeightList()));
        }
    }

    private void sortProbabilityTypeWeightList(List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList) {
        probabilityTypeCombinationWeightList.sort(Comparator.comparing(p -> p.getProbabilityType().getCode()));
    }

    /**
     * Only save if the weight is greater than zero
     * @param probabilityTypeCombinationWeightList The list with the weights to evalate
     * @return The list without the weights if they are zero
     */
    private List<ProbabilityTypeCombinationWeight> deleteEmptyProbabilityTypeWeightListItems(List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList) {
        return new ArrayList<>(probabilityTypeCombinationWeightList.stream().filter(probabilityTypeCombinationWeight -> probabilityTypeCombinationWeight.getWeight().compareTo(BigDecimal.ZERO) > 0).toList());
    }

    private String getCodeFromCombinationTypeWeightList(List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList) {
        StringBuilder result = new StringBuilder();
        for (ProbabilityTypeCombinationWeight probabilityTypeCombinationWeight : probabilityTypeCombinationWeightList) {
            if (!result.isEmpty()) {
                result.append("#");
            }
            result.append(probabilityTypeCombinationWeight.getProbabilityType().getCode()).append("[").append(probabilityTypeCombinationWeight.getPrettyWeight()).append("]");
        }

        return result.toString();
    }

    private void generateCombinations(List<ProbabilityType> types,
                                      List<BigDecimal> currentWeights,
                                      BigDecimal currentSum,
                                      BigDecimal step,
                                      List<ProbabilityTypeCombination> result) {
        int index = currentWeights.size();
        int size = types.size();

        if (index == size - 1) {
            // Calcular el último peso
            BigDecimal lastWeight = BigDecimal.ONE.subtract(currentSum);

            // Verificar que esté en rango válido y no sea igual a 1
            if (isValidWeight(lastWeight)) {
                List<BigDecimal> finalWeights = new ArrayList<>(currentWeights);
                finalWeights.add(lastWeight);

                ProbabilityTypeCombination combination = ProbabilityTypeCombination.builder().build();
                for (int i = 0; i < size; i++) {
                    combination.addProbabilityTypeWeight(
                            ProbabilityTypeCombinationWeight.builder()
                                    .probabilityType(types.get(i))
                                    .weight(finalWeights.get(i))
                                    .build()
                    );
                }
                result.add(combination);
            }
        } else {
            BigDecimal weight = BigDecimal.ZERO;
            while (weight.compareTo(BigDecimal.ONE) <= 0) {
                BigDecimal newSum = currentSum.add(weight);

                // Solo continuar si la suma parcial es menor que 1
                if (newSum.compareTo(BigDecimal.ONE) <= 0) {
                    List<BigDecimal> newWeights = new ArrayList<>(currentWeights);
                    newWeights.add(weight);
                    generateCombinations(types, newWeights, newSum, step, result);
                }

                weight = weight.add(step);
            }
        }
    }

    private boolean isValidWeight(BigDecimal weight) {
        // Aceptar solo pesos múltiplos del paso
        return weight.remainder(STEP).compareTo(BigDecimal.ZERO) == 0;
    }

}
