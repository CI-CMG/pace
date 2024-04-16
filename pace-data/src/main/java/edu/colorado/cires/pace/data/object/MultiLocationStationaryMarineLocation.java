package edu.colorado.cires.pace.data.object;

import java.util.List;

public interface MultiLocationStationaryMarineLocation extends MarineLocation {
  
  List<MarineInstrumentLocation> getLocations();

}
