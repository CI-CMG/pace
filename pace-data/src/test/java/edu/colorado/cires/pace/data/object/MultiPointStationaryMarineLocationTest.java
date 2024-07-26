package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MultiPointStationaryMarineLocation;

class MultiPointStationaryMarineLocationTest extends MarineLocationTest<MultiPointStationaryMarineLocation> {

  @Override
  protected MarineLocation createObject() {
    return MultiPointStationaryMarineLocation.builder().build();
  }
}