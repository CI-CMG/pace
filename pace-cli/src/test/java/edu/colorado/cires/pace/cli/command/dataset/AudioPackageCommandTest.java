package edu.colorado.cires.pace.cli.command.dataset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.colorado.cires.pace.cli.command.sea.SeaCommandTest;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MarineInstrumentLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PositionTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryMarineLocationTranslator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
                .build(), "file-type").getType()
        ))
        .build(), "instrument");
    
    seaArea = SeaCommandTest.writeObject(testPath, Sea.builder()
        .name("seaArea")
        .build(), objectMapper);
    
    qualityAnalyst = saveObject(Person.builder()
        .name("analyst")
        .organization("org")
        .position("position")
        .build(), "person");
    
    sensor1 = saveObject(DepthSensor.builder()
        .name("depthSensor")
        .description("description")
        .build(), "sensor");
    
    sensor2 = saveObject(DepthSensor.builder()
        .name("depthSensor 1")
        .description("sensorDescription")
        .build(), "sensor");
  }

  @Override
  protected void addPackageTypeSpecificFields(List<String> basePackageFields) {
    basePackageFields.addAll(List.of(
       "instrumentId",
        "deploymentTime",
        "recoveryTime",
        "comments",
        "sensors",
        "sensors x",
        "sensors y",
        "sensors z",
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
    Channel<String> channel = object.getChannels().get(0);
    SampleRate sampleRate = channel.getSampleRates().get(0);
    DutyCycle dutyCycle = channel.getDutyCycles().get(0);
    Gain gain = channel.getGains().get(0);
    
    fields.addAll(List.of(
      object.getInstrumentId(),
      object.getDeploymentTime().toString(),
      object.getRecoveryTime().toString(),
      object.getComments(),
      object.getSensors().get(0).getSensor(),
      object.getSensors().get(0).getPosition().getX().toString(),
      object.getSensors().get(0).getPosition().getY().toString(),
      object.getSensors().get(0).getPosition().getZ().toString(),
      object.getQualityAnalyst(), 
      object.getQualityAnalysisObjectives(),
      object.getQualityAnalysisMethod(),
      object.getQualityAssessmentDescription(),
      qualityEntry.getStartTime().toString(),
      qualityEntry.getEndTime().toString(),
      qualityEntry.getMinFrequency().toString(),
      qualityEntry.getMaxFrequency().toString(),
      qualityEntry.getQualityLevel().getName(),
      qualityEntry.getComments(),
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
        .deploymentTime(DefaultTimeTranslator.builder()
            .time("deploymentTime")
            .timeZone("timeZone")
            .build())
        .recoveryTime(DefaultTimeTranslator.builder()
            .time("recoveryTime")
            .timeZone("timeZone")
            .build())
        .comments("comments")
        .sensors(Collections.singletonList(PackageSensorTranslator.builder()
            .name("sensors")
            .position(PositionTranslator.builder()
                .x("sensors x")
                .y("sensors y")
                .z("sensors z")
                .build())
            .build()))
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
        stationaryMarineLocation.getSeaArea(),
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
    assertEquals(
        expected.getQualityAnalyst(),
        actual.getQualityAnalyst()
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
  
  private void assertChannelsEqual(Channel<String> expected, Channel<String> actual) throws JsonProcessingException {
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

    assertEquals(expectedLocation.getSeaArea(), actualLocation.getSeaArea());
    assertMarineLocationsEqual(expectedLocation.getDeploymentLocation(), actualLocation.getDeploymentLocation());
    assertMarineLocationsEqual(expectedLocation.getRecoveryLocation(), actualLocation.getRecoveryLocation());
  }

  @Override
  public AudioPackage createObject(String uniqueField, boolean withUUID) {
    return AudioPackage.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .temperaturePath(testPath.resolve("temperaturePath").toAbsolutePath())
        .biologicalPath(testPath.resolve("biologicalPath").toAbsolutePath())
        .otherPath(testPath.resolve("otherPath").toAbsolutePath())
        .documentsPath(testPath.resolve("documentsPath").toAbsolutePath())
        .calibrationDocumentsPath(testPath.resolve("calibrationDocumentsPath").toAbsolutePath())
        .sourcePath(testPath.resolve("sourcePath").toAbsolutePath())
        .siteOrCruiseName(uniqueField)
        .deploymentId(uniqueField)
        .dataCollectionName(uniqueField)
        .datasetPackager(datasetPackager.getName())
        .projects(List.of(project.getName()))
        .publicReleaseDate(LocalDate.of(2020, 1,1))
        .scientists(List.of(scientist.getName()))
        .sponsors(List.of(sponsor.getName()))
        .funders(List.of(funder.getName()))
        .platform(platform.getName())
        .instrument(instrument.getName())
        .instrumentId("instrumentId")
        .preDeploymentCalibrationDate(LocalDate.of(2019, 12, 31))
        .postDeploymentCalibrationDate(LocalDate.of(2020, 1, 10))
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentDescription("deploymentDescription")
        .deploymentPurpose("deploymentPurpose")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .qualityAnalyst(qualityAnalyst.getName())
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
        .sensors(List.of(PackageSensor.<String>builder()
                .sensor(sensor1.getName())
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
            .build()))
        .channels(List.of(Channel.<String>builder()
                .startTime(LocalDateTime.of(2019, 12, 31, 12, 5, 1))
                .endTime(LocalDateTime.of(2019, 12, 31, 23, 0, 1))
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
            .seaArea(seaArea.getName())
            .recoveryLocation(MarineInstrumentLocation.builder()
                .seaFloorDepth(-2f)
                .instrumentDepth(-1f)
                .latitude(60d)
                .longitude(50d)
                .build())
            .deploymentLocation(MarineInstrumentLocation.builder()
                .seaFloorDepth(-3f)
                .instrumentDepth(-2f)
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
        .siteOrCruiseName(uniqueField)
        .dataCollectionName(uniqueField)
        .build();
  }
}
