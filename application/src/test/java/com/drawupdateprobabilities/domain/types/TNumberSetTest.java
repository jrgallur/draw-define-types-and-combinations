package com.drawupdateprobabilities.domain.types;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TNumberSetTest {

    @Test
    void testConstructor() {
        TNumberSet numberSet = new TNumberSet();
        assertNotNull(numberSet);
        assertEquals(0, numberSet.size());
    }

    @Test
    void testConstructorWithTNumberSet() {
        TNumberSet originalNumberSet = new TNumberSet();
        originalNumberSet.add(1);
        originalNumberSet.add(2);

        TNumberSet newNumberSet = new TNumberSet(originalNumberSet);
        assertNotNull(newNumberSet);
        assertEquals(originalNumberSet.getSet(), newNumberSet.getSet());
    }

    @Test
    void testAdd() {
        TNumberSet numberSet = new TNumberSet();
        numberSet.add(1);
        assertTrue(numberSet.contains(1));
    }

    @Test
    void testContains() {
        TNumberSet numberSet = new TNumberSet();
        numberSet.add(1);
        assertTrue(numberSet.contains(1));
        assertFalse(numberSet.contains(2));
    }

    @Test
    void testGetSet() {
        TNumberSet numberSet = new TNumberSet();
        numberSet.add(1);
        numberSet.add(2);
        Set<Integer> compareTo = new HashSet<>();
        compareTo.add(1);
        compareTo.add(2);
        assertEquals(compareTo, numberSet.getSet());
    }

    @Test
    void testRemoveAll() {
        TNumberSet numberSet1 = new TNumberSet();
        numberSet1.add(1);
        numberSet1.add(2);

        TNumberSet numberSet2 = new TNumberSet();
        numberSet2.add(2);
        numberSet2.add(3);

        Set<Integer> compareTo = new HashSet<>();
        compareTo.add(1);

        numberSet1.removeSet(numberSet2);
        assertEquals(compareTo, numberSet1.getSet());
    }

    @Test
    void testSize() {
        TNumberSet numberSet = new TNumberSet();
        numberSet.add(1);
        numberSet.add(2);
        assertEquals(2, numberSet.size());
    }

    @Test
    void testEquals() {
        TNumberSet numberSet1 = new TNumberSet();
        numberSet1.add(1);
        numberSet1.add(2);

        TNumberSet numberSet2 = new TNumberSet();
        numberSet2.add(1);
        numberSet2.add(2);

        assertEquals(numberSet1, numberSet2);
    }
}