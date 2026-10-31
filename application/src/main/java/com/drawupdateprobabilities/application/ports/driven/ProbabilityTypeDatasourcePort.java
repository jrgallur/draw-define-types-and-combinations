package com.drawupdateprobabilities.application.ports.driven;

import com.drawupdateprobabilities.domain.ProbabilityType;

public interface ProbabilityTypeDatasourcePort {
    void upsert(ProbabilityType probabilityType);

    ProbabilityType getByCode(String code);
}
