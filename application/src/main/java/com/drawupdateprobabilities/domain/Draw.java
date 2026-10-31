package com.drawupdateprobabilities.domain;

import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TNumberList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
public class Draw implements Comparable<Draw> {
    @Getter
    private TDateInteger drawDate;
    @Getter
    private Short drawTypeId;
    private NumberCombination drawNumbers;

    /**
     * Constructor privado para evitar que pueda ser invocado sin parámetros
     */
    @SuppressWarnings("unused")
    private Draw() {

    }

    public Draw(TDateInteger date, TNumberList numbers, int complementary, Short drawTypeId) {
        this.drawDate = date;
        drawNumbers = new NumberCombination(numbers, complementary);
        this.drawTypeId = drawTypeId;
    }

    /**
     * Devuelve un string con la representación del objeto
     */
    public String toString() {
        return "[" +
                drawDate +
                "," +
                drawNumbers.toString() +
                "]";
    }

    /**
     * Returns the list of numbers of the draw, including the complementary
     *
     * @return The draw number list
     */
    public TNumberList getNumbersWC() {
        return drawNumbers.getNumbersWC();
    }

    /**
     * Devuelve el objeto con la combinación (y el complementario)
     *
     * @return El objeto con la combinación (y el complementario)
     */
    public NumberCombination getNumberCombination() {
        return drawNumbers;
    }

    /**
     * Devuelve uno de los números, del 1 al 6
     */
    public int getNumber(int index) {
        return getNumberCombination().getNumbersAsArray()[index - 1];
    }

    /**
     * Devuelve el complementario
     *
     * @return El complementario del sorteo
     */
    public byte getComplementary() {
        return drawNumbers.getComplementary();
    }

    /**
     * Compara dos sorteos para poder ordenarlos. Se basa en la fecha
     *
     * @param other El otro sorteo con el que comparar
     */
    @Override
    public int compareTo(Draw other) {
        return this.drawDate.compareTo(other.drawDate);
    }

    /**
     * Compara dos sorteos para poder indicar si son iguales o no, revisando todos los campos: fechas, números y complementario
     *
     * @param other El otro sorteo con el que comparar
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Draw otherDraw = (Draw) other;
        return this.drawDate.equals(otherDraw.drawDate) && drawNumbers.equals(otherDraw.drawNumbers, false);
    }

    @Override
    public int hashCode() {
        return drawDate.hashCode();
    }
}