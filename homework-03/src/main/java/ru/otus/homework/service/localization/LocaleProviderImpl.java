package ru.otus.homework.service.localization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationPropertiesHolder;

import java.util.Locale;

@Component
public class LocaleProviderImpl implements LocaleProvider {

    private final Locale locale;

    @Autowired
    public LocaleProviderImpl(ApplicationPropertiesHolder applicationPropertiesHolder) {
        String localeTag = applicationPropertiesHolder.getLocaleTag();
        this.locale = Locale.forLanguageTag(localeTag);
    }

    @Override
    public Locale getCurrent() {
        return locale;
    }
}
