package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocationTypeTest {

  @ParameterizedTest
  @CsvSource(value = {
      "stationary marine,STATIONARY_MARINE",
      "multipoint stationary marine,MULTIPOINT_STATIONARY_MARINE",
      "mobile marine,MOBILE_MARINE",
      "stationary terrestrial,STATIONARY_TERRESTRIAL"
  })
  void fromName(String inputString, LocationType expectedType) {
    assertEquals(expectedType, LocationType.fromName(inputString));
  }
  
  @Test
  void fromNameInvalidName() {
    String name = "invalid";
    Exception exception = assertThrows(IllegalArgumentException.class, () -> LocationType.fromName(name));
    assertEquals(String.format(
        "Invalid location type: %s. Was not one of: %s",
        name,
        Arrays.stream(LocationType.values())
            .map(LocationType::getName)
            .collect(Collectors.joining(", "))
    ), exception.getMessage());
  }
}