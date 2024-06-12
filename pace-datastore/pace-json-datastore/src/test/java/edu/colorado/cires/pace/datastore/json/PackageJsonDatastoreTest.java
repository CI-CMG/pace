package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class PackageJsonDatastoreTest extends JsonDatastoreTest<Package> {

  @Override
  protected Class<Package> getClazz() {
    return Package.class;
  }

  @Override
  protected JsonDatastore<Package> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new PackageJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "packageId";
  }

  @Override
  protected TypeReference<List<Package>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Package createNewObject(int suffix) {
    return createPackingJob(suffix);
  }

  @Override
  protected void assertObjectsEqual(Package expected, Package actual) {

    try {
      assertEquals(
          OBJECT_MAPPER.writeValueAsString(expected),
          OBJECT_MAPPER.writeValueAsString(actual)
      );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private Package createPackingJob(int suffix) {
    return AudioPackage.builder()
        .uuid(UUID.randomUUID())
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
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
}
