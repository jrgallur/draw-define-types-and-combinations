package com.drawupdateprobabilities.fakers;

import com.drawupdateprobabilities.domain.Draw;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TNumberList;

public class DrawFaker {
    public static Draw getDraw() {
        TDateInteger date = TDateInteger.valueOfUnknownFormat("20240316");
        TNumberList numbers = TNumberListFaker.getTNumberList();
        byte complementary = 10;

        return new Draw(date, numbers, complementary, (short) 1);
    }
}