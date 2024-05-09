package edu.colorado.cires.pace.data;

import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsDataset;
import java.nio.file.Path;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundPropagationModelsPackage extends SoundPropagationModelsDataset implements Package {

  private final UUID uuid;
  private final Path temperaturePath;
  private final Path biologicalPath;
  private final Path otherPath;
  private final Path documentsPath;
  private final Path calibrationDocumentsPath;
  private final Path navigationPath;
  private final Path sourcePath;

  @Override
  public String getPackageId() {
    return super.getPackageId();
  }
}
