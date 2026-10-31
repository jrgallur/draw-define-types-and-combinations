package com.drawupdateprobabilities.adapters;

import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeByDrawDatasourcePort;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDrawMO;
import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;
import com.drawupdateprobabilities.mappers.ProbabilityTypeByDrawMapper;
import com.drawupdateprobabilities.repositories.ProbabilityTypeByDrawRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ProbabilityTypeByDrawDatasourceAdapter implements ProbabilityTypeByDrawDatasourcePort {
    private final ProbabilityTypeByDrawRepository repository;
    private final ProbabilityTypeByDrawMapper mapper;

    @Override
    public void upsert(ProbabilityTypeByDraw probabilityTypeByDraw) {
        ProbabilityTypeByDrawMO probabilityTypeByDrawMO = mapper.toModel(probabilityTypeByDraw);
        List<ProbabilityTypeByDrawMO> probabilityListMOTypeByDraw = repository.findByProbabilityTypeIdAndDrawDateAndDrawTypeId(probabilityTypeByDrawMO.getProbabilityTypeId(), probabilityTypeByDrawMO.getDrawDate(), probabilityTypeByDrawMO.getDrawTypeId());
        if (probabilityListMOTypeByDraw !=null && !probabilityListMOTypeByDraw.isEmpty()) {
            probabilityListMOTypeByDraw.get(0).setValues(probabilityTypeByDrawMO.getValues());
            repository.save(probabilityListMOTypeByDraw.get(0));
        } else {
            repository.save(probabilityTypeByDrawMO);
        }
    }

    @Override
    public List<ProbabilityTypeByDraw> findByProbabilityTypeAndDrawTypeId(Short probabilityType, Short drawTypeId) {
        return repository.findByProbabilityTypeIdAndDrawTypeId(probabilityType, drawTypeId).stream().map(mapper::toDomainMinimal).toList();
    }
}
