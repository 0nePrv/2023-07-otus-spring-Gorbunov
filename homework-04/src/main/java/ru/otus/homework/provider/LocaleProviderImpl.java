package ru.otus.homework.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationPropertiesHolder;

import java.util.Locale;

@Component
public class LocaleProviderImpl implements LocaleProvider {
    
    private final String localeTag;

    @Autowired
    public LocaleProviderImpl(ApplicationPropertiesHolder holder) {
        this.localeTag = holder.getCurrentLocale();
    }

    @Override
    public Locale getCurrent() {
        return Locale.forLanguageTag(localeTag);
    }
}
