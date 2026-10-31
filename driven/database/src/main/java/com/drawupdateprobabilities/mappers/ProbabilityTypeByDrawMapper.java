package com.drawupdateprobabilities.mappers;

import com.drawupdateprobabilities.domain.*;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProbabilityTypeByDrawMapper {
    default ProbabilityTypeByDrawMO toModel(ProbabilityTypeByDraw probabilityTypeByDraw) {
        return ProbabilityTypeByDrawMO.builder()
                .drawDate(probabilityTypeByDraw.getDrawDate().toInteger())
                .probabilityTypeId(probabilityTypeByDraw.getProbabilityType().getId())
                .values(numberListToString(probabilityTypeByDraw.getNumberList()))
                .drawTypeId(probabilityTypeByDraw.getDrawTypeId())
                .build();
    }

    private String numberListToString(List<BigDecimal> numberList) {
        StringBuilder result = new StringBuilder();
        for (int cont=0; cont<49; cont++) {
            result.append(numberList.get(cont)).append("#");
        }
        return result.toString();
    }

    private List<BigDecimal> stringToNumberList(String numberListAsString) {
        return Arrays.stream(numberListAsString.split("#"))
                .map(BigDecimal::new)
                .collect(Collectors.toList());

    }

    default ProbabilityTypeByDraw toDomainMinimal(ProbabilityTypeByDrawMO probabilityTypeByDrawMO) {
        return ProbabilityTypeByDraw.builder()
                .probabilityType(ProbabilityType.builder().id(probabilityTypeByDrawMO.getProbabilityTypeId()).build())
                .drawDate(new TDateInteger(probabilityTypeByDrawMO.getDrawDate()))
                .drawTypeId(probabilityTypeByDrawMO.getDrawTypeId())
                .numberList(stringToNumberList(probabilityTypeByDrawMO.getValues()))
                .build();
    }
}