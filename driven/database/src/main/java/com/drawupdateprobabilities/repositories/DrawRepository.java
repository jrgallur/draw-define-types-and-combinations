package com.drawupdateprobabilities.repositories;

import com.drawupdateprobabilities.domain.DrawMO;
import com.drawupdateprobabilities.domain.pks.DrawId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrawRepository extends JpaRepository<DrawMO, DrawId> {
    /**
     * Get the draw list filtered by type
     * @param drawTypeId The type of draws to get list from
     * @return The full list of draws of the selected type
     */
    List<DrawMO> findByDrawTypeId(Short drawTypeId);

    @Query("""
            SELECT DISTINCT d.drawTypeId
            FROM DrawMO d
            """)
    List<Short> findDrawTypeList();
}