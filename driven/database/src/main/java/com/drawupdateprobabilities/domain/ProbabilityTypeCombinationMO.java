package com.drawupdateprobabilities.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@ToString
@Table(name = "probability_type_combination")
public class ProbabilityTypeCombinationMO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prob_type_comb_seq")
    @SequenceGenerator(name = "prob_type_comb_seq",
            sequenceName = "probability_type_combination_id_seq",
            allocationSize = 1)
    @Column(name="id")
    private Integer id;

    @Column(name="code")
    private String code;

    @OneToMany(mappedBy = "probabilityTypeCombination", fetch = FetchType.LAZY)
    private List<ProbabilityTypeCombinationWeightMO> probabilityTypeCombinationWeightList;
}
