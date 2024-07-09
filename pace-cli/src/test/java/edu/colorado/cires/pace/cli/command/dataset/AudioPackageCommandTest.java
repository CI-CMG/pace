package edu.colorado.cires.pace.cli.command.dataset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.colorado.cires.pace.cli.command.person.PersonCommandTest;
import edu.colorado.cires.pace.cli.command.sea.SeaCommandTest;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.translator.GainTranslator;
import edu.colorado.cires.pace.data.translator.MarineInstrumentLocationTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.data.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.translator.StationaryMarineLocationTranslator;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;

class AudioPackageCommandTest extends PackageCommandTest<AudioPackage, AudioPackageTranslator, StationaryMarineLocationTranslator> {
  
  private Person datasetPackager;
  private Project project;
  private Person scientist;
  private Organization sponsor;
  private Organization funder;
  private Platform platform;
  private Instrument instrument;
  private Sea seaArea;
  private Person qualityAnalyst;
  private Sensor sensor1;
  private Sensor sensor2;
  
  @BeforeEach
  public void beforeEach() throws IOException {
    super.beforeEach();
    
    datasetPackager = saveObject(Person.builder()
        .name("datasetPackager")
        .organization("org")
        .position("position")
        .build(), "person");
    
    project = saveObject(Project.builder()
        .name("project 1")
        .build(), "project");
    
    scientist = saveObject(Person.builder()
        .name("scientist 1")
        .organization("org")
        .position("position")
        .build(), "person");
    
    sponsor = saveObject(Organization.builder()
        .name("sponsor 1")
        .build(), "organization");
    
    funder = saveObject(Organization.builder()
        .name("funder 1")
        .build(), "organization");
    
    platform = saveObject(Platform.builder()
        .name("platform")
        .build(), "platform");
    
    instrument = saveObject(Instrument.builder()
        .name("instrument")
        .fileTypes(List.of(
            saveObject(FileType.builder()
                .type("fileType 1")
                .comment("comment")
                .build(), "file-type")
        ))
        .build(), "instrument");
    
    seaArea = saveObject(Sea.builder()
        .name("seaArea")
        .build(), "sea");
    
    qualityAnalyst = saveObject(Person.builder()
        .name("analyst")
        .organization("org")
        .position("position")
        .build(), "person");
    
    sensor1 = saveObject(DepthSensor.builder()
        .name("depthSensor")
        .description("description")
        .position(Position.builder()
            .x(101f)
            .y(201f)
            .z(301f)
            .build())
        .build(), "sensor");
    
    sensor2 = saveObject(DepthSensor.builder()
        .name("depthSensor 1")
        .description("sensorDescription")
        .position(Position.builder()
            .x(102f)
            .y(202f)
            .z(302f)
            .build())
        .build(), "sensor");
  }

  @Override
  protected void addPackageTypeSpecificFields(List<String> basePackageFields) {
    basePackageFields.addAll(List.of(
       "instrumentId",
        "hydrophoneSensitivity",
        "frequencyRange",
        "gain",
        "deploymentTime",
        "recoveryTime",
        "comments",
        "sensors",
        // data quality
        "qualityAnalyst",
        "qualityAnalysisObjectives",
        "qualityAnalysisMethod",
        "qualityAssessmentDescription",
        // data quality entry 1
        "qualityStartTime 1",
        "qualityEndTime 1",
        "qualityMinFrequency 1",
        "qualityMaxFrequency 1",
        "qualityLevel 1",
        "qualityComments 1",
        // channel 1
        "channel sensor 1",
        "channel startTime 1",
        "channel endTime 1",
        // channel 1 sample rate 1
        "channel 1 sample rate 1 startTime",
        "channel 1 sample rate 1 endTime",
        "channel 1 sample rate 1 sampleRate",
        "channel 1 sample rate 1 sampleBits",
        // channel 1 duty cycle 1
        "channel 1 duty cycle 1 startTime",
        "channel 1 duty cycle 1 endTime",
        "channel 1 duty cycle 1 duration",
        "channel 1 duty cycle 1 interval",
        // channel 1 gain 1
        "channel 1 gain 1 startTime",
        "channel 1 gain 1 endTime",
        "channel 1 gain 1 gain"
    ));
  }

