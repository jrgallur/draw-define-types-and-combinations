package com.drawupdateprobabilities.application.ports.driven;

import com.drawupdateprobabilities.domain.ProbabilityType;

import java.util.List;

public interface ProbabilityTypeDatasourcePort {
    void upsert(ProbabilityType probabilityType);

    ProbabilityType getByCode(String code);

    List<ProbabilityType> findAll();
}
