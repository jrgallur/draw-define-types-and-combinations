package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.pks.ProbabilityTypeCombinationWeightId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "probability_type_combination_weight")
@IdClass(ProbabilityTypeCombinationWeightId.class)
public class ProbabilityTypeCombinationWeightMO {
    @Id
    @ManyToOne
    @JoinColumn(name="probability_type_combination_id", nullable=false)
    private ProbabilityTypeCombinationMO probabilityTypeCombination;

    @Id
    @Column(name = "probability_type_id")
    private Integer probabilityTypeId;

    @Column(name="probability_type_code")
    private String probabilityTypeCode;

    BigDecimal weight;
}
