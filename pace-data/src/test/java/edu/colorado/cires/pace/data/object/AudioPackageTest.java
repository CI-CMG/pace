package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.position.Position;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AudioPackageTest extends PackageTest<AudioPackage> {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Override
  protected AudioPackage createObject() {
    return AudioPackage.builder().build();
  }

  @Test
  void validateChannels() {
    AudioPackage p = AudioPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", 1
        ))
        .deploymentId(String.format(
            "deploymentId-%s", 1
        ))
        .deploymentTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
        .recoveryTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .audioStartTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .datasetPackager("packager-name")
        .projects(List.of(
            "project-name-1", "project-name-2"
        )).publicReleaseDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sources(List.of(
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
    Set<ConstraintViolation<AudioPackage>> errors = validator.validate(p);
    assertFalse(errors.isEmpty());
  }
}
