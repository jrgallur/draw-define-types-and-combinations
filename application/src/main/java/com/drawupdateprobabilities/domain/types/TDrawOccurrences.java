package com.drawupdateprobabilities.domain.types;

import java.util.List;

public class TDrawOccurrences extends TOccurrences {
    public TDrawOccurrences(int drawsToCount) {
        super(drawsToCount);
    }

    public TDrawOccurrences(List<Integer> occurrenceValues) {
        super(occurrenceValues);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int cont = 0; cont< occurrenceValues.size(); cont++) {
            if (!occurrenceValues.get(cont).equals(0)) {
                result.append(cont).append(":").append(occurrenceValues.get(cont)).append(";");
            }
        }
        return result.toString();
    }
}
