package com.drawupdateprobabilities.application.ports.driven;

import com.drawupdateprobabilities.domain.MatrixOccurrence;

import java.util.Optional;

public interface MatrixOccurrenceDatasourcePort {
    void upsert(MatrixOccurrence matrixOccurrence);
    Optional<MatrixOccurrence> getByDrawTypeIdAndDrawsToCount(Short drawTypeId, Short drawsToCount);
    void deleteByDrawTypeId(Short drawTypeId);
}
