package com.drawupdateprobabilities.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "probability_type_by_draw")
public class ProbabilityTypeByDrawMO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer drawDate;
    private Short drawTypeId;
    private String values;
    private Short probabilityTypeId;
}
