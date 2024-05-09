package edu.colorado.cires.pace.data.object;

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
public class CPODPackage extends CPodDataset implements Package {

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
