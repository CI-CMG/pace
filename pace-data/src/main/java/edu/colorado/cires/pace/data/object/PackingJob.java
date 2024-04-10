package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.PackingJobValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class PackingJob {
  final Path temperaturePath;
  final Path biologicalPath;
  final Path otherPath;
  final Path documentsPath;
  final Path calibrationDocumentsPath;
  final Path navigationPath;
  final Path sourcePath;

  @Builder
  @Jacksonized
  private PackingJob(Path temperaturePath, Path biologicalPath, Path otherPath, Path documentsPath, Path calibrationDocumentsPath, Path navigationPath,
      Path sourcePath) throws ValidationException {
    this.temperaturePath = temperaturePath;
    this.biologicalPath = biologicalPath;
    this.otherPath = otherPath;
    this.documentsPath = documentsPath;
    this.calibrationDocumentsPath = calibrationDocumentsPath;
    this.navigationPath = navigationPath;
    this.sourcePath = sourcePath;
    
    new PackingJobValidator().validate(this);
  }
}
