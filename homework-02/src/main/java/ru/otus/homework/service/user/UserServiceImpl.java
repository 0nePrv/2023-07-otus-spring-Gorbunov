package ru.otus.homework.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.UnsupportedValidationTypeException;
import ru.otus.homework.exceptions.UserValidationException;
import ru.otus.homework.service.io.InputService;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Validator userValidator;

    private final InputService inputService;

    private User currentUser;

    @Autowired
    public UserServiceImpl(Validator userValidator,
                           InputService inputService) {
        this.userValidator = userValidator;
        this.inputService = inputService;
    }

    @Override
    public void registerUser() {
        String name = inputService.readStringWithPrompt("Enter your name: ").trim();
        String surname = inputService.readStringWithPrompt("Enter your surname: ").trim();
        validateUser(new User(name, surname));
    }

    public void validateUser(User user) {
        if (!userValidator.supports(User.class)) {
            throw new UnsupportedValidationTypeException("User validation not supported");
        }
        Errors errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            String errorString = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new UserValidationException(errorString);
        } else {
            currentUser = user;
        }
    }

    @Override
    public User getCurrentUser() {
        if (currentUser == null) {
            registerUser();
        }
        return currentUser;
    }
}
