package com.drawupdateprobabilities.domain.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TDrawsNumberAndFirstDrawDate {
    byte drawsNumber;
    TDateInteger firstDrawDate;
}
