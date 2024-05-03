package edu.colorado.cires.pace.translator;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum LocationType {
  STATIONARY_MARINE("stationary marine"),
  MULTIPOINT_STATIONARY_MARINE("multipoint stationary marine"),
  MOBILE_MARINE("mobile marine"),
  STATIONARY_TERRESTRIAL("stationary terrestrial");

  private final String name;

  LocationType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
  
  public static LocationType fromName(String name) {
    return switch (name) {
      case "stationary marine" -> STATIONARY_MARINE;
      case "multipoint stationary marine" -> MULTIPOINT_STATIONARY_MARINE;
      case "mobile marine" -> MOBILE_MARINE;
      case "stationary terrestrial" -> STATIONARY_TERRESTRIAL;
      default -> throw new IllegalArgumentException(String.format(
          "Invalid location type: %s. Was not one of: %s",
          name,
          Arrays.stream(LocationType.values())
              .map(LocationType::getName)
              .collect(Collectors.joining(", "))
      ));
    };
  }
}
