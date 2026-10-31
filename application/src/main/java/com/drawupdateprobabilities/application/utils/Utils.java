package com.drawupdateprobabilities.application.utils;

import java.text.DecimalFormat;

public class Utils {
    private Utils() {

    }

    /**
     * Devuelve una cadena con un número, rellenado con ceros a la izquierda
     *
     * @param number       El número al cual hay que ponerle ceros si hace falta
     * @param digitNumbers El número de dígitos que debe tener la cadena con el número rellenado con ceros a la
     *                     izquierda
     * @return Una cadena formada por ceros (si son necesarios) y el número, completando el número de dígitos recibido
     */
    public static String leftZeroes(long number, int digitNumbers) {
        String cadenaFormato = "%0" + digitNumbers + "d";
        return String.format(cadenaFormato, number);
    }

    private static String getZeroes(int number) {
        return String.format(String.format("%%0%dd", number), 0);
    }

    public static String format(double number, int intNumbers, int decimalNumbers) {
        DecimalFormat df = new DecimalFormat(getZeroes(intNumbers) + "." + getZeroes(decimalNumbers));
        return df.format(number);
    }
}