package com.drawupdateprobabilities;

import com.drawupdateprobabilities.application.usecases.CalculateCombinationsUseCase;
import com.drawupdateprobabilities.application.usecases.GenerateTypesAndCalculateProbabilities;
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
    private final GenerateTypesAndCalculateProbabilities generateTypesAndCalculateProbabilities;
    private final CalculateCombinationsUseCase calculateCombinationsUseCase;

    @Override
    public void run(String... args) {
        log.info("Iniciando tarea principal...");
        generateTypesAndCalculateProbabilities.generateProbabilityTypesAndValuesByDraw();
        calculateCombinationsUseCase.calculateAndSaveNotExistentCombinations();
        log.info("Tarea completada.");
    }
}