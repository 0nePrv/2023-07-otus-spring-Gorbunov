package ru.otus.homework.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Validator;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.UnsupportedValidationTypeException;
import ru.otus.homework.service.io.InputService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("User service")
public class UserServiceImplTest {

    @Mock
    private Validator mockValidator;

    @Mock
    private InputService mockInputService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void should_successfully_register_user() {
        when(mockValidator.supports(User.class)).thenReturn(true);
        when(mockInputService.readStringWithPrompt("Enter your name: ")).thenReturn("John");
        when(mockInputService.readStringWithPrompt("Enter your surname: ")).thenReturn("Doe");

        assertDoesNotThrow(() -> userService.registerUser());
    }

    @Test
    void should_throw_exception_if_type_is_not_supported() {
        User user = new User("John", "Doe");
        when(mockValidator.supports(User.class)).thenReturn(false);

        assertThrows(UnsupportedValidationTypeException.class, () -> userService.validateUser(user));
    }


    @Test
    void should_return_current_user() {
        when(mockValidator.supports(User.class)).thenReturn(true);
        User user = new User("John", "Doe");
        userService.validateUser(user);

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(user, currentUser);
    }
}
