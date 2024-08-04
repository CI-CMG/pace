package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioDataPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.ProcessingLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackageRepositoryTest extends CrudRepositoryTest<Package> {
  
  protected final Map<UUID, DetectionType> detectionTypes = new HashMap<>(0);
  protected final Map<UUID, Instrument> instruments = new HashMap<>(0);
  protected final Map<UUID, Organization> organizations = new HashMap<>(0);
  protected final Map<UUID, Person> people = new HashMap<>(0);
  protected final Map<UUID, Platform> platforms = new HashMap<>(0);
  protected final Map<UUID, Project> projects = new HashMap<>(0);
  protected final Map<UUID, Sea> seas = new HashMap<>(0);
  protected final Map<UUID, Sensor> sensors = new HashMap<>(0);
  protected final Map<UUID, Ship> ships = new HashMap<>(0);

  @BeforeEach
  void setUp() {
    detectionTypes.clear();
    instruments.clear();
    organizations.clear();
    people.clear();
    platforms.clear();
    projects.clear();
    seas.clear();
    sensors.clear();
    ships.clear();
  }

  @AfterEach
  void tearDown() {
    detectionTypes.clear();
    instruments.clear();
    organizations.clear();
    people.clear();
    platforms.clear();
    projects.clear();
    seas.clear();
    sensors.clear();
    ships.clear();
  }

  @Override
  protected CRUDRepository<Package> createRepository() {
    return new PackageRepository(
        createDatastore(),
        createDatastore(detectionTypes, DetectionType.class, "source"),
        createDatastore(instruments, Instrument.class, "name"),
        createDatastore(organizations, Organization.class, "name"),
        createDatastore(people, Person.class, "name"),
        createDatastore(platforms, Platform.class, "name"),
        createDatastore(projects, Project.class, "name"),
        createDatastore(seas, Sea.class, "name"),
        createDatastore(sensors, Sensor.class, "name"),
        createDatastore(ships, Ship.class, "name")
    );
  }

  @Override
  protected String getUniqueFieldName() {
    return "packageId";
  }

  @Override
  protected Class<Package> getObjectClass() {
    return Package.class;
  }

  @Override
  protected Package createNewObject(int suffix) {
    AudioDataPackage audioDataPackage = createAudioPackingJob(suffix);
    saveAudioDataDependencies(audioDataPackage);
    return audioDataPackage;
  }

  @Override
  protected Package copyWithUpdatedUniqueField(Package object, String uniqueField) {
    insertObjectIntoMap(
        projects,
        Project.builder()
            .uuid(UUID.randomUUID())
            .name(uniqueField)
            .build()
    );
    
    if (object instanceof AudioPackage) {
      return ((AudioPackage) object).toBuilder()
          .site(uniqueField)
          .projectName(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof CPODPackage) {
      return ((CPODPackage) object).toBuilder()
          .site(uniqueField)
          .projectName(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof DetectionsPackage) {
      return ((DetectionsPackage) object).toBuilder()
          .site(uniqueField)
          .projectName(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof SoundClipsPackage) {
      return ((SoundClipsPackage) object).toBuilder()
          .site(uniqueField)
          .projectName(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof SoundLevelMetricsPackage) {
      return ((SoundLevelMetricsPackage) object).toBuilder()
          .site(uniqueField)
          .projectName(Collections.singletonList(uniqueField))
          .deploymentId(uniqueField)
          .build();
    }
    if (object instanceof SoundPropagationModelsPackage) {
      return ((SoundPropagationModelsPackage) object).toBuilder()
          .site(uniqueField)
          .projectName(Collections.singletonList(uniqueField))
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
  void testCreateConstrainViolation() {
    Package object = createNewObject(1);
    object = copyWithUpdatedUniqueField(object, "");

    Package finalObject = object;
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> repository.create(finalObject));
    assertEquals(String.format(
        "%s validation failed", repository.getClassName()
    ), exception.getMessage());

    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    assertEquals(4, constraintViolations.size());
    List<ConstraintViolation<?>> constraintViolation = constraintViolations.stream()
        .sorted((Comparator.comparing(o -> o.getPropertyPath().toString())))
        .toList();
    assertEquals("dataCollectionName", constraintViolation.get(0).getPropertyPath().toString());
    assertEquals("at least dataCollectionName, site, or deploymentId required", constraintViolation.get(0).getMessage());
    assertEquals("deploymentId", constraintViolation.get(1).getPropertyPath().toString());
    assertEquals("at least dataCollectionName, site, or deploymentId required", constraintViolation.get(1).getMessage());
    assertEquals("projectName[0].<list element>", constraintViolation.get(2).getPropertyPath().toString());
    assertEquals("must not be blank", constraintViolation.get(2).getMessage());
    assertEquals("site", constraintViolation.get(3).getPropertyPath().toString());
    assertEquals("at least dataCollectionName, site, or deploymentId required", constraintViolation.get(3).getMessage());
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
  
  private void saveBaseDatasetDependencies(Package p) {
    insertObjectIntoMap(
        instruments,
        Instrument.builder()
            .uuid(UUID.randomUUID())
            .name(p.getInstrumentType())
            .fileTypes(Collections.singletonList("test"))
            .build()
    );

    insertObjectsIntoMap(
        projects,
        (List<Project>) p.getProjectName().stream()
            .map(name -> Project.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .build())
            .toList()
    );

    insertObjectsIntoMap(
        organizations,
        (List<Organization>) p.getSponsors().stream()
            .map(name -> Organization.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .build())
            .toList()
    );

    insertObjectsIntoMap(
        organizations,
        (List<Organization>) p.getFunders().stream()
            .map(name -> Organization.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .build())
            .toList()
    );

    if (p.getLocationDetail() instanceof MarineLocation marineLocation) {
      insertObjectIntoMap(
          seas,
          Sea.builder()
              .uuid(UUID.randomUUID())
              .name(marineLocation.getSeaArea())
              .build()
      );
    }

    insertObjectsIntoMap(
        people,
        (List<Person>) p.getScientists().stream()
            .map(name -> Person.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .build())
            .toList()
    );

    insertObjectIntoMap(
        people,
        Person.builder()
            .uuid(UUID.randomUUID())
            .name(p.getDatasetPackager())
            .build()
    );

    insertObjectIntoMap(
        platforms,
        Platform.builder()
            .uuid(UUID.randomUUID())
            .name(p.getPlatformName())
            .build()
    );
  }

  private void saveAudioDataDependencies(AudioDataPackage audioDataPackage) {
    saveBaseDatasetDependencies(audioDataPackage);
    insertObjectsIntoMap(
        sensors,
        (List<Sensor>) audioDataPackage.getSensors().stream()
            .map(packageSensor -> DepthSensor.builder()
                .uuid(UUID.randomUUID())
                .name(packageSensor.getSensor())
                .build())
            .toList()
    );
    
    insertObjectsIntoMap(
        sensors,
        (List<Sensor>) audioDataPackage.getChannels().stream()
            .map(Channel::getSensor)
            .map(packageSensor -> DepthSensor.builder()
                .uuid(UUID.randomUUID())
                .name(packageSensor.getSensor())
                .build())
            .toList()
    );
    
    insertObjectIntoMap(
        people,
        Person.builder()
            .uuid(UUID.randomUUID())
            .name(audioDataPackage.getQualityAnalyst())
            .build()
    );
  }

  private <T extends ObjectWithUniqueField> void insertObjectsIntoMap(Map<UUID, T> map, List<T> objects) {
    objects.forEach(
        object -> insertObjectIntoMap(map, object)
    );
  }

  private <T extends ObjectWithUniqueField> void insertObjectIntoMap(Map<UUID, T> map, T object) {
    map.put(object.getUuid(), object);
  }

  private void saveDetectionsDependencies(DetectionsPackage p) {
    saveBaseDatasetDependencies(p);

    insertObjectIntoMap(
        people,
        Person.builder()
            .uuid(UUID.randomUUID())
            .name(p.getQualityAnalyst())
            .build()
    );
    
    insertObjectIntoMap(
        detectionTypes,
        DetectionType.builder()
            .uuid(UUID.randomUUID())
            .source(p.getSoundSource())
            .build()
    );
    
    insertObjectIntoMap(
        ships,
        Ship.builder()
            .uuid(UUID.randomUUID())
            .name(((MobileMarineLocation) p.getLocationDetail()).getVessel())
            .build()
    );
  }

  @Test
  void testCreateDetectionsPackage() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createDetectionsDataset(1);
    saveDetectionsDependencies((DetectionsPackage) object);
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
  void testCreateDetectionsPackageSoundSourceNotFound() {
    Package object = createDetectionsDataset(1);
    saveDetectionsDependencies((DetectionsPackage) object);
    detectionTypes.clear();
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> repository.create(object));
    assertEquals("Package validation failed", exception.getMessage());
    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.iterator().next();
    assertEquals("soundSource", constraintViolation.getPropertyPath().toString());
    assertEquals("DetectionType with source = sound-source does not exist", constraintViolation.getMessage());
  }

  @Test
  void testCreateSoundClipsPackage() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createSoundClipsDataset(1);
    saveBaseDatasetDependencies(object);
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
    saveSoundLevelMetricsDependencies((SoundLevelMetricsPackage) object);
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

  private void saveSoundLevelMetricsDependencies(SoundLevelMetricsPackage p) {
    saveBaseDatasetDependencies(p);
    insertObjectIntoMap(
        people,
        Person.builder()
            .uuid(UUID.randomUUID())
            .name(p.getQualityAnalyst())
            .build()
    );
  }

  @Test
  void testCreateSoundPropagationModelsDataset() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Package object = createSoundPropagationModelsDataset(1);
    saveBaseDatasetDependencies(object);
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

  public static AudioDataPackage createAudioPackingJob(int suffix) {
    return AudioPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .site(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("packager-name")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).publishDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platformName("platform")
        .instrumentType("instrument")
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .hydrophoneSensitivity(10f)
        .frequencyRange("1-5")
        .gain(1f)
        .title("deployment-title")
        .purpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .deploymentAlias("alternate-deployment-name")
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
                .sensor(PackageSensor.<String>builder()
                    .sensor("audio-sensor")
                    .position(Position.builder()
                        .x(1f)
                        .y(2f)
                        .z(3f)
                        .build())
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

  public static Package createSoundPropagationModelsDataset(int suffix) {
    return SoundPropagationModelsPackage.builder()
        .modeledFrequency(1f)
        .sourcePath(Paths.get("source-path"))
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .site(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("packager-name")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).publishDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .title("deployment-title")
        .purpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .deploymentAlias("alternate-deployment-name")
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

  public static Package createSoundLevelMetricsDataset(int suffix) {
    return SoundLevelMetricsPackage.builder()
        .minFrequency(1f)
        .maxFrequency(2f)
        .sourcePath(Paths.get("source-path"))
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .site(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("packager-name")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).publishDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .title("deployment-title")
        .purpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .deploymentAlias("alternate-deployment-name")
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
        )).locationDetail(StationaryTerrestrialLocation.builder()
            .latitude(1d)
            .longitude(2d)
            .instrumentElevation(4f)
            .surfaceElevation(3f)
            .build())
        .build();
  }

  private Package createSoundClipsDataset(int suffix) {
    return SoundClipsPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .site(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).publishDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .title("deployment-title")
        .purpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .deploymentAlias("alternate-deployment-name")
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
  
  public static Package createDetectionsDataset(int suffix) {
    return DetectionsPackage.builder()
        .minFrequency(1f)
        .maxFrequency(2f)
        .soundSource("sound-source")
        .sourcePath(Paths.get("source-path"))
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .site(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).publishDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .title("deployment-title")
        .purpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .deploymentAlias("alternate-deployment-name")
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
        )).locationDetail(MobileMarineLocation.builder()
            .seaArea("sea-area")
            .vessel("vessel")
            .locationDerivationDescription("location description")
            .build())
        .build();
  }
  
  private Package createCPODDataset(int suffix) {
    CPODPackage cpodPackage = CPODPackage.builder()
        .sourcePath(Paths.get("source-path"))
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(Paths.get("temperature-path"))
        .otherPath(Paths.get("other-path"))
        .navigationPath(Paths.get("navigation-path"))
        .documentsPath(Paths.get("documents-path"))
        .calibrationDocumentsPath(Paths.get("calibration-documents-path"))
        .biologicalPath(Paths.get("biological-path"))
        .site(String.format(
            "siteOrCruiseName-%s", suffix
        ))
        .deploymentId(String.format(
            "deploymentId-%s", suffix
        ))
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).publishDate(LocalDate.now().plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platformName("platform")
        .instrumentType("instrument")
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.now().minusMinutes(1))
        .endTime(LocalDateTime.now())
        .preDeploymentCalibrationDate(LocalDate.now().minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.now().plusDays(1))
        .calibrationDescription("calibration-description")
        .hydrophoneSensitivity(10f)
        .frequencyRange("1-5")
        .gain(1f)
        .title("deployment-title")
        .purpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .deploymentAlias("alternate-deployment-name")
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
                .sensor(PackageSensor.<String>builder()
                    .sensor("audio-sensor")
                    .position(Position.builder()
                        .x(1f)
                        .y(2f)
                        .z(3f)
                        .build())
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
    
    saveAudioDataDependencies(cpodPackage);
    return cpodPackage;
  }
}
