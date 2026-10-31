package com.drawupdateprobabilities.repositories;

import com.drawupdateprobabilities.domain.ProbabilityTypeByDrawMO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProbabilityTypeByDrawRepository extends JpaRepository<ProbabilityTypeByDrawMO, Short> {
    List<ProbabilityTypeByDrawMO> findByProbabilityTypeIdAndDrawDateAndDrawTypeId(Short probabilityType, Integer drawDate, Short drawTypeId);
    List<ProbabilityTypeByDrawMO> findByProbabilityTypeIdAndDrawTypeId(Short probabilityType, Short drawTypeId);
}