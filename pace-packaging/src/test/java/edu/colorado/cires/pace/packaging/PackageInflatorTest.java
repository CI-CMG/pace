package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.AbstractObjectWithName;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.DetailedAudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.DetailedCPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.detections.DetailedDetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.DetailedSoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.DetailedSoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.DetailedSoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PackageInflatorTest {
  
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  private final ProjectRepository projectRepository = mock(ProjectRepository.class);
  private final PlatformRepository platformRepository = mock(PlatformRepository.class);
  private final InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
  private final SensorRepository sensorRepository = mock(SensorRepository.class);
  private final DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
  
  private final PackageInflator packageInflator = new PackageInflator(
      personRepository, organizationRepository,
      sensorRepository, detectionTypeRepository
  );
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  @Test
  void testProcessAudio() throws NotFoundException, DatastoreException, JsonProcessingException {
    UUID uuid = UUID.randomUUID();
    
    assertEquals(
        objectMapper.writeValueAsString(createDetailedAudio(uuid)),
        objectMapper.writeValueAsString(packageInflator.process(createAudio(uuid)))
    );
  }
  
  @Test
  void testProcessCPOD() throws NotFoundException, DatastoreException, JsonProcessingException {
    UUID uuid = UUID.randomUUID();
    
    assertEquals(
        objectMapper.writeValueAsString(createDetailedCPOD(uuid)),
        objectMapper.writeValueAsString(packageInflator.process(createCPOD(uuid)))
    );
  }
  
  @Test
  void testProcessDetections() throws NotFoundException, DatastoreException, JsonProcessingException {
    UUID uuid = UUID.randomUUID();

    assertEquals(
        objectMapper.writeValueAsString(createDetailedDetections(uuid)),
        objectMapper.writeValueAsString(packageInflator.process(createDetections(uuid)))
    );
  }
  
  @Test
  void testProcessSoundClips() throws NotFoundException, DatastoreException, JsonProcessingException {
    UUID uuid = UUID.randomUUID();

    assertEquals(
        objectMapper.writeValueAsString(createDetailedSoundCLips(uuid)),
        objectMapper.writeValueAsString(packageInflator.process(createSoundClips(uuid)))
    );
  }
  
  @Test
  void testProcessSoundLevelMetrics() throws NotFoundException, DatastoreException, JsonProcessingException {
    UUID uuid = UUID.randomUUID();

    assertEquals(
        objectMapper.writeValueAsString(createDetailedSoundLevelMetrics(uuid)),
        objectMapper.writeValueAsString(packageInflator.process(createSoundLevelMetrics(uuid)))
    );
  }
  
  @Test
  void testProcessSoundPropagationModels() throws JsonProcessingException, NotFoundException, DatastoreException {
    UUID uuid = UUID.randomUUID();

    assertEquals(
        objectMapper.writeValueAsString(createDetailedSoundPropagationModels(uuid)),
        objectMapper.writeValueAsString(packageInflator.process(createSoundPropagationModels(uuid)))
    );
  }
  
  private AudioPackage createAudio(UUID uuid) {
    return AudioPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
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
  
  private DetailedAudioPackage createDetailedAudio(UUID uuid) throws NotFoundException, DatastoreException {
    DetailedAudioPackage detailedPackage = DetailedAudioPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            "project-name-1",
            "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
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
        .qualityAnalyst(Person.builder()
            .name("qualityAnalyst")
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
            PackageSensor.<AbstractObject>builder()
                .sensor(AudioSensor.builder()
                    .name("audio-sensor")
                    .build())
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build(),
            PackageSensor.<AbstractObject>builder()
                .sensor(DepthSensor.builder()
                    .name("depth-sensor")
                    .build())
                .position(Position.builder()
                    .x(4f)
                    .y(5f)
                    .z(6f)
                    .build())
                .build()
        )).channels(List.of(
            Channel.<AbstractObject>builder()
                .sensor(PackageSensor.<AbstractObject>builder()
                    .sensor(AudioSensor.builder()
                        .name("audioSensor")
                        .build())
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

    for (Object scientist : detailedPackage.getScientists()) {
        AbstractObjectWithName object = (AbstractObjectWithName) scientist;
        when(personRepository.getByUniqueField(((AbstractObjectWithName)scientist).getName())).thenReturn(
            Person.builder()
                  .name(object.getName())
                  .build()
        );
    }
    when(personRepository.getByUniqueField(detailedPackage.getQualityAnalyst().getUniqueField())).thenReturn(
        (Person) detailedPackage.getQualityAnalyst());
    when(personRepository.getByUniqueField(((AbstractObject) detailedPackage.getDatasetPackager()).getUniqueField())).thenReturn(
        (Person) detailedPackage.getDatasetPackager()
    );

    for (Object funder : detailedPackage.getFunders()) {
        AbstractObjectWithName object = (AbstractObjectWithName) funder;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }
    for (Object sponsor : detailedPackage.getSponsors()) {
        AbstractObjectWithName object = (AbstractObjectWithName) sponsor;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }

    for (PackageSensor<AbstractObject> sensor : detailedPackage.getSensors()) {
      when(sensorRepository.getByUniqueField(sensor.getSensor().getUniqueField())).thenReturn(
          (Sensor) sensor.getSensor()
      );
    }
    for (Channel<AbstractObject> channel : detailedPackage.getChannels()) {
      when(sensorRepository.getByUniqueField(channel.getSensor().getSensor().getUniqueField())).thenReturn(
          (Sensor) channel.getSensor().getSensor()
      );
    }

    return detailedPackage;
  }
  
  private CPODPackage createCPOD(UUID uuid) {
    return CPODPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
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

  private DetailedCPODPackage createDetailedCPOD(UUID uuid) throws NotFoundException, DatastoreException {
    DetailedCPODPackage detailedPackage = DetailedCPODPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            "project-name-1",
            "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
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
        .qualityAnalyst(Person.builder()
            .name("qualityAnalyst")
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
            PackageSensor.<AbstractObject>builder()
                .sensor(AudioSensor.builder()
                    .name("audio-sensor")
                    .build())
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build(),
            PackageSensor.<AbstractObject>builder()
                .sensor(DepthSensor.builder()
                    .name("depth-sensor")
                    .build())
                .position(Position.builder()
                    .x(4f)
                    .y(5f)
                    .z(6f)
                    .build())
                .build()
        )).channels(List.of(
            Channel.<AbstractObject>builder()
                .sensor(PackageSensor.<AbstractObject>builder()
                    .sensor(AudioSensor.builder()
                        .name("audioSensor")
                        .build())
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

      for (Object scientist : detailedPackage.getScientists()) {
          AbstractObjectWithName object = (AbstractObjectWithName) scientist;
          when(personRepository.getByUniqueField(((AbstractObjectWithName)scientist).getName())).thenReturn(
                  Person.builder()
                          .name(object.getName())
                          .build()
          );
      }
    when(personRepository.getByUniqueField(detailedPackage.getQualityAnalyst().getUniqueField())).thenReturn(
        (Person) detailedPackage.getQualityAnalyst());
    when(personRepository.getByUniqueField(((AbstractObject) detailedPackage.getDatasetPackager()).getUniqueField())).thenReturn(
        (Person) detailedPackage.getDatasetPackager()
    );

    for (Object funder : detailedPackage.getFunders()) {
        AbstractObjectWithName object = (AbstractObjectWithName) funder;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }
    for (Object sponsor : detailedPackage.getSponsors()) {
        AbstractObjectWithName object = (AbstractObjectWithName) sponsor;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }

    for (PackageSensor<AbstractObject> sensor : detailedPackage.getSensors()) {
      when(sensorRepository.getByUniqueField(sensor.getSensor().getUniqueField())).thenReturn(
          (Sensor) sensor.getSensor()
      );
    }
    for (Channel<AbstractObject> channel : detailedPackage.getChannels()) {
      when(sensorRepository.getByUniqueField(channel.getSensor().getSensor().getUniqueField())).thenReturn(
          (Sensor) channel.getSensor().getSensor()
      );
    }

    return detailedPackage;
  }

  private DetectionsPackage createDetections(UUID uuid) {
    return DetectionsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
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
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .analysisTimeZone(1)
        .analysisEffort(2)
        .sampleRate(1f)
        .minFrequency(2f)
        .maxFrequency(3f)
        .soundSource("source")
        .build();
  }
  
  private DetailedDetectionsPackage createDetailedDetections(UUID uuid) throws NotFoundException, DatastoreException {
    DetailedDetectionsPackage detailedPackage = DetailedDetectionsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            "project-name-1",
            "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
        )).platform(
            "platform"
        ).instrument("instrument")
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .qualityAnalyst(Person.builder()
            .name("qualityAnalyst")
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .analysisTimeZone(1)
        .analysisEffort(2)
        .sampleRate(1f)
        .minFrequency(2f)
        .maxFrequency(3f)
        .soundSource(DetectionType.builder()
            .uuid(uuid)
            .source("source")
            .scienceName("science-name")
            .build())
        .build();

      for (Object scientist : detailedPackage.getScientists()) {
          AbstractObjectWithName object = (AbstractObjectWithName) scientist;
          when(personRepository.getByUniqueField(((AbstractObjectWithName)scientist).getName())).thenReturn(
                  Person.builder()
                          .name(object.getName())
                          .build()
          );
      }
    when(personRepository.getByUniqueField(detailedPackage.getQualityAnalyst().getUniqueField())).thenReturn(
        (Person) detailedPackage.getQualityAnalyst());
    when(personRepository.getByUniqueField(((AbstractObject) detailedPackage.getDatasetPackager()).getUniqueField())).thenReturn(
        (Person) detailedPackage.getDatasetPackager()
    );

    for (Object funder : detailedPackage.getFunders()) {
        AbstractObjectWithName object = (AbstractObjectWithName) funder;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }
    for (Object sponsor : detailedPackage.getSponsors()) {
        AbstractObjectWithName object = (AbstractObjectWithName) sponsor;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }

    when(detectionTypeRepository.getByUniqueField(detailedPackage.getSoundSource().getUniqueField())).thenReturn(
        (DetectionType) detailedPackage.getSoundSource()
    );

    return detailedPackage;
  }
  
  private SoundClipsPackage createSoundClips(UUID uuid) {
    return SoundClipsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
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
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .locationDetail(StationaryMarineLocation.builder()
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .build();
  }

  private DetailedSoundClipsPackage createDetailedSoundCLips(UUID uuid) throws NotFoundException, DatastoreException {
    DetailedSoundClipsPackage detailedPackage = DetailedSoundClipsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            "project-name-1",
            "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
        )).platform(
            "platform"
        ).instrument("instrument")
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .locationDetail(StationaryMarineLocation.builder()
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .build();

      for (Object scientist : detailedPackage.getScientists()) {
          AbstractObjectWithName object = (AbstractObjectWithName) scientist;
          when(personRepository.getByUniqueField(((AbstractObjectWithName)scientist).getName())).thenReturn(
                  Person.builder()
                          .name(object.getName())
                          .build()
          );
      }
    when(personRepository.getByUniqueField(((AbstractObject) detailedPackage.getDatasetPackager()).getUniqueField())).thenReturn(
        (Person) detailedPackage.getDatasetPackager()
    );

    for (Object funder : detailedPackage.getFunders()) {
        AbstractObjectWithName object = (AbstractObjectWithName) funder;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }
    for (Object sponsor : detailedPackage.getSponsors()) {
        AbstractObjectWithName object = (AbstractObjectWithName) sponsor;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }

    return detailedPackage;
  }

  private SoundLevelMetricsPackage createSoundLevelMetrics(UUID uuid) {
    return SoundLevelMetricsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
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
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .analysisTimeZone(1)
        .analysisEffort(2)
        .sampleRate(1f)
        .minFrequency(2f)
        .maxFrequency(3f)
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).plusDays(1))
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(1))
        .build();
  }

  private DetailedSoundLevelMetricsPackage createDetailedSoundLevelMetrics(UUID uuid) throws NotFoundException, DatastoreException {
    DetailedSoundLevelMetricsPackage detailedPackage = DetailedSoundLevelMetricsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            "project-name-1",
            "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
        )).platform(
            "platform"
        ).instrument("instrument")
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .qualityAnalyst(Person.builder()
            .name("qualityAnalyst")
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .analysisTimeZone(1)
        .analysisEffort(2)
        .sampleRate(1f)
        .minFrequency(2f)
        .maxFrequency(3f)
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).plusDays(1))
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(1))
        .build();

      for (Object scientist : detailedPackage.getScientists()) {
          AbstractObjectWithName object = (AbstractObjectWithName) scientist;
          when(personRepository.getByUniqueField(((AbstractObjectWithName)scientist).getName())).thenReturn(
                  Person.builder()
                          .name(object.getName())
                          .build()
          );
      }
    when(personRepository.getByUniqueField(detailedPackage.getQualityAnalyst().getUniqueField())).thenReturn(
        (Person) detailedPackage.getQualityAnalyst());
    when(personRepository.getByUniqueField(((AbstractObject) detailedPackage.getDatasetPackager()).getUniqueField())).thenReturn(
        (Person) detailedPackage.getDatasetPackager()
    );

    for (Object funder : detailedPackage.getFunders()) {
        AbstractObjectWithName object = (AbstractObjectWithName) funder;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }
    for (Object sponsor : detailedPackage.getSponsors()) {
        AbstractObjectWithName object = (AbstractObjectWithName) sponsor;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }

    return detailedPackage;
  }

  private SoundPropagationModelsPackage createSoundPropagationModels(UUID uuid) {
    return SoundPropagationModelsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
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
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .locationDetail(StationaryMarineLocation.builder()
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).plusDays(1))
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(1))
        .modeledFrequency(1000f)
        .build();
  }

  private DetailedSoundPropagationModelsPackage createDetailedSoundPropagationModels(UUID uuid) throws NotFoundException, DatastoreException {
    DetailedSoundPropagationModelsPackage detailedPackage = DetailedSoundPropagationModelsPackage.builder()
        .uuid(uuid)
        .sourcePath(Paths.get("sourcePath"))
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperaturePath"))
        .otherPath(Paths.get("otherPath"))
        .navigationPath(Paths.get("navigationPath"))
        .documentsPath(Paths.get("documentsPath"))
        .calibrationDocumentsPath(Paths.get("calibrationDocumentsPath"))
        .biologicalPath(Paths.get("biologicalPath"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            "project-name-1",
            "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
        )).platform(
            "platform"
        ).instrument("instrument")
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .locationDetail(StationaryMarineLocation.builder()
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
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).plusDays(1))
        .audioEndTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(1))
        .modeledFrequency(1000f)
        .build();

      for (Object scientist : detailedPackage.getScientists()) {
          AbstractObjectWithName object = (AbstractObjectWithName) scientist;
          when(personRepository.getByUniqueField(((AbstractObjectWithName)scientist).getName())).thenReturn(
                  Person.builder()
                          .name(object.getName())
                          .build()
          );
      }
    when(personRepository.getByUniqueField(((AbstractObject) detailedPackage.getDatasetPackager()).getUniqueField())).thenReturn(
        (Person) detailedPackage.getDatasetPackager()
    );

    for (Object funder : detailedPackage.getFunders()) {
        AbstractObjectWithName object = (AbstractObjectWithName) funder;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }
    for (Object sponsor : detailedPackage.getSponsors()) {
        AbstractObjectWithName object = (AbstractObjectWithName) sponsor;
      when(organizationRepository.getByUniqueField(object.getName())).thenReturn(
          Organization.builder()
                  .name(object.getName())
                  .build()
      );
    }

    return detailedPackage;
  }
  
}