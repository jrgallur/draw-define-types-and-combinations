package com.drawupdateprobabilities.domain.types;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class TOccurrences {
    protected final List<Integer> occurrenceValues;

    public TOccurrences(int drawsToCount) {
        occurrenceValues = new ArrayList<>(Collections.nCopies(1 << (drawsToCount + 1), 0));
    }

    public TOccurrences(List<Integer> occurrenceValues) {
        this.occurrenceValues = new ArrayList<>(occurrenceValues);
    }

    public void increment(Integer index) {
        occurrenceValues.set(index, occurrenceValues.get(index) + 1);
    }

    public abstract String toString();

    public int getNonZeroValues() {
        return occurrenceValues.stream().filter(index -> index>0).toList().size();
    }
}
