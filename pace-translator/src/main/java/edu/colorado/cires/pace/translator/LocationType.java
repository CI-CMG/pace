package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryTerrestrialLocation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides list of location types
 */
public enum LocationType {
  STATIONARY_MARINE("stationary marine"),
  MULTIPOINT_STATIONARY_MARINE("multipoint stationary marine"),
  MOBILE_MARINE("mobile marine"),
  STATIONARY_TERRESTRIAL("stationary terrestrial");

  private final String name;

  LocationType(String name) {
    this.name = name;
  }

  /**
   * Returns name of location type
   * @return String location type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns type given string of name
   * @param name string name of location type
   * @return LocationType location type
   */
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

  /**
   * Returns location type of location detail
   * @param locationDetail locationDetail to check type of
   * @return LocationType of data package
   */
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
