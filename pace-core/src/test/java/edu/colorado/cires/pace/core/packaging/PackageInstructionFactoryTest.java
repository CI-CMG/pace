package edu.colorado.cires.pace.core.packaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.core.exception.PackingException;
import edu.colorado.cires.pace.data.object.PackingJob;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
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
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      ",target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,true",
  })
  void testGetPackageInstructions(
      String biologicalPath,
      String calibrationDocumentsPath,
      String documentsPath,
      String navigationPath,
      String otherPath,
      String temperaturePath,
      String sourcePath,
      boolean sourceContainsAudioData
  ) throws PackingException, IOException {
    Path sp = sourcePath == null ? null : Path.of(sourcePath);
    Path tp = temperaturePath == null ? null : Path.of(temperaturePath);
    Path op = otherPath == null ? null : Path.of(otherPath);
    Path np = navigationPath == null ? null : Path.of(navigationPath);
    Path dp = documentsPath == null ? null : Path.of(documentsPath);
    Path cdp = calibrationDocumentsPath == null ? null : Path.of(calibrationDocumentsPath);
    Path bp = biologicalPath == null ? null : Path.of(biologicalPath);
    PackingJob packingJob = new PackingJob(
        tp,
        bp,
        op,
        dp,
        cdp,
        np,
        sp
    );
    writeFiles(bp);
    writeFiles(cdp);
    writeFiles(dp);
    writeFiles(np);
    writeFiles(op);
    writeFiles(tp);
    writeFiles(sp);
    
    List<PackageInstruction> packageInstructions = PackageInstructionFactory.getPackageInstructions(packingJob, TARGET_PATH, sourceContainsAudioData)
        .toList();
    
    String baseExpectedOutputPath = "target/target/data/";
    
    checkTargetPaths(packageInstructions, packingJob::biologicalPath, baseExpectedOutputPath + "biological");
    checkTargetPaths(packageInstructions, packingJob::calibrationDocumentsPath, baseExpectedOutputPath + "calibration");
    checkTargetPaths(packageInstructions, packingJob::documentsPath, baseExpectedOutputPath + "docs");
    checkTargetPaths(packageInstructions, packingJob::navigationPath, baseExpectedOutputPath + "nav_files");
    checkTargetPaths(packageInstructions, packingJob::otherPath, baseExpectedOutputPath + "other");
    checkTargetPaths(packageInstructions, packingJob::temperaturePath, baseExpectedOutputPath + "temperature");
    checkTargetPaths(packageInstructions, packingJob::sourcePath, baseExpectedOutputPath + (sourceContainsAudioData ? "acoustic_files" : "data_files"));
  }
  
  @Test
  void testPathDoesNotExist() throws IOException {
    Path sourcePath = Path.of("target/source/source-files").toAbsolutePath();
    PackingJob packingJob = new PackingJob(
        null,
        null,
        null,
        null,
        null,
        null,
        sourcePath
    );
    writeFiles(sourcePath);
    
    FileUtils.deleteQuietly(SOURCE_PATH.toFile());
    
    Exception exception = assertThrows(PackingException.class, () -> PackageInstructionFactory.getPackageInstructions(packingJob, TARGET_PATH, false));
    assertEquals(String.format(
        "Failed to compute packaging destinations for %s", SOURCE_PATH.resolve("source-files").toAbsolutePath()
    ), exception.getMessage());
  }
  
  private void checkTargetPaths(List<PackageInstruction> packageInstructions, Supplier<Path> valueGetter, String expectedOutputDirectory) {
    List<PackageInstruction> dataTypeSpecificInstructions = packageInstructions.stream()
        .filter(packageInstruction -> packageInstruction.target().toFile().toString().contains(expectedOutputDirectory))
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
          Paths.get(expectedOutputDirectory).resolve(
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
    }
  }

}
