package edu.colorado.cires.pace.data;

import edu.colorado.cires.pace.data.object.PackingJob;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsDataset;
import java.nio.file.Path;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Jacksonized
public class SoundPropagationModelsPackingJob extends SoundPropagationModelsDataset implements PackingJob {

  private final Path temperaturePath;
  private final Path biologicalPath;
  private final Path otherPath;
  private final Path documentsPath;
  private final Path calibrationDocumentsPath;
  private final Path navigationPath;
  private final Path sourcePath;
  
}
