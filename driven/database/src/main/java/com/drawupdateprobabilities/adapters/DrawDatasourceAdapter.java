package com.drawupdateprobabilities.adapters;

import com.drawupdateprobabilities.application.ports.driven.DrawDatasourcePort;
import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.DrawList;
import com.drawupdateprobabilities.mappers.DrawMapper;
import com.drawupdateprobabilities.repositories.DrawRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class DrawDatasourceAdapter implements DrawDatasourcePort {
    private final DrawMapper drawMapper;
    private final DrawRepository drawRepository;

    public DrawList getDrawListByDrawTypeId(Short drawTypeId) {
        return new DrawList(new ArrayList<>(drawRepository.findByDrawTypeId(drawTypeId).stream().map(drawMapper::toDomain).toList()));
    }

    public void insertDraw(Draw draw) {
        log.info("Saving draw: {}", draw);
        drawRepository.save(drawMapper.toModel(draw));
    }

    @Override
    public List<Short> getDrawTypeIdList() {
        return drawRepository.findDrawTypeList();
    }

}