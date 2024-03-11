package ru.otus.homework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

  @JsonIgnore
  private String city;

  @JsonProperty("last_updated")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime lastUpdated;

  @JsonProperty("temp_c")
  private double temperature;

  @JsonProperty("wind_mph")
  private double windMph;

  @JsonProperty("pressure_mb")
  private double pressureMb;

  @JsonProperty("vis_miles")
  private double visibilityMiles;
}