package ru.otus.homework.service.processor.solver;

import ru.otus.homework.dto.Decision;
import ru.otus.homework.dto.Weather;

public interface WeatherSolver {

  Decision solve(Weather weather);
}
