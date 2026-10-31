package com.drawupdateprobabilities.application.services.calculators;

import com.drawupdateprobabilities.application.ports.driven.MatrixOccurrenceDatasourcePort;
import com.drawupdateprobabilities.application.ports.driven.ProbabilityTypeDatasourcePort;
import com.drawupdateprobabilities.application.services.calculators.ifaces.HigherWhenLastNDraws;
import com.drawupdateprobabilities.domain.constans.CalculatorTypeCode;
import org.springframework.stereotype.Service;

@Service
public class HigherWhenLast04Draws extends HigherWhenLastNDraws {
    public HigherWhenLast04Draws(ProbabilityTypeDatasourcePort probabilityTypeDatasourcePort, MatrixOccurrenceDatasourcePort matrixOccurrenceDatasourcePort) {
        super(probabilityTypeDatasourcePort, matrixOccurrenceDatasourcePort, CalculatorTypeCode.LAST_04_DRAWS);
    }
}
