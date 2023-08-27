package ru.otus.homework.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ApplicationModeServiceImpl implements ApplicationModeService {

    private final AtomicBoolean resultProcessingFlag;

    private final AtomicBoolean registrationFlag;

    private final AtomicBoolean questionProcessingFlag;

    public ApplicationModeServiceImpl() {
        this.registrationFlag = new AtomicBoolean(true);
        this.questionProcessingFlag = new AtomicBoolean(false);
        this.resultProcessingFlag = new AtomicBoolean(false);
    }

    @Override
    public boolean isRegistrationRunning() {
        return registrationFlag.get();
    }

    @Override
    public boolean isTestProcessingRunning() {
        return questionProcessingFlag.get();
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
        registrationFlag.set(false);
        questionProcessingFlag.set(true);
    }

    @Override
    public void stopTestProcessing() {
        questionProcessingFlag.set(false);
        resultProcessingFlag.set(true);
    }

    @Override
    public void restartTestProcessing() {
        resultProcessingFlag.set(false);
        questionProcessingFlag.set(true);
    }

    @Override
    public void stopApplication() {
        registrationFlag.set(false);
        questionProcessingFlag.set(false);
        resultProcessingFlag.set(false);
    }
}
