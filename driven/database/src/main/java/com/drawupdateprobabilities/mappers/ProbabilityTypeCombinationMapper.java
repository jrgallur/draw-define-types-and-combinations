package com.drawupdateprobabilities.mappers;

import com.drawupdateprobabilities.domain.ProbabilityTypeCombination;
import com.drawupdateprobabilities.domain.ProbabilityTypeCombinationMO;
import com.drawupdateprobabilities.domain.projections.ProbabilityTypeCombinationProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProbabilityTypeCombinationWeightMapper.class})
public interface ProbabilityTypeCombinationMapper {
    ProbabilityTypeCombinationMO toModel(ProbabilityTypeCombination domain);

    ProbabilityTypeCombination toDomain(ProbabilityTypeCombinationMO mo);

    ProbabilityTypeCombination toDomain(ProbabilityTypeCombinationProjection ptcp);
}