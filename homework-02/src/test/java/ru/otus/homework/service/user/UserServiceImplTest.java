package ru.otus.homework.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.fatal.UnsupportedValidationTypeException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private Validator mockValidator;
    private UserService userService;

    @BeforeEach
    public void setup() {
        mockValidator = mock(Validator.class);
        userService = new UserServiceImpl(mockValidator);
    }

    @Test
    public void validateUser_SupportedType_UserValidationPassed() {
        User user = new User("John", "Doe");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "user");

        when(mockValidator.supports(User.class)).thenReturn(true);
        doNothing().when(mockValidator).validate(user, errors);

        assertDoesNotThrow(() -> userService.validateUser(user));
        verify(mockValidator).supports(User.class);
        verify(mockValidator).validate(user, errors);
    }

    @Test
    public void validateUser_UnsupportedType_ExceptionThrown() {
        User user = new User("John", "Doe");

        when(mockValidator.supports(User.class)).thenReturn(false);

        assertThrows(UnsupportedValidationTypeException.class, () -> userService.validateUser(user));
        verify(mockValidator).supports(User.class);
    }
}
