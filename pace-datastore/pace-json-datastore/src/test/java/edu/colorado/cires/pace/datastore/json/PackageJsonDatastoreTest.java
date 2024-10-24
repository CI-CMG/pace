package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
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
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("packager-name")
        .projects(List.of(
            "project-name-1", "project-name-2"
        )).publicReleaseDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platform("platform")
        .instrument("instrument")
        .instrumentId("instrumentId")
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .qualityAnalyst("quality-analyst")
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
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build()
        )).channels(List.of(
            Channel.<String>builder()
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
            .seaArea("sea-area")
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
