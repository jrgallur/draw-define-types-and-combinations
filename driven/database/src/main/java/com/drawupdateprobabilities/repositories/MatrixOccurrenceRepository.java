package com.drawupdateprobabilities.repositories;

import com.drawupdateprobabilities.domain.MatrixOccurrenceMO;
import com.drawupdateprobabilities.domain.pks.MatrixOccurrenceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatrixOccurrenceRepository extends JpaRepository<MatrixOccurrenceMO, MatrixOccurrenceId> {
    Optional<MatrixOccurrenceMO> getByDrawTypeIdAndDrawsToCount(Short drawTypeId, Short drawsToCount);
    void deleteByDrawTypeId(Short drawTypeId);
}