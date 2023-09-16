package ru.otus.homework.service.greeting;

import org.junit.jupiter.api.*;
import ru.otus.homework.domain.User;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.localization.LocalizationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Greeting service")
class GreetingServiceImplTest {

    private static final String NAME = "John";

    private static final String SURNAME = "Johns";

    private static final String NAME_REGISTRATION = "Enter name:";

    private static final String SURNAME_REGISTRATION = "Enter surname:";

    private IOService ioService;

    private GreetingService greetingService;

    private LocalizationService localizationService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        localizationService = mock(LocalizationService.class);
        greetingService = new GreetingServiceImpl(ioService, localizationService);
    }

    @Test
    public void should_return_correct_registered_user() {
        User expectedUser = new User(NAME, SURNAME);

        when(localizationService.getMessage("greeting.name")).thenReturn(NAME_REGISTRATION);
        when(localizationService.getMessage("greeting.surname")).thenReturn(SURNAME_REGISTRATION);
        when(ioService.readStringWithPrompt(NAME_REGISTRATION + " ")).thenReturn(NAME);
        when(ioService.readStringWithPrompt(SURNAME_REGISTRATION + " ")).thenReturn(SURNAME);

        User actualUser = greetingService.runRegistration();

        assertThat(expectedUser).usingRecursiveComparison().isEqualTo(actualUser);
    }
}