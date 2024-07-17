package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.PackageSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class PackageRepositoryTest extends CrudRepositoryTest<Package> {

  @Override
  protected CRUDRepository<Package> createRepository() {
    return new PackageRepository(createDatastore());
  }

  @Override
  protected SearchParameters<Package> createSearchParameters(List<Package> objects) {
    return PackageSearchParameters.builder()
        .packageIds(objects.stream()
            .map(Package::getPackageId)
            .toList())
        .build();
  }

  @Override
  protected Package createNewObject(int suffix) {
    return createPackingJob(suffix);
  }

  @Override
  protected Package copyWithUpdatedUniqueField(Package object, String uniqueField) {
    if (object instanceof AudioPackage) {
      return ((AudioPackage) object).toBuilder()
          .siteOrCruiseName(uniqueField)
          .projects(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof CPODPackage) {
      return ((CPODPackage) object).toBuilder()
          .siteOrCruiseName(uniqueField)
          .projects(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof DetectionsPackage) {
      return ((DetectionsPackage) object).toBuilder()
          .siteOrCruiseName(uniqueField)
          .projects(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof SoundClipsPackage) {
      return ((SoundClipsPackage) object).toBuilder()
          .siteOrCruiseName(uniqueField)
          .projects(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof SoundLevelMetricsPackage) {
      return ((SoundLevelMetricsPackage) object).toBuilder()
          .siteOrCruiseName(uniqueField)
          .projects(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof SoundPropagationModelsPackage) {
      return ((SoundPropagationModelsPackage) object).toBuilder()
          .siteOrCruiseName(uniqueField)
          .projects(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }

    return object;
  }

  @Override
  protected void assertObjectsEqual(Package expected, Package actual, boolean checkUUID) {
    ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
    
    if (!checkUUID) {
      if (actual instanceof AudioPackage) {
        actual = ((AudioPackage) actual).toBuilder()
            .uuid(null)
            .build();
      }
      if (actual instanceof CPODPackage) {
        actual = ((CPODPackage) actual).toBuilder()
            .uuid(null)
            .build();
      }
      if (actual instanceof DetectionsPackage) {
        actual = ((DetectionsPackage) actual).toBuilder()
            .uuid(null)
            .build();
      }
      if (actual instanceof SoundClipsPackage) {
        actual = ((SoundClipsPackage) actual).toBuilder()
            .uuid(null)
            .build();
      }
      if (actual instanceof SoundLevelMetricsPackage) {
        actual = ((SoundLevelMetricsPackage) actual).toBuilder()
            .uuid(null)
            .build();
      }
      if (actual instanceof SoundPropagationModelsPackage) {
        actual = ((SoundPropagationModelsPackage) actual).toBuilder()
            .uuid(null)
            .build();
      }
    }

    try {
      assertEquals(
          objectMapper.writeValueAsString(expected),
          objectMapper.writeValueAsString(actual)
      );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Test
  void testCreateCPODDataset() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createCPODDataset(1);
    Package created = repository.create(object);
    assertNotNull(created.getUuid());
    assertEquals(object.getPackageId(), created.getPackageId());
    assertObjectsEqual(object, created, false);

    Package saved = map.get(created.getUuid());
    assertNotNull(saved);
    assertEquals(created.getUuid(), saved.getUuid());
    assertEquals(created.getPackageId(), saved.getPackageId());
    assertObjectsEqual(created, saved, true);
  }

  @Test
  void testCreateDetectionsPackage() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createDetectionsDataset(1);
    Package created = repository.create(object);
    assertNotNull(created.getUuid());
    assertEquals(object.getPackageId(), created.getPackageId());
    assertObjectsEqual(object, created, false);

    Package saved = map.get(created.getUuid());
    assertNotNull(saved);
    assertEquals(created.getUuid(), saved.getUuid());
    assertEquals(created.getPackageId(), saved.getPackageId());
    assertObjectsEqual(created, saved, true);
  }
  
  @Test
  void testCreateSoundClipsPackage() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createSoundClipsDataset(1);
    Package created = repository.create(object);
    assertNotNull(created.getUuid());
    assertEquals(object.getPackageId(), created.getPackageId());
    assertObjectsEqual(object, created, false);

    Package saved = map.get(created.getUuid());
    assertNotNull(saved);
    assertEquals(created.getUuid(), saved.getUuid());
    assertEquals(created.getPackageId(), saved.getPackageId());
    assertObjectsEqual(created, saved, true);
  }
  
  @Test
  void testCreateSoundLevelMetricsDataset() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createSoundLevelMetricsDataset(1);
    Package created = repository.create(object);
    assertNotNull(created.getUuid());
    assertEquals(object.getPackageId(), created.getPackageId());
    assertObjectsEqual(object, created, false);

    Package saved = map.get(created.getUuid());
    assertNotNull(saved);
    assertEquals(created.getUuid(), saved.getUuid());
    assertEquals(created.getPackageId(), saved.getPackageId());
    assertObjectsEqual(created, saved, true);
  }

  @Test
  void testCreateSoundPropagationModelsDataset() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createSoundPropagationModelsDataset(1);
    Package created = repository.create(object);
    assertNotNull(created.getUuid());
    assertEquals(object.getPackageId(), created.getPackageId());
    assertObjectsEqual(object, created, false);

    Package saved = map.get(created.getUuid());
    assertNotNull(saved);
    assertEquals(created.getUuid(), saved.getUuid());
    assertEquals(created.getPackageId(), saved.getPackageId());
    assertObjectsEqual(created, saved, true);
  }
  
  @Test
  void testCreateUnsupportedDataset() {
    Package packingJob = new Package(
        ((SoundPropagationModelsPackage) createSoundPropagationModelsDataset(1)).toBuilder()
    ) {};
    
    Exception exception = assertThrows(BadArgumentException.class, () -> repository.create(packingJob));
    assertEquals(String.format(
        "Unsupported package type: %s", packingJob.getClass().getSimpleName()
    ), exception.getMessage());
  }

  private Package createPackingJob(int suffix) {
    return AudioPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", suffix
        ))
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
            "audio-sensor", "depth-sensor"
        )).channels(List.of(
            Channel.builder()
                .sensor("audio-sensor")
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

  private Package createSoundPropagationModelsDataset(int suffix) {
    return SoundPropagationModelsPackage.builder()
        .modeledFrequency(1f)
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", suffix
        ))
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
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .locationDetail(StationaryMarineLocation.builder()
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

  private Package createSoundLevelMetricsDataset(int suffix) {
    return SoundLevelMetricsPackage.builder()
        .minFrequency(1f)
        .maxFrequency(2f)
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", suffix
        ))
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
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
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

  private Package createSoundClipsDataset(int suffix) {
    return SoundClipsPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
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
        )).platform("platform")
        .instrument("instrument")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .locationDetail(StationaryMarineLocation.builder()
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
  
  private Package createDetectionsDataset(int suffix) {
    return DetectionsPackage.builder()
        .minFrequency(1f)
        .maxFrequency(2f)
        .soundSource("sound-source")
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
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
        )).platform("platform")
        .instrument("instrument")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
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
  
  private Package createCPODDataset(int suffix) {
    return CPODPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .siteOrCruiseName(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
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
        )).platform("platform")
        .instrument("instrument")
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
            "audio-sensor", "depth-sensor"
        )).channels(List.of(
            Channel.builder()
                .sensor("audio-sensor")
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
