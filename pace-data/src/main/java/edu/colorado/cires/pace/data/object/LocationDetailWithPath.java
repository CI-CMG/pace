package edu.colorado.cires.pace.data.object;

import java.nio.file.Path;

public interface LocationDetailWithPath extends LocationDetail {
  
  Path getNavigationFiles();

}
