package com.drawupdateprobabilities.application.ports.driven;

import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationWeight;

import java.util.List;

public interface ProbabilityTypeCombinationWeightDatasourcePort {
    void saveAll(List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList);
    void deleteByProbabilityTypeCombinationId(Integer probabilityTypeCombinationId);
    List<ProbabilityTypeCombinationWeight> findByProbabilityTypeCombination(Integer probabilityTypeCombinationId);
}
