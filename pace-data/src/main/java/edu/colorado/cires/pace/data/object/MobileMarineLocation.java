package edu.colorado.cires.pace.data.object;

public interface MobileMarineLocation extends MarineLocation {
  
  Ship getVessel();
  String getLocationDerivationDescription();

}
