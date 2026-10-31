package com.drawupdateprobabilities.domain.constans;

import lombok.Getter;

@Getter
public enum CalculatorTypeCode {
    APPEARS_MORE_TIME_BY_LAST_06_MONTHS("0002_06", null),
    APPEARS_MORE_TIME_BY_LAST_12_MONTHS("0002_12", null),
    APPEARS_MORE_TIME_BY_LAST_24_MONTHS("0002_24", null),
    APPEARS_MORE_TIME_BY_LAST_99_MONTHS("0002_99", null),
    LAST_APPEARANCE_IS_OLDER("0001", null),
    LAST_01_DRAW("0003_01",1),
    LAST_02_DRAWS("0003_02",2),
    LAST_03_DRAWS("0003_03",3),
    LAST_04_DRAWS("0003_04",4),
    LAST_05_DRAWS("0003_05",5),
    LAST_06_DRAWS("0003_06",6),
    LAST_07_DRAWS("0003_07",7),
    LAST_08_DRAWS("0003_08",8),
    LAST_09_DRAWS("0003_09",9),
    LAST_10_DRAWS("0003_10",10),
    LAST_11_DRAWS("0003_11",11),
    LAST_12_DRAWS("0003_12",12),
    LAST_13_DRAWS("0003_13",13),
    LAST_14_DRAWS("0003_14",14);

    private final String typeCode;
    private final Short numDraws;

    CalculatorTypeCode(String typeCode, Integer numDraws) {
        this.typeCode = typeCode;
        this.numDraws = numDraws != null ? numDraws.shortValue() : null;
    }

}
