package ru.otus.homework.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Forecast {

  private final List<Decision> decisions = new ArrayList<>();

  @Getter
  private final String city;

  public Forecast(String city) {
    this.city = city;
  }

  public void addDecision(Decision decision) {
    decisions.add(decision);
  }

  public List<Decision> getDecisions() {
    return List.copyOf(decisions);
  }
}
