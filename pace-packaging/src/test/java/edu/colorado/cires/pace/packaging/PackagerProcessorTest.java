package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.colorado.cires.pace.data.object.AudioDataset;
import edu.colorado.cires.pace.data.object.AudioPackingJob;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.PackingJob;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackagerProcessorTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  private final PackageProcessor processor = new PackageProcessor(objectMapper);
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

  @ParameterizedTest
  @CsvSource(value = {
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      ",target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,,target/source/other,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,,target/source/temperature,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,,target/source/source-files,false",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files,true",
  })
  void testProcess(
      String biologicalPath,
      String calibrationDocumentsPath,
      String documentsPath,
      String navigationPath,
      String otherPath,
      String temperaturePath,
      String sourcePath,
      boolean sourceContainsAudioData
  ) throws IOException, PackagingException {
    Path sp = sourcePath == null ? null : Path.of(sourcePath).toAbsolutePath();
    Path tp = temperaturePath == null ? null : Path.of(temperaturePath).toAbsolutePath();
    Path op = otherPath == null ? null : Path.of(otherPath).toAbsolutePath();
    Path np = navigationPath == null ? null : Path.of(navigationPath).toAbsolutePath();
    Path dp = documentsPath == null ? null : Path.of(documentsPath).toAbsolutePath();
    Path cdp = calibrationDocumentsPath == null ? null : Path.of(calibrationDocumentsPath).toAbsolutePath();
    Path bp = biologicalPath == null ? null : Path.of(biologicalPath).toAbsolutePath();
    
    PackingJob packingJob = createPackingJob(
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
        objectMapper.writeValueAsString(packingJob), PackingJob.class
    );

    String expectedMetadata = objectMapper.writerWithView(Dataset.class).writeValueAsString(packingJob);
    
    processor.process(packingJob, testOutputPath, sourceContainsAudioData);

    Path baseExpectedOutputPath = testOutputPath.resolve("data");

    checkTargetPaths(packingJob.getBiologicalPath(), baseExpectedOutputPath.resolve("biological"));
    checkTargetPaths(packingJob.getCalibrationDocumentsPath(), baseExpectedOutputPath.resolve("calibration"));
    checkTargetPaths(packingJob.getDocumentsPath(), baseExpectedOutputPath.resolve("docs"));
    checkTargetPaths(packingJob.getNavigationPath(), baseExpectedOutputPath.resolve("nav_files"));
    checkTargetPaths(packingJob.getOtherPath(), baseExpectedOutputPath.resolve("other"));
    checkTargetPaths(packingJob.getTemperaturePath(), baseExpectedOutputPath.resolve("temperature"));
    checkTargetPaths(packingJob.getSourcePath(), baseExpectedOutputPath.resolve(
        sourceContainsAudioData ? "acoustic_files" : "data_files"
    ));
    
    String actualMetadata = FileUtils.readFileToString(testOutputPath.resolve("data").resolve(String.format(
        "%s.json", ((Dataset) packingJob).getPackageId()
    )).toFile(), StandardCharsets.UTF_8);
    
    assertEquals(expectedMetadata, actualMetadata);
    
    Dataset dataset = objectMapper.readValue(actualMetadata, Dataset.class);
    assertInstanceOf(AudioDataset.class, dataset);
  }

  private PackingJob createPackingJob(
      Path sourcePath,
      Path temperaturePath,
      Path otherPath,
      Path navigationPath,
      Path documentsPath,
      Path calibrationDocumentsPath,
      Path biologicalPath
  ) {
    return AudioPackingJob.builder()
        .sourcePath(sourcePath)
        .temperaturePath(temperaturePath)
        .otherPath(otherPath)
        .navigationPath(navigationPath)
        .documentsPath(documentsPath)
        .calibrationDocumentsPath(calibrationDocumentsPath)
        .biologicalPath(biologicalPath)
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("packager-name")
            .position("packer-position")
            .organization("packer-organization")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-name-1")
                .build(),
            Project.builder()
                .name("project-name-1")
                .build()
        )).publicReleaseDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .position("scientist-1-position")
                .organization("scientist-1-organization")
                .build(),
            Person.builder()
                .name("scientist-2")
                .position("scientist-2-position")
                .organization("scientist-2-organization")
                .build()
        )).sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        )).platform(
            Platform.builder()
                .name("platform")
                .build()
        ).instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .comment("comment-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .comment("comment-2")
                    .build()
            ))
            .build())
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
        .qualityAnalyst(Person.builder()
            .name("quality-analyst")
            .position("quality-analyzer")
            .organization("quality-analysis-organization")
            .build())
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
            AudioSensor.builder()
                .preampId("preampId")
                .hydrophoneId("hydrophoneId")
                .description("audio-sensor-description")
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .name("audio-sensor-1")
                .build(),
            DepthSensor.builder()
                .name("depth-sensor")
                .description("depth-sensor-description")
                .position(Position.builder()
                    .x(10f)
                    .y(20f)
                    .z(30f)
                    .build())
                .build()
        )).channels(List.of(
            Channel.builder()
                .sensor(AudioSensor.builder()
                    .preampId("preampId")
                    .hydrophoneId("hydrophoneId")
                    .description("audio-sensor-description")
                    .position(Position.builder()
                        .x(1f)
                        .y(2f)
                        .z(3f)
                        .build())
                    .name("audio-sensor-2")
                    .build())
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
            .seaArea(Sea.builder()
                .name("sea-area")
                .build())
            .deploymentLocation(MarineInstrumentLocation.builder()
                .instrumentDepth(-10f)
                .seaFloorDepth(-100f)
                .longitude(10f)
                .latitude(10f)
                .build())
            .recoveryLocation(MarineInstrumentLocation.builder()
                .instrumentDepth(-20f)
                .seaFloorDepth(-110f)
                .longitude(15f)
                .latitude(15f)
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
