package com.drawupdateprobabilities.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains one combination of different types
 */
@ToString
@Getter
@Setter
@Builder
public class ProbabilityTypeCombination {
    private Integer id;
    private String code;

    @Builder.Default
    private List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList = new ArrayList<>();

    public void addProbabilityTypeWeight(ProbabilityTypeCombinationWeight probabilityTypeCombinationWeight) {
        probabilityTypeCombinationWeightList.add(probabilityTypeCombinationWeight);
    }

    public String getCode() {
        if (code==null && probabilityTypeCombinationWeightList.isEmpty()) {
            return null;
        }

        if (code==null) {
            List<ProbabilityTypeCombinationWeight> combinationAux = new ArrayList<>(getProbabilityTypeCombinationWeightList());
            combinationAux.sort(
                    Comparator.comparing(p -> p.getProbabilityType().getCode())
            );
            StringBuilder sb = new StringBuilder();
            for (ProbabilityTypeCombinationWeight row : combinationAux) {
                if (!sb.isEmpty()) {
                    sb.append("#");
                }
                sb.append("#").append(row.getProbabilityType().getCode());
            }
            code = sb.toString();
        }
        return code;
    }
}
