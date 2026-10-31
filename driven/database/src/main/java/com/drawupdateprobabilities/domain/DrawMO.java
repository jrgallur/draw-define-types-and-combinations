package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.pks.DrawId;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@IdClass(DrawId.class)
@Table(name = "draw")
public class DrawMO {
    // Fecha del sorteo
    @Id
    private Integer drawDate;
    @Id
    // The draw type: [1 Bonoloto, 2 Primitiva, ...]
    private Short drawTypeId;
    // Números del sorteo, sin el complementario
    private Byte number1;
    private Byte number2;
    private Byte number3;
    private Byte number4;
    private Byte number5;
    private Byte number6;
    // Número complementario del sorteo
    private Byte complementary;
}