package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.position.Position;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class MockDropPackagerTest {
  
  private final Path SOURCE_DIR = Paths.get("target").resolve("source").toAbsolutePath();
  private final Path TARGET_DIR = Paths.get("target").resolve("target").toAbsolutePath();
  
  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(SOURCE_DIR.toFile());
    FileUtils.deleteQuietly(TARGET_DIR.toFile());
    
    FileUtils.forceMkdir(SOURCE_DIR.toFile());
    
    writeDir();
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(SOURCE_DIR.toFile());
    FileUtils.deleteQuietly(TARGET_DIR.toFile());
  }
  
  private void writeDir() throws IOException {
    for (int i = 0; i < 2; i++) {
      String fileName = String.format(
          "test-%s.wav", i
      );
      try (FileWriter writer = new FileWriter(SOURCE_DIR.resolve(fileName).toFile(), StandardCharsets.UTF_8, true)) {
        for (int j = 0; j < 2; j++) {
          writer.append(String.format(
              "test-content\t%s%s", i, j
          ));
        }
      }
    }
  }
  
  private Stream<PackageInstruction> getInstructionForSourceDir() throws IOException {
    return Files.walk(SOURCE_DIR)
        .filter(Files::isRegularFile)
        .map(p -> new PackageInstruction(
            Paths.get(p.toString().replace("target","target***")),
            TARGET_DIR.resolve("data").resolve(SOURCE_DIR.relativize(p))
        ));
  }

  @Test
  void testRunPassAfterOne() throws IOException, PackagingException {
    List<PackageInstruction> packageInstructions = getInstructionForSourceDir().toList();
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    List<Package> packages = new ArrayList<Package>();
    packages.add(buildPackage());
    List<Person> people = new ArrayList<Person>();
    people.add(buildPerson());

    long startTime = System.nanoTime();
    MockDropPackager.run(1, packageInstructions.stream(), TARGET_DIR, packages, people, LogManager.getLogger("test"), progressIndicator);
    long endTime = System.nanoTime();

    assertTrue(TimeUnit.SECONDS.convert(endTime-startTime, TimeUnit.NANOSECONDS) > 2);

    Path bagitFile = TARGET_DIR.resolve("bagit.txt");
    List<String> lines = FileUtils.readLines(bagitFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(2, lines.size());
    assertEquals("BagIt-Version: 0.97", lines.get(0));
    assertEquals(String.format(
        "Tag-File-Character-Encoding: %s", StandardCharsets.UTF_8.displayName()
    ), lines.get(1));
    
    Path bagInfoFile = TARGET_DIR.resolve("bag-info.txt");
    lines = FileUtils.readLines(bagInfoFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(8, lines.size());

    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedString = localDate.format(formatter);
    assertEquals(
        "Source-Organization: organization-1",
        lines.get(0)
    );
    
    Path manifestFile = TARGET_DIR.resolve("manifest-md5.txt");
    lines = FileUtils.readLines(manifestFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(2, lines.size());
    
    for (String line : lines) {
      String[] lineParts = line.split(" {2}");
      String relativePath = lineParts[0];
      String checksum = lineParts[1];
      
      assertTrue(relativePath.startsWith("data"));
      
      Path targetFile = TARGET_DIR.resolve(relativePath);
      assertTrue(targetFile.toFile().exists());
      assertTrue(targetFile.toFile().isFile());
      
      Path sourceFile = SOURCE_DIR.resolve(targetFile.toFile().getName());
      assertTrue(sourceFile.toFile().exists());
      assertTrue(sourceFile.toFile().isFile());
      
      try (InputStream inputStream = new FileInputStream(sourceFile.toFile())) {
        String expectedChecksum = DigestUtils.md5Hex(inputStream);
        assertEquals(expectedChecksum, checksum);
      }
    }
    
    Path tagmanifestFile = TARGET_DIR.resolve("tagmanifest-md5.txt");
    lines = FileUtils.readLines(tagmanifestFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(3, lines.size());
    
    for (String line : lines) {
      String[] lineParts = line.split(" {2}");
      String relativePath = lineParts[0];
      String checksum = lineParts[1];
      
      assertFalse(relativePath.contains(File.separator));
      
      Path targetFile = TARGET_DIR.resolve(relativePath);
      assertTrue(targetFile.toFile().exists());
      assertTrue(targetFile.toFile().isFile());
      
      Path sourceFile = SOURCE_DIR.resolve(targetFile.toFile().getName());
      assertFalse(sourceFile.toFile().exists());
      
      try (InputStream inputStream = new FileInputStream(targetFile.toFile())) {
        String expectedChecksum = DigestUtils.md5Hex(inputStream);
        assertEquals(expectedChecksum, checksum);
      }
    }
    
    verify(progressIndicator, times(packageInstructions.size() + 4)).incrementProcessedRecords();
  }

  @Test
  void testRunFailAfterFive() throws IOException, PackagingException {
    List<PackageInstruction> packageInstructions = getInstructionForSourceDir().toList();
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    List<Package> packages = new ArrayList<Package>();
    packages.add(buildPackage());
    List<Person> people = new ArrayList<Person>();
    people.add(buildPerson());

    assertThrows(PackagingException.class, () -> MockDropPackager.run(8, packageInstructions.stream(), TARGET_DIR, packages, people, LogManager.getLogger("test"), progressIndicator));

    verify(progressIndicator, times(0)).incrementProcessedRecords();
  }


  private Person buildPerson() {
    return Person.builder()
        .name("dataset-packager")
        .organization("organization")
        .position("position")
        .phone("123-123-1234")
        .email("fakeemail@aol.com")
        .uuid(UUID.randomUUID())
        .build();
  }

  @Test
  void testWriteBagitFileDirectoryDoesNotExist() {
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    
    Exception exception = assertThrows(PackagingException.class, () -> MockDropPackager.writeBagItFile(TARGET_DIR,
        progressIndicator::incrementProcessedRecords, LogManager.getLogger("test")));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("bagit.txt")
    ), exception.getMessage());
    
    verify(progressIndicator, times(0)).incrementProcessedRecords();
  }
  
  @Test
  void testWriteBagInfoFileDirectoryDoesNotExist() {
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    List<Package> packages = new ArrayList<Package>();
    List<Person> people = new ArrayList<Person>();

    Exception exception = assertThrows(PackagingException.class, () -> MockDropPackager.writeBagInfoFile(TARGET_DIR, progressIndicator::incrementProcessedRecords, LogManager.getLogger("test"), packages, people));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("bag-info.txt")
    ), exception.getMessage());
    
    verify(progressIndicator, times(0)).incrementProcessedRecords();
  }
  
  @Test
  void testWriteTagManifestFileDoesNotExist() throws PackagingException {
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    
    MockDropPackager.mkdir(TARGET_DIR);
    Exception exception = assertThrows(PackagingException.class, () -> MockDropPackager.writeTagManifestFile(
        TARGET_DIR.resolve("bag-info.txt"),
        TARGET_DIR.resolve("bagit.txt"),
        TARGET_DIR.resolve("manifest-md5.txt"),
        TARGET_DIR,
        progressIndicator::incrementProcessedRecords,
        LogManager.getLogger("test")
    ));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("tagmanifest-md5.txt")
    ), exception.getMessage());
    
    verify(progressIndicator, times(0)).incrementProcessedRecords();
  }
  
  @Test
  void testFileProcessingFailure() throws IOException {
    try (MockedStatic<edu.colorado.cires.pace.packaging.FileUtils> mockedStatic = Mockito.mockStatic(
        edu.colorado.cires.pace.packaging.FileUtils.class)) {
      Exception exception = new IOException("test file error");

      List<Package> packages = new ArrayList<Package>();
      List<Person> people = new ArrayList<Person>();

      mockedStatic.when(() -> edu.colorado.cires.pace.packaging.FileUtils.appendChecksumToManifest(any(), any(), any())).thenThrow(
          exception
      );
      mockedStatic.when(() -> edu.colorado.cires.pace.packaging.FileUtils.mkdir(any())).thenCallRealMethod();

      List<PackageInstruction> packageInstructions = getInstructionForSourceDir().toList();
      
      ProgressIndicator progressIndicator = mock(ProgressIndicator.class);

      Exception packagingException = assertThrows(PackagingException.class, () -> MockDropPackager.run(0, packageInstructions.stream(), TARGET_DIR, packages, people, LogManager.getLogger("test"), progressIndicator));
      assertEquals(String.format(
          "Packaging failed: java.io.IOException: %s", exception.getMessage()
      ), packagingException.getMessage());
      
      verify(progressIndicator, times(0)).incrementProcessedRecords();
    }
  }
  
  @Test
  void testWriteManifestDirectoryDoesNotExist() {
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    
    Exception exception = assertThrows(PackagingException.class, () -> MockDropPackager.copyFilesAndWriteManifest(Stream.empty(), TARGET_DIR, progressIndicator::incrementProcessedRecords, LogManager.getLogger("test"), 0));
    assertEquals(String.format(
        "Packaging failed: %s (No such file or directory)", TARGET_DIR.resolve("manifest-md5.txt")
    ), exception.getMessage());
    
    verify(progressIndicator, times(0)).incrementProcessedRecords();
  }

  private Package buildPackage(){
    return AudioPackage.builder()
        .uuid(UUID.randomUUID())
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projects(List.of(
            "project-name-1", "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platform(
            "platform"
        ).instrument("instrument")
        .instrumentId("instrumentId")
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .qualityAnalyst("")
        .qualityAnalysisObjectives("quality-analysis-objectives")
        .qualityAnalysisMethod("quality-analysis-method")
        .qualityAssessmentDescription("quality-assessment-description")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .comments("comment-1")
                .qualityLevel(QualityLevel.good)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
                .channelNumbers(List.of(1))
                .build(),
            DataQualityEntry.builder()
                .comments("comment-2")
                .qualityLevel(QualityLevel.unusable)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(20))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                .channelNumbers(List.of(1))
                .build()
        )).deploymentTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(4))
        .recoveryTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(1))
        .comments("deployment-comments")
        .sensors(List.of(
            PackageSensor.<String>builder()
                .sensor("audio-sensor")
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build(),
            PackageSensor.<String>builder()
                .sensor("depth-sensor")
                .position(Position.builder()
                    .x(4f)
                    .y(5f)
                    .z(6f)
                    .build())
                .build()
        )).channels(List.of(
            Channel.<String>builder()
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(2))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
                        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
                        .sampleBits(10)
                        .sampleRate(10f)
                        .build()
                )).dutyCycles(List.of(
                    DutyCycle.builder()
                        .duration(100f)
                        .interval(1000f)
                        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(5))
                        .build()
                )).gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(20))
                        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(5))
                        .gain(1000f)
                        .build()
                ))
                .build()
        )).locationDetail(MobileMarineLocation.builder()
            .seaArea("seaArea")
            .vessel("vessel")
            .locationDerivationDescription("the description of the location")
            .build())
        .build();
  }
}
