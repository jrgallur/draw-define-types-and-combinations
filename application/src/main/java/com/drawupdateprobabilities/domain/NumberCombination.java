package com.drawupdateprobabilities.domain;


import com.drawupdateprobabilities.domain.types.TNumberList;
import com.drawupdateprobabilities.domain.types.TNumberSet;
import com.drawupdateprobabilities.application.utils.Utils;
import lombok.Getter;

import java.util.List;

/**
 * Clase que contiene los números de un sorteo y las operaciones para manejarlos y, principalmente, compararlos
 *
 * @author jgallurm
 */
public class NumberCombination {
    // Lista de números, sin el complementario
    private TNumberList numberList = new TNumberList();

    // Conjunto de números sin el complementario, para comprobaciones
    private TNumberSet numberSet;

    // Conjunto de números con el complementario, para comparaciones más rápidas
    private TNumberSet numberSetWC;

    // Lista de números con el complementario, para comparaciones más rápidas
    private TNumberList numberListWC = new TNumberList();

    /**
     * -- GETTER --
     *  Devuelve el número complementario
     *
     */
    // Complementario
    @Getter
    private byte complementary;

    /**
     * Constructor privado para que no pueda ser invocado de manera externa
     */
    @SuppressWarnings("unused")
    private NumberCombination() {
    }

    /**
     * Constructor que admite un array de números y un complementario
     *
     * @param numbers       Array de números
     * @param complementary Complementario
     */
    public NumberCombination(byte[] numbers, int complementary) {
        TNumberList tmpNumberList = new TNumberList(numbers);
        setInternalNumbers(tmpNumberList, (byte) complementary);
    }

    /**
     * Constructor que admite una lista de números y un complementario
     *
     * @param numbers       Lista de números
     * @param complementary Complementario
     */
    public NumberCombination(TNumberList numbers, int complementary) {
        setInternalNumbers(numbers, (byte) complementary);
    }

    /**
     * Constructor que admite los números como cadenas de texto
     */
    public static NumberCombination valueOf(String n1, String n2, String n3, String n4, String n5, String n6, String c) {
        TNumberList numberList = new TNumberList(
                List.of(Integer.parseInt(n1.trim()),
                        Integer.parseInt(n2.trim()),
                        Integer.parseInt(n3.trim()),
                        Integer.parseInt(n4.trim()),
                        Integer.parseInt(n5.trim()),
                        Integer.parseInt(n6.trim())
                )
        );
        return new NumberCombination(numberList, Byte.parseByte(c.trim()));
    }

    /**
     * Inicializa las variables que contienen los números (la lista y el conjunto)
     *
     * @param numbers Array con los números.
     * @throws IllegalArgumentException En el caso de que se falte o sobre algún número o cualquiera sea incorrecto
     */
    private void setInternalNumbers(TNumberList numbers, byte complementary) throws IllegalArgumentException {
        if (numbers == null || numbers.size() != 6 || !validateListNumbers(numbers) || isInvalidNumber(complementary)) {
            throw new IllegalArgumentException("Incorrect number(s): [" + numbers + ", C = " + complementary + "]");
        }
        numberList = new TNumberList();
        numberListWC = new TNumberList();
        numberSet = new TNumberSet();
        numberSetWC = new TNumberSet();
        for (Integer number : numbers.getList()) {
            numberList.add(number);
            numberListWC.add(number);
            numberSet.add(number.byteValue());
            numberSetWC.add(number.byteValue());
        }
        this.complementary = complementary;
        numberSetWC.add(complementary);
        numberListWC.add(complementary);
    }

    /**
     * Valida si una lista de números cumple las condiciones (debe ser entre 1 y 49)
     *
     * @param numbers Lista de números
     * @return true si la lista de números es correcta
     */
    private boolean validateListNumbers(TNumberList numbers) {
        for (int number : numbers.getList()) {
            if (isInvalidNumber(number)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Valida que un número sea correcto. Debe ser distinto de null y estar entre 1 y 49
     *
     * @param number El número a validar
     * @return true si el número es válido o false en caso contrario
     */
    private boolean isInvalidNumber(int number) {
        return (number < 1 || number > 49);
    }

    /**
     * Devuelve un string con la representación del objeto
     */
    public String toString() {
        StringBuilder resultado = new StringBuilder();
        resultado.append(" [");
        resultado.append(Utils.leftZeroes(numberList.get(1), 2));
        for (byte cont = 2; cont <= 6; cont++) {
            resultado.append(", ");
            resultado.append(Utils.leftZeroes(numberList.get(cont), 2));
        }
        resultado.append(", C = ");
        resultado.append(Utils.leftZeroes(complementary, 2));
        resultado.append("]");
        return resultado.toString();
    }

    /**
     * Indica si un número existe en el sorteo o no, incluyendo el complementario o no
     *
     * @param number           el número a evaluar
     * @param useComplementary Indica si se debe comprobar el complementario o no
     * @return true si el número está en la lista o en el complementario
     */
    public boolean contains(int number, boolean useComplementary) {
        if (useComplementary) {
            return numberSetWC.contains((byte) number);
        }
        return numberSet.contains((byte) number);
    }

    /**
     * Obtiene la lista de los números como objeto
     *
     * @return La lista de los números como objeto
     */
    public TNumberList getNumbers() {
        return numberList;
    }

    /**
     * Gets the full number list, including the complementary
     * @return The full number list, including the complementary
     */
    public TNumberList getNumbersWC() {
        return numberListWC;
    }

    /**
     * Obtiene la lista de los números como vector
     *
     * @return La lista de los números como vector
     */
    public byte[] getNumbersAsArray() {
        byte[] result = new byte[numberList.size()];
        for (int index = 1; index <= numberList.size(); index++) {
            result[index - 1] = numberList.get(index);
        }
        return result;
    }

    /**
     * Comprueba si un conjunto de números es igual a otro, teniendo en cuenta o no el complementario
     *
     * @param other            El conjunto de números a comparar con el actual
     * @param useComplementary Indica si se tiene en cuenta o no el complementario
     * @return true si el conjunto de números es igual al otro, teniendo en cuenta o no el complementario
     */
    public boolean equals(NumberCombination other, boolean useComplementary) {
        if (other == null) {
            return false;
        }
        boolean numberEquals = this.numberSet.equals(other.numberSet);
        if (useComplementary) {
            return this.complementary == other.complementary && numberEquals;
        }
        return numberEquals;
    }

    /**
     * Devuelve el número de coincidencias que hay entre dos sorteos, teniendo en cuenta (o no) el complementario
     *
     * @param other            El otro conjunto de números y complementario a comparar
     * @param useComplementary Si se va a usar o no el complementario en la comparación
     * @return El número de valores que están en un sorteo y en el otro
     */
    public int getCoincidences(NumberCombination other, boolean useComplementary) {
        if (other == null) {
            return 0;
        }
        TNumberSet thisNumberSetAux;
        TNumberSet otherNumberSetAux;
        int maxCoincidences;
        if (useComplementary) {
            maxCoincidences = 7;
            thisNumberSetAux = new TNumberSet(numberSetWC);
            otherNumberSetAux = new TNumberSet(other.numberSetWC);
        } else {
            maxCoincidences = 6;
            thisNumberSetAux = new TNumberSet(numberSet);
            otherNumberSetAux = new TNumberSet(other.numberSet);
        }
        thisNumberSetAux.removeSet(otherNumberSetAux);
        return maxCoincidences - thisNumberSetAux.size();
    }
}