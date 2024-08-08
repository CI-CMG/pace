package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
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
    Stream<PackageInstruction> temperatureFiles = processDirectory(
        packingJob::getTemperaturePath,
        dataDirectory.resolve("temperature"),
        logger
    );

    logger.info("Scanning biological files from {}", packingJob.getBiologicalPath());
    Stream<PackageInstruction> biologicalFiles = processDirectory(
        packingJob::getBiologicalPath,
        dataDirectory.resolve("biological"),
        logger
    );
    
    logger.info("Scanning other files from {}", packingJob.getOtherPath());
    Stream<PackageInstruction> otherFiles = processDirectory(
        packingJob::getOtherPath,
        dataDirectory.resolve("other"),
        logger
    );
    
    logger.info("Scanning documentation files from {}", packingJob.getDocumentsPath());
    Stream<PackageInstruction> docsFiles = processDirectory(
        packingJob::getDocumentsPath, 
        dataDirectory.resolve("docs"),
        logger
    );
    
    logger.info("Scanning calibration documentation from {} , files", packingJob.getCalibrationDocumentsPath());
    Stream<PackageInstruction> calibrationDocsFiles = processDirectory(
        packingJob::getCalibrationDocumentsPath,
        dataDirectory.resolve("calibration"),
        logger
    );

    LocationDetail locationDetail = packingJob.getLocationDetail();
    Stream<PackageInstruction> navigationFiles;
    if (locationDetail instanceof MobileMarineLocation mobileMarineLocation) {
      List<Path> paths = mobileMarineLocation.getFileList();
      logger.info(
          "Scanning navigation files from {}",
          paths.stream()
              .map(Path::toString)
              .collect(Collectors.joining(","))
      );

      Map<Path, Path> fileNameConversions = new HashMap<>(0); // account for duplicate file names from multiple directories
      paths.stream()
          .sorted((Comparator.comparingInt(Path::getNameCount)))
        .collect(Collectors.groupingBy(
            Path::getFileName
        )).forEach((key, value) -> {
            for (int i = 0; i < value.size(); i++) {
              Path sourcePath = value.get(i);
              String fileNameSuffix = i == 0 ? "" : String.format(" (%s)", i);
              String fileName = sourcePath.getFileName().toString();
              String fileExtension = FilenameUtils.getExtension(fileName);
              String baseFileName = FilenameUtils.getBaseName(fileName);
              fileNameConversions.put(
                  sourcePath,
                  sourcePath.getParent().resolve(String.format(
                      "%s%s.%s", baseFileName, fileNameSuffix, fileExtension
                  ))
              );
            }
        });
      
      navigationFiles = processPaths(
          paths.stream(),
          dataDirectory.resolve("nav_files"),
          logger,
          p -> fileNameConversions.get(p).getFileName()
      );
    } else {
      navigationFiles = Stream.empty();
    }
    
    logger.info("Scanning source files from {}", packingJob.getSourcePath());
    Stream<PackageInstruction> sourceFiles = processDirectory(
        packingJob::getSourcePath, 
        (packingJob instanceof AudioPackage || packingJob instanceof CPODPackage) ? dataDirectory.resolve("acoustic_files") : dataDirectory.resolve("data_files"),
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
  
  private static Stream<PackageInstruction> processDirectory(Supplier<Path> pathGetter, Path outputDirectory, Logger logger) throws PackagingException {
    Path path = pathGetter.get();
    if (path == null) {
      return Stream.empty();
    }

    try {
      return processPaths(Files.walk(path), outputDirectory, logger, path::relativize);
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to compute packaging destinations for %s", path
      ), e);
    }
  }
  
  private static Stream<PackageInstruction> processPaths(Stream<Path> paths, Path outputDirectory, Logger logger, Function<Path, Path> getPathTailSegment) {
    return paths
      .filter(p -> p.toFile().isFile())
          .filter(p -> {
            try {
              boolean regularFile = FileUtils.filterHidden(p);
              if (!regularFile) {
                logger.warn("Hidden file will not be moved: {}", p);
              }
              return regularFile;
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          })
          .map(p -> new PackageInstruction(
                  p,
                  outputDirectory.resolve(getPathTailSegment.apply(p))
              )
          ).filter(packageInstruction -> {
            try {
              boolean shouldMoveFile = FileUtils.filterByChecksum(packageInstruction.source(), packageInstruction.target());
              if (!shouldMoveFile) {
                logger.warn("Identical file already exists: {}", packageInstruction.target());
              }
              return shouldMoveFile;
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
  }

}
