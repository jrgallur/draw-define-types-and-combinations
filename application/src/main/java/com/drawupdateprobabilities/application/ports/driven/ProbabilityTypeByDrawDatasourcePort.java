package com.drawupdateprobabilities.application.ports.driven;

import com.drawupdateprobabilities.domain.ProbabilityTypeByDraw;

import java.util.List;

public interface ProbabilityTypeByDrawDatasourcePort {
    void upsert(ProbabilityTypeByDraw probabilityTypeByDraw);
    List<ProbabilityTypeByDraw> findByProbabilityTypeAndDrawTypeId(Short probabilityType, Short drawTypeId);
}
