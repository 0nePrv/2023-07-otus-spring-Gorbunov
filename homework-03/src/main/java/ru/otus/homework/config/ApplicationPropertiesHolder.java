package ru.otus.homework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Map;

@ConfigurationProperties(prefix = "custom.testing")
public class ApplicationPropertiesHolder {

    private final Map<String, String> resourcePathMap;

    private final String localeTag;

    private final int totalScore;

    private final int passingScore;


    @ConstructorBinding
    public ApplicationPropertiesHolder(Map<String, String> resourcePathMap, String localeTag,
                                       int scoreTotal, int scorePassing) {
        this.resourcePathMap = resourcePathMap;
        this.totalScore = scoreTotal;
        this.passingScore = scorePassing;
        this.localeTag = localeTag;
    }

    public String getResourcePath() {
        if (resourcePathMap.containsKey(localeTag)) {
            return resourcePathMap.get(localeTag);
        }
        return resourcePathMap.get("default");
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public String getLocaleTag() {
        return localeTag;
    }
}
