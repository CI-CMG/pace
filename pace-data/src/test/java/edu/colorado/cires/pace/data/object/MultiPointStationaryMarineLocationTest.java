package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

class MultiPointStationaryMarineLocationTest extends MarineLocationTest<MultiPointStationaryMarineLocation> {

  @Override
  protected MarineLocation createObject() {
    return MultiPointStationaryMarineLocation.builder().build();
  }
}