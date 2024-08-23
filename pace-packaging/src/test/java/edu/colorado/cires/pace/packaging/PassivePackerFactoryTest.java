package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioDataPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.other.OtherSensor;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.passivePacker.data.PassivePackerPackage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PassivePackerFactoryTest {

  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper()
      .configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false) // for testing only
      .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
      .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE);
  
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  private final SensorRepository sensorRepository = mock(SensorRepository.class);
  private final DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
  
  private final PassivePackerFactory factory = new PassivePackerFactory(personRepository, organizationRepository, sensorRepository, detectionTypeRepository);
  
  @BeforeEach
  void beforeEach() throws NotFoundException, DatastoreException {
    when(personRepository.getByUniqueField("Chuck Anderson")).thenReturn(Person.builder()
        .uuid(UUID.fromString("ad7776a0-6c05-11e2-bcfd-0800200c9a66"))
        .name("Chuck Anderson")
        .organization("NESDIS/NCEI")
        .position("Water Column Data Manager")
        .street("NOAA/NCEI, 325 Broadway")
        .city("Boulder")
        .state("CO")
        .zip("80305")
        .country("USA")
        .email("")
        .phone("")
        .build());

    when(organizationRepository.getByUniqueField("NOAA AFSC")).thenReturn(Organization.builder()
        .uuid(UUID.fromString("01082b23-154c-4c2d-bef7-5e35315d41e0"))
        .name("NOAA AFSC")
        .build());
    when(organizationRepository.getByUniqueField("BOEM")).thenReturn(Organization.builder()
        .uuid(UUID.fromString("fee4d17a-a7dd-4f13-ade5-817b4f1fc86d"))
        .name("BOEM")
        .build());
    
    when(sensorRepository.getByUniqueField("sensor name 1")).thenReturn(AudioSensor.builder()
            .name("sensor name 1")
            .id("sensor id 1")
            .description("audio sensor description")
            .hydrophoneId("hydrophone id")
            .preampId("preamp id")
        .build());
    when(sensorRepository.getByUniqueField("sensor name 2")).thenReturn(DepthSensor.builder()
            .name("sensor name 2")
            .id("sensor id 2")
            .description("depth sensor description")
        .build());
    when(sensorRepository.getByUniqueField("sensor name 3")).thenReturn(OtherSensor.builder()
            .name("sensor name 3")
            .id("sensor id 3")
            .description("other sensor description")
            .sensorType("sensor type")
            .properties("sensor properties")
        .build());
    
    when(detectionTypeRepository.getByUniqueField("Blue whale")).thenReturn(DetectionType.builder()
            .source("Blue whale")
            .scienceName("Balaenoptera musculus")
        .build());
  }
  
  @Test
  void testMobileMarineAudio() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/mobile_audio.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createAudioPackage(createMobileMarineLocation()))
        )
    );
  }
  
  @Test
  void testMultipointMarineAudio() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/multipoint_audio.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createAudioPackage(createMultiPointMarineLocation()))
        )
    );
  }

  @Test
  void testStationaryMarineAudio() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/stationary_marine_audio.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createAudioPackage(createStationaryMarineLocation()))
        )
    );
  }

  @Test
  void testStationaryTerrestrialAudio() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/stationary_terrestrial_audio.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createAudioPackage(createStationaryTerrestrialLocation()))
        )
    );
  }

  @Test
  void testSoundClipsCalibrated() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/sound_clips_calibrated.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createSoundClipsPackage(createStationaryMarineLocation(), LocalDate.of(2010, 1, 1)))
        )
    );
  }

  @Test
  void testSoundClipsUncalibrated() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/sound_clips_uncalibrated.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createSoundClipsPackage(createStationaryMarineLocation(), null))
        )
    );
  }
  
  @Test
  void testSoundLevelMetrics() throws NotFoundException, DatastoreException, IOException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/sound_level_metrics.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createSoundLevelMetricsPackage(createStationaryMarineLocation(), LocalDate.of(2010, 1, 1)))
        )
    );
  }

  @Test
  void testSoundPropagationModels() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/sound_propagation_models.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createSoundPropagationModelsPackage(createStationaryMarineLocation(), LocalDate.of(2010, 1, 1)))
        )
    );
  }

  @Test
  void testCPODFactoryCalibrated() throws NotFoundException, DatastoreException, IOException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/stationary_cpod_factory_calibrated.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createCPODPackage(createStationaryMarineLocation(), null, null))
        )
    );
  }
  
  @Test
  void testCPODPostDeploymentCalibrated() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/stationary_cpod_post_deployment_calibrated.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createCPODPackage(createStationaryMarineLocation(), null, LocalDate.of(2010, 1, 1)))
        )
    );
  }
  
  @Test
  void testCPODPreDeploymentCalibrated() throws NotFoundException, DatastoreException, IOException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/stationary_cpod_pre_deployment_calibrated.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createCPODPackage(createStationaryMarineLocation(), LocalDate.of(2010, 1, 1), null))
        )
    );
  }
  
  @Test
  void testCPODPrePostDeploymentCalibrated() throws IOException, NotFoundException, DatastoreException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/stationary_cpod_pre_post_deployment_calibrated.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createCPODPackage(createStationaryMarineLocation(), LocalDate.of(2010, 1, 1), LocalDate.of(2011, 1, 2)))
        )
    );
  }
  
  @Test
  void testDetections() throws NotFoundException, DatastoreException, IOException {
    assertEquals(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readValue(new File("src/test/resources/detections.json"), PassivePackerPackage.class)
        ),
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            factory.createPackage(createDetectionsPackage(createStationaryMarineLocation(), null))
        )
    );
  }

  private Package createDetectionsPackage(LocationDetail locationDetail, LocalDate preDeploymentCalibrationDate) {
    return DetectionsPackage.builder()
        .baseFields(createBasePackage(locationDetail))
        .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
        .postDeploymentCalibrationDate(null)
        .soundSource("Blue whale")
        .qualityAnalyst("Chuck Anderson")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.good)
                .minFrequency(0f)
                .maxFrequency(500f)
                .startTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2011, 1, 1, 15, 0,0))
                .comments("quality comments")
                .channelNumbers(List.of(1, 2))
                .build(),
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.unusable)
                .minFrequency(1f)
                .maxFrequency(20f)
                .startTime(LocalDateTime.of(2011, 1, 6, 13, 0, 0))
                .endTime(LocalDateTime.of(2012, 1, 8, 13, 0, 0))
                .comments("quality comments 2")
                .channelNumbers(List.of(2))
                .build()
        )).qualityAnalysisMethod("quality method")
        .qualityAnalysisObjectives("quality objectives")
        .qualityAssessmentDescription("quality assessment description")
        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .endTime(LocalDateTime.of(2011, 1, 2, 13, 0, 0))
        .analysisTimeZone(6)
        .analysisEffort(20)
        .minFrequency(200f)
        .maxFrequency(400f)
        .sampleRate(20000f)
        .softwareNames("software name")
        .softwareVersions("software version")
        .softwareProtocolCitation("protocol citation")
        .softwareDescription("software statement")
        .softwareProcessingDescription("software processing description")
        .build();
  }

  private Package createSoundPropagationModelsPackage(LocationDetail locationDetail, LocalDate preDeploymentCalibrationDate) {
    return SoundPropagationModelsPackage.builder()
        .baseFields(createBasePackage(locationDetail))
        .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
        .postDeploymentCalibrationDate(null)
        .softwareNames("software names")
        .softwareVersions("software versions")
        .softwareProtocolCitation("protocol citation")
        .softwareDescription("software description")
        .softwareProcessingDescription("software processing description")
        .audioStartTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .audioEndTime(LocalDateTime.of(2010, 1, 2, 13, 0, 0))
        .startTime(LocalDateTime.of(2010, 1, 2, 13, 0, 0))
        .endTime(LocalDateTime.of(2011, 1, 6, 13, 0, 0))
        .modeledFrequency(10000f)
        .build();
  }

  private Package createSoundClipsPackage(LocationDetail locationDetail, LocalDate preDeploymentCalibrationDate) {
    return SoundClipsPackage.builder()
        .baseFields(createBasePackage(locationDetail))
        .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
        .postDeploymentCalibrationDate(null)
        .softwareNames("software names")
        .softwareVersions("software versions")
        .softwareProtocolCitation("protocol citation")
        .softwareDescription("software description")
        .softwareProcessingDescription("software processing description")
        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .endTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
        .audioStartTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .audioEndTime(LocalDateTime.of(2010, 1, 1, 13, 30, 0))
        .build();
  }

  private Package createSoundLevelMetricsPackage(LocationDetail locationDetail, LocalDate preDeploymentCalibrationDate) {
    return SoundLevelMetricsPackage.builder()
        .baseFields(createBasePackage(locationDetail))
        .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
        .postDeploymentCalibrationDate(null)
        .softwareNames("software names")
        .softwareVersions("software versions")
        .softwareProtocolCitation("protocol citation")
        .softwareDescription("software description")
        .softwareProcessingDescription("software processing description")
        .qualityAnalyst("Chuck Anderson")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.unverified)
                .minFrequency(10f)
                .maxFrequency(20f)
                .startTime(LocalDateTime.of(2010, 1, 1, 13, 30, 0))
                .endTime(LocalDateTime.of(2010, 1, 1, 14, 0,0))
                .comments("Quality comment 1")
                .build(),
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.good)
                .minFrequency(30f)
                .maxFrequency(40f)
                .startTime(LocalDateTime.of(2010, 1, 1, 14, 0, 0))
                .endTime(LocalDateTime.of(2010, 1, 1, 15, 0, 0))
                .comments("Quality comment 2")
                .build()
        )).qualityAnalysisMethod("quality method")
        .qualityAnalysisObjectives("quality objectives")
        .qualityAssessmentDescription("quality assessment description")
        .audioStartTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .audioEndTime(LocalDateTime.of(2010, 1, 2, 13, 0, 0))
        .startTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .endTime(LocalDateTime.of(2010, 1, 2, 13, 0, 0))
        .analysisTimeZone(6)
        .analysisEffort(20)
        .minFrequency(200f)
        .maxFrequency(400f)
        .sampleRate(20000f)
        .build();
  }

  private Package createBasePackage(LocationDetail locationDetail) {
    return Package.builder()
        .publicReleaseDate(LocalDate.of(2024, 8, 2))
        .projects(List.of("project"))
        .deploymentId("deployment-id")
        .alternateDeploymentName("alternate deployment name")
        .siteOrCruiseName("site")
        .alternateSiteName("alternate site name")
        .deploymentTitle("deployment title")
        .deploymentDescription("deployment abstract")
        .deploymentPurpose("deployment purpose")
        .locationDetail(locationDetail)
        .sourcePath(Path.of("/Users/paytoncain/Desktop/Project 12_Site 12_12"))
        .preDeploymentCalibrationDate(LocalDate.of(2010, 1, 1))
        .calibrationDescription("calibration description")
        .calibrationDocumentsPath(Paths.get("/Users/paytoncain/Desktop/Project 10_Site 10_10"))
        .scientists(List.of("Chuck Anderson"))
        .sponsors(List.of("NOAA AFSC"))
        .funders(List.of("BOEM"))
        .datasetPackager("Chuck Anderson")
        .platform("Drifter")
        .instrument("AUH")
        .build();
  }
  
  private <P extends AudioDataPackage, B extends AudioDataPackage.AudioDataPackageBuilder<P, ?>> P createAudioDataPackage(LocationDetail locationDetail, B audioDataPackageBuilder) {
    return audioDataPackageBuilder
        .baseFields(createBasePackage(locationDetail))
        .channels(List.of(
            Channel.<String>builder()
                .startTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2012, 1, 1, 13, 0, 0))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                        .endTime(LocalDateTime.of(2012, 1, 2, 13, 0, 0))
                        .sampleRate(1f)
                        .sampleBits(10)
                        .build()
                )).gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.of(2011, 1, 3, 13, 0, 0))
                        .endTime(LocalDateTime.of(2012, 1, 4, 13, 0, 0))
                        .gain(2f)
                        .build()
                )).dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.of(2011, 1, 5, 13, 0, 0))
                        .endTime(LocalDateTime.of(2012, 1, 6, 13, 0, 0))
                        .duration(3f)
                        .interval(30f)
                        .build()
                )).build(),
            Channel.<String>builder()
                .startTime(LocalDateTime.of(2011, 1,1, 13, 0, 0))
                .endTime(LocalDateTime.of(2012, 1, 1, 13, 0, 0))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2011, 1, 1, 13, 0 , 0))
                        .endTime(LocalDateTime.of(2012, 1, 1, 14, 0, 0))
                        .sampleRate(4f)
                        .sampleBits(40)
                        .build()
                )).gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.of(2011, 1,1, 14, 0, 0))
                        .endTime(LocalDateTime.of(2012, 1, 1, 15, 0, 0))
                        .gain(5f)
                        .build()
                )).dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.of(2011, 1, 1, 16, 0, 0))
                        .endTime(LocalDateTime.of(2012, 1, 1, 17, 0, 0))
                        .duration(6f)
                        .interval(60f)
                        .build()
                )).build()
        )).qualityAnalyst("Chuck Anderson")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.good)
                .minFrequency(0f)
                .maxFrequency(500f)
                .startTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
                .endTime(LocalDateTime.of(2011, 1, 1, 15, 0,0))
                .comments("quality comments")
                .channelNumbers(List.of(1, 2))
                .build(),
            DataQualityEntry.builder()
                .qualityLevel(QualityLevel.unusable)
                .minFrequency(1f)
                .maxFrequency(20f)
                .startTime(LocalDateTime.of(2011, 1, 6, 13, 0, 0))
                .endTime(LocalDateTime.of(2012, 1, 8, 13, 0, 0))
                .comments("quality comments 2")
                .channelNumbers(List.of(2))
                .build()
        )).qualityAnalysisMethod("quality method")
        .qualityAnalysisObjectives("quality objectives")
        .qualityAssessmentDescription("quality assessment description")
        .instrumentId("Instrument ID")
        .deploymentTime(LocalDateTime.of(2010, 1, 1, 13, 0, 0))
        .recoveryTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
        .audioStartTime(LocalDateTime.of(2011, 1, 1, 13, 0, 0))
        .audioEndTime(LocalDateTime.of(2012, 1, 1, 13, 0, 0))
        .hydrophoneSensitivity(9f)
        .frequencyRange("10")
        .gain(11f)
        .sensors(List.of(
            PackageSensor.<String>builder()
                .sensor("sensor name 1")
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build(),
            PackageSensor.<String>builder()
                .sensor("sensor name 2")
                .position(Position.builder()
                    .x(4f)
                    .y(5f)
                    .z(6f)
                    .build())
                .build(),
            PackageSensor.<String>builder()
                .sensor("sensor name 3")
                .position(Position.builder()
                    .x(7f)
                    .y(8f)
                    .z(9f)
                    .build())
                .build()
        )).comments("deployment comments")
        .build();
  }
  
  private AudioPackage createAudioPackage(LocationDetail locationDetail) {
    return createAudioDataPackage(locationDetail, AudioPackage.builder());
  }
  
  private CPODPackage createCPODPackage(LocationDetail locationDetail, LocalDate preDeploymentCalibrationDate, LocalDate postDeploymentCalibrationDate) {
    return createAudioDataPackage(locationDetail, CPODPackage.builder()).toBuilder()
        .instrument("C-POD")
        .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
        .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
        .build();
  }
  
  private MobileMarineLocation createMobileMarineLocation() {
    return MobileMarineLocation.builder()
        .seaArea("Baltic Sea")
        .vessel("R/V Fulmar")
        .locationDerivationDescription("location derivation description")
        .fileList(List.of(
            Paths.get("/Users/paytoncain/Desktop/Project 15_Site 15_15/data/other/test-8127.txt"),
            Paths.get("/Users/paytoncain/Desktop/Project 15_Site 15_15/data/other/test-9239.txt")
        ))
        .build();
  }

  private MultiPointStationaryMarineLocation createMultiPointMarineLocation() {
    return MultiPointStationaryMarineLocation.builder()
        .seaArea("Baltic Sea")
        .locations(List.of(
            MarineInstrumentLocation.builder()
                .latitude(-80d)
                .longitude(80d)
                .seaFloorDepth(-200f)
                .instrumentDepth(-100f)
                .build(),
            MarineInstrumentLocation.builder()
                .latitude(-79d)
                .longitude(79d)
                .seaFloorDepth(-199f)
                .instrumentDepth(-99f)
                .build(),
            MarineInstrumentLocation.builder()
                .latitude(-78d)
                .longitude(78d)
                .seaFloorDepth(-198f)
                .instrumentDepth(-98f)
                .build(),
            MarineInstrumentLocation.builder()
                .latitude(-77d)
                .longitude(77d)
                .seaFloorDepth(-197f)
                .instrumentDepth(-97f)
                .build(),
            MarineInstrumentLocation.builder()
                .latitude(-76d)
                .longitude(76d)
                .seaFloorDepth(-196f)
                .instrumentDepth(-96f)
                .build()
        ))
        .build();
  }

  private LocationDetail createStationaryMarineLocation() {
    return StationaryMarineLocation.builder()
        .seaArea("Baltic Sea")
        .deploymentLocation(MarineInstrumentLocation.builder()
            .latitude(-80d)
            .longitude(80d)
            .seaFloorDepth(-200f)
            .instrumentDepth(-100f)
            .build())
        .recoveryLocation(MarineInstrumentLocation.builder()
            .latitude(-85d)
            .longitude(85d)
            .seaFloorDepth(-190f)
            .instrumentDepth(-90f)
            .build())
        .build();
  }

  private LocationDetail createStationaryTerrestrialLocation() {
    return StationaryTerrestrialLocation.builder()
        .latitude(-80d)
        .longitude(80d)
        .instrumentElevation(25f)
        .surfaceElevation(20f)
        .build();
  }
}

