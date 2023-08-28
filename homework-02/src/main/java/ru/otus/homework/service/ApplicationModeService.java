package ru.otus.homework.service;

public interface ApplicationModeService {
    boolean isRegistrationRunning();

    boolean isTestProcessingRunning();

    boolean isResultProcessingRunning();

    default boolean isApplicationRunning() {
        return isRegistrationRunning() || isTestProcessingRunning() || isResultProcessingRunning();
    }

    void stopRegistrationProcessing();

    void stopTestProcessing();

    void stopApplication();
}
