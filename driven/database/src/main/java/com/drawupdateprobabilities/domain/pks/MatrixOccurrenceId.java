package com.drawupdateprobabilities.domain.pks;

import lombok.Data;

import java.io.Serializable;

@Data
public class MatrixOccurrenceId implements Serializable {
    private Short drawTypeId;
    private Short drawsToCount;
}
