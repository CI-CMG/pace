package edu.colorado.cires.pace.translator;

enum LocationType {
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
}
