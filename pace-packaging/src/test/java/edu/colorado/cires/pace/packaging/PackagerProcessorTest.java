package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackagerProcessorTest {
  
  private static final List<Person> PEOPLE = List.of(Person.builder()
          .name("person")
          .organization("organization")
          .position("position")
      .build()); 
  
  private static final List<Organization> ORGANIZATIONS = List.of(Organization.builder()
          .name("organization")
      .build());
  
  private static final List<Project> PROJECTS = List.of(Project.builder()
          .name("project")
      .build());

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  private final Path testOutputPath = Paths.get("target").resolve("output");
  private final Path testSourcePath = Paths.get("target").resolve("source");
  
  @BeforeEach
  void beforeEach() {
    FileUtils.deleteQuietly(testOutputPath.toFile());
    FileUtils.deleteQuietly(testSourcePath.toFile());
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(testOutputPath.toFile());
    FileUtils.deleteQuietly(testSourcePath.toFile());
  }
  
  @Test
  void testInvalidPackingJobNoSourcePath() {
    Package packingJob = createPackingJob(
        null, null, null, null, null, null, null
    );
    
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> new PackageProcessor(
        objectMapper, PEOPLE, ORGANIZATIONS, PROJECTS, Collections.singletonList(packingJob), testOutputPath, progressIndicator
    ).process());
    assertEquals(String.format(
        "%s validation failed", Package.class.getSimpleName()
    ), exception.getMessage());
    
    assertEquals(1, exception.getConstraintViolations().size());
    ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().iterator().next();
    assertEquals("sourcePath", constraintViolation.getPropertyPath().toString());
    assertEquals("must not be null", constraintViolation.getMessage());
    
    verify(progressIndicator, times(0)).incrementProcessedRecords();
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
  void testProcess(
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
    
    Package packingJob = createPackingJob(
        sp,
        tp,
        op,
        np,
        dp,
        cdp,
        bp
    );
    
    writeFiles(bp);
    writeFiles(cdp);
    writeFiles(dp);
    writeFiles(np);
    writeFiles(op);
    writeFiles(tp);
    writeFiles(sp);
    
    packingJob = objectMapper.readValue(
        objectMapper.writeValueAsString(packingJob), Package.class
    );

    String expectedMetadata = objectMapper.writerWithView(Dataset.class).writeValueAsString(packingJob);
    String expectedPeople = objectMapper.writeValueAsString(PEOPLE);
    String expectedOrganizations = objectMapper.writeValueAsString(ORGANIZATIONS);
    String expectedProjects = objectMapper.writeValueAsString(PROJECTS);
    
    int expectedNumberOfInvocations = 0;
    if (biologicalPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (calibrationDocumentsPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (documentsPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (navigationPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (otherPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (temperaturePath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (sourcePath != null) {
      expectedNumberOfInvocations += 20;
    }
    
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    new PackageProcessor(
        objectMapper, PEOPLE, ORGANIZATIONS, PROJECTS, Collections.singletonList(packingJob), testOutputPath, progressIndicator
    ).process();
    verify(progressIndicator, times(expectedNumberOfInvocations + 8)).incrementProcessedRecords();
    
    Path baseExpectedOutputPath = testOutputPath.resolve(packingJob.getPackageId()).resolve("data");

    checkTargetPaths(packingJob.getBiologicalPath(), baseExpectedOutputPath.resolve("biological"));
    checkTargetPaths(packingJob.getCalibrationDocumentsPath(), baseExpectedOutputPath.resolve("calibration"));
    checkTargetPaths(packingJob.getDocumentsPath(), baseExpectedOutputPath.resolve("docs"));
    checkTargetPaths(packingJob.getNavigationPath(), baseExpectedOutputPath.resolve("nav_files"));
    checkTargetPaths(packingJob.getOtherPath(), baseExpectedOutputPath.resolve("other"));
    checkTargetPaths(packingJob.getTemperaturePath(), baseExpectedOutputPath.resolve("temperature"));
    checkTargetPaths(packingJob.getSourcePath(), baseExpectedOutputPath.resolve(
        (packingJob instanceof AudioPackage || packingJob instanceof CPODPackage) ? "acoustic_files" : "data_files"
    ));
    
    String actualMetadata = FileUtils.readFileToString(baseExpectedOutputPath.resolve(String.format(
        "%s.json", packingJob.getPackageId()
    )).toFile(), StandardCharsets.UTF_8);
    
    assertEquals(expectedMetadata, actualMetadata);
    
    assertTrue(baseExpectedOutputPath.getParent().resolve("process.log").toFile().exists());
    
    String actualPeople = FileUtils.readFileToString(baseExpectedOutputPath.resolve("people.json").toFile(), StandardCharsets.UTF_8);
    assertEquals(expectedPeople, actualPeople);
    
    String actualOrganizations = FileUtils.readFileToString(baseExpectedOutputPath.resolve("organizations.json").toFile(), StandardCharsets.UTF_8);
    assertEquals(expectedOrganizations, actualOrganizations);
    
    String actualProjects = FileUtils.readFileToString(baseExpectedOutputPath.resolve("projects.json").toFile(), StandardCharsets.UTF_8);
    assertEquals(expectedProjects, actualProjects);
    
    Package dataset = objectMapper.readValue(actualMetadata, Package.class);
    assertInstanceOf(AudioPackage.class, dataset);
    assertNull(dataset.getUuid());
    assertNull(dataset.getTemperaturePath());
    assertNull(dataset.getBiologicalPath());
    assertNull(dataset.getOtherPath());
    assertNull(dataset.getDocumentsPath());
    assertNull(dataset.getCalibrationDocumentsPath());
    assertNull(dataset.getNavigationPath());
    assertNull(dataset.getSourcePath());
  }

  private Package createPackingJob(
      Path sourcePath,
      Path temperaturePath,
      Path otherPath,
      Path navigationPath,
      Path documentsPath,
      Path calibrationDocumentsPath,
      Path biologicalPath
  ) {
    return AudioPackage.builder()
        .sourcePath(sourcePath)
        .temperaturePath(temperaturePath)
        .otherPath(otherPath)
        .navigationPath(navigationPath)
        .documentsPath(documentsPath)
        .calibrationDocumentsPath(calibrationDocumentsPath)
        .biologicalPath(biologicalPath)
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projects(List.of(
            "project-name-1", "project-name-2"
        )).publicReleaseDate(LocalDate.now().plusDays(1))
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
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .hydrophoneSensitivity(10f)
        .frequencyRange(5f)
        .gain(1f)
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .qualityAnalyst("qualityAnalyst")
        .qualityAnalysisObjectives("quality-analyst-objectives")
        .qualityAnalysisMethod("quality-analysis-method")
        .qualityAssessmentDescription("quality-assessment-description")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .comments("comment-1")
                .qualityLevel(QualityLevel.good)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.now().minusMinutes(10))
                .endTime(LocalDateTime.now())
                .build(),
            DataQualityEntry.builder()
                .comments("comment-2")
                .qualityLevel(QualityLevel.unusable)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.now().minusMinutes(20))
                .endTime(LocalDateTime.now().minusMinutes(10))
                .build()
        )).deploymentTime(LocalDateTime.now().minusDays(4))
        .recoveryTime(LocalDateTime.now().minusDays(1))
        .comments("deployment-comments")
        .sensors(List.of(
            "audio-sensor", "depth-sensor"
        )).channels(List.of(
            Channel.builder()
                .sensor("audioSensor")
                .startTime(LocalDateTime.now().minusMinutes(2))
                .endTime(LocalDateTime.now().minusMinutes(1))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.now().minusMinutes(1))
                        .endTime(LocalDateTime.now())
                        .sampleBits(10)
                        .sampleRate(10f)
                        .build()
                )).dutyCycles(List.of(
                    DutyCycle.builder()
                        .duration(100f)
                        .interval(100f)
                        .startTime(LocalDateTime.now().minusMinutes(10))
                        .endTime(LocalDateTime.now().minusMinutes(5))
                        .build()
                )).gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.now().minusMinutes(20))
                        .endTime(LocalDateTime.now().minusMinutes(5))
                        .gain(1000f)
                        .build()
                ))
                .build()
        )).locationDetail(StationaryMarineLocation.builder()
            .seaArea("seaArea")
            .deploymentLocation(MarineInstrumentLocation.builder()
                .instrumentDepth(-10f)
                .seaFloorDepth(-100f)
                .longitude(10d)
                .latitude(10d)
                .build())
            .recoveryLocation(MarineInstrumentLocation.builder()
                .instrumentDepth(-20f)
                .seaFloorDepth(-110f)
                .longitude(15d)
                .latitude(15d)
                .build())
            .build())
        .build();
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

  private void checkTargetPaths(Path inputDirectory, Path expectedOutputDirectory) throws IOException {
    if (inputDirectory == null) {
      assertFalse(expectedOutputDirectory.toFile().exists());
      return;
    }
    
    Set<Path> outputPaths = Files.walk(expectedOutputDirectory)
        .filter(p -> p.toFile().isFile())
        .map(expectedOutputDirectory::relativize)
        .collect(Collectors.toSet());
    
    Set<Path> inputPaths = Files.walk(inputDirectory)
        .filter(p -> p.toFile().isFile() && !p.toFile().isHidden())
        .map(inputDirectory::relativize)
        .collect(Collectors.toSet());

    assertEquals(20, outputPaths.size()); // should not contain hidden files. should contain subdirectory contents in tree structure

    assertEquals(inputPaths, outputPaths);
  }

}
