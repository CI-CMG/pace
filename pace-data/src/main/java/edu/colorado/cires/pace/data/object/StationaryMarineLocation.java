package edu.colorado.cires.pace.data.object;

public interface StationaryMarineLocation extends MarineLocation {
  
  MarineInstrumentLocation getDeploymentLocation();
  MarineInstrumentLocation getRecoveryLocation();

}
