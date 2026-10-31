package com.drawupdateprobabilities.mappers;

import com.drawupdateprobabilities.domain.MatrixOccurrence;
import com.drawupdateprobabilities.domain.MatrixOccurrenceMO;
import com.drawupdateprobabilities.domain.types.TDateInteger;
import com.drawupdateprobabilities.domain.types.TDrawOccurrences;
import com.drawupdateprobabilities.domain.types.TOccurrences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.*;

@Mapper(componentModel = "spring")
public interface MatrixOccurrenceMapper {
    @Mapping(target = "untilDate", expression = "java(matrixOccurrence.getUntilDate().toInteger())")
    @Mapping(target = "occurrences", expression = "java(matrixOccurrence.getOccurrences().toString())")
    MatrixOccurrenceMO toModel(MatrixOccurrence matrixOccurrence);

    @Mapping(target = "untilDate", expression = "java(toTDateInteger(matrixOccurrenceMO.getUntilDate()))")
    @Mapping(target = "occurrences", expression = "java(stringToOccurrences(matrixOccurrenceMO.getOccurrences()))")
    MatrixOccurrence toDomain(MatrixOccurrenceMO matrixOccurrenceMO);

    default TOccurrences stringToOccurrences(String input) {
        if (input == null || input.isBlank()) {
            return new TDrawOccurrences(List.of());
        }

        // Dividimos en pares "índice:valor"
        String[] pairs = input.split(";");
        Map<Integer, Integer> map = new HashMap<>();
        int maxIndex = 0;

        for (String pair : pairs) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                int index = Integer.parseInt(parts[0]);
                int value = Integer.parseInt(parts[1]);
                map.put(index, value);
                maxIndex = Math.max(maxIndex, index);
            }
        }

        // Creamos la lista hasta el último índice con valor
        List<Integer> result = new ArrayList<>(Collections.nCopies(maxIndex + 1, 0));
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result.set(entry.getKey(), entry.getValue());
        }

        // Completamos con 0's hasta la siguiente potencia de 2, si es necesario
        if (!isPowerOfTwo(maxIndex + 1 )) {
            int maxSize = nextPowerOfTwo(maxIndex + 1);
            for (int cont = maxIndex + 1; cont < maxSize; cont++) {
                result.add(0);
            }
        }

        return new TDrawOccurrences(result);
    }

    default boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    default int nextPowerOfTwo(int value) {
        if (value <= 0) {
            return 1; // el primer 2^n mayor que 0 es 1 (2^0)
        }

        int power = Integer.highestOneBit(value);
        return (power == value) ? power : power << 1;
    }

    default TDateInteger toTDateInteger(Integer value) {
        return new TDateInteger(value);
    }

}