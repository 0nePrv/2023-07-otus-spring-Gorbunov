package ru.otus.homework.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationPropertiesHolder;

import java.nio.file.Path;

@Component
public class ResourcePathProviderImpl implements ResourcePathProvider {

    private final String resourceName;

    private final String dataDir;

    @Autowired
    public ResourcePathProviderImpl(ApplicationPropertiesHolder holder) {
        this.resourceName = holder.getResourceName();
        this.dataDir = holder.getDataDir();
    }

    @Override
    public Path getPath() {
        return Path.of(dataDir, resourceName);
    }
}
