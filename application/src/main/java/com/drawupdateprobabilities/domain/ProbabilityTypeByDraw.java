package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.types.TDateInteger;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class ProbabilityTypeByDraw {
    private Short drawTypeId;
    private TDateInteger drawDate;
    private ProbabilityType probabilityType;
    private List<BigDecimal> numberList;

    /**
     * Return the probability of a number where 1 is the first number and 49 is the last
     * @param number The number to get its probability
     * @return The probability associated to the number
     */
    public BigDecimal getNumberProbability(int number) {
        initializeNumberList();
        return numberList.get(number - 1);
    }

    public void setNumberProbability(int number, BigDecimal value) {
        initializeNumberList();
        numberList.set(number - 1, value);
    }

    private void initializeNumberList() {
        if (numberList == null) {
            numberList = new ArrayList<>(Collections.nCopies(49, new BigDecimal(0)));
        }
    }
}
