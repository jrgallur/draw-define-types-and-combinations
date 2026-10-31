package com.drawupdateprobabilities.domain.pks;

import lombok.Data;

import java.io.Serializable;

@Data
public class DrawId implements Serializable {
    private Integer drawDate;
    private Short drawTypeId;
}
