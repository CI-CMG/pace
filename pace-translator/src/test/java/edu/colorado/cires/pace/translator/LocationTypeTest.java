package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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
  
  @ParameterizedTest
  @ValueSource(classes = {
      StationaryMarineLocation.class,
      MultiPointStationaryMarineLocation.class,
      MobileMarineLocation.class,
      StationaryTerrestrialLocation.class,
      LocationDetail.class
  })
  void fromLocationDetail(Class<? extends LocationDetail> clazz) {
    LocationType expectedType = switch (clazz.getSimpleName()) {
      case "StationaryMarineLocation" -> LocationType.STATIONARY_MARINE;
      case "MultiPointStationaryMarineLocation" -> LocationType.MULTIPOINT_STATIONARY_MARINE;
      case "MobileMarineLocation" -> LocationType.MOBILE_MARINE;
      case "StationaryTerrestrialLocation" -> LocationType.STATIONARY_TERRESTRIAL;
      case "LocationDetail" -> null;
      default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
    };
    
    if (expectedType == null) {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> LocationType.fromLocationDetail(mock(clazz)));
      assertTrue(exception.getMessage().contains("Invalid location type"));
    } else {
      assertEquals(expectedType, LocationType.fromLocationDetail(mock(clazz)));
    }
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