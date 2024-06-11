package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.data.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.translator.GainTranslator;
import edu.colorado.cires.pace.data.translator.MarineInstrumentLocationTranslator;
import edu.colorado.cires.pace.data.translator.MobileMarineLocationTranslator;
import edu.colorado.cires.pace.data.translator.MultipointStationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.data.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.translator.SoundPropagationModelsPackageTranslator;
import edu.colorado.cires.pace.data.translator.StationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PackageConverterTest {
  
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final ProjectRepository projectRepository = mock(ProjectRepository.class);
  private final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  private final PlatformRepository platformRepository = mock(PlatformRepository.class);
  private final InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
  private final SeaRepository seaRepository = mock(SeaRepository.class);
  private final ShipRepository shipRepository = mock(ShipRepository.class);
  private final DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
  private final SensorRepository sensorRepository = mock(SensorRepository.class);
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  
  private final Converter<PackageTranslator, Package> converter = new PackageConverter(
      personRepository,
      projectRepository,
      organizationRepository,
      platformRepository,
      instrumentRepository,
      seaRepository,
      shipRepository,
      detectionTypeRepository,
      sensorRepository
  );

  @Test
  void convertAudioPackage() throws TranslationException, JsonProcessingException, NotFoundException, DatastoreException {
    DepthSensor depthSensor = DepthSensor.builder()
        .name("sensor-1")
        .position(Position.builder()
            .x(101f)
            .y(201f)
            .z(301f)
            .build())
        .description("description-1")
        .build();

    AudioSensor audioSensor = AudioSensor.builder()
        .name("sensor-2")
        .position(Position.builder()
            .x(102f)
            .y(202f)
            .z(302f)
            .build())
        .description("description-2")
        .hydrophoneId("hydrophoneId")
        .preampId("preampId")
        .build();
    
    AudioPackage audioPackage = AudioPackage.builder()
        .uuid(UUID.randomUUID())
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-1")
                .build(),
            Project.builder()
                .name("project-2")
                .build()
        ))
        .publicReleaseDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .build(),
            Person.builder()
                .name("scientist-2")
                .build()
        ))
        .sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        ))
        .funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        ))
        .platform(Platform.builder()
            .name("platform")
            .build())
        .instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build())
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .hydrophoneSensitivity(1f)
        .frequencyRange(2f)
        .gain(3f)
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .qualityAnalyst(Person.builder()
            .name("quality-analyst")
            .build())
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .deploymentTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .recoveryTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .comments("comments")
        .sensors(List.of(
           depthSensor, audioSensor 
        ))
        .channels(List.of(
            Channel.builder()
                .sensor(audioSensor)
                .startTime(LocalDateTime.parse("2020-01-01T01:45:00"))
                .endTime(LocalDateTime.parse("2020-01-01T03:00:00"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:50:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:55:00"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T02:15:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T02:20:00"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:35:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:40:00"))
                        .duration(505f)
                        .interval(506f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:20:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:38:00"))
                        .duration(605f)
                        .interval(606f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:28:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:29:00"))
                        .gain(707f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:31:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:33:00"))
                        .gain(808f)
                        .build()
                ))
                .build(),
            Channel.builder()
                .sensor(depthSensor)
                .startTime(LocalDateTime.parse("2020-01-02T01:45:00"))
                .endTime(LocalDateTime.parse("2020-01-02T03:00:00"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:50:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:55:00"))
                        .sampleRate(702f)
                        .sampleBits(702)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T02:15:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T02:20:00"))
                        .sampleRate(802f)
                        .sampleBits(802)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:35:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:40:00"))
                        .duration(105f)
                        .interval(106f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:20:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:38:00"))
                        .duration(305f)
                        .interval(306f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:28:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:29:00"))
                        .gain(407f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:31:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:33:00"))
                        .gain(508f)
                        .build()
                ))
                .build()
        )).locationDetail(MultiPointStationaryMarineLocation.builder()
            .seaArea(Sea.builder()
                .name("sea-1")
                .build())
            .locations(List.of(
                MarineInstrumentLocation.builder()
                    .latitude(1f)
                    .longitude(2f)
                    .seaFloorDepth(100f)
                    .instrumentDepth(99f)
                    .build(),
                MarineInstrumentLocation.builder()
                    .latitude(2f)
                    .longitude(3f)
                    .seaFloorDepth(101f)
                    .instrumentDepth(100f)
                    .build()
            ))
            .build())
        .build();

    AudioPackageTranslator audioPackageTranslator = AudioPackageTranslator.builder()
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate("publicReleaseDate")
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime("startTime")
        .endTime("endTime")
        .preDeploymentCalibrationDate("preDeploymentCalibrationDate")
        .postDeploymentCalibrationDate("postDeploymentCalibrationDate")
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(MultipointStationaryMarineLocationTranslator.builder()
            .seaArea("seaArea")
            .locationTranslators(List.of(
                MarineInstrumentLocationTranslator.builder()
                    .latitude("latitude-1")
                    .longitude("longitude-1")
                    .seaFloorDepth("seaFloorDepth-1")
                    .instrumentDepth("instrumentDepth-1")
                    .build(),
                MarineInstrumentLocationTranslator.builder()
                    .latitude("latitude-2")
                    .longitude("longitude-2")
                    .seaFloorDepth("seaFloorDepth-2")
                    .instrumentDepth("instrumentDepth-2")
                    .build()
            ))
            .build())
        .instrumentId("instrumentId")
        .hydrophoneSensitivity("hydrophoneSensitivity")
        .frequencyRange("frequencyRange")
        .gain("gain")
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst("qualityAnalyst")
            .qualityAnalysisObjectives("qualityAnalysisObjectives")
            .qualityAnalysisMethod("qualityAnalysisMethod")
            .qualityAssessmentDescription("qualityAssessmentDescription")
            .qualityEntryTranslators(List.of(
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-1")
                    .endTime("quality-entry-endTime-1")
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-2")
                    .endTime("quality-entry-endTime-2")
                    .minFrequency("quality-entry-minFrequency-2")
                    .maxFrequency("quality-entry-maxFrequency-2")
                    .qualityLevel("quality-entry-qualityLevel-2")
                    .comments("quality-entry-comments-2")
                    .build()
            ))
            .build())
        .deploymentTime("deploymentTime")
        .recoveryTime("recoveryTime")
        .comments("comments")
        .sensors("sensors")
        .channelTranslators(List.of(
            ChannelTranslator.builder()
                .sensor("channel-sensor-1")
                .startTime("channel-startTime-1")
                .endTime("channel-endTime-1")
                .sampleRateTranslators(List.of(
                    SampleRateTranslator.builder()
                        .startTime("channel-1-sample-rate-1-startTime")
                        .endTime("channel-1-sample-rate-1-endTime")
                        .sampleRate("channel-1-sample-rate-1-sampleRate")
                        .sampleBits("channel-1-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime("channel-1-sample-rate-2-startTime")
                        .endTime("channel-1-sample-rate-2-endTime")
                        .sampleRate("channel-1-sample-rate-2-sampleRate")
                        .sampleBits("channel-1-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycleTranslators(List.of(
                    DutyCycleTranslator.builder()
                        .startTime("channel-1-duty-cycle-1-startTime")
                        .endTime("channel-1-duty-cycle-1-endTime")
                        .duration("channel-1-duty-cycle-1-duration")
                        .interval("channel-1-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime("channel-1-duty-cycle-2-startTime")
                        .endTime("channel-1-duty-cycle-2-endTime")
                        .duration("channel-1-duty-cycle-2-duration")
                        .interval("channel-1-duty-cycle-2-interval")
                        .build()
                ))
                .gainTranslators(List.of(
                    GainTranslator.builder()
                        .startTime("channel-1-gain-1-startTime")
                        .endTime("channel-1-gain-1-endTime")
                        .gain("channel-1-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime("channel-1-gain-2-startTime")
                        .endTime("channel-1-gain-2-endTime")
                        .gain("channel-1-gain-2-gain")
                        .build()
                ))
                .build(),
            ChannelTranslator.builder()
                .sensor("channel-sensor-2")
                .startTime("channel-startTime-2")
                .endTime("channel-endTime-2")
                .sampleRateTranslators(List.of(
                    SampleRateTranslator.builder()
                        .startTime("channel-2-sample-rate-1-startTime")
                        .endTime("channel-2-sample-rate-1-endTime")
                        .sampleRate("channel-2-sample-rate-1-sampleRate")
                        .sampleBits("channel-2-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime("channel-2-sample-rate-2-startTime")
                        .endTime("channel-2-sample-rate-2-endTime")
                        .sampleRate("channel-2-sample-rate-2-sampleRate")
                        .sampleBits("channel-2-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycleTranslators(List.of(
                    DutyCycleTranslator.builder()
                        .startTime("channel-2-duty-cycle-1-startTime")
                        .endTime("channel-2-duty-cycle-1-endTime")
                        .duration("channel-2-duty-cycle-1-duration")
                        .interval("channel-2-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime("channel-2-duty-cycle-2-startTime")
                        .endTime("channel-2-duty-cycle-2-endTime")
                        .duration("channel-2-duty-cycle-2-duration")
                        .interval("channel-2-duty-cycle-2-interval")
                        .build()
                ))
                .gainTranslators(List.of(
                    GainTranslator.builder()
                        .startTime("channel-2-gain-1-startTime")
                        .endTime("channel-2-gain-1-endTime")
                        .gain("channel-2-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime("channel-2-gain-2-startTime")
                        .endTime("channel-2-gain-2-endTime")
                        .gain("channel-2-gain-2-gain")
                        .build()
                ))
                .build()
        ))
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(audioPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(audioPackage.getUuid().toString()), 1));
    map.put(audioPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(audioPackage.getTemperaturePath().toString()), 2));
    map.put(audioPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getBiologicalPath().toString()), 3));
    map.put(audioPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getOtherPath().toString()), 4));
    map.put(audioPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getDocumentsPath().toString()), 5));
    map.put(audioPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(audioPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getNavigationPath().toString()), 7));
    map.put(audioPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(audioPackage.getSourcePath().toString()), 8));
    map.put(audioPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(audioPackage.getSiteOrCruiseName()), 9));
    map.put(audioPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentId()), 10));
    map.put(audioPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(audioPackage.getDatasetPackager().getName()), 11));
    map.put(audioPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(audioPackage.getProjects().stream().map(Project::getName).collect(Collectors.joining(";"))), 12));
    map.put(audioPackageTranslator.getPublicReleaseDate(), new ValueWithColumnNumber(Optional.of(audioPackage.getPublicReleaseDate().toString()), 13));
    map.put(audioPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(audioPackage.getScientists().stream().map(Person::getName).collect(Collectors.joining(";"))), 14));
    map.put(audioPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(audioPackage.getSponsors().stream().map(Organization::getName).collect(Collectors.joining(";"))), 15));
    map.put(audioPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(audioPackage.getFunders().stream().map(Organization::getName).collect(Collectors.joining(";"))), 16));
    map.put(audioPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(audioPackage.getPlatform().getName()), 17));
    map.put(audioPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(audioPackage.getInstrument().getName()), 18));
    map.put(audioPackageTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getStartTime().toString()), 19));
    map.put(audioPackageTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getEndTime().toString()), 20));
    map.put(audioPackageTranslator.getPreDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(audioPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(audioPackageTranslator.getPostDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(audioPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(audioPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(audioPackage.getCalibrationDescription()), 23));
    map.put(audioPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentTitle()), 24));
    map.put(audioPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentPurpose()), 25));
    map.put(audioPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentDescription()), 26));
    map.put(audioPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(audioPackage.getAlternateSiteName()), 27));
    map.put(audioPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(audioPackage.getAlternateDeploymentName()), 28));

    MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator = (MultipointStationaryMarineLocationTranslator) audioPackageTranslator.getLocationDetailTranslator();
    map.put(multipointStationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getSeaArea().getName()), 29));
    MarineInstrumentLocationTranslator marineInstrumentLocationTranslator = multipointStationaryMarineLocationTranslator.getLocationTranslators().get(0);
    map.put(marineInstrumentLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(0).getLatitude().toString()), 30));
    map.put(marineInstrumentLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(0).getLongitude().toString()), 31));
    map.put(marineInstrumentLocationTranslator.getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(0).getSeaFloorDepth().toString()), 32));
    map.put(marineInstrumentLocationTranslator.getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(0).getInstrumentDepth().toString()), 33));

    marineInstrumentLocationTranslator = multipointStationaryMarineLocationTranslator.getLocationTranslators().get(1);
    map.put(marineInstrumentLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(1).getLatitude().toString()), 34));
    map.put(marineInstrumentLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(1).getLongitude().toString()), 35));
    map.put(marineInstrumentLocationTranslator.getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(1).getSeaFloorDepth().toString()), 36));
    map.put(marineInstrumentLocationTranslator.getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getLocations().get(1).getInstrumentDepth().toString()), 37));
    
    map.put(audioPackageTranslator.getInstrumentId(), new ValueWithColumnNumber(Optional.of(audioPackage.getInstrumentId()), 38));
    map.put(audioPackageTranslator.getHydrophoneSensitivity(), new ValueWithColumnNumber(Optional.of(audioPackage.getHydrophoneSensitivity().toString()), 39));
    map.put(audioPackageTranslator.getFrequencyRange(), new ValueWithColumnNumber(Optional.of(audioPackage.getFrequencyRange().toString()), 40));
    map.put(audioPackageTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getGain().toString()), 41));
    
    QualityControlDetailTranslator qualityControlDetailTranslator = audioPackageTranslator.getQualityControlDetailTranslator();
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAnalyst().getName()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAssessmentDescription()), 45));
    
    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getComments()), 57));
    
    map.put(audioPackageTranslator.getDeploymentTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentTime().toString()), 58));
    map.put(audioPackageTranslator.getRecoveryTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getRecoveryTime().toString()), 59));
    map.put(audioPackageTranslator.getComments(), new ValueWithColumnNumber(Optional.of(audioPackage.getComments()), 60));
    map.put(audioPackageTranslator.getSensors(), new ValueWithColumnNumber(Optional.of(audioPackage.getSensors().stream().map(Sensor::getName).collect(
        Collectors.joining(";"))), 61));
    
    ChannelTranslator channelTranslator = audioPackageTranslator.getChannelTranslators().get(0);
    map.put(channelTranslator.getSensor(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSensor().getName()), 62));
    map.put(channelTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getStartTime().toString()), 63));
    map.put(channelTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getEndTime().toString()), 64));
    
    SampleRateTranslator sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(0);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getStartTime().toString()), 65));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getEndTime().toString()), 66));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString()), 67));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString()), 68));

    sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(1);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getStartTime().toString()), 69));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getEndTime().toString()), 70));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString()), 71));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString()), 72));
    
    DutyCycleTranslator dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(0);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString()), 73));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString()), 74));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getDuration().toString()), 75));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getInterval().toString()), 76));

    dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(1);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString()), 77));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString()), 78));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getDuration().toString()), 79));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getInterval().toString()), 80));
    
    GainTranslator gainTranslator = channelTranslator.getGainTranslators().get(0);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(0).getStartTime().toString()), 81));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(0).getEndTime().toString()), 82));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(0).getGain().toString()), 83));

    gainTranslator = channelTranslator.getGainTranslators().get(1);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(1).getStartTime().toString()), 84));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(1).getEndTime().toString()), 85));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(1).getGain().toString()), 86));

    channelTranslator = audioPackageTranslator.getChannelTranslators().get(1);
    map.put(channelTranslator.getSensor(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSensor().getName()), 87));
    map.put(channelTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getStartTime().toString()), 88));
    map.put(channelTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getEndTime().toString()), 89));

    sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(0);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getStartTime().toString()), 90));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getEndTime().toString()), 91));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString()), 92));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString()), 93));

    sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(1);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getStartTime().toString()), 94));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getEndTime().toString()), 95));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString()), 96));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString()), 97));

    dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(0);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString()), 98));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString()), 99));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getDuration().toString()), 100));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getInterval().toString()), 101));

    dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(1);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString()), 102));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString()), 103));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getDuration().toString()), 104));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getInterval().toString()), 105));

    gainTranslator = channelTranslator.getGainTranslators().get(0);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(0).getStartTime().toString()), 106));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(0).getEndTime().toString()), 107));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(0).getGain().toString()), 108));

    gainTranslator = channelTranslator.getGainTranslators().get(1);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(1).getStartTime().toString()), 109));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(1).getEndTime().toString()), 110));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(1).getGain().toString()), 111));
    
    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(
        Person.builder()
            .name("dataset-packager")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(
        Person.builder()
            .name("scientist-1")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(
        Person.builder()
            .name("scientist-2")
            .build()
    );
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(
        Person.builder()
            .name("quality-analyst")
            .build()
    );
    
    when(projectRepository.getByUniqueField("project-1")).thenReturn(
        Project.builder()
            .name("project-1")
            .build()
    );
    when(projectRepository.getByUniqueField("project-2")).thenReturn(
        Project.builder()
            .name("project-2")
            .build()
    );
    
    when(organizationRepository.getByUniqueField("organization-1")).thenReturn(
        Organization.builder()
            .name("organization-1")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-2")).thenReturn(
        Organization.builder()
            .name("organization-2")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-3")).thenReturn(
        Organization.builder()
            .name("organization-3")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-4")).thenReturn(
        Organization.builder()
            .name("organization-4")
            .build()
    );
    
    when(platformRepository.getByUniqueField("platform")).thenReturn(
        Platform.builder()
            .name("platform")
            .build()
    );
    
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(
        Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build()
    );
    
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(
        DepthSensor.builder()
            .name("sensor-1")
            .position(Position.builder()
                .x(101f)
                .y(201f)
                .z(301f)
                .build())
            .description("description-1")
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(
        AudioSensor.builder()
            .name("sensor-2")
            .position(Position.builder()
                .x(102f)
                .y(202f)
                .z(302f)
                .build())
            .description("description-2")
            .preampId("preampId")
            .hydrophoneId("hydrophoneId")
            .build()
    );
    
    when(seaRepository.getByUniqueField("sea-1")).thenReturn(
        Sea.builder()
            .name("sea-1")
            .build()
    );

    RuntimeException runtimeException = new RuntimeException();
    AudioPackage result = (AudioPackage) converter.convert(audioPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(audioPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertCPODPackage() throws TranslationException, JsonProcessingException, NotFoundException, DatastoreException {
    DepthSensor depthSensor = DepthSensor.builder()
        .name("sensor-1")
        .position(Position.builder()
            .x(101f)
            .y(201f)
            .z(301f)
            .build())
        .description("description-1")
        .build();

    AudioSensor audioSensor = AudioSensor.builder()
        .name("sensor-2")
        .position(Position.builder()
            .x(102f)
            .y(202f)
            .z(302f)
            .build())
        .description("description-2")
        .hydrophoneId("hydrophoneId")
        .preampId("preampId")
        .build();

    CPODPackage cpodPackage = CPODPackage.builder()
        .uuid(UUID.randomUUID())
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-1")
                .build(),
            Project.builder()
                .name("project-2")
                .build()
        ))
        .publicReleaseDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .build(),
            Person.builder()
                .name("scientist-2")
                .build()
        ))
        .sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        ))
        .funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        ))
        .platform(Platform.builder()
            .name("platform")
            .build())
        .instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build())
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .hydrophoneSensitivity(1f)
        .frequencyRange(2f)
        .gain(3f)
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .qualityAnalyst(Person.builder()
            .name("quality-analyst")
            .build())
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .deploymentTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .recoveryTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .comments("comments")
        .sensors(List.of(
            depthSensor, audioSensor
        ))
        .channels(List.of(
            Channel.builder()
                .sensor(audioSensor)
                .startTime(LocalDateTime.parse("2020-01-01T01:45:00"))
                .endTime(LocalDateTime.parse("2020-01-01T03:00:00"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:50:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:55:00"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T02:15:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T02:20:00"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:35:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:40:00"))
                        .duration(505f)
                        .interval(506f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:20:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:38:00"))
                        .duration(605f)
                        .interval(606f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:28:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:29:00"))
                        .gain(707f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:31:00"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:33:00"))
                        .gain(808f)
                        .build()
                ))
                .build(),
            Channel.builder()
                .sensor(depthSensor)
                .startTime(LocalDateTime.parse("2020-01-02T01:45:00"))
                .endTime(LocalDateTime.parse("2020-01-02T03:00:00"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:50:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:55:00"))
                        .sampleRate(702f)
                        .sampleBits(702)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T02:15:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T02:20:00"))
                        .sampleRate(802f)
                        .sampleBits(802)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:35:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:40:00"))
                        .duration(105f)
                        .interval(106f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:20:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:38:00"))
                        .duration(305f)
                        .interval(306f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:28:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:29:00"))
                        .gain(407f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:31:00"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:33:00"))
                        .gain(508f)
                        .build()
                ))
                .build()
        )).locationDetail(MobileMarineLocation.builder()
            .seaArea(Sea.builder()
                .name("sea-1")
                .build())
            .vessel(Ship.builder()
                .name("ship-1")
                .build())
            .locationDerivationDescription("locationDerivationDescription")
            .build())
        .build();

    CPODPackageTranslator cpodPackageTranslator = CPODPackageTranslator.builder()
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate("publicReleaseDate")
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime("startTime")
        .endTime("endTime")
        .preDeploymentCalibrationDate("preDeploymentCalibrationDate")
        .postDeploymentCalibrationDate("postDeploymentCalibrationDate")
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(MobileMarineLocationTranslator.builder()
            .seaArea("seaArea")
            .vessel("ship-1")
            .locationDerivationDescription("locationDerivationDescription")
            .build())
        .instrumentId("instrumentId")
        .hydrophoneSensitivity("hydrophoneSensitivity")
        .frequencyRange("frequencyRange")
        .gain("gain")
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst("qualityAnalyst")
            .qualityAnalysisObjectives("qualityAnalysisObjectives")
            .qualityAnalysisMethod("qualityAnalysisMethod")
            .qualityAssessmentDescription("qualityAssessmentDescription")
            .qualityEntryTranslators(List.of(
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-1")
                    .endTime("quality-entry-endTime-1")
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-2")
                    .endTime("quality-entry-endTime-2")
                    .minFrequency("quality-entry-minFrequency-2")
                    .maxFrequency("quality-entry-maxFrequency-2")
                    .qualityLevel("quality-entry-qualityLevel-2")
                    .comments("quality-entry-comments-2")
                    .build()
            ))
            .build())
        .deploymentTime("deploymentTime")
        .recoveryTime("recoveryTime")
        .comments("comments")
        .sensors("sensors")
        .channelTranslators(List.of(
            ChannelTranslator.builder()
                .sensor("channel-sensor-1")
                .startTime("channel-startTime-1")
                .endTime("channel-endTime-1")
                .sampleRateTranslators(List.of(
                    SampleRateTranslator.builder()
                        .startTime("channel-1-sample-rate-1-startTime")
                        .endTime("channel-1-sample-rate-1-endTime")
                        .sampleRate("channel-1-sample-rate-1-sampleRate")
                        .sampleBits("channel-1-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime("channel-1-sample-rate-2-startTime")
                        .endTime("channel-1-sample-rate-2-endTime")
                        .sampleRate("channel-1-sample-rate-2-sampleRate")
                        .sampleBits("channel-1-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycleTranslators(List.of(
                    DutyCycleTranslator.builder()
                        .startTime("channel-1-duty-cycle-1-startTime")
                        .endTime("channel-1-duty-cycle-1-endTime")
                        .duration("channel-1-duty-cycle-1-duration")
                        .interval("channel-1-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime("channel-1-duty-cycle-2-startTime")
                        .endTime("channel-1-duty-cycle-2-endTime")
                        .duration("channel-1-duty-cycle-2-duration")
                        .interval("channel-1-duty-cycle-2-interval")
                        .build()
                ))
                .gainTranslators(List.of(
                    GainTranslator.builder()
                        .startTime("channel-1-gain-1-startTime")
                        .endTime("channel-1-gain-1-endTime")
                        .gain("channel-1-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime("channel-1-gain-2-startTime")
                        .endTime("channel-1-gain-2-endTime")
                        .gain("channel-1-gain-2-gain")
                        .build()
                ))
                .build(),
            ChannelTranslator.builder()
                .sensor("channel-sensor-2")
                .startTime("channel-startTime-2")
                .endTime("channel-endTime-2")
                .sampleRateTranslators(List.of(
                    SampleRateTranslator.builder()
                        .startTime("channel-2-sample-rate-1-startTime")
                        .endTime("channel-2-sample-rate-1-endTime")
                        .sampleRate("channel-2-sample-rate-1-sampleRate")
                        .sampleBits("channel-2-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime("channel-2-sample-rate-2-startTime")
                        .endTime("channel-2-sample-rate-2-endTime")
                        .sampleRate("channel-2-sample-rate-2-sampleRate")
                        .sampleBits("channel-2-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycleTranslators(List.of(
                    DutyCycleTranslator.builder()
                        .startTime("channel-2-duty-cycle-1-startTime")
                        .endTime("channel-2-duty-cycle-1-endTime")
                        .duration("channel-2-duty-cycle-1-duration")
                        .interval("channel-2-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime("channel-2-duty-cycle-2-startTime")
                        .endTime("channel-2-duty-cycle-2-endTime")
                        .duration("channel-2-duty-cycle-2-duration")
                        .interval("channel-2-duty-cycle-2-interval")
                        .build()
                ))
                .gainTranslators(List.of(
                    GainTranslator.builder()
                        .startTime("channel-2-gain-1-startTime")
                        .endTime("channel-2-gain-1-endTime")
                        .gain("channel-2-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime("channel-2-gain-2-startTime")
                        .endTime("channel-2-gain-2-endTime")
                        .gain("channel-2-gain-2-gain")
                        .build()
                ))
                .build()
        ))
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(cpodPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(cpodPackage.getUuid().toString()), 1));
    map.put(cpodPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getTemperaturePath().toString()), 2));
    map.put(cpodPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getBiologicalPath().toString()), 3));
    map.put(cpodPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getOtherPath().toString()), 4));
    map.put(cpodPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDocumentsPath().toString()), 5));
    map.put(cpodPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(cpodPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getNavigationPath().toString()), 7));
    map.put(cpodPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSourcePath().toString()), 8));
    map.put(cpodPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSiteOrCruiseName()), 9));
    map.put(cpodPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentId()), 10));
    map.put(cpodPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDatasetPackager().getName()), 11));
    map.put(cpodPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(cpodPackage.getProjects().stream().map(Project::getName).collect(Collectors.joining(";"))), 12));
    map.put(cpodPackageTranslator.getPublicReleaseDate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPublicReleaseDate().toString()), 13));
    map.put(cpodPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(cpodPackage.getScientists().stream().map(Person::getName).collect(Collectors.joining(";"))), 14));
    map.put(cpodPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSponsors().stream().map(Organization::getName).collect(Collectors.joining(";"))), 15));
    map.put(cpodPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(cpodPackage.getFunders().stream().map(Organization::getName).collect(Collectors.joining(";"))), 16));
    map.put(cpodPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPlatform().getName()), 17));
    map.put(cpodPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(cpodPackage.getInstrument().getName()), 18));
    map.put(cpodPackageTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getStartTime().toString()), 19));
    map.put(cpodPackageTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getEndTime().toString()), 20));
    map.put(cpodPackageTranslator.getPreDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(cpodPackageTranslator.getPostDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(cpodPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(cpodPackage.getCalibrationDescription()), 23));
    map.put(cpodPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentTitle()), 24));
    map.put(cpodPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentPurpose()), 25));
    map.put(cpodPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentDescription()), 26));
    map.put(cpodPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getAlternateSiteName()), 27));
    map.put(cpodPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getAlternateDeploymentName()), 28));

    MobileMarineLocationTranslator mobileMarineLocationTranslator = (MobileMarineLocationTranslator) cpodPackageTranslator.getLocationDetailTranslator();
    map.put(mobileMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MobileMarineLocation) cpodPackage.getLocationDetail()).getSeaArea().getName()), 29));
    map.put(mobileMarineLocationTranslator.getVessel(), new ValueWithColumnNumber(Optional.of(((MobileMarineLocation) cpodPackage.getLocationDetail()).getVessel().getName()), 29));
    map.put(mobileMarineLocationTranslator.getLocationDerivationDescription(), new ValueWithColumnNumber(Optional.of(((MobileMarineLocation) cpodPackage.getLocationDetail()).getLocationDerivationDescription()), 29));

    map.put(cpodPackageTranslator.getInstrumentId(), new ValueWithColumnNumber(Optional.of(cpodPackage.getInstrumentId()), 38));
    map.put(cpodPackageTranslator.getHydrophoneSensitivity(), new ValueWithColumnNumber(Optional.of(cpodPackage.getHydrophoneSensitivity().toString()), 39));
    map.put(cpodPackageTranslator.getFrequencyRange(), new ValueWithColumnNumber(Optional.of(cpodPackage.getFrequencyRange().toString()), 40));
    map.put(cpodPackageTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getGain().toString()), 41));

    QualityControlDetailTranslator qualityControlDetailTranslator = cpodPackageTranslator.getQualityControlDetailTranslator();
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAnalyst().getName()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAssessmentDescription()), 45));

    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getComments()), 57));

    map.put(cpodPackageTranslator.getDeploymentTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentTime().toString()), 58));
    map.put(cpodPackageTranslator.getRecoveryTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getRecoveryTime().toString()), 59));
    map.put(cpodPackageTranslator.getComments(), new ValueWithColumnNumber(Optional.of(cpodPackage.getComments()), 60));
    map.put(cpodPackageTranslator.getSensors(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSensors().stream().map(Sensor::getName).collect(
        Collectors.joining(";"))), 61));

    ChannelTranslator channelTranslator = cpodPackageTranslator.getChannelTranslators().get(0);
    map.put(channelTranslator.getSensor(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSensor().getName()), 62));
    map.put(channelTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getStartTime().toString()), 63));
    map.put(channelTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getEndTime().toString()), 64));

    SampleRateTranslator sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(0);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getStartTime().toString()), 65));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getEndTime().toString()), 66));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString()), 67));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString()), 68));

    sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(1);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getStartTime().toString()), 69));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getEndTime().toString()), 70));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString()), 71));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString()), 72));

    DutyCycleTranslator dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(0);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString()), 73));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString()), 74));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getDuration().toString()), 75));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getInterval().toString()), 76));

    dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(1);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString()), 77));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString()), 78));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getDuration().toString()), 79));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getInterval().toString()), 80));

    GainTranslator gainTranslator = channelTranslator.getGainTranslators().get(0);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(0).getStartTime().toString()), 81));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(0).getEndTime().toString()), 82));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(0).getGain().toString()), 83));

    gainTranslator = channelTranslator.getGainTranslators().get(1);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(1).getStartTime().toString()), 84));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(1).getEndTime().toString()), 85));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(1).getGain().toString()), 86));

    channelTranslator = cpodPackageTranslator.getChannelTranslators().get(1);
    map.put(channelTranslator.getSensor(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSensor().getName()), 87));
    map.put(channelTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getStartTime().toString()), 88));
    map.put(channelTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getEndTime().toString()), 89));

    sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(0);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getStartTime().toString()), 90));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getEndTime().toString()), 91));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString()), 92));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString()), 93));

    sampleRateTranslator = channelTranslator.getSampleRateTranslators().get(1);
    map.put(sampleRateTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getStartTime().toString()), 94));
    map.put(sampleRateTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getEndTime().toString()), 95));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString()), 96));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString()), 97));

    dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(0);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString()), 98));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString()), 99));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getDuration().toString()), 100));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getInterval().toString()), 101));

    dutyCycleTranslator = channelTranslator.getDutyCycleTranslators().get(1);
    map.put(dutyCycleTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString()), 102));
    map.put(dutyCycleTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString()), 103));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getDuration().toString()), 104));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getInterval().toString()), 105));

    gainTranslator = channelTranslator.getGainTranslators().get(0);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(0).getStartTime().toString()), 106));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(0).getEndTime().toString()), 107));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(0).getGain().toString()), 108));

    gainTranslator = channelTranslator.getGainTranslators().get(1);
    map.put(gainTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(1).getStartTime().toString()), 109));
    map.put(gainTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(1).getEndTime().toString()), 110));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(1).getGain().toString()), 111));

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(
        Person.builder()
            .name("dataset-packager")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(
        Person.builder()
            .name("scientist-1")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(
        Person.builder()
            .name("scientist-2")
            .build()
    );
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(
        Person.builder()
            .name("quality-analyst")
            .build()
    );

    when(projectRepository.getByUniqueField("project-1")).thenReturn(
        Project.builder()
            .name("project-1")
            .build()
    );
    when(projectRepository.getByUniqueField("project-2")).thenReturn(
        Project.builder()
            .name("project-2")
            .build()
    );

    when(organizationRepository.getByUniqueField("organization-1")).thenReturn(
        Organization.builder()
            .name("organization-1")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-2")).thenReturn(
        Organization.builder()
            .name("organization-2")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-3")).thenReturn(
        Organization.builder()
            .name("organization-3")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-4")).thenReturn(
        Organization.builder()
            .name("organization-4")
            .build()
    );

    when(platformRepository.getByUniqueField("platform")).thenReturn(
        Platform.builder()
            .name("platform")
            .build()
    );

    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(
        Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(
        DepthSensor.builder()
            .name("sensor-1")
            .position(Position.builder()
                .x(101f)
                .y(201f)
                .z(301f)
                .build())
            .description("description-1")
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(
        AudioSensor.builder()
            .name("sensor-2")
            .position(Position.builder()
                .x(102f)
                .y(202f)
                .z(302f)
                .build())
            .description("description-2")
            .preampId("preampId")
            .hydrophoneId("hydrophoneId")
            .build()
    );

    when(seaRepository.getByUniqueField("sea-1")).thenReturn(
        Sea.builder()
            .name("sea-1")
            .build()
    );
    
    when(shipRepository.getByUniqueField("ship-1")).thenReturn(
        Ship.builder()
            .name("ship-1")
            .build()
    );

    RuntimeException runtimeException = new RuntimeException();
    CPODPackage result = (CPODPackage) converter.convert(cpodPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(cpodPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertDetectionsPackage() throws TranslationException, JsonProcessingException, NotFoundException, DatastoreException {
    DetectionsPackage detectionsPackage = DetectionsPackage.builder()
        .uuid(UUID.randomUUID())
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-1")
                .build(),
            Project.builder()
                .name("project-2")
                .build()
        ))
        .publicReleaseDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .build(),
            Person.builder()
                .name("scientist-2")
                .build()
        ))
        .sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        ))
        .funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        ))
        .platform(Platform.builder()
            .name("platform")
            .build())
        .instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build())
        .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .qualityAnalyst(Person.builder()
            .name("quality-analyst")
            .build())
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .locationDetail(StationaryTerrestrialLocation.builder()
            .surfaceElevation(10f)
            .instrumentElevation(15f)
            .longitude(101f)
            .latitude(102f)
            .build())
        .soundSource(DetectionType.builder()
            .source("sound-source-1")
            .build())
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .analysisTimeZone(40)
        .analysisEffort(20)
        .sampleRate(1f)
        .minFrequency(2f)
        .maxFrequency(3f)
        .build();

    DetectionsPackageTranslator detectionsPackageTranslator = DetectionsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate("publicReleaseDate")
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime("startTime")
        .endTime("endTime")
        .preDeploymentCalibrationDate("preDeploymentCalibrationDate")
        .postDeploymentCalibrationDate("postDeploymentCalibrationDate")
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(StationaryTerrestrialLocationTranslator.builder()
            .latitude("terrestrial-latitude")
            .longitude("terrestrial-longitude")
            .surfaceElevation("terrestrial-surfaceElevation")
            .instrumentElevation("terrestrial-instrumentElevation")
            .build())
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst("qualityAnalyst")
            .qualityAnalysisObjectives("qualityAnalysisObjectives")
            .qualityAnalysisMethod("qualityAnalysisMethod")
            .qualityAssessmentDescription("qualityAssessmentDescription")
            .qualityEntryTranslators(List.of(
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-1")
                    .endTime("quality-entry-endTime-1")
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-2")
                    .endTime("quality-entry-endTime-2")
                    .minFrequency("quality-entry-minFrequency-2")
                    .maxFrequency("quality-entry-maxFrequency-2")
                    .qualityLevel("quality-entry-qualityLevel-2")
                    .comments("quality-entry-comments-2")
                    .build()
            ))
            .build())
        .soundSource("soundSource")
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .analysisTimeZone("analysisTimeZone")
        .analysisEffort("analysisEffort")
        .sampleRate("sampleRate")
        .minFrequency("minFrequency")
        .maxFrequency("maxFrequency")
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(detectionsPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getUuid().toString()), 1));
    map.put(detectionsPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getTemperaturePath().toString()), 2));
    map.put(detectionsPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getBiologicalPath().toString()), 3));
    map.put(detectionsPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getOtherPath().toString()), 4));
    map.put(detectionsPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDocumentsPath().toString()), 5));
    map.put(detectionsPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(detectionsPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getNavigationPath().toString()), 7));
    map.put(detectionsPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSourcePath().toString()), 8));
    map.put(detectionsPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSiteOrCruiseName()), 9));
    map.put(detectionsPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentId()), 10));
    map.put(detectionsPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDatasetPackager().getName()), 11));
    map.put(detectionsPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getProjects().stream().map(Project::getName).collect(Collectors.joining(";"))), 12));
    map.put(detectionsPackageTranslator.getPublicReleaseDate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPublicReleaseDate().toString()), 13));
    map.put(detectionsPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getScientists().stream().map(Person::getName).collect(Collectors.joining(";"))), 14));
    map.put(detectionsPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSponsors().stream().map(Organization::getName).collect(Collectors.joining(";"))), 15));
    map.put(detectionsPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getFunders().stream().map(Organization::getName).collect(Collectors.joining(";"))), 16));
    map.put(detectionsPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPlatform().getName()), 17));
    map.put(detectionsPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getInstrument().getName()), 18));
    map.put(detectionsPackageTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getStartTime().toString()), 19));
    map.put(detectionsPackageTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getEndTime().toString()), 20));
    map.put(detectionsPackageTranslator.getPreDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(detectionsPackageTranslator.getPostDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(detectionsPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getCalibrationDescription()), 23));
    map.put(detectionsPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentTitle()), 24));
    map.put(detectionsPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentPurpose()), 25));
    map.put(detectionsPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentDescription()), 26));
    map.put(detectionsPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getAlternateSiteName()), 27));
    map.put(detectionsPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getAlternateDeploymentName()), 28));

    StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator = (StationaryTerrestrialLocationTranslator) detectionsPackageTranslator.getLocationDetailTranslator();
    map.put(stationaryTerrestrialLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getLatitude().toString()), 29));
    map.put(stationaryTerrestrialLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getLongitude().toString()), 29));
    map.put(stationaryTerrestrialLocationTranslator.getSurfaceElevation(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getSurfaceElevation().toString()), 29));
    map.put(stationaryTerrestrialLocationTranslator.getInstrumentElevation(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getInstrumentElevation().toString()), 29));

    map.put(detectionsPackageTranslator.getSoundSource(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSoundSource().getSource()), 38));
    map.put(detectionsPackageTranslator.getSoftwareNames(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSoftwareNames()), 39));
    map.put(detectionsPackageTranslator.getSoftwareVersions(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSoftwareVersions()), 40));
    map.put(detectionsPackageTranslator.getSoftwareProtocolCitation(), new ValueWithColumnNumber(Optional.of(
        detectionsPackage.getSoftwareProtocolCitation()), 41));
    map.put(detectionsPackageTranslator.getSoftwareDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackageTranslator.getSoftwareDescription()), 41));
    map.put(detectionsPackageTranslator.getSoftwareProcessingDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackageTranslator.getSoftwareProcessingDescription()), 41));
    map.put(detectionsPackageTranslator.getAnalysisTimeZone(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getAnalysisTimeZone().toString()), 40));
    map.put(detectionsPackageTranslator.getAnalysisEffort(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getAnalysisEffort().toString()), 40));
    map.put(detectionsPackageTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSampleRate().toString()), 40));
    map.put(detectionsPackageTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getMinFrequency().toString()), 40));
    map.put(detectionsPackageTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getMaxFrequency().toString()), 40));

    QualityControlDetailTranslator qualityControlDetailTranslator = detectionsPackageTranslator.getQualityControlDetailTranslator();
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAnalyst().getName()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAssessmentDescription()), 45));

    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getComments()), 57));

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(
        Person.builder()
            .name("dataset-packager")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(
        Person.builder()
            .name("scientist-1")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(
        Person.builder()
            .name("scientist-2")
            .build()
    );
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(
        Person.builder()
            .name("quality-analyst")
            .build()
    );

    when(projectRepository.getByUniqueField("project-1")).thenReturn(
        Project.builder()
            .name("project-1")
            .build()
    );
    when(projectRepository.getByUniqueField("project-2")).thenReturn(
        Project.builder()
            .name("project-2")
            .build()
    );

    when(organizationRepository.getByUniqueField("organization-1")).thenReturn(
        Organization.builder()
            .name("organization-1")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-2")).thenReturn(
        Organization.builder()
            .name("organization-2")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-3")).thenReturn(
        Organization.builder()
            .name("organization-3")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-4")).thenReturn(
        Organization.builder()
            .name("organization-4")
            .build()
    );

    when(platformRepository.getByUniqueField("platform")).thenReturn(
        Platform.builder()
            .name("platform")
            .build()
    );

    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(
        Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build()
    );

    when(detectionTypeRepository.getByUniqueField("sound-source-1")).thenReturn(
        DetectionType.builder()
            .source("sound-source-1")
            .build()
    );

    RuntimeException runtimeException = new RuntimeException();
    DetectionsPackage result = (DetectionsPackage) converter.convert(detectionsPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(detectionsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertSoundClipsPackage() throws TranslationException, JsonProcessingException, NotFoundException, DatastoreException {
    SoundClipsPackage soundClipsPackage = SoundClipsPackage.builder()
        .uuid(UUID.randomUUID())
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-1")
                .build(),
            Project.builder()
                .name("project-2")
                .build()
        ))
        .publicReleaseDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .build(),
            Person.builder()
                .name("scientist-2")
                .build()
        ))
        .sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        ))
        .funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        ))
        .platform(Platform.builder()
            .name("platform")
            .build())
        .instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build())
        .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .locationDetail(MultiPointStationaryMarineLocation.builder()
            .seaArea(Sea.builder()
                .name("sea-1")
                .build())
            .locations(List.of(
                MarineInstrumentLocation.builder()
                    .latitude(1f)
                    .longitude(2f)
                    .seaFloorDepth(100f)
                    .instrumentDepth(99f)
                    .build(),
                MarineInstrumentLocation.builder()
                    .latitude(2f)
                    .longitude(3f)
                    .seaFloorDepth(101f)
                    .instrumentDepth(100f)
                    .build()
            ))
            .build())
        .build();

    SoundClipsPackageTranslator soundClipsPackageTranslator = SoundClipsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate("publicReleaseDate")
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime("startTime")
        .endTime("endTime")
        .preDeploymentCalibrationDate("preDeploymentCalibrationDate")
        .postDeploymentCalibrationDate("postDeploymentCalibrationDate")
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(MultipointStationaryMarineLocationTranslator.builder()
            .seaArea("seaArea")
            .locationTranslators(List.of(
                MarineInstrumentLocationTranslator.builder()
                    .latitude("latitude-1")
                    .longitude("longitude-1")
                    .seaFloorDepth("seaFloorDepth-1")
                    .instrumentDepth("instrumentDepth-1")
                    .build(),
                MarineInstrumentLocationTranslator.builder()
                    .latitude("latitude-2")
                    .longitude("longitude-2")
                    .seaFloorDepth("seaFloorDepth-2")
                    .instrumentDepth("instrumentDepth-2")
                    .build()
            ))
            .build())
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(soundClipsPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getUuid().toString()), 1));
    map.put(soundClipsPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getTemperaturePath().toString()), 2));
    map.put(soundClipsPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getBiologicalPath().toString()), 3));
    map.put(soundClipsPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getOtherPath().toString()), 4));
    map.put(soundClipsPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDocumentsPath().toString()), 5));
    map.put(soundClipsPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(soundClipsPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getNavigationPath().toString()), 7));
    map.put(soundClipsPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSourcePath().toString()), 8));
    map.put(soundClipsPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSiteOrCruiseName()), 9));
    map.put(soundClipsPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentId()), 10));
    map.put(soundClipsPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDatasetPackager().getName()), 11));
    map.put(soundClipsPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getProjects().stream().map(Project::getName).collect(Collectors.joining(";"))), 12));
    map.put(soundClipsPackageTranslator.getPublicReleaseDate(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPublicReleaseDate().toString()), 13));
    map.put(soundClipsPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getScientists().stream().map(Person::getName).collect(Collectors.joining(";"))), 14));
    map.put(soundClipsPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSponsors().stream().map(Organization::getName).collect(Collectors.joining(";"))), 15));
    map.put(soundClipsPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getFunders().stream().map(Organization::getName).collect(Collectors.joining(";"))), 16));
    map.put(soundClipsPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPlatform().getName()), 17));
    map.put(soundClipsPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getInstrument().getName()), 18));
    map.put(soundClipsPackageTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getStartTime().toString()), 19));
    map.put(soundClipsPackageTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getEndTime().toString()), 20));
    map.put(soundClipsPackageTranslator.getPreDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(soundClipsPackageTranslator.getPostDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(soundClipsPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getCalibrationDescription()), 23));
    map.put(soundClipsPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentTitle()), 24));
    map.put(soundClipsPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentPurpose()), 25));
    map.put(soundClipsPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentDescription()), 26));
    map.put(soundClipsPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getAlternateSiteName()), 27));
    map.put(soundClipsPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getAlternateDeploymentName()), 28));

    MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator = (MultipointStationaryMarineLocationTranslator) soundClipsPackageTranslator.getLocationDetailTranslator();
    map.put(multipointStationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getSeaArea().getName()), 29));
    MarineInstrumentLocationTranslator marineInstrumentLocationTranslator = multipointStationaryMarineLocationTranslator.getLocationTranslators().get(0);
    map.put(marineInstrumentLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(0).getLatitude().toString()), 30));
    map.put(marineInstrumentLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(0).getLongitude().toString()), 31));
    map.put(marineInstrumentLocationTranslator.getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(0).getSeaFloorDepth().toString()), 32));
    map.put(marineInstrumentLocationTranslator.getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(0).getInstrumentDepth().toString()), 33));

    marineInstrumentLocationTranslator = multipointStationaryMarineLocationTranslator.getLocationTranslators().get(1);
    map.put(marineInstrumentLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(1).getLatitude().toString()), 34));
    map.put(marineInstrumentLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(1).getLongitude().toString()), 35));
    map.put(marineInstrumentLocationTranslator.getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(1).getSeaFloorDepth().toString()), 36));
    map.put(marineInstrumentLocationTranslator.getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getLocations().get(1).getInstrumentDepth().toString()), 37));

    

    map.put(soundClipsPackageTranslator.getSoftwareNames(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSoftwareNames()), 58));
    map.put(soundClipsPackageTranslator.getSoftwareVersions(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSoftwareVersions()), 59));
    map.put(soundClipsPackageTranslator.getSoftwareProtocolCitation(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSoftwareProtocolCitation()), 60));
    map.put(soundClipsPackageTranslator.getSoftwareDescription(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSoftwareDescription()), 61));
    map.put(soundClipsPackageTranslator.getSoftwareProcessingDescription(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSoftwareProcessingDescription()), 61));

    

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(
        Person.builder()
            .name("dataset-packager")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(
        Person.builder()
            .name("scientist-1")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(
        Person.builder()
            .name("scientist-2")
            .build()
    );
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(
        Person.builder()
            .name("quality-analyst")
            .build()
    );

    when(projectRepository.getByUniqueField("project-1")).thenReturn(
        Project.builder()
            .name("project-1")
            .build()
    );
    when(projectRepository.getByUniqueField("project-2")).thenReturn(
        Project.builder()
            .name("project-2")
            .build()
    );

    when(organizationRepository.getByUniqueField("organization-1")).thenReturn(
        Organization.builder()
            .name("organization-1")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-2")).thenReturn(
        Organization.builder()
            .name("organization-2")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-3")).thenReturn(
        Organization.builder()
            .name("organization-3")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-4")).thenReturn(
        Organization.builder()
            .name("organization-4")
            .build()
    );

    when(platformRepository.getByUniqueField("platform")).thenReturn(
        Platform.builder()
            .name("platform")
            .build()
    );

    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(
        Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(
        DepthSensor.builder()
            .name("sensor-1")
            .position(Position.builder()
                .x(101f)
                .y(201f)
                .z(301f)
                .build())
            .description("description-1")
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(
        AudioSensor.builder()
            .name("sensor-2")
            .position(Position.builder()
                .x(102f)
                .y(202f)
                .z(302f)
                .build())
            .description("description-2")
            .preampId("preampId")
            .hydrophoneId("hydrophoneId")
            .build()
    );

    when(seaRepository.getByUniqueField("sea-1")).thenReturn(
        Sea.builder()
            .name("sea-1")
            .build()
    );

    RuntimeException runtimeException = new RuntimeException();
    SoundClipsPackage result = (SoundClipsPackage) converter.convert(soundClipsPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(soundClipsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertSoundLevelMetricsPackage() throws TranslationException, JsonProcessingException, NotFoundException, DatastoreException {
    SoundLevelMetricsPackage soundLevelMetricsPackage = SoundLevelMetricsPackage.builder()
        .uuid(UUID.randomUUID())
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-1")
                .build(),
            Project.builder()
                .name("project-2")
                .build()
        ))
        .publicReleaseDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .build(),
            Person.builder()
                .name("scientist-2")
                .build()
        ))
        .sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        ))
        .funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        ))
        .platform(Platform.builder()
            .name("platform")
            .build())
        .instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build())
        .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .qualityAnalyst(Person.builder()
            .name("quality-analyst")
            .build())
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:00"))
                .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .locationDetail(MultiPointStationaryMarineLocation.builder()
            .seaArea(Sea.builder()
                .name("sea-1")
                .build())
            .locations(List.of(
                MarineInstrumentLocation.builder()
                    .latitude(1f)
                    .longitude(2f)
                    .seaFloorDepth(100f)
                    .instrumentDepth(99f)
                    .build(),
                MarineInstrumentLocation.builder()
                    .latitude(2f)
                    .longitude(3f)
                    .seaFloorDepth(101f)
                    .instrumentDepth(100f)
                    .build()
            ))
            .build())
        .analysisTimeZone(10)
        .analysisEffort(5)
        .sampleRate(1000f)
        .minFrequency(150f)
        .maxFrequency(200f)
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .build();

    SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator = SoundLevelMetricsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate("publicReleaseDate")
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime("startTime")
        .endTime("endTime")
        .preDeploymentCalibrationDate("preDeploymentCalibrationDate")
        .postDeploymentCalibrationDate("postDeploymentCalibrationDate")
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(MultipointStationaryMarineLocationTranslator.builder()
            .seaArea("seaArea")
            .locationTranslators(List.of(
                MarineInstrumentLocationTranslator.builder()
                    .latitude("latitude-1")
                    .longitude("longitude-1")
                    .seaFloorDepth("seaFloorDepth-1")
                    .instrumentDepth("instrumentDepth-1")
                    .build(),
                MarineInstrumentLocationTranslator.builder()
                    .latitude("latitude-2")
                    .longitude("longitude-2")
                    .seaFloorDepth("seaFloorDepth-2")
                    .instrumentDepth("instrumentDepth-2")
                    .build()
            ))
            .build())
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst("qualityAnalyst")
            .qualityAnalysisObjectives("qualityAnalysisObjectives")
            .qualityAnalysisMethod("qualityAnalysisMethod")
            .qualityAssessmentDescription("qualityAssessmentDescription")
            .qualityEntryTranslators(List.of(
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-1")
                    .endTime("quality-entry-endTime-1")
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime("quality-entry-startTime-2")
                    .endTime("quality-entry-endTime-2")
                    .minFrequency("quality-entry-minFrequency-2")
                    .maxFrequency("quality-entry-maxFrequency-2")
                    .qualityLevel("quality-entry-qualityLevel-2")
                    .comments("quality-entry-comments-2")
                    .build()
            ))
            .build())
        .analysisTimeZone("analysisTimeZone")
        .analysisEffort("analysisEffort")
        .sampleRate("sampleRate")
        .minFrequency("minFrequency")
        .maxFrequency("maxFrequency")
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(soundLevelMetricsPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getUuid().toString()), 1));
    map.put(soundLevelMetricsPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getTemperaturePath().toString()), 2));
    map.put(soundLevelMetricsPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getBiologicalPath().toString()), 3));
    map.put(soundLevelMetricsPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getOtherPath().toString()), 4));
    map.put(soundLevelMetricsPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDocumentsPath().toString()), 5));
    map.put(soundLevelMetricsPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(soundLevelMetricsPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getNavigationPath().toString()), 7));
    map.put(soundLevelMetricsPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSourcePath().toString()), 8));
    map.put(soundLevelMetricsPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSiteOrCruiseName()), 9));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentId()), 10));
    map.put(soundLevelMetricsPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDatasetPackager().getName()), 11));
    map.put(soundLevelMetricsPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getProjects().stream().map(Project::getName).collect(Collectors.joining(";"))), 12));
    map.put(soundLevelMetricsPackageTranslator.getPublicReleaseDate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPublicReleaseDate().toString()), 13));
    map.put(soundLevelMetricsPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getScientists().stream().map(Person::getName).collect(Collectors.joining(";"))), 14));
    map.put(soundLevelMetricsPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSponsors().stream().map(Organization::getName).collect(Collectors.joining(";"))), 15));
    map.put(soundLevelMetricsPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getFunders().stream().map(Organization::getName).collect(Collectors.joining(";"))), 16));
    map.put(soundLevelMetricsPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPlatform().getName()), 17));
    map.put(soundLevelMetricsPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getInstrument().getName()), 18));
    map.put(soundLevelMetricsPackageTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getStartTime().toString()), 19));
    map.put(soundLevelMetricsPackageTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getEndTime().toString()), 20));
    map.put(soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(soundLevelMetricsPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getCalibrationDescription()), 23));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentTitle()), 24));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentPurpose()), 25));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentDescription()), 26));
    map.put(soundLevelMetricsPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAlternateSiteName()), 27));
    map.put(soundLevelMetricsPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAlternateDeploymentName()), 28));

    MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator = (MultipointStationaryMarineLocationTranslator) soundLevelMetricsPackageTranslator.getLocationDetailTranslator();
    map.put(multipointStationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getSeaArea().getName()), 29));
    MarineInstrumentLocationTranslator marineInstrumentLocationTranslator = multipointStationaryMarineLocationTranslator.getLocationTranslators().get(0);
    map.put(marineInstrumentLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(0).getLatitude().toString()), 30));
    map.put(marineInstrumentLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(0).getLongitude().toString()), 31));
    map.put(marineInstrumentLocationTranslator.getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(0).getSeaFloorDepth().toString()), 32));
    map.put(marineInstrumentLocationTranslator.getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(0).getInstrumentDepth().toString()), 33));

    marineInstrumentLocationTranslator = multipointStationaryMarineLocationTranslator.getLocationTranslators().get(1);
    map.put(marineInstrumentLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(1).getLatitude().toString()), 34));
    map.put(marineInstrumentLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(1).getLongitude().toString()), 35));
    map.put(marineInstrumentLocationTranslator.getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(1).getSeaFloorDepth().toString()), 36));
    map.put(marineInstrumentLocationTranslator.getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getLocations().get(1).getInstrumentDepth().toString()), 37));

    map.put(soundLevelMetricsPackageTranslator.getAnalysisTimeZone(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAnalysisTimeZone().toString()), 38));
    map.put(soundLevelMetricsPackageTranslator.getAnalysisEffort(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAnalysisEffort().toString()), 39));
    map.put(soundLevelMetricsPackageTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSampleRate().toString()), 40));
    map.put(soundLevelMetricsPackageTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getMinFrequency().toString()), 41));
    map.put(soundLevelMetricsPackageTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getMaxFrequency().toString()), 41));
    map.put(soundLevelMetricsPackageTranslator.getSoftwareNames(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSoftwareNames()), 41));
    map.put(soundLevelMetricsPackageTranslator.getSoftwareVersions(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSoftwareVersions()), 41));
    map.put(soundLevelMetricsPackageTranslator.getSoftwareProtocolCitation(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSoftwareProtocolCitation()), 41));
    map.put(soundLevelMetricsPackageTranslator.getSoftwareDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSoftwareDescription()), 41));
    map.put(soundLevelMetricsPackageTranslator.getSoftwareProcessingDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSoftwareProcessingDescription()), 41));

    QualityControlDetailTranslator qualityControlDetailTranslator = soundLevelMetricsPackageTranslator.getQualityControlDetailTranslator();
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAnalyst().getName()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAssessmentDescription()), 45));

    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put(dataQualityEntryTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put(dataQualityEntryTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getComments()), 57));

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(
        Person.builder()
            .name("dataset-packager")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(
        Person.builder()
            .name("scientist-1")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(
        Person.builder()
            .name("scientist-2")
            .build()
    );
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(
        Person.builder()
            .name("quality-analyst")
            .build()
    );

    when(projectRepository.getByUniqueField("project-1")).thenReturn(
        Project.builder()
            .name("project-1")
            .build()
    );
    when(projectRepository.getByUniqueField("project-2")).thenReturn(
        Project.builder()
            .name("project-2")
            .build()
    );

    when(organizationRepository.getByUniqueField("organization-1")).thenReturn(
        Organization.builder()
            .name("organization-1")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-2")).thenReturn(
        Organization.builder()
            .name("organization-2")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-3")).thenReturn(
        Organization.builder()
            .name("organization-3")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-4")).thenReturn(
        Organization.builder()
            .name("organization-4")
            .build()
    );

    when(platformRepository.getByUniqueField("platform")).thenReturn(
        Platform.builder()
            .name("platform")
            .build()
    );

    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(
        Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(
        DepthSensor.builder()
            .name("sensor-1")
            .position(Position.builder()
                .x(101f)
                .y(201f)
                .z(301f)
                .build())
            .description("description-1")
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(
        AudioSensor.builder()
            .name("sensor-2")
            .position(Position.builder()
                .x(102f)
                .y(202f)
                .z(302f)
                .build())
            .description("description-2")
            .preampId("preampId")
            .hydrophoneId("hydrophoneId")
            .build()
    );

    when(seaRepository.getByUniqueField("sea-1")).thenReturn(
        Sea.builder()
            .name("sea-1")
            .build()
    );

    RuntimeException runtimeException = new RuntimeException();
    SoundLevelMetricsPackage result = (SoundLevelMetricsPackage) converter.convert(soundLevelMetricsPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(soundLevelMetricsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertSoundPropagationModels() throws TranslationException, JsonProcessingException, NotFoundException, DatastoreException {
    SoundPropagationModelsPackage soundPropagationModelsPackage = SoundPropagationModelsPackage.builder()
        .uuid(UUID.randomUUID())
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager(Person.builder()
            .name("dataset-packager")
            .build())
        .projects(List.of(
            Project.builder()
                .name("project-1")
                .build(),
            Project.builder()
                .name("project-2")
                .build()
        ))
        .publicReleaseDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            Person.builder()
                .name("scientist-1")
                .build(),
            Person.builder()
                .name("scientist-2")
                .build()
        ))
        .sponsors(List.of(
            Organization.builder()
                .name("organization-1")
                .build(),
            Organization.builder()
                .name("organization-2")
                .build()
        ))
        .funders(List.of(
            Organization.builder()
                .name("organization-3")
                .build(),
            Organization.builder()
                .name("organization-4")
                .build()
        ))
        .platform(Platform.builder()
            .name("platform")
            .build())
        .instrument(Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build())
        .startTime(LocalDateTime.parse("2020-01-01T01:00:00"))
        .endTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetail(StationaryMarineLocation.builder()
            .seaArea(Sea.builder()
                .name("sea-1")
                .build())
            .deploymentLocation(MarineInstrumentLocation.builder()
                .latitude(1f)
                .longitude(2f)
                .seaFloorDepth(100f)
                .instrumentDepth(99f)
                .build())
            .recoveryLocation(MarineInstrumentLocation.builder()
                .latitude(2f)
                .longitude(3f)
                .seaFloorDepth(101f)
                .instrumentDepth(100f)
                .build())
            .build())
        .modeledFrequency(900f)
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .audioStartTime(LocalDateTime.parse("2020-01-01T02:00:00"))
        .audioEndTime(LocalDateTime.parse("2020-01-01T02:30:00"))
        .build();

    SoundPropagationModelsPackageTranslator soundPropagationModelsTranslator = SoundPropagationModelsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate("publicReleaseDate")
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime("startTime")
        .endTime("endTime")
        .preDeploymentCalibrationDate("preDeploymentCalibrationDate")
        .postDeploymentCalibrationDate("postDeploymentCalibrationDate")
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(StationaryMarineLocationTranslator.builder()
            .seaArea("seaArea")
            .deploymentLocationTranslator(MarineInstrumentLocationTranslator.builder()
                .latitude("latitude-1")
                .longitude("longitude-1")
                .seaFloorDepth("seaFloorDepth-1")
                .instrumentDepth("instrumentDepth-1")
                .build())
            .recoveryLocationTranslator(MarineInstrumentLocationTranslator.builder()
                .latitude("latitude-2")
                .longitude("longitude-2")
                .seaFloorDepth("seaFloorDepth-2")
                .instrumentDepth("instrumentDepth-2")
                .build())
            .build())
        .modeledFrequency("modeledFrequency")
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .audioStartTime("audioStartTime")
        .audioEndTime("audioEndTime")
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(soundPropagationModelsTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getUuid().toString()), 1));
    map.put(soundPropagationModelsTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getTemperaturePath().toString()), 2));
    map.put(soundPropagationModelsTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getBiologicalPath().toString()), 3));
    map.put(soundPropagationModelsTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getOtherPath().toString()), 4));
    map.put(soundPropagationModelsTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDocumentsPath().toString()), 5));
    map.put(soundPropagationModelsTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(soundPropagationModelsTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getNavigationPath().toString()), 7));
    map.put(soundPropagationModelsTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSourcePath().toString()), 8));
    map.put(soundPropagationModelsTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSiteOrCruiseName()), 9));
    map.put(soundPropagationModelsTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentId()), 10));
    map.put(soundPropagationModelsTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDatasetPackager().getName()), 11));
    map.put(soundPropagationModelsTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getProjects().stream().map(Project::getName).collect(Collectors.joining(";"))), 12));
    map.put(soundPropagationModelsTranslator.getPublicReleaseDate(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPublicReleaseDate().toString()), 13));
    map.put(soundPropagationModelsTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getScientists().stream().map(Person::getName).collect(Collectors.joining(";"))), 14));
    map.put(soundPropagationModelsTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSponsors().stream().map(Organization::getName).collect(Collectors.joining(";"))), 15));
    map.put(soundPropagationModelsTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getFunders().stream().map(Organization::getName).collect(Collectors.joining(";"))), 16));
    map.put(soundPropagationModelsTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPlatform().getName()), 17));
    map.put(soundPropagationModelsTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getInstrument().getName()), 18));
    map.put(soundPropagationModelsTranslator.getStartTime(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getStartTime().toString()), 19));
    map.put(soundPropagationModelsTranslator.getEndTime(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getEndTime().toString()), 20));
    map.put(soundPropagationModelsTranslator.getPreDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(soundPropagationModelsTranslator.getPostDeploymentCalibrationDate(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(soundPropagationModelsTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getCalibrationDescription()), 23));
    map.put(soundPropagationModelsTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentTitle()), 24));
    map.put(soundPropagationModelsTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentPurpose()), 25));
    map.put(soundPropagationModelsTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentDescription()), 26));
    map.put(soundPropagationModelsTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getAlternateSiteName()), 27));
    map.put(soundPropagationModelsTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getAlternateDeploymentName()), 28));

    StationaryMarineLocationTranslator stationaryMarineLocationTranslator = (StationaryMarineLocationTranslator) soundPropagationModelsTranslator.getLocationDetailTranslator();
    map.put(stationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getSeaArea().getName()), 29));
    map.put(stationaryMarineLocationTranslator.getDeploymentLocationTranslator().getLatitude(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getDeploymentLocation().getLatitude().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getDeploymentLocationTranslator().getLongitude(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getDeploymentLocation().getLongitude().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getDeploymentLocationTranslator().getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getDeploymentLocation().getInstrumentDepth().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getDeploymentLocationTranslator().getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getDeploymentLocation().getSeaFloorDepth().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getRecoveryLocationTranslator().getLatitude(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getRecoveryLocation().getLatitude().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getRecoveryLocationTranslator().getLongitude(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getRecoveryLocation().getLongitude().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getRecoveryLocationTranslator().getInstrumentDepth(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getRecoveryLocation().getInstrumentDepth().toString()), 29));
    map.put(stationaryMarineLocationTranslator.getRecoveryLocationTranslator().getSeaFloorDepth(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getRecoveryLocation().getSeaFloorDepth().toString()), 29));

    map.put(soundPropagationModelsTranslator.getModeledFrequency(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getModeledFrequency().toString()), 38));
    map.put(soundPropagationModelsTranslator.getSoftwareNames(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSoftwareNames()), 39));
    map.put(soundPropagationModelsTranslator.getSoftwareVersions(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSoftwareVersions()), 40));
    map.put(soundPropagationModelsTranslator.getSoftwareProtocolCitation(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getSoftwareProtocolCitation()), 41));
    map.put(soundPropagationModelsTranslator.getSoftwareDescription(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getSoftwareDescription()), 41));
    map.put(soundPropagationModelsTranslator.getSoftwareProcessingDescription(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getSoftwareProcessingDescription()), 41));
    map.put(soundPropagationModelsTranslator.getAudioStartTime(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getAudioStartTime().toString()), 41));
    map.put(soundPropagationModelsTranslator.getAudioEndTime(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getAudioEndTime().toString()), 41));

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(
        Person.builder()
            .name("dataset-packager")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(
        Person.builder()
            .name("scientist-1")
            .build()
    );
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(
        Person.builder()
            .name("scientist-2")
            .build()
    );
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(
        Person.builder()
            .name("quality-analyst")
            .build()
    );

    when(projectRepository.getByUniqueField("project-1")).thenReturn(
        Project.builder()
            .name("project-1")
            .build()
    );
    when(projectRepository.getByUniqueField("project-2")).thenReturn(
        Project.builder()
            .name("project-2")
            .build()
    );

    when(organizationRepository.getByUniqueField("organization-1")).thenReturn(
        Organization.builder()
            .name("organization-1")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-2")).thenReturn(
        Organization.builder()
            .name("organization-2")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-3")).thenReturn(
        Organization.builder()
            .name("organization-3")
            .build()
    );
    when(organizationRepository.getByUniqueField("organization-4")).thenReturn(
        Organization.builder()
            .name("organization-4")
            .build()
    );

    when(platformRepository.getByUniqueField("platform")).thenReturn(
        Platform.builder()
            .name("platform")
            .build()
    );

    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(
        Instrument.builder()
            .name("instrument")
            .fileTypes(List.of(
                FileType.builder()
                    .type("file-type-1")
                    .build(),
                FileType.builder()
                    .type("file-type-2")
                    .build()
            ))
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(
        DepthSensor.builder()
            .name("sensor-1")
            .position(Position.builder()
                .x(101f)
                .y(201f)
                .z(301f)
                .build())
            .description("description-1")
            .build()
    );

    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(
        AudioSensor.builder()
            .name("sensor-2")
            .position(Position.builder()
                .x(102f)
                .y(202f)
                .z(302f)
                .build())
            .description("description-2")
            .preampId("preampId")
            .hydrophoneId("hydrophoneId")
            .build()
    );

    when(seaRepository.getByUniqueField("sea-1")).thenReturn(
        Sea.builder()
            .name("sea-1")
            .build()
    );

    RuntimeException runtimeException = new RuntimeException();
    SoundPropagationModelsPackage result = (SoundPropagationModelsPackage) converter.convert(soundPropagationModelsTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(soundPropagationModelsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }
  
  @Test
  void covertInvalidPackageType() {
    PackageTranslator packageTranslator = PackageTranslator.builder().build();
    
    Exception exception = assertThrows(TranslationException.class, () -> converter.convert(packageTranslator, Collections.emptyMap(), 1, new RuntimeException()));
    assertEquals("Translation not supported for PackageTranslator", exception.getMessage());
  }
}