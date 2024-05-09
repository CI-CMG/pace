package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.AudioDataset;
import edu.colorado.cires.pace.data.object.CPodDataset;
import edu.colorado.cires.pace.data.object.Package;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PackageInstructionFactory {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PackageInstructionFactory.class);
  
  public static Stream<PackageInstruction> getPackageInstructions(Package packingJob, Path metadataPath, Path outputDirectory)
      throws PackagingException {
    Path dataDirectory = outputDirectory.resolve("data");
    
    Stream<PackageInstruction> temperatureFiles = processPath(
        packingJob::getTemperaturePath,
        dataDirectory.resolve("temperature")
    );
    Stream<PackageInstruction> biologicalFiles = processPath(
        packingJob::getBiologicalPath,
        dataDirectory.resolve("biological")
    );
    Stream<PackageInstruction> otherFiles = processPath(
        packingJob::getOtherPath,
        dataDirectory.resolve("other")
    );
    Stream<PackageInstruction> docsFiles = processPath(
        packingJob::getDocumentsPath, 
        dataDirectory.resolve("docs")
    );
    Stream<PackageInstruction> calibrationDocsFiles = processPath(
        packingJob::getCalibrationDocumentsPath,
        dataDirectory.resolve("calibration")
    );
    Stream<PackageInstruction> navigationFiles = processPath(
        packingJob::getNavigationPath, 
        dataDirectory.resolve("nav_files")
    );
    Stream<PackageInstruction> sourceFiles = processPath(
        packingJob::getSourcePath, 
        (packingJob instanceof AudioDataset || packingJob instanceof CPodDataset) ? dataDirectory.resolve("acoustic_files") : dataDirectory.resolve("data_files")
    );
    
    Stream<PackageInstruction> metadataFiles = Stream.<PackageInstruction>builder()
        .add(new PackageInstruction(metadataPath, metadataPath))
        .build();
    
    return Stream.of(
      temperatureFiles,
      biologicalFiles,
      otherFiles,
      docsFiles,
      calibrationDocsFiles,
      navigationFiles,
      sourceFiles,
      metadataFiles
    ).flatMap(stream -> stream);
  }
  
  private static Stream<PackageInstruction> processPath(Supplier<Path> pathGetter, Path outputDirectory) throws PackagingException {
    Path path = pathGetter.get();
    if (path == null) {
      return Stream.empty();
    }

    try {
      return Files.walk(path)
          .filter(p -> p.toFile().isFile())
          .filter(p -> {
            try {
              return FileUtils.filterHidden(p);
            } catch (IOException e) {
              LOGGER.error("Failed to determine is {} is hidden file", p);
              throw new RuntimeException(e);
            }
          })
          .map(p -> new PackageInstruction(
                  p,
                  outputDirectory
                      .resolve(path.relativize(p))
              )
          ).filter(packageInstruction -> {
            try {
              return FileUtils.filterTimeSize(packageInstruction.source(), packageInstruction.target());
            } catch (IOException e) {
              LOGGER.error("Failed to determine file equivalency between {} and {}", packageInstruction.source(), packageInstruction.target());
              throw new RuntimeException(e);
            }
          });
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to compute packaging destinations for %s", path
      ), e);
    }
  }

}
