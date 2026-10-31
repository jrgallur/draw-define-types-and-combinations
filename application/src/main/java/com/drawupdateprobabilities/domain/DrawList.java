package com.drawupdateprobabilities.domain;


import com.drawupdateprobabilities.domain.types.TDateInteger;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawList {
    private final Map<TDateInteger, Draw> draws;
    private final Map<TDateInteger, TDateInteger> previousDate;
    private final Map<TDateInteger, TDateInteger> nextDate;

    /**
     * -- GETTER --
     *  The date of the first draw in list
     */
    @Getter
    private final TDateInteger firstDrawDate;

    /**
     * -- GETTER --
     *  The date of the last draw in list
     */
    @Getter
    private final TDateInteger lastDrawDate;


    /**
     * Devuelve la fecha del anterior sorteo a la recibida
     *
     * @param dateInteger Fecha del sorteo
     * @return La fecha del anterior sorteo a la recibida o null si no existe
     */
    public TDateInteger getPreviousDate(TDateInteger dateInteger) {
        return previousDate.get(dateInteger);
    }

    /**
     * Devuelve la fecha del siguiente sorteo a la recibida
     *
     * @param dateInteger Fecha del sorteo
     * @return La fecha del siguiente sorteo a la recibida o null si no existe
     */
    public TDateInteger getNextDate(TDateInteger dateInteger) {
        return nextDate.get(dateInteger);
    }

    /**
     * Devuelve un sorteo a partir de una fecha
     *
     * @param dateInteger Fecha del sorteo
     * @return El sorteo correspondiente a esa fecha o null si no se encuentra ninguno
     */
    public Draw getDraw(TDateInteger dateInteger) {
        return draws.get(dateInteger);
    }

    /**
     * Crea y guarda una lista de sorteos de manera ordenada
     *
     * @param drawList Lista de sorteos a guardar
     */
    public DrawList(List<Draw> drawList) {
        // Ordenamos la lista de sorteos
        drawList.sort(null);
        draws = new HashMap<>();
        previousDate = new HashMap<>();
        nextDate = new HashMap<>();
        // Recorremos la lista (ahora ordenada) secuencialmente
        for (int cont = 0; cont < drawList.size(); cont++) {
            draws.put(drawList.get(cont).getDrawDate(), drawList.get(cont));
            if (cont > 0) {
                previousDate.put(drawList.get(cont).getDrawDate(), drawList.get(cont - 1).getDrawDate());
                nextDate.put(drawList.get(cont - 1).getDrawDate(), drawList.get(cont).getDrawDate());
            }
        }
        // Establecemos la primera y la última fecha de los sorteos
        firstDrawDate = drawList.get(0).getDrawDate();
        lastDrawDate = drawList.get(drawList.size() - 1).getDrawDate();
    }

    public TDateInteger findFirstDrawDateFromDate(TDateInteger dateFrom) {
        if (getDraw(dateFrom)!=null) {
            return dateFrom;
        }
        while (getDraw(dateFrom)==null) {
            dateFrom = dateFrom.add(TDateInteger.addTypes.DAY, 1);
        }
        return dateFrom;
    }
}