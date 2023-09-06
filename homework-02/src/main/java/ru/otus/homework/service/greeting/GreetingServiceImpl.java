package ru.otus.homework.service.greeting;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.User;
import ru.otus.homework.service.io.IOService;

@Service
public class GreetingServiceImpl implements GreetingService {

    private static final String USER_GREETING = """
            Welcome to our testing program!
            Test your knowledge in various fields, evaluate your results, and pinpoint areas for improvement.
            Register now to dive into the world of learning and self-assessment.
            """;

    private final IOService ioService;

    public GreetingServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }


    @Override
    public User runRegistration() {
        ioService.outputStringLine(USER_GREETING);
        User user;
        while (true) {
            user = getNewUser();
            if (!user.getName().isBlank() && !user.getSurname().isBlank()) {
                break;
            }
            ioService.outputStringLine("Incorrect name or surname. Please try again");
        }
        return user;
    }

    private User getNewUser() {
        var name = ioService.readStringWithPrompt("Enter your name: ").trim();
        var surname = ioService.readStringWithPrompt("Enter your surname: ").trim();
        return new User(name, surname);
    }
}