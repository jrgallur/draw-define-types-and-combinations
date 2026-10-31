package com.drawupdateprobabilities.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@Builder
public class ProbabilityTypeCombinationWeight {
    Integer probabilityTypeCombinationId;
    ProbabilityType probabilityType;
    BigDecimal weight;

    public String getPrettyWeight() {
        return (new DecimalFormat("#0.0")).format(weight);
    }
}
