package ru.otus.homework.service.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOServiceStreams implements IOService {
    private final PrintStream output;

    private final Scanner input;

    public IOServiceStreams(@Value("#{T(java.lang.System).out}") PrintStream outputStream,
                            @Value("#{T(java.lang.System).in}") InputStream inputStream) {
        output = outputStream;
        input = new Scanner(inputStream);
    }

    @Override
    public void outputStringLine(String s) {
        output.println(s);
    }

    @Override
    public void outputString(String s) {
        output.print(s);
    }

    @Override
    public int readIntWithPrompt(String prompt) {
        outputString(prompt);
        return Integer.parseInt(input.nextLine());
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        outputString(prompt);
        return input.nextLine();
    }
}
