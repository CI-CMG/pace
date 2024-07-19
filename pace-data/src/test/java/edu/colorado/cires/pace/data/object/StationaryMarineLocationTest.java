package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class StationaryMarineLocationTest extends MarineLocationTest<StationaryMarineLocation> {

  @Override
  protected MarineLocation createObject() {
    return StationaryMarineLocation.builder().build();
  }
}