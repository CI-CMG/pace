package edu.colorado.cires.pace.data.object;

import java.nio.file.Path;

public interface DatasetDescriptionWithPath extends DatasetDescription {

  Path getDocumentationPath();

}
