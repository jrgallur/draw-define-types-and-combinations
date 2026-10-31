package com.drawupdateprobabilities.domain.types;

import java.util.List;

public class TNumberOccurrences extends TOccurrences {
    public TNumberOccurrences(List<Integer> occurrenceValues) {
        super(occurrenceValues);
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int cont = 0; cont< occurrenceValues.size(); cont++) {
            if (!occurrenceValues.get(cont).equals(0)) {
                result.append(cont + 1).append(":").append(occurrenceValues.get(cont)).append(";");
            }
        }
        return result.toString();
    }
}
