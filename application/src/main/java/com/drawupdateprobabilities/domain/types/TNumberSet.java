package com.drawupdateprobabilities.domain.types;

import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gestiona un conjunto de números para validar de manera rápida si existe, por ejemplo
 *
 * @author jogal
 */
@EqualsAndHashCode
public class TNumberSet {
    private final Set<Byte> numberSet;

    public TNumberSet() {
        numberSet = new HashSet<>();
    }

    public TNumberSet(TNumberSet numberSet) {
        this.numberSet = new HashSet<>(numberSet.getByteSet());
    }

    public void add(int number) {
        numberSet.add((byte) number);
    }

    public boolean contains(int number) {
        return numberSet.contains((byte) number);
    }

    public Set<Integer> getSet() {
        return numberSet.stream()
                .map(Byte::intValue) // Convertir cada Byte a su valor int
                .collect(Collectors.toSet());
    }

    public Set<Byte> getByteSet() {
        return numberSet;
    }

    public void removeAll() {
        numberSet.clear();
    }

    public void removeSet(TNumberSet otherNumberSet) {
        numberSet.removeAll(otherNumberSet.getByteSet());
    }

    public int size() {
        return numberSet.size();
    }
}