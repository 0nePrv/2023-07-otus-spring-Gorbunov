package ru.otus.homework.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.UserValidationException;
import ru.otus.homework.exceptions.fatal.UnsupportedValidationTypeException;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Validator userValidator;

    @Autowired
    public UserServiceImpl(Validator userValidator) {
        this.userValidator = userValidator;
    }

    public void validateUser(User user) {
        checkTypeSupport();
        var errors = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            var errorString = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new UserValidationException(errorString);
        }
    }

    private void checkTypeSupport() {
        if (!userValidator.supports(User.class)) {
            throw new UnsupportedValidationTypeException("User validation not supported");
        }
    }
}
