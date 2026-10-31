package com.drawupdateprobabilities.application.ports.driven;


import com.drawupdateprobabilities.domain.ProbabilityTypeCombination;

import java.util.List;

public interface ProbabilityTypeCombinationDatasourcePort {
    List<String> findByCodeList(List<String> code);
    ProbabilityTypeCombination upsert(ProbabilityTypeCombination probabilityTypeCombination);

    List<ProbabilityTypeCombination> getAllSimpleProbabilityTypeCombination();
}
