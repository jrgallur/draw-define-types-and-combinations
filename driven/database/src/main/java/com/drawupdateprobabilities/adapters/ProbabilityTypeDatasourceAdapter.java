package com.drawupdateprobabilities.adapters;

import com.drawupdateprobabilities.mappers.ProbabilityTypeMapper;
import com.drawupdateprobabilities.domain.ProbabilityType;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.repositories.ProbabilityTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ProbabilityTypeDatasourceAdapter implements ProbabilityTypeDatasourcePort {
    private final ProbabilityTypeRepository repository;
    private final ProbabilityTypeMapper mapper;

    @Override
    public void upsert(ProbabilityType probabilityType) {
        repository.save(mapper.toModel(probabilityType));
    }

    @Override
    public ProbabilityType getByCode(String code) {
        return mapper.toDomain(repository.findByCode(code).orElse(null));
    }
}
