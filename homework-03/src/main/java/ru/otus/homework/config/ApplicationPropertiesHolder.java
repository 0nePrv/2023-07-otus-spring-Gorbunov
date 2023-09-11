package ru.otus.homework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Map;

@ConfigurationProperties(prefix = "custom.testing")
public class ApplicationPropertiesHolder {

    private final Map<String, String> resourceMap;

    private final String dataDirPath;

    private final String localeTag;

    private final int totalScore;

    private final int passingScore;


    @ConstructorBinding
    public ApplicationPropertiesHolder(Map<String, String> resourceMap, String dataDirPath,
                                       String localeTag, int scoreTotal, int scorePassing) {
        this.resourceMap = resourceMap;
        this.dataDirPath = dataDirPath;
        this.totalScore = scoreTotal;
        this.passingScore = scorePassing;
        this.localeTag = localeTag;
    }

    public String getResourcePath() {
        String resourceDir = dataDirPath + (dataDirPath.endsWith("/") ? "" : "/");
        if (resourceMap.containsKey(localeTag)) {
            return resourceDir + resourceMap.get(localeTag);
        }
        return resourceDir + resourceMap.get("default");
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