  @Override
  protected void addPackageTypeSpecificFields(List<String> fields, AudioPackage object) {
    DataQualityEntry qualityEntry = object.getQualityEntries().get(0);
    Channel channel = object.getChannels().get(0);
    SampleRate sampleRate = channel.getSampleRates().get(0);
    DutyCycle dutyCycle = channel.getDutyCycles().get(0);
    Gain gain = channel.getGains().get(0);
    
    fields.addAll(List.of(
      object.getInstrumentId(),
      object.getHydrophoneSensitivity().toString(),
      object.getFrequencyRange().toString(),
      object.getGain().toString(),
      object.getDeploymentTime().toString(),
      object.getRecoveryTime().toString(),
      object.getComments(),
      object.getSensors().stream()
          .map(Sensor::getName)
          .collect(Collectors.joining(";")),
      object.getQualityAnalyst().getName(), 
      object.getQualityAnalysisObjectives(),
      object.getQualityAnalysisMethod(),
      object.getQualityAssessmentDescription(),
      qualityEntry.getStartTime().toString(),
      qualityEntry.getEndTime().toString(),
      qualityEntry.getMinFrequency().toString(),
      qualityEntry.getMaxFrequency().toString(),
      qualityEntry.getQualityLevel().getName(),
      qualityEntry.getComments(),
      channel.getSensor().getName(),
      channel.getStartTime().toString(),
      channel.getEndTime().toString(),
      sampleRate.getStartTime().toString(),
      sampleRate.getEndTime().toString(),
      sampleRate.getSampleRate().toString(),
      sampleRate.getSampleBits().toString(),
      dutyCycle.getStartTime().toString(),
      dutyCycle.getEndTime().toString(),
      dutyCycle.getDuration().toString(),
      dutyCycle.getInterval().toString(),
      gain.getStartTime().toString(),
      gain.getEndTime().toString(),
      gain.getGain().toString()
    ));
    
  }

  @Override
  protected void addLocationDetailTypeSpecificFields(List<String> basePackageFields) {
    basePackageFields.addAll(List.of(
        "seaArea",
        "deployment latitude",
        "deployment longitude",
        "deployment seaFloorDepth",
        "deployment instrumentDepth",
        "recovery latitude",
        "recovery longitude",
        "recovery seaFloorDepth",
        "recovery instrumentDepth"
    ));
  }

  @Override
  protected StationaryMarineLocationTranslator createLocationDetailTranslator() {
    return StationaryMarineLocationTranslator.builder()
        .seaArea("seaArea")
        .deploymentLocationTranslator(MarineInstrumentLocationTranslator.builder()
            .latitude("deployment latitude")
            .longitude("deployment longitude")
            .seaFloorDepth("deployment seaFloorDepth")
            .instrumentDepth("deployment instrumentDepth")
            .build())
        .recoveryLocationTranslator(MarineInstrumentLocationTranslator.builder()
            .latitude("recovery latitude")
            .longitude("recovery longitude")
            .seaFloorDepth("recovery seaFloorDepth")
            .instrumentDepth("recovery instrumentDepth")
            .build())
        .build();
  }

