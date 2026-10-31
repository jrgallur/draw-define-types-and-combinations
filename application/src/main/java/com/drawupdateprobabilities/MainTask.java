package com.drawupdateprobabilities;

import com.drawupdateprobabilities.application.usecases.TypeCombinationUseCase;
import com.drawupdateprobabilities.application.usecases.TypeUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@SuppressWarnings("unused")
// This component class is executed when the application starts. Here invokes to the application service who does the action
public class MainTask implements CommandLineRunner {
    private final TypeUseCase typeUseCase;
    private final TypeCombinationUseCase calculateCombinationsUseCase;

    @Override
    public void run(String... args) {
        log.info("Iniciando tarea principal...");
        typeUseCase.generateProbabilityTypesAndValuesByDraw();
        calculateCombinationsUseCase.calculateAndSaveNotExistentCombinations();
        log.info("Tarea completada.");
    }
}