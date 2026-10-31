package com.drawupdateprobabilities.adapters;

import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeCombinationWeightDatasourcePort;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationMO;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationWeight;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationWeightMO;
import com.drawupdateprobabilities.mappers.ProbabilityTypeCombinationWeightMapper;
import com.drawupdateprobabilities.repositories.ProbabilityTypeCombinationWeightRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ProbabilityTypeCombinationWeightDatasourceAdapter implements ProbabilityTypeCombinationWeightDatasourcePort {
    private final ProbabilityTypeCombinationWeightRepository repository;
    private final ProbabilityTypeCombinationWeightMapper mapper;

    @Override
    public void saveAll(List<ProbabilityTypeCombinationWeight> probabilityTypeCombinationWeightList) {
        List<ProbabilityTypeCombinationWeightMO> probabilityTypeCombinationWeightMOList = (probabilityTypeCombinationWeightList.stream().map(mapper::toModel).toList());
        repository.saveAll(probabilityTypeCombinationWeightMOList);
    }

    @Override
    public void deleteByProbabilityTypeCombinationId(Integer probabilityTypeCombinationId) {
        repository.deleteByProbabilityTypeCombinationId(probabilityTypeCombinationId);
    }

    @Override
    public List<ProbabilityTypeCombinationWeight> findByProbabilityTypeCombination(Integer probabilityTypeCombinationId) {
        ProbabilityTypeCombinationMO probabilityTypeCombinationMO = ProbabilityTypeCombinationMO.builder()
                .id(probabilityTypeCombinationId)
                .build();
        return repository.findByProbabilityTypeCombination(probabilityTypeCombinationMO).stream().map(mapper::toDomain).toList();
    }
}
