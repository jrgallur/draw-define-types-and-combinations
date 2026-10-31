package com.drawupdateprobabilities.domain.types;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TNumberListTest {

    @Test
    void testConstructor() {
        TNumberList numberList = new TNumberList();
        assertNotNull(numberList);
        assertEquals(0, numberList.size());
    }

    @Test
    void testConstructorWithList() {
        List<Integer> initialList = Arrays.asList(1, 2, 3);
        TNumberList numberList = new TNumberList(initialList);
        assertNotNull(numberList);
        assertEquals(initialList, numberList.getList());
    }

    @Test
    void testConstructorWithTNumberList() {
        List<Integer> initialList = Arrays.asList(1, 2, 3);
        TNumberList originalNumberList = new TNumberList(initialList);
        TNumberList newNumberList = new TNumberList(originalNumberList);
        assertNotNull(newNumberList);
        assertEquals(initialList, newNumberList.getList());
    }

    @Test
    void testAddAndGet() {
        TNumberList numberList = new TNumberList();
        numberList.add(1);
        assertEquals(1, numberList.get(1));
    }

    @Test
    void testSize() {
        TNumberList numberList = new TNumberList();
        numberList.add(1);
        numberList.add(2);
        assertEquals(2, numberList.size());
    }

    @Test
    void testSort() {
        List<Integer> unsortedList = Arrays.asList(3, 1, 2);
        TNumberList numberList = new TNumberList(unsortedList);
        numberList.sort();
        assertEquals(Arrays.asList(1, 2, 3), numberList.getList());
    }

    @Test
    void testSet() {
        TNumberList numberList = new TNumberList(Arrays.asList(1, 2, 3));
        numberList.set(1, 5);
        assertEquals(5, numberList.get(1));
    }

    @Test
    void testSubList() {
        List<Integer> initialList = Arrays.asList(1, 2, 3, 4, 5);
        TNumberList numberList = new TNumberList(initialList);
        TNumberList subList = numberList.subList(1, 4);
        assertEquals(Arrays.asList(1, 2, 3, 4), subList.getList());
    }

}