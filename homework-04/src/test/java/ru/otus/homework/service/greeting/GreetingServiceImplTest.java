package ru.otus.homework.service.greeting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.homework.domain.User;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.localization.LocalizationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Greeting service")
@SpringBootTest
class GreetingServiceImplTest {

    private static final String NAME = "John";

    private static final String SURNAME = "Johns";

    private static final String NAME_REGISTRATION = "Enter name:";

    private static final String SURNAME_REGISTRATION = "Enter surname:";

    @MockBean
    private IOService ioService;

    @MockBean
    private LocalizationService localizationService;

    @Autowired
    private GreetingService greetingService;

    @SpringBootConfiguration
    @Import(GreetingServiceImpl.class)
    static class GreetingServiceConfiguration {
    }

    @Test
    @DisplayName("should return correct registered user")
    public void shouldReturnCorrectRegisteredUser() {
        User expectedUser = new User(NAME, SURNAME);

        given(localizationService.getMessage("greeting.name")).willReturn(NAME_REGISTRATION);
        given(localizationService.getMessage("greeting.surname")).willReturn(SURNAME_REGISTRATION);
        given(ioService.readStringWithPrompt(NAME_REGISTRATION + " ")).willReturn(NAME);
        given(ioService.readStringWithPrompt(SURNAME_REGISTRATION + " ")).willReturn(SURNAME);

        User actualUser = greetingService.runRegistration();

        assertThat(expectedUser).usingRecursiveComparison().isEqualTo(actualUser);
    }
}