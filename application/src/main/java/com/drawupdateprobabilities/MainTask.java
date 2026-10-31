package com.drawupdateprobabilities;

import com.drawupdateprobabilities.application.usecases.CalculateProbabilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("unused")
// This component class is executed when the application starts. Here invokes to the application service who does the action
public class MainTask implements CommandLineRunner {
    private final CalculateProbabilities service;

    public MainTask(CalculateProbabilities calculateProbabilities) {
        this.service = calculateProbabilities;
    }

    @Override
    public void run(String... args) {
        log.info("Iniciando tarea principal...");
        service.executeAll();
        log.info("Tarea completada.");
    }
}