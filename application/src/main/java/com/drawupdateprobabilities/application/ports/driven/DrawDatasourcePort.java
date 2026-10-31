package com.drawupdateprobabilities.application.ports.driven;

import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.DrawList;

import java.util.List;

public interface DrawDatasourcePort {
    /**
     * Get the draw list
     *
     * @return Draw list
     */
    DrawList getDrawListByDrawTypeId(Short drawTypeId);

    /**
     * Insert a draw in the database
     *
     * @param draw Draw to insert
     */
    void insertDraw(Draw draw);


    List<Short> getDrawTypeIdList();
}