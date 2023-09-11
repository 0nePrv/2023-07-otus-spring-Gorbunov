package ru.otus.homework.service.greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.User;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.localization.LocalizationService;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final IOService ioService;

    private final LocalizationService localizationService;

    @Autowired
    public GreetingServiceImpl(IOService ioService,
                               LocalizationService localizationService) {
        this.ioService = ioService;
        this.localizationService = localizationService;
    }


    @Override
    public User runRegistration() {
        String greetingIntroMessage = localizationService.getMessage("greeting.introduction");
        ioService.outputStringLine(greetingIntroMessage);
        User user;
        while (true) {
            user = getNewUser();
            if (!user.getName().isBlank() && !user.getSurname().isBlank()) {
                break;
            }
            String greetingInvalidMessage = localizationService.getMessage("greeting.invalid");
            ioService.outputStringLine(greetingInvalidMessage);
        }
        return user;
    }

    private User getNewUser() {
        String nameMessage = localizationService.getMessage("greeting.name");
        String surnameMessage = localizationService.getMessage("greeting.surname");
        var name = ioService.readStringWithPrompt(nameMessage + " ").trim();
        var surname = ioService.readStringWithPrompt(surnameMessage + " ").trim();
        return new User(name, surname);
    }
}