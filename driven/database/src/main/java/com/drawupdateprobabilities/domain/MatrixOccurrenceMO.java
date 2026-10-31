package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.pks.MatrixOccurrenceId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "matrix_occurrence")
@IdClass(MatrixOccurrenceId.class)
@NoArgsConstructor
@Getter
@Setter
public class MatrixOccurrenceMO {
    @Id
    @Column(name = "draw_type_id", nullable = false)
    private Short drawTypeId;

    @Id
    @Column(name = "draws_to_count", nullable = false)
    private Short drawsToCount;

    @Column(name = "until_date", nullable = false)
    private Integer untilDate;

    @Column(name = "occurrences", nullable = false, columnDefinition = "TEXT")
    private String occurrences;
}

