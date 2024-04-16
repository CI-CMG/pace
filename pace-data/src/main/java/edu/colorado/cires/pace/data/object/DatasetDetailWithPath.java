package edu.colorado.cires.pace.data.object;

import java.nio.file.Path;

public interface DatasetDetailWithPath extends DatasetDetail {

  Path getDataPath();

}
