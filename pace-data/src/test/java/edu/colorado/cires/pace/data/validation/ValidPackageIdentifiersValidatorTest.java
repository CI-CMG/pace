package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.base.ProcessingLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.position.Position;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ValidPackageIdentifiersValidatorTest {
  @ParameterizedTest
  @CsvSource({
      "deploymentId,siteOrCruiseName,dataCollectionName,true",
      "deploymentId,,,true",
      ",siteOrCruiseName,,true",
      ",,dataCollectionName,true",
      "deploymentId,siteOrCruiseName,,true",
      "deploymentId,,dataCollectionName,true",
      ",siteOrCruiseName,dataCollectionName,true",
      ",,,false",
  })
  void testNoDeploymentId(String deploymentId, String siteOrCruiseName, String dataCollectionName, boolean expectedPass) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    AudioPackage audioPackage = createAudio(UUID.randomUUID()).toBuilder()
        .deploymentId(deploymentId)
        .siteOrCruiseName(siteOrCruiseName)
        .dataCollectionName(dataCollectionName)
        .build();
    Set<ConstraintViolation<AudioPackage>> constraintViolations = validator.validate(audioPackage);
    
    if (expectedPass) {
      assertEquals(0, constraintViolations.size());
    } else {
      assertEquals(3, constraintViolations.size());

      assertTrue(
          constraintViolations.stream()
              .allMatch(constraintViolation -> constraintViolation.getMessage()
                  .equals("at least dataCollectionName, site, or deploymentId required"))
      );
      
      assertEquals(
          Set.of("deploymentId","site","dataCollectionName"),
          constraintViolations.stream()
              .map(ConstraintViolation::getPropertyPath)
              .map(Path::toString).collect(Collectors.toSet())
      );
    }
  }

  private AudioPackage createAudio(UUID uuid) {
    return AudioPackage.builder()
        .uuid(uuid)
        .processingLevel(ProcessingLevel.Raw)
        .sourcePath(Paths.get("sourcePath"))
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
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
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .hydrophoneSensitivity(10f)
        .frequencyRange("1-5")
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
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
                .build(),
            DataQualityEntry.builder()
                .comments("comment-2")
                .qualityLevel(QualityLevel.unusable)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(20))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
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
                .sensor(PackageSensor.<String>builder()
                    .sensor("audioSensor")
                    .position(Position.builder()
                        .x(7f)
                        .y(8f)
                        .z(9f)
                        .build())
                    .build())
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
                        .interval(100f)
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

}
