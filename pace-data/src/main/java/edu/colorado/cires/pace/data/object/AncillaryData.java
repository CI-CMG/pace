package edu.colorado.cires.pace.data.object;

import java.nio.file.Path;

public interface AncillaryData {
  
  Path getTemperaturePath();
  Path getBiologicalPath();
  Path getOtherPath();

}
