package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackageInstructionFactoryTest {
  
  private static final Path SOURCE_PATH = Paths.get("target").resolve("source");
  private static final Path TARGET_PATH = Paths.get("target").resolve("target");
  
  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(SOURCE_PATH.toFile());
    FileUtils.forceMkdir(SOURCE_PATH.toFile());
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(TARGET_PATH.toFile());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      ",target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
  })
  void testGetPackageInstructionsAudio(
      String biologicalPath,
      String calibrationDocumentsPath,
      String documentsPath,
      String navigationPath,
      String otherPath,
      String temperaturePath,
      String sourcePath
  ) throws IOException, PackagingException {
    Path sp = sourcePath == null ? null : Path.of(sourcePath).toAbsolutePath();
    Path tp = temperaturePath == null ? null : Path.of(temperaturePath).toAbsolutePath();
    Path op = otherPath == null ? null : Path.of(otherPath).toAbsolutePath();
    Path np = navigationPath == null ? null : Path.of(navigationPath).toAbsolutePath();
    Path dp = documentsPath == null ? null : Path.of(documentsPath).toAbsolutePath();
    Path cdp = calibrationDocumentsPath == null ? null : Path.of(calibrationDocumentsPath).toAbsolutePath();
    Path bp = biologicalPath == null ? null : Path.of(biologicalPath).toAbsolutePath();

    writeFiles(bp);
    writeFiles(cdp);
    writeFiles(dp);
    writeFiles(np);
    writeFiles(op);
    writeFiles(tp);
    writeFiles(sp);
    
    Package packingJob = AudioPackage.builder()
        .temperaturePath(tp)
        .biologicalPath(bp)
        .otherPath(op)
        .documentsPath(dp)
        .calibrationDocumentsPath(cdp)
        .sourcePath(sp)
        .locationDetail(MobileMarineLocation.builder()
            .fileList(
                np == null ? Collections.emptyList() : 
                    Files.walk(np)
                        .filter(p -> p.toFile().isFile())
                        .filter(p -> {
                          try {
                            return edu.colorado.cires.pace.packaging.FileUtils.filterHidden(p);
                          } catch (IOException e) {
                            throw new RuntimeException(e);
                          }
                        })
                        .toList()
            )
            .build())
        .build();
    
    Path metadataPath = TARGET_PATH.resolve("metadata.json");
    Path peoplePath = TARGET_PATH.resolve("people.json");
    Path organizationsPath = TARGET_PATH.resolve("organizations.json");
    Path projectsPath = TARGET_PATH.resolve("projects.json");
    
    List<PackageInstruction> packageInstructions = PackageInstructionFactory.getPackageInstructions(packingJob, metadataPath, peoplePath, organizationsPath, projectsPath, TARGET_PATH, LogManager.getLogger("test"))
        .toList();
    
    Path baseExpectedOutputPath = Path.of("target").resolve("target").resolve("data");
    
    checkTargetPaths(packageInstructions, packingJob::getBiologicalPath, baseExpectedOutputPath.resolve("biological"));
    checkTargetPaths(packageInstructions, packingJob::getCalibrationDocumentsPath, baseExpectedOutputPath.resolve("calibration"));
    checkTargetPaths(packageInstructions, packingJob::getDocumentsPath, baseExpectedOutputPath.resolve("docs"));

    LocationDetail locationDetail = packingJob.getLocationDetail();
    assertInstanceOf(MobileMarineLocation.class, locationDetail);
    MobileMarineLocation mobileMarineLocation = (MobileMarineLocation) locationDetail;
    List<Path> navPaths = mobileMarineLocation.getFileList();
    if (!navPaths.isEmpty()) {
      List<PackageInstruction> navInstructions = packageInstructions.stream()
          .filter(packageInstruction -> packageInstruction.target().toString().contains("nav_files"))
          .toList();
      
      assertEquals(20, navInstructions.size());
      
      navInstructions.stream()
          .filter(p -> p.source().toString().contains("subdir"))
          .toList().forEach(
              packageInstruction -> {
                String fileName = FilenameUtils.getName(packageInstruction.source().toString());
                String extension = FilenameUtils.getExtension(fileName);
                String baseName = FilenameUtils.getBaseName(fileName);
                assertEquals(
                    baseExpectedOutputPath.resolve("nav_files").resolve(
                        String.format("%s (1).%s", baseName, extension)
                    ).toString(),
                    packageInstruction.target().toString()
                );
              }
          );
      navInstructions.stream()
          .filter(p -> !p.source().toString().contains("subdir"))
          .forEach(
              packageInstruction -> assertEquals(
                  baseExpectedOutputPath.resolve("nav_files").resolve(packageInstruction.source().getFileName()).toString(),
                  packageInstruction.target().toString()
              )
          );
    }
    checkTargetPaths(packageInstructions, packingJob::getOtherPath, baseExpectedOutputPath.resolve("other"));
    checkTargetPaths(packageInstructions, packingJob::getTemperaturePath, baseExpectedOutputPath.resolve("temperature"));
    checkTargetPaths(packageInstructions, packingJob::getSourcePath, baseExpectedOutputPath.resolve("acoustic_files"));
    assertTrue(
        packageInstructions.stream()
            .anyMatch(packageInstruction -> 
                packageInstruction.source().equals(metadataPath) &&
                packageInstruction.target().equals(metadataPath)
            )
    );
  }

  @ParameterizedTest
  @CsvSource(value = {
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      ",target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
  })
  void testGetPackageInstructionsCPOD(
      String biologicalPath,
      String calibrationDocumentsPath,
      String documentsPath,
      String navigationPath,
      String otherPath,
      String temperaturePath,
      String sourcePath
  ) throws IOException, PackagingException {
    Path sp = sourcePath == null ? null : Path.of(sourcePath).toAbsolutePath();
    Path tp = temperaturePath == null ? null : Path.of(temperaturePath).toAbsolutePath();
    Path op = otherPath == null ? null : Path.of(otherPath).toAbsolutePath();
    Path np = navigationPath == null ? null : Path.of(navigationPath).toAbsolutePath();
    Path dp = documentsPath == null ? null : Path.of(documentsPath).toAbsolutePath();
    Path cdp = calibrationDocumentsPath == null ? null : Path.of(calibrationDocumentsPath).toAbsolutePath();
    Path bp = biologicalPath == null ? null : Path.of(biologicalPath).toAbsolutePath();
    Package packingJob = CPODPackage.builder()
        .temperaturePath(tp)
        .biologicalPath(bp)
        .otherPath(op)
        .documentsPath(dp)
        .calibrationDocumentsPath(cdp)
        .sourcePath(sp)
        .locationDetail(StationaryTerrestrialLocation.builder()
            .surfaceElevation(1f)
            .instrumentElevation(2f)
            .longitude(3d)
            .latitude(4d)
            .build())
        .build();
    writeFiles(bp);
    writeFiles(cdp);
    writeFiles(dp);
    writeFiles(np);
    writeFiles(op);
    writeFiles(tp);
    writeFiles(sp);

    Path metadataPath = TARGET_PATH.resolve("metadata.json");
    Path peoplePath = TARGET_PATH.resolve("people.json");
    Path organizationsPath = TARGET_PATH.resolve("organizations.json");
    Path projectsPath = TARGET_PATH.resolve("projects.json");

    List<PackageInstruction> packageInstructions = PackageInstructionFactory.getPackageInstructions(packingJob, metadataPath, peoplePath, organizationsPath, projectsPath, TARGET_PATH, LogManager.getLogger("test"))
        .toList();

    Path baseExpectedOutputPath = Path.of("target").resolve("target").resolve("data");

    checkTargetPaths(packageInstructions, packingJob::getBiologicalPath, baseExpectedOutputPath.resolve("biological"));
    checkTargetPaths(packageInstructions, packingJob::getCalibrationDocumentsPath, baseExpectedOutputPath.resolve("calibration"));
    checkTargetPaths(packageInstructions, packingJob::getDocumentsPath, baseExpectedOutputPath.resolve("docs"));
    assertFalse(baseExpectedOutputPath.resolve("nav_files").toFile().exists());
    checkTargetPaths(packageInstructions, packingJob::getOtherPath, baseExpectedOutputPath.resolve("other"));
    checkTargetPaths(packageInstructions, packingJob::getTemperaturePath, baseExpectedOutputPath.resolve("temperature"));
    checkTargetPaths(packageInstructions, packingJob::getSourcePath, baseExpectedOutputPath.resolve("acoustic_files"));
    assertTrue(
        packageInstructions.stream()
            .anyMatch(packageInstruction ->
                packageInstruction.source().equals(metadataPath) &&
                    packageInstruction.target().equals(metadataPath)
            )
    );
  }

  @ParameterizedTest
  @CsvSource(value = {
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      ",target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
  })
  void testGetPackageInstructionsSoundLevelMetrics(
      String biologicalPath,
      String calibrationDocumentsPath,
      String documentsPath,
      String navigationPath,
      String otherPath,
      String temperaturePath,
      String sourcePath
  ) throws IOException, PackagingException {
    Path sp = sourcePath == null ? null : Path.of(sourcePath).toAbsolutePath();
    Path tp = temperaturePath == null ? null : Path.of(temperaturePath).toAbsolutePath();
    Path op = otherPath == null ? null : Path.of(otherPath).toAbsolutePath();
    Path np = navigationPath == null ? null : Path.of(navigationPath).toAbsolutePath();
    Path dp = documentsPath == null ? null : Path.of(documentsPath).toAbsolutePath();
    Path cdp = calibrationDocumentsPath == null ? null : Path.of(calibrationDocumentsPath).toAbsolutePath();
    Path bp = biologicalPath == null ? null : Path.of(biologicalPath).toAbsolutePath();
    Package packingJob = SoundLevelMetricsPackage.builder()
        .temperaturePath(tp)
        .biologicalPath(bp)
        .otherPath(op)
        .documentsPath(dp)
        .calibrationDocumentsPath(cdp)
        .sourcePath(sp)
        .build();
    writeFiles(bp);
    writeFiles(cdp);
    writeFiles(dp);
    writeFiles(np);
    writeFiles(op);
    writeFiles(tp);
    writeFiles(sp);

    Path metadataPath = TARGET_PATH.resolve("metadata.json");
    Path peoplePath = TARGET_PATH.resolve("people.json");
    Path organizationsPath = TARGET_PATH.resolve("organizations.json");
    Path projectsPath = TARGET_PATH.resolve("projects.json");

    List<PackageInstruction> packageInstructions = PackageInstructionFactory.getPackageInstructions(packingJob, metadataPath, peoplePath, organizationsPath, projectsPath, TARGET_PATH, LogManager.getLogger("test"))
        .toList();

    Path baseExpectedOutputPath = Path.of("target").resolve("target").resolve("data");

    checkTargetPaths(packageInstructions, packingJob::getBiologicalPath, baseExpectedOutputPath.resolve("biological"));
    checkTargetPaths(packageInstructions, packingJob::getCalibrationDocumentsPath, baseExpectedOutputPath.resolve("calibration"));
    checkTargetPaths(packageInstructions, packingJob::getDocumentsPath, baseExpectedOutputPath.resolve("docs"));
    assertFalse(baseExpectedOutputPath.resolve("nav_files").toFile().exists());
    checkTargetPaths(packageInstructions, packingJob::getOtherPath, baseExpectedOutputPath.resolve("other"));
    checkTargetPaths(packageInstructions, packingJob::getTemperaturePath, baseExpectedOutputPath.resolve("temperature"));
    checkTargetPaths(packageInstructions, packingJob::getSourcePath, baseExpectedOutputPath.resolve("data_files"));
    assertTrue(
        packageInstructions.stream()
            .anyMatch(packageInstruction ->
                packageInstruction.source().equals(metadataPath) &&
                    packageInstruction.target().equals(metadataPath)
            )
    );
  }
  
  @Test
  void testPathDoesNotExist() throws IOException {
    Path sourcePath = Path.of("target/source/source-files").toAbsolutePath();
    Package packingJob = AudioPackage.builder()
        .sourcePath(sourcePath)
        .build();
    writeFiles(sourcePath);
    
    FileUtils.deleteQuietly(SOURCE_PATH.toFile());
    
    Path metadataPath = TARGET_PATH.resolve("metadata.json");
    Path peoplePath = TARGET_PATH.resolve("people.json");
    Path organizationsPath = TARGET_PATH.resolve("organizations.json");
    Path projectsPath = TARGET_PATH.resolve("projects.json");
    
    Exception exception = assertThrows(PackagingException.class, () -> PackageInstructionFactory.getPackageInstructions(packingJob, metadataPath, peoplePath, organizationsPath, projectsPath, TARGET_PATH, LogManager.getLogger("test")));
    assertEquals(String.format(
        "Failed to compute packaging destinations for %s", SOURCE_PATH.resolve("source-files").toAbsolutePath()
    ), exception.getMessage());
  }
  
  private void checkTargetPaths(List<PackageInstruction> packageInstructions, Supplier<Path> valueGetter, Path expectedOutputDirectory) {
    List<PackageInstruction> dataTypeSpecificInstructions = packageInstructions.stream()
        .filter(packageInstruction -> packageInstruction.target().toFile().toString().contains(expectedOutputDirectory.toFile().toString()))
        .sorted(Comparator.comparing(PackageInstruction::target))
        .toList();
    
    Path value = valueGetter.get();
    if (value == null) {
      assertTrue(dataTypeSpecificInstructions.isEmpty());
      return;
    }
    
    assertEquals(20, dataTypeSpecificInstructions.size()); // should not contain hidden files. should contain subdirectory contents in tree structure

    for (PackageInstruction instruction : dataTypeSpecificInstructions) {
      assertEquals(
          expectedOutputDirectory.resolve(
              value.relativize(instruction.source())
          ).toAbsolutePath(),
          instruction.target().toAbsolutePath()
      );
    }
  }
  
  private void writeFiles(Path value) throws IOException {
    if (value == null) {
      return;
    }
    
    Path path = value.toAbsolutePath();
    
    for (int i = 0; i < 10; i++) {
      Path filePath = path.resolve(String.format(
          "test-%s.txt", i
      ));
      Path hiddenFilePath = path.resolve(String.format(
          ".test-%s.hidden", i
      ));
      FileUtils.createParentDirectories(filePath.toFile());
      try (
          FileWriter writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8, true);
          FileWriter hiddenFileWriter = new FileWriter(hiddenFilePath.toFile(), StandardCharsets.UTF_8, true)
      ) {
        for (int j = 0; j < 10; j++) {
          writer.append(String.format(
              "test-data\t%s-%s", i, j
          ));
          hiddenFileWriter.append(String.format(
              "test-data\t%s-%s", i, j
          ));
        }
      }
      try {
        Files.setAttribute(hiddenFilePath, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
      } catch (UnsupportedOperationException ignored) {} // not running on Windows OS
      
      Path fileDirectoryPath = path.resolve("subdir").resolve(String.format(
          "test-%s.txt", i
      ));
      Path hiddenFileDirectoryPath = path.resolve("subdir").resolve(String.format(
          ".test-%s.hidden", i
      ));
      FileUtils.createParentDirectories(fileDirectoryPath.toFile());
      try (
          FileWriter writer = new FileWriter(fileDirectoryPath.toFile(), StandardCharsets.UTF_8, true);
          FileWriter hiddenFileWriter = new FileWriter(hiddenFileDirectoryPath.toFile(), StandardCharsets.UTF_8, true)
      ) {
        for (int j = 0; j < 10; j++) {
          writer.append(String.format(
              "test-data\t%s-%s", i, j
          ));
          hiddenFileWriter.append(String.format(
              "test-data\t%s-%s", i, j
          ));
        }
      }
      try {
        Files.setAttribute(hiddenFileDirectoryPath, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
      } catch (UnsupportedOperationException ignored) {} // not running on Windows OS
    }
  }

}
