package ru.otus.homework.service.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.otus.homework.domain.User;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        var user = (User) target;
        validateStringField("name", user.getName(), 2, 10, e);
        validateStringField("surname", user.getSurname(), 3, 15, e);
    }

    private void validateStringField(String field, String value, int minLength, int maxLength, Errors e) {
        ValidationUtils.rejectIfEmptyOrWhitespace(e, field, field + ".empty", "Empty " + field);
        if (!e.getFieldErrors(field).isEmpty()) {
            return;
        }
        var length = value.length();
        if (length < minLength || length > maxLength || Character.isLowerCase(value.charAt(0)) || value.contains(" ")) {
            e.rejectValue(field, field + ".incorrect", "Wrong " + field + " format");
        }
    }
}
