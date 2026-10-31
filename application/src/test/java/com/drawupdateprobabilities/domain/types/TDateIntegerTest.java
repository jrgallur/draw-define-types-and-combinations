package com.drawupdateprobabilities.domain.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TDateIntegerTest {

    @Test
    void testConstructorWithString() {
        TDateInteger dateInteger = TDateInteger.valueOfUnknownFormat("20240316");
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testConstructorWithStringAndFormat() {
        TDateInteger dateInteger = new TDateInteger("2024-03-16", "yyyy-MM-dd");
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testConstructorWithYearMonthDay() {
        TDateInteger dateInteger = new TDateInteger(2024, 2, 16);
        assertEquals("20240216", dateInteger.toString());
    }

    @Test
    void testSetFecha() {
        TDateInteger dateInteger = new TDateInteger();
        dateInteger.setDate("20240316");
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testCompareTo() {
        TDateInteger dateInteger1 = TDateInteger.valueOfUnknownFormat("20240316");
        TDateInteger dateInteger2 = TDateInteger.valueOfUnknownFormat("20240316");
        assertEquals(0, dateInteger1.compareTo(dateInteger2));
    }

    @Test
    void testEquals() {
        TDateInteger dateInteger1 = TDateInteger.valueOfUnknownFormat("20240316");
        TDateInteger dateInteger2 = TDateInteger.valueOfUnknownFormat("20240316");
        assertEquals(dateInteger1, dateInteger2);
    }

    @Test
    void testAddYear() {
        TDateInteger dateInteger = TDateInteger.valueOfUnknownFormat("20240316");
        TDateInteger newDateInteger = dateInteger.add(TDateInteger.addTypes.YEAR, 1);
        assertEquals("20250316", newDateInteger.toString());
    }

    @Test
    void testAddMonth() {
        TDateInteger dateInteger = TDateInteger.valueOfUnknownFormat("20240316");
        TDateInteger newDateInteger = dateInteger.add(TDateInteger.addTypes.MONTH, 1);
        assertEquals("20240416", newDateInteger.toString());
    }

    @Test
    void testAddDay() {
        TDateInteger dateInteger = TDateInteger.valueOfUnknownFormat("20240316");
        TDateInteger newDateInteger = dateInteger.add(TDateInteger.addTypes.DAY, 1);
        assertEquals("20240317", newDateInteger.toString());
    }

    @Test
    void valueOfWithFormat_valueAndFormat_shouldReturnNewTDateInteger() {
        TDateInteger dateInteger = TDateInteger.valueOf("23-04-2024","dd-MM-yyyy");
        assertEquals("20240423", dateInteger.toString());
    }

    @Test
    void testUnknownFormatThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> TDateInteger.valueOf("16/03/2024", "invalid-format"));
        assertEquals("[16/03/2024] is not correct with format [invalid-format]", exception.getMessage());
    }

    @Test
    void testAddMonthCrossingYearBoundary() {
        TDateInteger dateInteger = TDateInteger.valueOfUnknownFormat("20231231");
        TDateInteger newDateInteger = dateInteger.add(TDateInteger.addTypes.MONTH, 1);
        assertEquals("20240131", newDateInteger.toString());
    }

    @Test
    void testHashCodeConsistency() {
        TDateInteger date1 = TDateInteger.valueOfUnknownFormat("20240316");
        TDateInteger date2 = TDateInteger.valueOfUnknownFormat("20240316");
        assertEquals(date1.hashCode(), date2.hashCode());
    }

    @Test
    void testDateWithMonthName() {
        TDateInteger dateInteger = TDateInteger.valueOf("16 Marzo 2024", "dd MMM yyyy");
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testToStringRegularDate() {
        TDateInteger dateInteger = new TDateInteger(2024, 3, 16);
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testToStringWithLeapYear() {
        TDateInteger dateInteger = new TDateInteger(2024, 2, 29);
        assertEquals("20240229", dateInteger.toString());
    }

    @Test
    void testToStringEndOfYear() {
        TDateInteger dateInteger = new TDateInteger(2023, 12, 31);
        assertEquals("20231231", dateInteger.toString());
    }

    @Test
    void testToStringEndOfMonth() {
        TDateInteger dateInteger = new TDateInteger(2023, 11, 30);
        assertEquals("20231130", dateInteger.toString());
    }

    @Test
    void testToStringFromSetDateInteger() {
        TDateInteger dateInteger = new TDateInteger();
        dateInteger.setDate(20240316);
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testToStringFromSetDateString() {
        TDateInteger dateInteger = new TDateInteger();
        dateInteger.setDate("2024-03-16");
        assertEquals("20240316", dateInteger.toString());
    }

    @Test
    void testToStringMinDate() {
        TDateInteger dateInteger = new TDateInteger(1, 1, 1); // Min Gregorian date
        assertEquals("00010101", dateInteger.toString());
    }

    @Test
    void testToStringMaxDate() {
        TDateInteger dateInteger = new TDateInteger(9999, 12, 31); // Max Gregorian date
        assertEquals("99991231", dateInteger.toString());
    }

    @Test
    void testToStringAfterAddition() {
        TDateInteger dateInteger = new TDateInteger(2023, 12, 31);
        TDateInteger newDate = dateInteger.add(TDateInteger.addTypes.DAY, 1); // Crosses year boundary
        assertEquals("20240101", newDate.toString());
    }

    @Test
    void testToInteger() {
        // Caso básico: fecha regular
        TDateInteger dateInteger = new TDateInteger(2024, 3, 16);
        assertEquals(20240316, dateInteger.toInteger());

        // Caso límite: último día del año
        dateInteger = new TDateInteger(2023, 12, 31);
        assertEquals(20231231, dateInteger.toInteger());

        // Caso límite: año bisiesto
        dateInteger = new TDateInteger(2024, 2, 29);
        assertEquals(20240229, dateInteger.toInteger());

        // Caso límite: primer día del calendario
        dateInteger = new TDateInteger(1, 1, 1);
        assertEquals(10101, dateInteger.toInteger());

        // Caso límite: último día soportado
        dateInteger = new TDateInteger(9999, 12, 31);
        assertEquals(99991231, dateInteger.toInteger());

        // Caso general: inicialización desde una fecha en formato cadena
        dateInteger = TDateInteger.valueOfUnknownFormat("20240316");
        assertEquals(20240316, dateInteger.toInteger());
    }

    @Test
    void testEquals_equalObjects() {
        TDateInteger dateInteger1 = new TDateInteger(2024, 3, 16);
        TDateInteger dateInteger2 = new TDateInteger(2024, 3, 16);
        // Comparación entre dos objetos con la misma fecha
        assertEquals(dateInteger1, dateInteger2);
    }

    @Test
    void testEquals_differentObjects() {
        TDateInteger dateInteger1 = new TDateInteger(2024, 3, 16);
        TDateInteger dateInteger2 = new TDateInteger(2023, 12, 31);
        // Comparación entre dos objetos con fechas diferentes
        assertNotEquals(dateInteger1, dateInteger2);
    }

    @Test
    void testEquals_nullObject() {
        TDateInteger dateInteger = new TDateInteger(2024, 3, 16);
        // Comparación con un objeto nulo
        assertNotEquals(null, dateInteger);
    }

    @Test
    void testEquals_differentClassObject() {
        TDateInteger dateInteger = new TDateInteger(2024, 3, 16);
        String differentClassObject = "Not a TDateInteger";
        // Comparación con un objeto de una clase diferente
        assertNotEquals(dateInteger, differentClassObject);
    }

    @Test
    void testSetDate_invalidDateFormat_shouldThrowException() {
        TDateInteger dateInteger = new TDateInteger();

        String invalidDate = "invalid-date"; // Fecha con un formato no soportado

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dateInteger.setDate(invalidDate),
                "Expected setDate() to throw IllegalArgumentException for an invalid date format"
        );

        // Validar el mensaje de la excepción
        assertEquals("Can't determine date format in [" + invalidDate + "]", exception.getMessage());
    }
}

