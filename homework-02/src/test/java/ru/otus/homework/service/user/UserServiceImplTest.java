package ru.otus.homework.service.user;

import org.junit.jupiter.api.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.fatal.UnsupportedValidationTypeException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("User service")
public class UserServiceImplTest {

    private Validator mockValidator;
    private UserService userService;

    @BeforeEach
    public void setup() {
        mockValidator = mock(Validator.class);
        userService = new UserServiceImpl(mockValidator);
    }

    @Test
    public void should_not_throw_exception_when_user_validation_is_supported() {
        User user = new User("John", "Johns");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "user");

        when(mockValidator.supports(User.class)).thenReturn(true);
        doNothing().when(mockValidator).validate(user, errors);

        assertDoesNotThrow(() -> userService.validateUser(user));
        verify(mockValidator, times(1)).supports(User.class);
        verify(mockValidator, times(1)).validate(user, errors);
    }

    @Test
    public void should_throw_exception_when_user_validation_is_not_supported() {
        User user = new User("John", "Johns");

        when(mockValidator.supports(User.class)).thenReturn(false);

        assertThrows(UnsupportedValidationTypeException.class, () -> userService.validateUser(user));
        verify(mockValidator).supports(User.class);
    }
}
