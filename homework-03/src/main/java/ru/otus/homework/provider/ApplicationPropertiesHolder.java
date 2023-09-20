package ru.otus.homework.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Map;

@ConfigurationProperties(prefix = "custom.testing")
public class ApplicationPropertiesHolder {

    private final Map<String, String> resourceMap;

    private final String dataDir;

    private final String localeTag;

    private final int totalScore;

    private final int passingScore;


    @ConstructorBinding
    public ApplicationPropertiesHolder(Map<String, String> resourceMap, String dataDir,
                                       String localeTag, int scoreTotal, int scorePassing) {
        this.resourceMap = resourceMap;
        this.dataDir = dataDir;
        this.totalScore = scoreTotal;
        this.passingScore = scorePassing;
        this.localeTag = localeTag;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public String getResourceName() {
        var containsLocaleTag = resourceMap.containsKey(localeTag);
        return resourceMap.get(containsLocaleTag ? localeTag : "default");
    }

    public String getDataDir() {
        return dataDir;
    }

    public String getCurrentLocale() {
        return localeTag;
    }
}
