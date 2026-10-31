package com.drawupdateprobabilities.adapters;

import com.drawupdateprobabilities.application.ports.driven.MatrixOccurrenceDatasourcePort;
import com.drawupdateprobabilities.domain.MatrixOccurrence;
import com.drawupdateprobabilities.mappers.MatrixOccurrenceMapper;
import com.drawupdateprobabilities.repositories.MatrixOccurrenceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class MatrixOccurrenceDatasourceAdapter implements MatrixOccurrenceDatasourcePort {
    private final MatrixOccurrenceMapper mapper;
    private final MatrixOccurrenceRepository repository;

    @Override
    public void upsert(MatrixOccurrence matrixOccurrence) {
        repository.save(mapper.toModel(matrixOccurrence));
    }

    @Override
    public Optional<MatrixOccurrence> getByDrawTypeIdAndDrawsToCount(Short drawTypeId, Short drawsToCount) {
        return repository.getByDrawTypeIdAndDrawsToCount(drawTypeId, drawsToCount).map(mapper::toDomain);
    }

    @Override
    public void deleteByDrawTypeId(Short drawTypeId) {
        repository.deleteByDrawTypeId(drawTypeId);
    }
}