  @Override
  protected AudioPackageTranslator createPackageTranslator(PackageTranslator packageTranslator) {
    return AudioPackageTranslator.toBuilder(packageTranslator)
        .instrumentId("instrumentId")
        .hydrophoneSensitivity("hydrophoneSensitivity")
        .frequencyRange("frequencyRange")
        .gain("gain")
        .deploymentTime(DefaultTimeTranslator.builder()
            .time("deploymentTime")
            .timeZone("timeZone")
            .build())
        .recoveryTime(DefaultTimeTranslator.builder()
            .time("recoveryTime")
            .timeZone("timeZone")
            .build())
        .comments("comments")
        .sensors("sensors")
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst("qualityAnalyst")
            .qualityAnalysisObjectives("qualityAnalysisObjectives")
            .qualityAnalysisMethod("qualityAnalysisMethod")
            .qualityAssessmentDescription("qualityAssessmentDescription")
            .qualityEntryTranslators(List.of(DataQualityEntryTranslator.builder()
                    .startTime(DefaultTimeTranslator.builder()
                        .timeZone("timeZone")
                        .time("qualityStartTime 1")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .timeZone("timeZone")
                        .time("qualityEndTime 1")
                        .build())
                    .minFrequency("qualityMinFrequency 1")
                    .maxFrequency("qualityMaxFrequency 1")
                    .qualityLevel("qualityLevel 1")
                    .comments("qualityComments 1")
                .build()))
            .build())
        .channelTranslators(List.of(
            ChannelTranslator.builder()
                .sensor("channel sensor 1")
                .startTime(DefaultTimeTranslator.builder()
                    .timeZone("timeZone")
                    .time("channel startTime 1")
                    .build())
                .endTime(DefaultTimeTranslator.builder()
                    .timeZone("timeZone")
                    .time("channel endTime 1")
                    .build())
                .sampleRates(List.of(SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .timeZone("timeZone")
                            .time("channel 1 sample rate 1 startTime")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .timeZone("timeZone")
                            .time("channel 1 sample rate 1 endTime")
                            .build())
                        .sampleRate("channel 1 sample rate 1 sampleRate")
                        .sampleBits("channel 1 sample rate 1 sampleBits")
                    .build()))
                .dutyCycles(List.of(DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .timeZone("timeZone")
                            .time("channel 1 duty cycle 1 startTime")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .timeZone("timeZone")
                            .time("channel 1 duty cycle 1 endTime")
                            .build())
                        .duration("channel 1 duty cycle 1 duration")
                        .interval("channel 1 duty cycle 1 interval")
                    .build()))
                .gains(List.of(GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .timeZone("timeZone")
                            .time("channel 1 gain 1 startTime")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .timeZone("timeZone")
                            .time("channel 1 gain 1 endTime")
                            .build())
                        .gain("channel 1 gain 1 gain")
                    .build()))
                .build()
        ))
        .build();
  }

  @Override
  protected void addLocationDetailTypeSpecificFields(List<String> fields, LocationDetail locationDetail) {
    assertInstanceOf(StationaryMarineLocation.class, locationDetail);
    
    StationaryMarineLocation stationaryMarineLocation = (StationaryMarineLocation) locationDetail;
    MarineInstrumentLocation deploymentLocation = stationaryMarineLocation.getDeploymentLocation();
    MarineInstrumentLocation recoveryLocation = stationaryMarineLocation.getRecoveryLocation();
    fields.addAll(List.of(
        stationaryMarineLocation.getSeaArea().getName(),
        deploymentLocation.getLatitude().toString(),
        deploymentLocation.getLongitude().toString(),
        deploymentLocation.getSeaFloorDepth().toString(),
        deploymentLocation.getInstrumentDepth().toString(),
        recoveryLocation.getLatitude().toString(),
        recoveryLocation.getLongitude().toString(),
        recoveryLocation.getSeaFloorDepth().toString(),
        recoveryLocation.getInstrumentDepth().toString()
    ));
  }

  @Override
  protected void assertTypeSpecificPackagesEqual(AudioPackage expected, AudioPackage actual) throws JsonProcessingException {
    assertEquals(expected.getInstrumentId(), actual.getInstrumentId());
    assertEquals(expected.getHydrophoneSensitivity(), actual.getHydrophoneSensitivity());
    assertEquals(expected.getGain(), actual.getGain());
    assertEquals(expected.getDeploymentTime(), actual.getDeploymentTime());
    assertEquals(expected.getRecoveryTime(), actual.getRecoveryTime());
    assertEquals(expected.getComments(), actual.getComments());

    for (int i = 0; i < expected.getSensors().size(); i++) {
      assertEquals(
          objectMapper.writeValueAsString(expected.getSensors().get(i)),
          objectMapper.writeValueAsString(actual.getSensors().get(i))
      );
    }

    for (int i = 0; i < expected.getQualityEntries().size(); i++) {
      assertQualityEntriesEqual(
          expected.getQualityEntries().get(i),
          actual.getQualityEntries().get(i)
      );
    }
    PersonCommandTest.assertPeopleEqual(
        expected.getQualityAnalyst(),
        actual.getQualityAnalyst(),
        true
    );
    assertEquals(expected.getQualityAnalysisObjectives(), actual.getQualityAnalysisObjectives());
    assertEquals(expected.getQualityAnalysisMethod(), actual.getQualityAnalysisMethod());
    assertEquals(expected.getQualityAssessmentDescription(), actual.getQualityAssessmentDescription());

    for (int i = 0; i < expected.getChannels().size(); i++) {
      assertChannelsEqual(
          expected.getChannels().get(i),
          actual.getChannels().get(i)
      );
    }
  }
  
  private void assertChannelsEqual(Channel expected, Channel actual) throws JsonProcessingException {
    assertEquals(
        objectMapper.writeValueAsString(expected.getSensor()),
        objectMapper.writeValueAsString(actual.getSensor())
    );
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());

    for (int i = 0; i < expected.getSampleRates().size(); i++) {
      assertSampleRatesEqual(
          expected.getSampleRates().get(i),
          actual.getSampleRates().get(i)
      );
    }

    for (int i = 0; i < expected.getDutyCycles().size(); i++) {
      assertDutyCyclesEqual(
          expected.getDutyCycles().get(i),
          actual.getDutyCycles().get(i)
      );
    }

    for (int i = 0; i < expected.getGains().size(); i++) {
      assertGainsEqual(
          expected.getGains().get(i),
          actual.getGains().get(i)
      );
    }
  }
  
  private void assertGainsEqual(Gain expected, Gain actual) {
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());
    assertEquals(expected.getGain(), actual.getGain());
  }
  
  private void assertDutyCyclesEqual(DutyCycle expected, DutyCycle actual) {
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());
    assertEquals(expected.getDuration(), actual.getDuration());
    assertEquals(expected.getInterval(), actual.getInterval());
  }
  
  private void assertSampleRatesEqual(SampleRate expected, SampleRate actual) {
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());
    assertEquals(expected.getSampleRate(), actual.getSampleRate());
    assertEquals(expected.getSampleBits(), actual.getSampleBits());
  }
  
  private void assertQualityEntriesEqual(DataQualityEntry expected, DataQualityEntry actual) {
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());
    assertEquals(expected.getMinFrequency(), actual.getMinFrequency());
    assertEquals(expected.getMaxFrequency(), actual.getMaxFrequency());
    assertEquals(expected.getQualityLevel(), actual.getQualityLevel());
    assertEquals(expected.getComments(), actual.getComments());
  }
  
  private void assertMarineLocationsEqual(MarineInstrumentLocation expected, MarineInstrumentLocation actual) {
    assertEquals(expected.getLatitude(), actual.getLatitude());
    assertEquals(expected.getLongitude(), actual.getLongitude());
    assertEquals(expected.getInstrumentDepth(), actual.getInstrumentDepth());
    assertEquals(expected.getSeaFloorDepth(), actual.getSeaFloorDepth());
  }

  @Override
  protected void assertLocationDetailsEquals(LocationDetail expected, LocationDetail actual) {
    assertInstanceOf(StationaryMarineLocation.class, expected);
    assertInstanceOf(StationaryMarineLocation.class, actual);
    
    StationaryMarineLocation expectedLocation = (StationaryMarineLocation) expected;
    StationaryMarineLocation actualLocation = (StationaryMarineLocation) actual;

    SeaCommandTest.assertSeasEqual(expectedLocation.getSeaArea(), actualLocation.getSeaArea(), true);
    assertMarineLocationsEqual(expectedLocation.getDeploymentLocation(), actualLocation.getDeploymentLocation());
    assertMarineLocationsEqual(expectedLocation.getRecoveryLocation(), actualLocation.getRecoveryLocation());
  }

  @Override
  public AudioPackage createObject(String uniqueField) {
    return AudioPackage.builder()
        .temperaturePath(Path.of("temperaturePath").toAbsolutePath())
        .biologicalPath(Path.of("biologicalPath").toAbsolutePath())
        .otherPath(Path.of("otherPath").toAbsolutePath())
        .documentsPath(Path.of("documentsPath").toAbsolutePath())
        .calibrationDocumentsPath(Path.of("calibrationDocumentsPath").toAbsolutePath())
        .navigationPath(Path.of("navigationPath").toAbsolutePath())
        .sourcePath(Path.of("sourcePath").toAbsolutePath())
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId(uniqueField)
        .datasetPackager(datasetPackager)
        .projects(List.of(project))
        .publicReleaseDate(LocalDate.of(2020, 1,1))
        .scientists(List.of(scientist))
        .sponsors(List.of(sponsor))
        .funders(List.of(funder))
        .platform(platform)
        .instrument(instrument)
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.of(2019, 12, 31, 12, 0, 1))
        .endTime(LocalDateTime.of(2019, 12, 31, 23, 0, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2019, 12, 31))
        .postDeploymentCalibrationDate(LocalDate.of(2020, 1, 10))
        .calibrationDescription("calibrationDescription")
        .hydrophoneSensitivity(10f)
        .frequencyRange(1f)
        .gain(100f)
        .deploymentTitle("deploymentTitle")
        .deploymentDescription("deploymentDescription")
        .deploymentPurpose("deploymentPurpose")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .qualityAnalyst(qualityAnalyst)
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(DataQualityEntry.builder()
                .startTime(LocalDateTime.of(2019, 12, 31, 12, 1, 1))
                .endTime(LocalDateTime.of(2019, 12, 31, 12, 2, 1))
                .minFrequency(11f)
                .maxFrequency(12f)
                .qualityLevel(QualityLevel.unusable)
                .comments("qualityComments")
            .build()))
        .deploymentTime(LocalDateTime.of(2019, 12, 31, 12, 1, 1))
        .recoveryTime(LocalDateTime.of(2019, 12, 31, 22, 59, 1))
        .comments("deployment comments")
        .sensors(List.of(sensor1))
        .channels(List.of(Channel.builder()
                .startTime(LocalDateTime.of(2019, 12, 31, 12, 5, 1))
                .endTime(LocalDateTime.of(2019, 12, 31, 23, 0, 1))
                .sensor(sensor2)
                .sampleRates(List.of(SampleRate.builder()
                        .startTime(LocalDateTime.of(2019, 12, 31, 13, 15, 1))
                        .endTime(LocalDateTime.of(2019, 12, 31, 14, 0, 1))
                        .sampleRate(1000f)
                        .sampleBits(105)
                    .build()))
                .dutyCycles(List.of(DutyCycle.builder()
                        .startTime(LocalDateTime.of(2019, 12, 31, 14, 1, 1))
                        .endTime(LocalDateTime.of(2019, 12, 31, 15, 1, 1))
                        .duration(90f)
                        .interval(800f)
                    .build()))
                .gains(List.of(Gain.builder()
                        .startTime(LocalDateTime.of(2019, 12, 31, 16, 0, 1))
                        .endTime(LocalDateTime.of(2019, 12, 31, 17, 0, 1))
                        .gain(400f)
                    .build()))
            .build()))
        .locationDetail(StationaryMarineLocation.builder()
            .seaArea(seaArea)
            .recoveryLocation(MarineInstrumentLocation.builder()
                .seaFloorDepth(1f)
                .instrumentDepth(2f)
                .latitude(60d)
                .longitude(50d)
                .build())
            .deploymentLocation(MarineInstrumentLocation.builder()
                .seaFloorDepth(2f)
                .instrumentDepth(3f)
                .latitude(61d)
                .longitude(51d)
                .build())
            .build())
        .build();
  }

  @Override
  protected AudioPackage updateObject(AudioPackage original, String uniqueField) {
    return original.toBuilder()
        .deploymentId(uniqueField)
        .build();
  }
}
