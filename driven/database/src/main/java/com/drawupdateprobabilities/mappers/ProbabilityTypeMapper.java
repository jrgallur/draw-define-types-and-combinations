package com.drawupdateprobabilities.mappers;

import com.drawupdateprobabilities.domain.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProbabilityTypeMapper {
    ProbabilityTypeMO toModel(ProbabilityType probabilityType);

    ProbabilityType toDomain(ProbabilityTypeMO probabilityTypeMO);
}