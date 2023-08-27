package ru.otus.homework.service;

public interface ApplicationModeService {
    boolean isRegistrationRunning();

    boolean isTestProcessingRunning();

    boolean isResultProcessingRunning();

    boolean isApplicationRunning();

    void stopRegistrationProcessing();

    void stopTestProcessing();

    void stopApplication();

    void restartTestProcessing();
}
