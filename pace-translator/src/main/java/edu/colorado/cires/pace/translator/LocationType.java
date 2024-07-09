package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import java.util.Arrays;
import java.util.List;
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
  
  public static LocationType fromLocationDetail(LocationDetail locationDetail) {
    return switch (locationDetail.getClass().getSimpleName()) {
      case "StationaryMarineLocation" -> STATIONARY_MARINE;
      case "MultiPointStationaryMarineLocation" -> MULTIPOINT_STATIONARY_MARINE;
      case "MobileMarineLocation" -> MOBILE_MARINE;
      case "StationaryTerrestrialLocation" -> STATIONARY_TERRESTRIAL;
      default -> throw new IllegalArgumentException(String.format(
          "Invalid location type: %s. Was not one of: %s",
          locationDetail.getClass().getSimpleName(),
          String.join(", ", List.of(
              StationaryMarineLocation.class.getSimpleName(),
              MultiPointStationaryMarineLocation.class.getSimpleName(),
              MobileMarineLocation.class.getSimpleName(),
              StationaryTerrestrialLocation.class.getSimpleName()
          ))
      ));
    };
  }
}
