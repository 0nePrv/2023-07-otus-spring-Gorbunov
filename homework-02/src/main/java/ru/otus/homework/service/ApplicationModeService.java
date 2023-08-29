package ru.otus.homework.service;

public interface ApplicationModeService {
    boolean isRegistrationRunning();

    boolean isTestProcessingRunning();

    boolean isResultProcessingRunning();

    void stopRegistrationProcessing();

    void stopTestProcessing();

    void stopApplication();
}
