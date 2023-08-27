package ru.otus.homework.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ApplicationModeServiceImpl implements ApplicationModeService {

    private final AtomicBoolean resultProcessingFlag;

    private final AtomicBoolean registrationProcessingFlag;

    private final AtomicBoolean testProcessingFlag;

    public ApplicationModeServiceImpl() {
        this.registrationProcessingFlag = new AtomicBoolean(true);
        this.testProcessingFlag = new AtomicBoolean(false);
        this.resultProcessingFlag = new AtomicBoolean(false);
    }

    @Override
    public boolean isRegistrationRunning() {
        return registrationProcessingFlag.get();
    }

    @Override
    public boolean isTestProcessingRunning() {
        return testProcessingFlag.get();
    }

    @Override
    public boolean isResultProcessingRunning() {
        return resultProcessingFlag.get();
    }

    @Override
    public boolean isApplicationRunning() {
        return isRegistrationRunning() || isTestProcessingRunning() || isResultProcessingRunning();
    }

    @Override
    public void stopRegistrationProcessing() {
        registrationProcessingFlag.set(false);
        testProcessingFlag.set(true);
    }

    @Override
    public void stopTestProcessing() {
        testProcessingFlag.set(false);
        resultProcessingFlag.set(true);
    }

    @Override
    public void restartTestProcessing() {
        resultProcessingFlag.set(false);
        testProcessingFlag.set(true);
    }

    @Override
    public void stopApplication() {
        registrationProcessingFlag.set(false);
        testProcessingFlag.set(false);
        resultProcessingFlag.set(false);
    }
}
