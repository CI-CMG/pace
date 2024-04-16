package edu.colorado.cires.pace.data.object;

public interface MarineInstrumentLocation extends LatLonPair {
  Float getSeaFloorDepth();
  Float getInstrumentDepth();
}
