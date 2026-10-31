package com.drawupdateprobabilities.domain.pks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProbabilityTypeCombinationWeightId implements Serializable {
    private Integer probabilityTypeCombination;
    private Integer probabilityTypeId;
}
