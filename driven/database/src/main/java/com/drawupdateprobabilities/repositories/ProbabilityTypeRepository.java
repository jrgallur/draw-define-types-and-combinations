package com.drawupdateprobabilities.repositories;

import com.drawupdateprobabilities.domain.ProbabilityTypeMO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProbabilityTypeRepository extends JpaRepository<ProbabilityTypeMO, Short> {
    /**
     * Get the type list
     *
     * @return Whole type list
     */
    @NonNull
    List<ProbabilityTypeMO> findAll();

    @NonNull
    Optional<ProbabilityTypeMO> findByCode(@NonNull String code);
}