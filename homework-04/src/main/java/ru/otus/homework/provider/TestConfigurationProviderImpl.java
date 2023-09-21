package ru.otus.homework.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationPropertiesHolder;

@Component
public class TestConfigurationProviderImpl implements TestConfigurationProvider {

    private final int totalScore;

    private final int passingScore;

    @Autowired
    public TestConfigurationProviderImpl(ApplicationPropertiesHolder holder) {
        this.totalScore = holder.getTotalScore();
        this.passingScore = holder.getPassingScore();
    }

    @Override
    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public int getPassingScore() {
        return passingScore;
    }
}
