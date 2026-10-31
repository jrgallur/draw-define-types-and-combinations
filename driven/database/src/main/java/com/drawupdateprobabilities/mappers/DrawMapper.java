package com.drawupdateprobabilities.mappers;

import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.DrawMO;
import com.drawupdateprobabilities.domain.NumberCombination;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DrawMapper {

    default DrawMO toModel(Draw draw) {
        return DrawMO.builder()
                .drawDate(draw.getDrawDate().toInteger())
                .drawTypeId(draw.getDrawTypeId())
                .number1(draw.getNumberCombination().getNumbersAsArray()[0])
                .number2(draw.getNumberCombination().getNumbersAsArray()[1])
                .number3(draw.getNumberCombination().getNumbersAsArray()[2])
                .number4(draw.getNumberCombination().getNumbersAsArray()[3])
                .number5(draw.getNumberCombination().getNumbersAsArray()[4])
                .number6(draw.getNumberCombination().getNumbersAsArray()[5])
                .complementary(draw.getComplementary())
                .build();
    }

    default Draw toDomain(DrawMO drawMO) {
        NumberCombination numberCombination = new NumberCombination(new byte[]{drawMO.getNumber1(), drawMO.getNumber2(), drawMO.getNumber3(), drawMO.getNumber4(), drawMO.getNumber5(), drawMO.getNumber6()}, drawMO.getComplementary());
        return Draw.builder()
                .drawDate(new TDateInteger(drawMO.getDrawDate()))
                .drawTypeId(drawMO.getDrawTypeId())
                .drawNumbers(numberCombination)
                .build();
    }
}