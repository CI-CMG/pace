package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;

class StationaryMarineLocationTest extends MarineLocationTest<StationaryMarineLocation> {

  @Override
  protected MarineLocation createObject() {
    return StationaryMarineLocation.builder().build();
  }
}