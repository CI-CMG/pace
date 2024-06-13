package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.AudioDataset;
import edu.colorado.cires.pace.data.object.CPodDataset;
import edu.colorado.cires.pace.data.object.Package;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;

class PackageInstructionFactory {
  
  public static long getInstructionCount(Package packingJob, Path outputDirectory, Logger logger) throws PackagingException {
    return getPackageInstructions(
        packingJob,
        Paths.get("metadata"),
        Paths.get("people"),
        Paths.get("organizations"),
        Paths.get("projects"),
        outputDirectory,
        logger
    ).count() + 4L; // account for generated files
  }
  
  public static Stream<PackageInstruction> getPackageInstructions(
      Package packingJob,
      Path metadataPath,
      Path peoplePath,
      Path organizationsPath,
      Path projectsPath,
      Path outputDirectory,
      Logger logger
  ) throws PackagingException {
    Path dataDirectory = outputDirectory.resolve("data");
    
    logger.info("Scanning temperature files from {}", packingJob.getTemperaturePath());
    Stream<PackageInstruction> temperatureFiles = processPath(
        packingJob::getTemperaturePath,
        dataDirectory.resolve("temperature"),
        logger
    );

    logger.info("Scanning biological files from {}", packingJob.getBiologicalPath());
    Stream<PackageInstruction> biologicalFiles = processPath(
        packingJob::getBiologicalPath,
        dataDirectory.resolve("biological"),
        logger
    );
    
    logger.info("Scanning other files from {}", packingJob.getOtherPath());
    Stream<PackageInstruction> otherFiles = processPath(
        packingJob::getOtherPath,
        dataDirectory.resolve("other"),
        logger
    );
    
    logger.info("Scanning documentation files from {}", packingJob.getDocumentsPath());
    Stream<PackageInstruction> docsFiles = processPath(
        packingJob::getDocumentsPath, 
        dataDirectory.resolve("docs"),
        logger
    );
    
    logger.info("Scanning calibration documentation from {} , files", packingJob.getCalibrationDocumentsPath());
    Stream<PackageInstruction> calibrationDocsFiles = processPath(
        packingJob::getCalibrationDocumentsPath,
        dataDirectory.resolve("calibration"),
        logger
    );
    
    logger.info("Scanning navigation files from {}", packingJob.getNavigationPath());
    Stream<PackageInstruction> navigationFiles = processPath(
        packingJob::getNavigationPath, 
        dataDirectory.resolve("nav_files"),
        logger
    );
    
    logger.info("Scanning source files from {}", packingJob.getSourcePath());
    Stream<PackageInstruction> sourceFiles = processPath(
        packingJob::getSourcePath, 
        (packingJob instanceof AudioDataset || packingJob instanceof CPodDataset) ? dataDirectory.resolve("acoustic_files") : dataDirectory.resolve("data_files"),
        logger
    );
    
    Stream<PackageInstruction> additionalFiles = Stream.<PackageInstruction>builder()
        .add(new PackageInstruction(metadataPath, metadataPath))
        .add(new PackageInstruction(peoplePath, peoplePath))
        .add(new PackageInstruction(organizationsPath, organizationsPath))
        .add(new PackageInstruction(projectsPath, projectsPath))
        .build();
    
    return Stream.of(
      temperatureFiles,
      biologicalFiles,
      otherFiles,
      docsFiles,
      calibrationDocsFiles,
      navigationFiles,
      sourceFiles,
      additionalFiles
    ).flatMap(stream -> stream);
  }
  
  private static Stream<PackageInstruction> processPath(Supplier<Path> pathGetter, Path outputDirectory, Logger logger) throws PackagingException {
    Path path = pathGetter.get();
    if (path == null) {
      return Stream.empty();
    }

    try {
      return Files.walk(path)
          .filter(p -> p.toFile().isFile())
          .filter(p -> {
            try {
              boolean regularFile = FileUtils.filterHidden(p);
              if (!regularFile) {
                logger.warn("Filtered file will not be moved: {}", p);
              }
              return regularFile;
            } catch (IOException e) {
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
              boolean shouldMoveFile = FileUtils.filterTimeSize(packageInstruction.source(), packageInstruction.target());
              if (!shouldMoveFile) {
                logger.warn("Filtered file will not be moved: {}", packageInstruction.source());
              }
              return shouldMoveFile;
            } catch (IOException e) {
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
