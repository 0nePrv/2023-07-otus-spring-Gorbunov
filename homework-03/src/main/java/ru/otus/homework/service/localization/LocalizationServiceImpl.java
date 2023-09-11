package ru.otus.homework.service.localization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class LocalizationServiceImpl implements LocalizationService {

    private final LocaleProvider localeProvider;

    private final MessageSource messageSource;

    @Autowired
    public LocalizationServiceImpl(LocaleProvider localeProvider,
                                   MessageSource messageSource) {
        this.localeProvider = localeProvider;
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, localeProvider.getCurrent());
    }
}
