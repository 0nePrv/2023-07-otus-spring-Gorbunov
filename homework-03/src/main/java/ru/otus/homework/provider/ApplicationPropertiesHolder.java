package ru.otus.homework.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "custom.testing")
public class ApplicationPropertiesHolder implements TestConfigurationProvider, ResourcePathProvider, LocaleProvider {

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

    @Override
    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public int getPassingScore() {
        return passingScore;
    }

    @Override
    public Path getPath() {
        var containsLocaleTag = resourceMap.containsKey(localeTag);
        var resourceName = resourceMap.get(containsLocaleTag ? localeTag : "default");
        return Path.of(dataDir, resourceName);
    }

    @Override
    public Locale getCurrentLocale() {
        return Locale.forLanguageTag(localeTag);
    }
}
