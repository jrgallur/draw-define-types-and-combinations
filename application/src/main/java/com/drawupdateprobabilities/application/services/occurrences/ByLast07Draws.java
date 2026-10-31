package com.drawupdateprobabilities.application.services.occurrences;

import com.drawupdateprobabilities.application.ports.driven.MatrixOccurrenceDatasourcePort;
import com.drawupdateprobabilities.application.services.occurrences.ifaces.ByLastNDraws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ByLast07Draws extends ByLastNDraws {
    private static final Short DRAWS_TO_COUNT = 7;

    public ByLast07Draws(MatrixOccurrenceDatasourcePort matrixOccurrenceDatasourcePort) {
        super(matrixOccurrenceDatasourcePort);
    }

    @Override
    public Short getDrawsToCount() {
        return DRAWS_TO_COUNT;
    }
}
