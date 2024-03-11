package ru.otus.homework.service.city;

import java.util.Random;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.City;

@Service
public class CityServiceImpl implements CityService {

  @Override
  public City getRandom() {
    City[] cities = City.values();
    int randomInt = new Random().nextInt(0, cities.length);
    return cities[randomInt];
  }
}
