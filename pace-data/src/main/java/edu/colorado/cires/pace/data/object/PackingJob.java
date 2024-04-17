package edu.colorado.cires.pace.data.object;

import java.nio.file.Path;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class PackingJob {
  
  private final Path temperaturePath;
  private final Path biologicalPath;
  private final Path otherPath;
  private final Path documentsPath;
  private final Path calibrationDocumentsPath;
  private final Path navigationPath;
  private final Path sourcePath;
  
}
