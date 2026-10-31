package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.types.TNumberList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NumberCombinationTest {

    @Test
    void testConstructorWithArray() {
        byte[] numbers = {1, 2, 3, 4, 5, 6};
        int complementary = 10;
        NumberCombination combination = new NumberCombination(numbers, complementary);
        assertNotNull(combination);
        assertEquals(6, combination.getNumbers().size());
        assertEquals(complementary, combination.getComplementary());
    }

    @Test
    void testConstructorWithTNumberList() {
        TNumberList numbers = new TNumberList(Arrays.asList(1, 2, 3, 4, 5, 6));
        int complementary = 10;
        NumberCombination combination = new NumberCombination(numbers, complementary);
        assertNotNull(combination);
        assertEquals(6, combination.getNumbers().size());
        assertEquals(complementary, combination.getComplementary());
    }

    @Test
    void testToString() {
        byte[] numbers = {1, 2, 3, 4, 5, 6};
        byte complementary = 10;
        NumberCombination combination = new NumberCombination(numbers, complementary);
        assertEquals(" [01, 02, 03, 04, 05, 06, C = 10]", combination.toString());
    }

    @Test
    void testContains() {
        byte[] numbers = {1, 2, 3, 4, 5, 6};
        byte complementary = 10;
        NumberCombination combination = new NumberCombination(numbers, complementary);
        assertTrue(combination.contains(3, true));
        assertTrue(combination.contains(10, true));
        assertFalse(combination.contains(7, true));
        assertTrue(combination.contains(3, false));
        assertFalse(combination.contains(10, false));
        assertFalse(combination.contains(7, false));
    }

    @Test
    void testEquals() {
        byte[] numbers1 = {1, 2, 3, 4, 5, 6};
        byte complementary1 = 10;
        NumberCombination combination1 = new NumberCombination(numbers1, complementary1);

        byte[] numbers2 = {1, 2, 3, 4, 5, 6};
        byte complementary2 = 10;
        NumberCombination combination2 = new NumberCombination(numbers2, complementary2);

        assertTrue(combination1.equals(combination2, true));
        assertTrue(combination1.equals(combination2, false));

        byte[] numbers3 = {1, 2, 3, 4, 5, 6};
        byte complementary3 = 11;
        NumberCombination combination3 = new NumberCombination(numbers3, complementary3);

        assertFalse(combination1.equals(combination3, true));
        assertTrue(combination1.equals(combination3, false));
    }

    @Test
    void testGetCoincidences() {
        byte[] numbers1 = {1, 2, 3, 4, 5, 6};
        byte complementary1 = 10;
        NumberCombination combination1 = new NumberCombination(numbers1, complementary1);

        byte[] numbers2 = {1, 2, 3, 4, 5, 6};
        byte complementary2 = 10;
        NumberCombination combination2 = new NumberCombination(numbers2, complementary2);

        assertEquals(7, combination1.getCoincidences(combination2, true));
        assertEquals(6, combination1.getCoincidences(combination2, false));

        byte[] numbers3 = {1, 2, 3, 4, 5, 6};
        byte complementary3 = 11;
        NumberCombination combination3 = new NumberCombination(numbers3, complementary3);

        assertEquals(6, combination1.getCoincidences(combination3, true));
        assertEquals(6, combination1.getCoincidences(combination3, false));
    }
}