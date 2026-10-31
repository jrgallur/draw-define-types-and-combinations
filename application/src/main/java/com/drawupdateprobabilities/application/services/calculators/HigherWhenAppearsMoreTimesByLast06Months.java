package com.drawupdateprobabilities.application.services.calculators;

import com.drawupdateprobabilities.application.services.calculators.ifaces.HigherWhenAppearsMoreTimesByLastNMonths;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.domain.constans.CalculatorTypeCode;
import org.springframework.stereotype.Service;

@Service
public class HigherWhenAppearsMoreTimesByLast06Months extends HigherWhenAppearsMoreTimesByLastNMonths {
    public static final String TYPE_CODE = CalculatorTypeCode.APPEARS_MORE_TIME_BY_LAST_06_MONTHS.getTypeCode();
    public static final String TYPE_DESCRIPTION = "Higher probability when the number appears more times during the last 6 months";

    public static final int MONTHS_NUMBER = 6;

    @Override
    protected String getProbabilityTypeCode() {
        return TYPE_CODE;
    }

    @Override
    protected String getProbabilityTypeDescription() {
        return TYPE_DESCRIPTION;
    }

    public HigherWhenAppearsMoreTimesByLast06Months(ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort) {
        super(probabilityTypeDatasourcePort);
    }

    public int getNumberOfMonthsToCalculate() {
        return MONTHS_NUMBER;
    }

}
