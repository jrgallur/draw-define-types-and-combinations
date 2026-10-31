package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TOccurrences;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
public class MatrixOccurrence {
    private Short drawTypeId;
    private Short drawsToCount;
    private TDateInteger untilDate;
    @Setter
    private TOccurrences occurrences;
}
