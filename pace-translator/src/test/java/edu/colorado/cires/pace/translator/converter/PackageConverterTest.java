package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MarineInstrumentLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MobileMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MultipointStationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PositionTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundClips.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
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
import org.junit.jupiter.api.Test;

class PackageConverterTest {
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  
  private final Converter<PackageTranslator, Package> converter = new PackageConverter();

  @Test
  void convertAudioPackage() throws TranslationException, JsonProcessingException {
    AudioPackage audioPackage = AudioPackage.builder()
        .uuid(UUID.randomUUID())
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .site("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-1", "project-2"
        ))
        .publishDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        ))
        .sponsors(List.of(
            "organization-1", "organization-2"
        ))
        .funders(List.of(
            "organization-3", "organization-4"
        ))
        .platformName("platform")
        .instrumentType("instrument")
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .hydrophoneSensitivity(1f)
        .frequencyRange(2f)
        .gain(3f)
        .title("deploymentTitle")
        .purpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .deploymentAlias("alternateDeploymentName")
        .qualityAnalyst("quality-analyst")
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .deploymentTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .recoveryTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .comments("comments")
        .sensors(List.of(
           PackageSensor.<String>builder()
               .sensor("depthSensor")
               .position(Position.builder()
                   .x(1f)
                   .y(2f)
                   .z(3f)
                   .build())
               .build() 
        ))
        .channels(List.of(
            Channel.<String>builder()
                .sensor(PackageSensor.<String>builder()
                    .sensor("audioSensor")
                    .position(Position.builder()
                        .x(4f)
                        .y(5f)
                        .z(6f)
                        .build())
                    .build())
                .startTime(LocalDateTime.parse("2020-01-01T01:45:01"))
                .endTime(LocalDateTime.parse("2020-01-01T03:01:01"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:50:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:55:01"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T02:15:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T02:20:01"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:35:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:40:01"))
                        .duration(505f)
                        .interval(506f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:20:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:38:01"))
                        .duration(605f)
                        .interval(606f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:28:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:29:01"))
                        .gain(707f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:31:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:33:01"))
                        .gain(808f)
                        .build()
                ))
                .build(),
            Channel.<String>builder()
                .sensor(PackageSensor.<String>builder()
                    .sensor("depthSensor")
                    .position(Position.builder()
                        .x(4f)
                        .y(5f)
                        .z(6f)
                        .build())
                    .build())
                .startTime(LocalDateTime.parse("2020-01-02T01:45:01"))
                .endTime(LocalDateTime.parse("2020-01-02T03:01:01"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:50:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:55:01"))
                        .sampleRate(702f)
                        .sampleBits(702)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T02:15:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T02:20:01"))
                        .sampleRate(802f)
                        .sampleBits(802)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:35:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:40:01"))
                        .duration(105f)
                        .interval(106f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:20:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:38:01"))
                        .duration(305f)
                        .interval(306f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:28:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:29:01"))
                        .gain(407f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:31:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:33:01"))
                        .gain(508f)
                        .build()
                ))
                .build()
        )).locationDetail(MultiPointStationaryMarineLocation.builder()
            .seaArea("sea-1")
            .locations(List.of(
                MarineInstrumentLocation.builder()
                    .latitude(1d)
                    .longitude(2d)
                    .seaFloorDepth(100f)
                    .instrumentDepth(99f)
                    .build(),
                MarineInstrumentLocation.builder()
                    .latitude(2d)
                    .longitude(3d)
                    .seaFloorDepth(101f)
                    .instrumentDepth(100f)
                    .build()
            ))
            .build())
        .build();

    AudioPackageTranslator audioPackageTranslator = AudioPackageTranslator.builder()
        .packageUUID("packageUUID")
        .dataCollectionName("dataCollectionNAME")
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
        .publicReleaseDate(DateTranslator.builder()
            .date("publicReleaseDate")
            .timeZone("timeZone")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("startTime").build())
        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("endTime").build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("preDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("postDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
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
                    .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("quality-entry-startTime-1").build())
                    .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("quality-entry-endTime-1").build())
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("quality-entry-startTime-2").build())
                    .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("quality-entry-endTime-2").build())
                    .minFrequency("quality-entry-minFrequency-2")
                    .maxFrequency("quality-entry-maxFrequency-2")
                    .qualityLevel("quality-entry-qualityLevel-2")
                    .comments("quality-entry-comments-2")
                    .build()
            ))
            .build())
        .deploymentTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("deploymentTime").build())
        .recoveryTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("recoveryTime").build())
        .comments("comments")
        .sensors(List.of(
            PackageSensorTranslator.builder()
                .name("audioSensor")
                .position(PositionTranslator.builder()
                    .x("audioSensor X")
                    .y("audioSensor Y")
                    .z("audioSensor Z")
                    .build())
                .build()
        ))
        .channelTranslators(List.of(
            ChannelTranslator.builder()
                .sensor(PackageSensorTranslator.builder()
                    .name("channel-sensor-1")
                    .position(PositionTranslator.builder()
                        .x("channel-sensor-1 X")
                        .y("channel-sensor-1 Y")
                        .z("channel-sensor-1 Z")
                        .build())
                    .build())
                .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-startTime-1").build())
                .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-endTime-1").build())
                .sampleRates(List.of(
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-sample-rate-1-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-sample-rate-1-endTime").build())
                        .sampleRate("channel-1-sample-rate-1-sampleRate")
                        .sampleBits("channel-1-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-sample-rate-2-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-sample-rate-2-endTime").build())
                        .sampleRate("channel-1-sample-rate-2-sampleRate")
                        .sampleBits("channel-1-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-duty-cycle-1-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-duty-cycle-1-endTime").build())
                        .duration("channel-1-duty-cycle-1-duration")
                        .interval("channel-1-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-duty-cycle-2-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-duty-cycle-2-endTime").build())
                        .duration("channel-1-duty-cycle-2-duration")
                        .interval("channel-1-duty-cycle-2-interval")
                        .build()
                ))
                .gains(List.of(
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-gain-1-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-gain-1-endTime").build())
                        .gain("channel-1-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-gain-2-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-1-gain-2-endTime").build())
                        .gain("channel-1-gain-2-gain")
                        .build()
                ))
                .build(),
            ChannelTranslator.builder()
                .sensor(PackageSensorTranslator.builder()
                    .name("channel-sensor-2")
                    .position(PositionTranslator.builder()
                        .x("channel-sensor-2 X")
                        .y("channel-sensor-2 Y")
                        .z("channel-sensor-2 Z")
                        .build())
                    .build())
                .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-startTime-2").build())
                .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-endTime-2").build())
                .sampleRates(List.of(
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-sample-rate-1-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-sample-rate-1-endTime").build())
                        .sampleRate("channel-2-sample-rate-1-sampleRate")
                        .sampleBits("channel-2-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-sample-rate-2-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-sample-rate-2-endTime").build())
                        .sampleRate("channel-2-sample-rate-2-sampleRate")
                        .sampleBits("channel-2-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-duty-cycle-1-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-duty-cycle-1-endTime").build())
                        .duration("channel-2-duty-cycle-1-duration")
                        .interval("channel-2-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-duty-cycle-2-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-duty-cycle-2-endTime").build())
                        .duration("channel-2-duty-cycle-2-duration")
                        .interval("channel-2-duty-cycle-2-interval")
                        .build()
                ))
                .gains(List.of(
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-gain-1-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-gain-1-endTime").build())
                        .gain("channel-2-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-gain-2-startTime").build())
                        .endTime(DefaultTimeTranslator.builder().timeZone("timeZone").time("channel-2-gain-2-endTime").build())
                        .gain("channel-2-gain-2-gain")
                        .build()
                ))
                .build()
        ))
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(audioPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(audioPackage.getUuid().toString()), 1));
    map.put(audioPackageTranslator.getDataCollectionName(), new ValueWithColumnNumber(Optional.of(audioPackage.getDataCollectionName()), 1));
    map.put(audioPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(audioPackage.getTemperaturePath().toString()), 2));
    map.put(audioPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getBiologicalPath().toString()), 3));
    map.put(audioPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getOtherPath().toString()), 4));
    map.put(audioPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getDocumentsPath().toString()), 5));
    map.put(audioPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(audioPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(audioPackage.getNavigationPath().toString()), 7));
    map.put(audioPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(audioPackage.getSourcePath().toString()), 8));
    map.put(audioPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(audioPackage.getSite()), 9));
    map.put(audioPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentId()), 10));
    map.put(audioPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(audioPackage.getDatasetPackager()), 11));
    map.put(audioPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(String.join(";", audioPackage.getProjectName())), 12));
    map.put(audioPackageTranslator.getPublicReleaseDate().getDate(), new ValueWithColumnNumber(Optional.of(audioPackage.getPublishDate().toString()), 13));
    map.put(audioPackageTranslator.getPublicReleaseDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 13));
    map.put(audioPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(String.join(";", audioPackage.getScientists())), 14));
    map.put(audioPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(String.join(";", audioPackage.getSponsors())), 15));
    map.put(audioPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(String.join(";", audioPackage.getFunders())), 16));
    map.put(audioPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(audioPackage.getPlatformName()), 17));
    map.put(audioPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(audioPackage.getInstrumentType()), 18));
    map.put((audioPackageTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getStartTime().toString()), 19));
    map.put((audioPackageTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getEndTime().toString()), 20));
    map.put(audioPackageTranslator.getPreDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(audioPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(audioPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 21));
    map.put(audioPackageTranslator.getPostDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(audioPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(audioPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 22));
    map.put(audioPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(audioPackage.getCalibrationDescription()), 23));
    map.put(audioPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(audioPackage.getTitle()), 24));
    map.put(audioPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(audioPackage.getPurpose()), 25));
    map.put(audioPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentDescription()), 26));
    map.put(audioPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(audioPackage.getAlternateSiteName()), 27));
    map.put(audioPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentAlias()), 28));

    MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator = (MultipointStationaryMarineLocationTranslator) audioPackageTranslator.getLocationDetailTranslator();
    map.put(multipointStationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) audioPackage.getLocationDetail()).getSeaArea()), 29));
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
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAnalyst()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityAssessmentDescription()), 45));
    
    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put(dataQualityEntryTranslator.getStartTime().getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put(
        dataQualityEntryTranslator.getEndTime().getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put(dataQualityEntryTranslator.getStartTime().getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put(dataQualityEntryTranslator.getEndTime().getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(audioPackage.getQualityEntries().get(1).getComments()), 57));
    
    map.put(audioPackageTranslator.getDeploymentTime().getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getDeploymentTime().toString()), 58));
    map.put(audioPackageTranslator.getRecoveryTime().getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getRecoveryTime().toString()), 59));
    map.put(audioPackageTranslator.getComments(), new ValueWithColumnNumber(Optional.of(audioPackage.getComments()), 60));

    for (int i = 0; i < audioPackageTranslator.getSensors().size(); i++) {
      map.put(audioPackageTranslator.getSensors().get(i).getName(), new ValueWithColumnNumber(Optional.of(audioPackage.getSensors().get(i).getSensor()), 61));
      map.put(audioPackageTranslator.getSensors().get(i).getPosition().getX(), new ValueWithColumnNumber(Optional.of(audioPackage.getSensors().get(i).getPosition().getX().toString()), 61));
      map.put(audioPackageTranslator.getSensors().get(i).getPosition().getY(), new ValueWithColumnNumber(Optional.of(audioPackage.getSensors().get(i).getPosition().getY().toString()), 61));
      map.put(audioPackageTranslator.getSensors().get(i).getPosition().getZ(), new ValueWithColumnNumber(Optional.of(audioPackage.getSensors().get(i).getPosition().getZ().toString()), 61));
    }
    
    ChannelTranslator channelTranslator = audioPackageTranslator.getChannelTranslators().get(0);
    map.put(channelTranslator.getSensor().getName(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSensor().getSensor()), 62));
    map.put(channelTranslator.getSensor().getPosition().getX(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSensor().getPosition().getX().toString()), 62));
    map.put(channelTranslator.getSensor().getPosition().getY(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSensor().getPosition().getY().toString()), 62));
    map.put(channelTranslator.getSensor().getPosition().getZ(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSensor().getPosition().getZ().toString()), 62));
    map.put((channelTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getStartTime().toString()), 63));
    map.put((channelTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getEndTime().toString()), 64));
    
    SampleRateTranslator sampleRateTranslator = channelTranslator.getSampleRates().get(0);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getStartTime().toString()), 65));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getEndTime().toString()), 66));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString()), 67));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString()), 68));

    sampleRateTranslator = channelTranslator.getSampleRates().get(1);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getStartTime().toString()), 69));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getEndTime().toString()), 70));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString()), 71));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString()), 72));
    
    DutyCycleTranslator dutyCycleTranslator = channelTranslator.getDutyCycles().get(0);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString()), 73));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString()), 74));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getDuration().toString()), 75));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(0).getInterval().toString()), 76));

    dutyCycleTranslator = channelTranslator.getDutyCycles().get(1);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString()), 77));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString()), 78));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getDuration().toString()), 79));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getDutyCycles().get(1).getInterval().toString()), 80));
    
    GainTranslator gainTranslator = channelTranslator.getGains().get(0);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(0).getStartTime().toString()), 81));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(0).getEndTime().toString()), 82));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(0).getGain().toString()), 83));

    gainTranslator = channelTranslator.getGains().get(1);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(1).getStartTime().toString()), 84));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(1).getEndTime().toString()), 85));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(0).getGains().get(1).getGain().toString()), 86));

    channelTranslator = audioPackageTranslator.getChannelTranslators().get(1);
    map.put(channelTranslator.getSensor().getName(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSensor().getSensor()), 87));
    map.put(channelTranslator.getSensor().getPosition().getX(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSensor().getPosition().getX().toString()), 87));
    map.put(channelTranslator.getSensor().getPosition().getY(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSensor().getPosition().getY().toString()), 87));
    map.put(channelTranslator.getSensor().getPosition().getZ(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSensor().getPosition().getZ().toString()), 87));
    map.put((channelTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getStartTime().toString()), 88));
    map.put((channelTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getEndTime().toString()), 89));

    sampleRateTranslator = channelTranslator.getSampleRates().get(0);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getStartTime().toString()), 90));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getEndTime().toString()), 91));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString()), 92));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString()), 93));

    sampleRateTranslator = channelTranslator.getSampleRates().get(1);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getStartTime().toString()), 94));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getEndTime().toString()), 95));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString()), 96));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString()), 97));

    dutyCycleTranslator = channelTranslator.getDutyCycles().get(0);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString()), 98));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString()), 99));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getDuration().toString()), 100));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(0).getInterval().toString()), 101));

    dutyCycleTranslator = channelTranslator.getDutyCycles().get(1);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString()), 102));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString()), 103));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getDuration().toString()), 104));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getDutyCycles().get(1).getInterval().toString()), 105));

    gainTranslator = channelTranslator.getGains().get(0);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(0).getStartTime().toString()), 106));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(0).getEndTime().toString()), 107));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(0).getGain().toString()), 108));

    gainTranslator = channelTranslator.getGains().get(1);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(1).getStartTime().toString()), 109));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(1).getEndTime().toString()), 110));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(audioPackage.getChannels().get(1).getGains().get(1).getGain().toString()), 111));

    RuntimeException runtimeException = new RuntimeException();
    AudioPackage result = (AudioPackage) converter.convert(audioPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(audioPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertCPODPackage() throws TranslationException, JsonProcessingException {
    CPODPackage cpodPackage = CPODPackage.builder()
        .uuid(UUID.randomUUID())
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .site("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-1", "project-2"
        ))
        .publishDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        ))
        .sponsors(List.of(
            "organization-1", "organization-2"
        ))
        .funders(List.of(
            "organization-3", "organization-4"
        ))
        .platformName("platform")
        .instrumentType("instrument")
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .hydrophoneSensitivity(1f)
        .frequencyRange(2f)
        .gain(3f)
        .title("deploymentTitle")
        .purpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .deploymentAlias("alternateDeploymentName")
        .qualityAnalyst("quality-analyst")
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .deploymentTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .recoveryTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .comments("comments")
        .sensors(List.of(
            PackageSensor.<String>builder()
                .sensor("depthSensor")
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build()
        ))
        .channels(List.of(
            Channel.<String>builder()
                .sensor(PackageSensor.<String>builder()
                    .sensor("audioSensor")
                    .position(Position.builder()
                        .x(4f)
                        .y(5f)
                        .z(6f)
                        .build())
                    .build())
                .startTime(LocalDateTime.parse("2020-01-01T01:45:01"))
                .endTime(LocalDateTime.parse("2020-01-01T03:01:01"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:50:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:55:01"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T02:15:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T02:20:01"))
                        .sampleRate(202f)
                        .sampleBits(302)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:35:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:40:01"))
                        .duration(505f)
                        .interval(506f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:20:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:38:01"))
                        .duration(605f)
                        .interval(606f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:28:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:29:01"))
                        .gain(707f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-01T01:31:01"))
                        .endTime(LocalDateTime.parse("2020-01-01T01:33:01"))
                        .gain(808f)
                        .build()
                ))
                .build(),
            Channel.<String>builder()
                .sensor(PackageSensor.<String>builder()
                    .sensor("depthSensor")
                    .position(Position.builder()
                        .x(4f)
                        .y(5f)
                        .z(6f)
                        .build())
                    .build())
                .startTime(LocalDateTime.parse("2020-01-02T01:45:01"))
                .endTime(LocalDateTime.parse("2020-01-02T03:01:01"))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:50:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:55:01"))
                        .sampleRate(702f)
                        .sampleBits(702)
                        .build(),
                    SampleRate.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T02:15:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T02:20:01"))
                        .sampleRate(802f)
                        .sampleBits(802)
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:35:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:40:01"))
                        .duration(105f)
                        .interval(106f)
                        .build(),
                    DutyCycle.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:20:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:38:01"))
                        .duration(305f)
                        .interval(306f)
                        .build()
                ))
                .gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:28:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:29:01"))
                        .gain(407f)
                        .build(),
                    Gain.builder()
                        .startTime(LocalDateTime.parse("2020-01-02T01:31:01"))
                        .endTime(LocalDateTime.parse("2020-01-02T01:33:01"))
                        .gain(508f)
                        .build()
                ))
                .build()
        )).locationDetail(MobileMarineLocation.builder()
            .seaArea("sea-1")
            .vessel("ship-1")
            .locationDerivationDescription("locationDerivationDescription")
            .build())
        .build();

    CPODPackageTranslator cpodPackageTranslator = CPODPackageTranslator.builder()
        .packageUUID("packageUUID")
        .dataCollectionName("dataCollectionNAME")
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
        .publicReleaseDate(DateTranslator.builder()
            .date("publicReleaseDate")
            .timeZone("timeZone")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder()
            .time("startTime")
            .timeZone("timeZone")
            .build())
        .endTime(DefaultTimeTranslator.builder()
        .time("endTime")
            .timeZone("timeZone")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("preDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("postDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
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
                    .startTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-startTime-1")
                        .timeZone("timeZone")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-endTime-1")
                        .timeZone("timeZone")
                        .build())
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-startTime-2")
                        .timeZone("timeZone")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-endTime-2")
                        .timeZone("timeZone")
                        .build())
                    .minFrequency("quality-entry-minFrequency-2")
                    .maxFrequency("quality-entry-maxFrequency-2")
                    .qualityLevel("quality-entry-qualityLevel-2")
                    .comments("quality-entry-comments-2")
                    .build()
            ))
            .build())
        .deploymentTime(DefaultTimeTranslator.builder()
            .time("deploymentTime")
            .timeZone("timeZone")
            .build())
        .recoveryTime(DefaultTimeTranslator.builder()
            .time("recoveryTime")
            .timeZone("timeZone")
            .build())
        .comments("comments")
        .sensors(List.of(
            PackageSensorTranslator.builder()
                .name("audioSensor")
                .position(PositionTranslator.builder()
                    .x("audioSensor X")
                    .y("audioSensor Y")
                    .z("audioSensor Z")
                    .build())
                .build()
        ))
        .channelTranslators(List.of(
            ChannelTranslator.builder()
                .sensor(PackageSensorTranslator.builder()
                    .name("channel-sensor-1")
                    .position(PositionTranslator.builder()
                        .x("channel-sensor-1 X")
                        .y("channel-sensor-1 Y")
                        .z("channel-sensor-1 Z")
                        .build())
                    .build())
                .startTime(DefaultTimeTranslator.builder()
                    .time("channel-startTime-1")
                    .timeZone("timeZone")
                    .build())
                .endTime(DefaultTimeTranslator.builder()
                    .time("channel-endTime-1")
                    .timeZone("timeZone")
                    .build())
                .sampleRates(List.of(
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-1-sample-rate-1-startTime")
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-1-sample-rate-1-endTime")
                            .timeZone("timeZone")
                            .build())
                        .sampleRate("channel-1-sample-rate-1-sampleRate")
                        .sampleBits("channel-1-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-1-sample-rate-2-startTime")
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-1-sample-rate-2-endTime")
                            .timeZone("timeZone")
                            .build())
                        .sampleRate("channel-1-sample-rate-2-sampleRate")
                        .sampleBits("channel-1-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-1-duty-cycle-1-startTime")
                            .timeZone("timeZone")
                        .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-1-duty-cycle-1-endTime")
                            .timeZone("timeZone")
                            .build())
                        .duration("channel-1-duty-cycle-1-duration")
                        .interval("channel-1-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-1-duty-cycle-2-startTime")         
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-1-duty-cycle-2-endTime")
                            .timeZone("timeZone")
                            .build())
                        .duration("channel-1-duty-cycle-2-duration")
                        .interval("channel-1-duty-cycle-2-interval")
                        .build()
                ))
                .gains(List.of(
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-1-gain-1-startTime")
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-1-gain-1-endTime")
                            .timeZone("timeZone")
                        .build())
                        .gain("channel-1-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-1-gain-2-startTime")
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-1-gain-2-endTime")
                            .timeZone("timeZone")
                            .build())
                        .gain("channel-1-gain-2-gain")
                        .build()
                ))
                .build(),
            ChannelTranslator.builder()
                .sensor(PackageSensorTranslator.builder()
                    .name("channel-sensor-2")
                    .position(PositionTranslator.builder()
                        .x("channel-sensor-2 X")
                        .y("channel-sensor-2 Y")
                        .z("channel-sensor-2 Z")
                        .build())
                    .build())
                .startTime(DefaultTimeTranslator.builder()
                    .time("channel-startTime-2")                
                    .timeZone("timeZone")
                    .build())
                .endTime(DefaultTimeTranslator.builder()
                    .time("channel-endTime-2")
                    .timeZone("timeZone")
                    .build())
                .sampleRates(List.of(
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-2-sample-rate-1-startTime")   
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-2-sample-rate-1-endTime")
                            .timeZone("timeZone")
                            .build())
                        .sampleRate("channel-2-sample-rate-1-sampleRate")
                        .sampleBits("channel-2-sample-rate-1-sampleBits")
                        .build(),
                    SampleRateTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-2-sample-rate-2-startTime")
                            .timeZone("timeZone")
                        .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-2-sample-rate-2-endTime")
                            .timeZone("timeZone")
                            .build())
                        .sampleRate("channel-2-sample-rate-2-sampleRate")
                        .sampleBits("channel-2-sample-rate-2-sampleBits")
                        .build()
                ))
                .dutyCycles(List.of(
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-2-duty-cycle-1-startTime")
                            .timeZone("timeZone")
                        .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-2-duty-cycle-1-endTime")
                            .timeZone("timeZone")
                        .build())
                        .duration("channel-2-duty-cycle-1-duration")
                        .interval("channel-2-duty-cycle-1-interval")
                        .build(),
                    DutyCycleTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-2-duty-cycle-2-startTime")
                            .timeZone("timeZone")
                        .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-2-duty-cycle-2-endTime")
                            .timeZone("timeZone")
                            .build())
                        .duration("channel-2-duty-cycle-2-duration")
                        .interval("channel-2-duty-cycle-2-interval")
                        .build()
                ))
                .gains(List.of(
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-2-gain-1-startTime")
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-2-gain-1-endTime") 
                            .timeZone("timeZone")
                            .build())
                        .gain("channel-2-gain-1-gain")
                        .build(),
                    GainTranslator.builder()
                        .startTime(DefaultTimeTranslator.builder()
                            .time("channel-2-gain-2-startTime")
                            .timeZone("timeZone")
                            .build())
                        .endTime(DefaultTimeTranslator.builder()
                            .time("channel-2-gain-2-endTime")
                            .timeZone("timeZone")
                            .build())
                        .gain("channel-2-gain-2-gain")
                        .build()
                ))
                .build()
        ))
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(cpodPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(cpodPackage.getUuid().toString()), 1));
    map.put(cpodPackageTranslator.getDataCollectionName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDataCollectionName()), 1));
    map.put(cpodPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getTemperaturePath().toString()), 2));
    map.put(cpodPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getBiologicalPath().toString()), 3));
    map.put(cpodPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getOtherPath().toString()), 4));
    map.put(cpodPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDocumentsPath().toString()), 5));
    map.put(cpodPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(cpodPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getNavigationPath().toString()), 7));
    map.put(cpodPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSourcePath().toString()), 8));
    map.put(cpodPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSite()), 9));
    map.put(cpodPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentId()), 10));
    map.put(cpodPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDatasetPackager()), 11));
    map.put(cpodPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(String.join(";", cpodPackage.getProjectName())), 12));
    map.put(cpodPackageTranslator.getPublicReleaseDate().getDate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPublishDate().toString()), 13));
    map.put(cpodPackageTranslator.getPublicReleaseDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 13));
    map.put(cpodPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(String.join(";", cpodPackage.getScientists())), 14));
    map.put(cpodPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(String.join(";", cpodPackage.getSponsors())), 15));
    map.put(cpodPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(String.join(";", cpodPackage.getFunders())), 16));
    map.put(cpodPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPlatformName()), 17));
    map.put(cpodPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(cpodPackage.getInstrumentType()), 18));
    map.put((cpodPackageTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getStartTime().toString()), 19));
    map.put((cpodPackageTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getEndTime().toString()), 20));
    map.put(cpodPackageTranslator.getPreDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(cpodPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 21));
    map.put(cpodPackageTranslator.getPostDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(cpodPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 22));
    map.put(cpodPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(cpodPackage.getCalibrationDescription()), 23));
    map.put(cpodPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(cpodPackage.getTitle()), 24));
    map.put(cpodPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(cpodPackage.getPurpose()), 25));
    map.put(cpodPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentDescription()), 26));
    map.put(cpodPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getAlternateSiteName()), 27));
    map.put(cpodPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentAlias()), 28));

    MobileMarineLocationTranslator mobileMarineLocationTranslator = (MobileMarineLocationTranslator) cpodPackageTranslator.getLocationDetailTranslator();
    map.put(mobileMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MobileMarineLocation) cpodPackage.getLocationDetail()).getSeaArea()), 29));
    map.put(mobileMarineLocationTranslator.getVessel(), new ValueWithColumnNumber(Optional.of(((MobileMarineLocation) cpodPackage.getLocationDetail()).getVessel()), 29));
    map.put(mobileMarineLocationTranslator.getLocationDerivationDescription(), new ValueWithColumnNumber(Optional.of(((MobileMarineLocation) cpodPackage.getLocationDetail()).getLocationDerivationDescription()), 29));

    map.put(cpodPackageTranslator.getInstrumentId(), new ValueWithColumnNumber(Optional.of(cpodPackage.getInstrumentId()), 38));
    map.put(cpodPackageTranslator.getHydrophoneSensitivity(), new ValueWithColumnNumber(Optional.of(cpodPackage.getHydrophoneSensitivity().toString()), 39));
    map.put(cpodPackageTranslator.getFrequencyRange(), new ValueWithColumnNumber(Optional.of(cpodPackage.getFrequencyRange().toString()), 40));
    map.put(cpodPackageTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getGain().toString()), 41));

    QualityControlDetailTranslator qualityControlDetailTranslator = cpodPackageTranslator.getQualityControlDetailTranslator();
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAnalyst()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityAssessmentDescription()), 45));

    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put((dataQualityEntryTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put((dataQualityEntryTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put((dataQualityEntryTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put((dataQualityEntryTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(cpodPackage.getQualityEntries().get(1).getComments()), 57));

    map.put((cpodPackageTranslator.getDeploymentTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getDeploymentTime().toString()), 58));
    map.put((cpodPackageTranslator.getRecoveryTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getRecoveryTime().toString()), 59));
    map.put(cpodPackageTranslator.getComments(), new ValueWithColumnNumber(Optional.of(cpodPackage.getComments()), 60));
    for (int i = 0; i < cpodPackageTranslator.getSensors().size(); i++) {
      map.put(cpodPackageTranslator.getSensors().get(i).getName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSensors().get(i).getSensor()), 61));
      map.put(cpodPackageTranslator.getSensors().get(i).getPosition().getX(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSensors().get(i).getPosition().getX().toString()), 61));
      map.put(cpodPackageTranslator.getSensors().get(i).getPosition().getY(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSensors().get(i).getPosition().getY().toString()), 61));
      map.put(cpodPackageTranslator.getSensors().get(i).getPosition().getZ(), new ValueWithColumnNumber(Optional.of(cpodPackage.getSensors().get(i).getPosition().getZ().toString()), 61));
    }

    ChannelTranslator channelTranslator = cpodPackageTranslator.getChannelTranslators().get(0);
    map.put(channelTranslator.getSensor().getName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSensor().getSensor()), 62));
    map.put(channelTranslator.getSensor().getPosition().getX(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSensor().getPosition().getX().toString()), 62));
    map.put(channelTranslator.getSensor().getPosition().getY(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSensor().getPosition().getY().toString()), 62));
    map.put(channelTranslator.getSensor().getPosition().getZ(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSensor().getPosition().getZ().toString()), 62));
    map.put((channelTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getStartTime().toString()), 63));
    map.put((channelTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getEndTime().toString()), 64));

    SampleRateTranslator sampleRateTranslator = channelTranslator.getSampleRates().get(0);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getStartTime().toString()), 65));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getEndTime().toString()), 66));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString()), 67));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString()), 68));

    sampleRateTranslator = channelTranslator.getSampleRates().get(1);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getStartTime().toString()), 69));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getEndTime().toString()), 70));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString()), 71));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString()), 72));

    DutyCycleTranslator dutyCycleTranslator = channelTranslator.getDutyCycles().get(0);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString()), 73));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString()), 74));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getDuration().toString()), 75));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(0).getInterval().toString()), 76));

    dutyCycleTranslator = channelTranslator.getDutyCycles().get(1);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString()), 77));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString()), 78));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getDuration().toString()), 79));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getDutyCycles().get(1).getInterval().toString()), 80));

    GainTranslator gainTranslator = channelTranslator.getGains().get(0);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(0).getStartTime().toString()), 81));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(0).getEndTime().toString()), 82));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(0).getGain().toString()), 83));

    gainTranslator = channelTranslator.getGains().get(1);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(1).getStartTime().toString()), 84));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(1).getEndTime().toString()), 85));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(0).getGains().get(1).getGain().toString()), 86));

    channelTranslator = cpodPackageTranslator.getChannelTranslators().get(1);
    map.put(channelTranslator.getSensor().getName(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSensor().getSensor()), 62));
    map.put(channelTranslator.getSensor().getPosition().getX(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSensor().getPosition().getX().toString()), 62));
    map.put(channelTranslator.getSensor().getPosition().getY(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSensor().getPosition().getY().toString()), 62));
    map.put(channelTranslator.getSensor().getPosition().getZ(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSensor().getPosition().getZ().toString()), 62));
    map.put((channelTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getStartTime().toString()), 88));
    map.put((channelTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getEndTime().toString()), 89));

    sampleRateTranslator = channelTranslator.getSampleRates().get(0);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getStartTime().toString()), 90));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getEndTime().toString()), 91));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString()), 92));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString()), 93));

    sampleRateTranslator = channelTranslator.getSampleRates().get(1);
    map.put((sampleRateTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getStartTime().toString()), 94));
    map.put((sampleRateTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getEndTime().toString()), 95));
    map.put(sampleRateTranslator.getSampleRate(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString()), 96));
    map.put(sampleRateTranslator.getSampleBits(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString()), 97));

    dutyCycleTranslator = channelTranslator.getDutyCycles().get(0);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString()), 98));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString()), 99));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getDuration().toString()), 100));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(0).getInterval().toString()), 101));

    dutyCycleTranslator = channelTranslator.getDutyCycles().get(1);
    map.put((dutyCycleTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString()), 102));
    map.put((dutyCycleTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString()), 103));
    map.put(dutyCycleTranslator.getDuration(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getDuration().toString()), 104));
    map.put(dutyCycleTranslator.getInterval(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getDutyCycles().get(1).getInterval().toString()), 105));

    gainTranslator = channelTranslator.getGains().get(0);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(0).getStartTime().toString()), 106));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(0).getEndTime().toString()), 107));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(0).getGain().toString()), 108));

    gainTranslator = channelTranslator.getGains().get(1);
    map.put((gainTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(1).getStartTime().toString()), 109));
    map.put((gainTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(1).getEndTime().toString()), 110));
    map.put(gainTranslator.getGain(), new ValueWithColumnNumber(Optional.of(cpodPackage.getChannels().get(1).getGains().get(1).getGain().toString()), 111));

    RuntimeException runtimeException = new RuntimeException();
    CPODPackage result = (CPODPackage) converter.convert(cpodPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(cpodPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertDetectionsPackage() throws TranslationException, JsonProcessingException {
    DetectionsPackage detectionsPackage = DetectionsPackage.builder()
        .uuid(UUID.randomUUID())
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .site("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-1", "project-2"
        ))
        .publishDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        ))
        .sponsors(List.of(
            "organization-1", "organization-2"
        ))
        .funders(List.of(
            "organization-3", "organization-4"
        ))
        .platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .title("deploymentTitle")
        .purpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .deploymentAlias("alternateDeploymentName")
        .qualityAnalyst("quality-analyst")
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .locationDetail(StationaryTerrestrialLocation.builder()
            .surfaceElevation(10f)
            .instrumentElevation(15f)
            .longitude(101d)
            .latitude(102d)
            .build())
        .soundSource("sound-source-1")
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
.dataCollectionName("dataCollectionNAME")
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
        .publicReleaseDate(DateTranslator.builder()
            .date("publicReleaseDate")
            .timeZone("timeZone")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder()
            .time("startTime")
            .timeZone("timeZone")
            .build())
        .endTime(DefaultTimeTranslator.builder()
            .time("endTime")
        .timeZone("timeZone")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("preDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("postDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
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
                    .startTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-startTime-1")
                        .timeZone("timeZone")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-endTime-1")
                        .timeZone("timeZone")
                        .build())
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-startTime-2")
                        .timeZone("timeZone")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-endTime-2")
                        .timeZone("timeZone")
                        .build())
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
    map.put(detectionsPackageTranslator.getDataCollectionName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDataCollectionName()), 1));
    map.put(detectionsPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getTemperaturePath().toString()), 2));
    map.put(detectionsPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getBiologicalPath().toString()), 3));
    map.put(detectionsPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getOtherPath().toString()), 4));
    map.put(detectionsPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDocumentsPath().toString()), 5));
    map.put(detectionsPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(detectionsPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getNavigationPath().toString()), 7));
    map.put(detectionsPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSourcePath().toString()), 8));
    map.put(detectionsPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSite()), 9));
    map.put(detectionsPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentId()), 10));
    map.put(detectionsPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDatasetPackager()), 11));
    map.put(detectionsPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(String.join(";", detectionsPackage.getProjectName())), 12));
    map.put(detectionsPackageTranslator.getPublicReleaseDate().getDate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPublishDate().toString()), 13));
    map.put(detectionsPackageTranslator.getPublicReleaseDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 13));
    map.put(detectionsPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(String.join(";", detectionsPackage.getScientists())), 14));
    map.put(detectionsPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(String.join(";", detectionsPackage.getSponsors())), 15));
    map.put(detectionsPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(String.join(";", detectionsPackage.getFunders())), 16));
    map.put(detectionsPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPlatformName()), 17));
    map.put(detectionsPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getInstrumentType()), 18));
    map.put((detectionsPackageTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getStartTime().toString()), 19));
    map.put((detectionsPackageTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getEndTime().toString()), 20));
    map.put(detectionsPackageTranslator.getPreDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(detectionsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 21));
    map.put(detectionsPackageTranslator.getPostDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(detectionsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 22));
    map.put(detectionsPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getCalibrationDescription()), 23));
    map.put(detectionsPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getTitle()), 24));
    map.put(detectionsPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getPurpose()), 25));
    map.put(detectionsPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentDescription()), 26));
    map.put(detectionsPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getAlternateSiteName()), 27));
    map.put(detectionsPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getDeploymentAlias()), 28));

    StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator = (StationaryTerrestrialLocationTranslator) detectionsPackageTranslator.getLocationDetailTranslator();
    map.put(stationaryTerrestrialLocationTranslator.getLatitude(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getLatitude().toString()), 29));
    map.put(stationaryTerrestrialLocationTranslator.getLongitude(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getLongitude().toString()), 29));
    map.put(stationaryTerrestrialLocationTranslator.getSurfaceElevation(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getSurfaceElevation().toString()), 29));
    map.put(stationaryTerrestrialLocationTranslator.getInstrumentElevation(), new ValueWithColumnNumber(Optional.of(((StationaryTerrestrialLocation) detectionsPackage.getLocationDetail()).getInstrumentElevation().toString()), 29));

    map.put(detectionsPackageTranslator.getSoundSource(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getSoundSource()), 38));
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
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAnalyst()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityAssessmentDescription()), 45));

    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put((dataQualityEntryTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put((dataQualityEntryTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put((dataQualityEntryTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put((dataQualityEntryTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(detectionsPackage.getQualityEntries().get(1).getComments()), 57));

    RuntimeException runtimeException = new RuntimeException();
    DetectionsPackage result = (DetectionsPackage) converter.convert(detectionsPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(detectionsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertSoundClipsPackage() throws TranslationException, JsonProcessingException {
    SoundClipsPackage soundClipsPackage = SoundClipsPackage.builder()
        .uuid(UUID.randomUUID())
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .site("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-1", "project-2"
        ))
        .publishDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        ))
        .sponsors(List.of(
            "organization-1", "organization-2"
        ))
        .funders(List.of(
            "organization-3", "organization-4"
        ))
        .platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .title("deploymentTitle")
        .purpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .deploymentAlias("alternateDeploymentName")
        .softwareNames("softwareNames")
        .softwareVersions("softwareVersions")
        .softwareProtocolCitation("softwareProtocolCitation")
        .softwareDescription("softwareDescription")
        .softwareProcessingDescription("softwareProcessingDescription")
        .locationDetail(MultiPointStationaryMarineLocation.builder()
            .seaArea("sea-1")
            .locations(List.of(
                MarineInstrumentLocation.builder()
                    .latitude(1d)
                    .longitude(2d)
                    .seaFloorDepth(100f)
                    .instrumentDepth(99f)
                    .build(),
                MarineInstrumentLocation.builder()
                    .latitude(2d)
                    .longitude(3d)
                    .seaFloorDepth(101f)
                    .instrumentDepth(100f)
                    .build()
            ))
            .build())
        .build();

    SoundClipsPackageTranslator soundClipsPackageTranslator = SoundClipsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .dataCollectionName("dataCollectionNAME")
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
        .publicReleaseDate(DateTranslator.builder()
            .date("publicReleaseDate")
            .timeZone("timeZone")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder()
            .time("startTime")
            .timeZone("timeZone")
            .build())
        .endTime(DefaultTimeTranslator.builder()
            .time("endTime")
            .timeZone("timeZone")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("preDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("postDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
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
    map.put(soundClipsPackageTranslator.getDataCollectionName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDataCollectionName()), 1));
    map.put(soundClipsPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getTemperaturePath().toString()), 2));
    map.put(soundClipsPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getBiologicalPath().toString()), 3));
    map.put(soundClipsPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getOtherPath().toString()), 4));
    map.put(soundClipsPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDocumentsPath().toString()), 5));
    map.put(soundClipsPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(soundClipsPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getNavigationPath().toString()), 7));
    map.put(soundClipsPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSourcePath().toString()), 8));
    map.put(soundClipsPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getSite()), 9));
    map.put(soundClipsPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentId()), 10));
    map.put(soundClipsPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDatasetPackager()), 11));
    map.put(soundClipsPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(String.join(";", soundClipsPackage.getProjectName())), 12));
    map.put(soundClipsPackageTranslator.getPublicReleaseDate().getDate(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPublishDate().toString()), 13));
    map.put(soundClipsPackageTranslator.getPublicReleaseDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 13));
    map.put(soundClipsPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(String.join(";", soundClipsPackage.getScientists())), 14));
    map.put(soundClipsPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(String.join(";", soundClipsPackage.getSponsors())), 15));
    map.put(soundClipsPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(String.join(";", soundClipsPackage.getFunders())), 16));
    map.put(soundClipsPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPlatformName()), 17));
    map.put(soundClipsPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getInstrumentType()), 18));
    map.put((soundClipsPackageTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getStartTime().toString()), 19));
    map.put((soundClipsPackageTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getEndTime().toString()), 20));
    map.put(soundClipsPackageTranslator.getPreDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(soundClipsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 21));
    map.put(soundClipsPackageTranslator.getPostDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(soundClipsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 22));
    map.put(soundClipsPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getCalibrationDescription()), 23));
    map.put(soundClipsPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getTitle()), 24));
    map.put(soundClipsPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getPurpose()), 25));
    map.put(soundClipsPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentDescription()), 26));
    map.put(soundClipsPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getAlternateSiteName()), 27));
    map.put(soundClipsPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(soundClipsPackage.getDeploymentAlias()), 28));

    MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator = (MultipointStationaryMarineLocationTranslator) soundClipsPackageTranslator.getLocationDetailTranslator();
    map.put(multipointStationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundClipsPackage.getLocationDetail()).getSeaArea()), 29));
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

    RuntimeException runtimeException = new RuntimeException();
    SoundClipsPackage result = (SoundClipsPackage) converter.convert(soundClipsPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(soundClipsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertSoundLevelMetricsPackage() throws TranslationException, JsonProcessingException {
    SoundLevelMetricsPackage soundLevelMetricsPackage = SoundLevelMetricsPackage.builder()
        .uuid(UUID.randomUUID())
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .site("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-1", "project-2"
        ))
        .publishDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        ))
        .sponsors(List.of(
            "organization-1", "organization-2"
        ))
        .funders(List.of(
            "organization-3", "organization-4"
        ))
        .platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .title("deploymentTitle")
        .purpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .deploymentAlias("alternateDeploymentName")
        .qualityAnalyst("quality-analyst")
        .qualityAnalysisObjectives("qualityAnalysisObjectives")
        .qualityAnalysisMethod("qualityAnalysisMethod")
        .qualityAssessmentDescription("qualityAssessmentDescription")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
                .endTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .minFrequency(10f)
                .maxFrequency(11f)
                .qualityLevel(QualityLevel.good)
                .comments("comments-1")
                .build(),
            DataQualityEntry.builder()
                .startTime(LocalDateTime.parse("2020-01-01T01:30:01"))
                .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
                .minFrequency(12f)
                .maxFrequency(13f)
                .qualityLevel(QualityLevel.compromised)
                .comments("comments-2")
                .build()
        ))
        .locationDetail(MultiPointStationaryMarineLocation.builder()
            .seaArea("sea-1")
            .locations(List.of(
                MarineInstrumentLocation.builder()
                    .latitude(1d)
                    .longitude(2d)
                    .seaFloorDepth(100f)
                    .instrumentDepth(99f)
                    .build(),
                MarineInstrumentLocation.builder()
                    .latitude(2d)
                    .longitude(3d)
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
        .audioStartTime(LocalDateTime.parse("2020-01-01T05:00:01"))
        .audioEndTime(LocalDateTime.parse("2020-01-01T06:00:01"))
        .build();

    SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator = SoundLevelMetricsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .dataCollectionName("dataCollectionNAME")
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
        .publicReleaseDate(DateTranslator.builder()
            .date("publicReleaseDate")
            .timeZone("timeZone")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder()
            .timeZone("timeZone")
            .time("startTime")
            .build())
        .endTime(DefaultTimeTranslator.builder()
            .time("endTime")
            .timeZone("timeZone")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("preDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("postDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
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
                    .startTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-startTime-1")
                        .timeZone("timeZone")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-endTime-1")
                        .timeZone("timeZone")
                        .build())
                    .minFrequency("quality-entry-minFrequency-1")
                    .maxFrequency("quality-entry-maxFrequency-1")
                    .qualityLevel("quality-entry-qualityLevel-1")
                    .comments("quality-entry-comments-1")
                    .build(),
                DataQualityEntryTranslator.builder()
                    .startTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-startTime-2")
                        .timeZone("timeZone")
                        .build())
                    .endTime(DefaultTimeTranslator.builder()
                        .time("quality-entry-endTime-2")
                        .timeZone("timeZone")
                        .build())
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
        .audioStartTimeTranslator(DefaultTimeTranslator.builder()
            .time("audio-start-time")
            .timeZone("timeZone")
            .build())
        .audioEndTimeTranslator(DefaultTimeTranslator.builder()
            .time("audio-end-time")
            .timeZone("timeZone")
            .build())
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(soundLevelMetricsPackageTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getUuid().toString()), 1));
    map.put(soundLevelMetricsPackageTranslator.getDataCollectionName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDataCollectionName()), 1));
    map.put(soundLevelMetricsPackageTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getTemperaturePath().toString()), 2));
    map.put(soundLevelMetricsPackageTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getBiologicalPath().toString()), 3));
    map.put(soundLevelMetricsPackageTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getOtherPath().toString()), 4));
    map.put(soundLevelMetricsPackageTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDocumentsPath().toString()), 5));
    map.put(soundLevelMetricsPackageTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(soundLevelMetricsPackageTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getNavigationPath().toString()), 7));
    map.put(soundLevelMetricsPackageTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSourcePath().toString()), 8));
    map.put(soundLevelMetricsPackageTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getSite()), 9));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentId()), 10));
    map.put(soundLevelMetricsPackageTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDatasetPackager()), 11));
    map.put(soundLevelMetricsPackageTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(String.join(";", soundLevelMetricsPackage.getProjectName())), 12));
    map.put(soundLevelMetricsPackageTranslator.getPublicReleaseDate().getDate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPublishDate().toString()), 13));
    map.put(soundLevelMetricsPackageTranslator.getPublicReleaseDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 13));
    map.put(soundLevelMetricsPackageTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(String.join(";", soundLevelMetricsPackage.getScientists())), 14));
    map.put(soundLevelMetricsPackageTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(String.join(";", soundLevelMetricsPackage.getSponsors())), 15));
    map.put(soundLevelMetricsPackageTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(String.join(";", soundLevelMetricsPackage.getFunders())), 16));
    map.put(soundLevelMetricsPackageTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPlatformName()), 17));
    map.put(soundLevelMetricsPackageTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getInstrumentType()), 18));
    map.put((soundLevelMetricsPackageTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getStartTime().toString()), 19));
    map.put((soundLevelMetricsPackageTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getEndTime().toString()), 20));
    map.put(soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 21));
    map.put(soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 22));
    map.put(soundLevelMetricsPackageTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getCalibrationDescription()), 23));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getTitle()), 24));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getPurpose()), 25));
    map.put(soundLevelMetricsPackageTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentDescription()), 26));
    map.put(soundLevelMetricsPackageTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAlternateSiteName()), 27));
    map.put(soundLevelMetricsPackageTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getDeploymentAlias()), 28));
    map.put((soundLevelMetricsPackageTranslator.getAudioStartTimeTranslator()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAudioStartTime().toString()), 27));
    map.put(soundLevelMetricsPackageTranslator.getAudioStartTimeTranslator().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 27));
    map.put((soundLevelMetricsPackageTranslator.getAudioEndTimeTranslator()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getAudioEndTime().toString()), 28));
    map.put(soundLevelMetricsPackageTranslator.getAudioEndTimeTranslator().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 28));

    MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator = (MultipointStationaryMarineLocationTranslator) soundLevelMetricsPackageTranslator.getLocationDetailTranslator();
    map.put(multipointStationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((MultiPointStationaryMarineLocation) soundLevelMetricsPackage.getLocationDetail()).getSeaArea()), 29));
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
    map.put(qualityControlDetailTranslator.getQualityAnalyst(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAnalyst()), 42));
    map.put(qualityControlDetailTranslator.getQualityAnalysisObjectives(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAnalysisObjectives()), 43));
    map.put(qualityControlDetailTranslator.getQualityAnalysisMethod(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAnalysisMethod()), 44));
    map.put(qualityControlDetailTranslator.getQualityAssessmentDescription(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityAssessmentDescription()), 45));

    DataQualityEntryTranslator dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(0);
    map.put((dataQualityEntryTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getStartTime().toString()), 46));
    map.put((dataQualityEntryTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getEndTime().toString()), 47));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getMinFrequency().toString()), 48));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getMaxFrequency().toString()), 49));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getQualityLevel().getName()), 50));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(0).getComments()), 51));

    dataQualityEntryTranslator = qualityControlDetailTranslator.getQualityEntryTranslators().get(1);
    map.put((dataQualityEntryTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getStartTime().toString()), 52));
    map.put((dataQualityEntryTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getEndTime().toString()), 53));
    map.put(dataQualityEntryTranslator.getMinFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getMinFrequency().toString()), 54));
    map.put(dataQualityEntryTranslator.getMaxFrequency(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getMaxFrequency().toString()), 55));
    map.put(dataQualityEntryTranslator.getQualityLevel(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getQualityLevel().getName()), 56));
    map.put(dataQualityEntryTranslator.getComments(), new ValueWithColumnNumber(Optional.of(soundLevelMetricsPackage.getQualityEntries().get(1).getComments()), 57));

    RuntimeException runtimeException = new RuntimeException();
    SoundLevelMetricsPackage result = (SoundLevelMetricsPackage) converter.convert(soundLevelMetricsPackageTranslator, map, 1, runtimeException);
    assertEquals(objectMapper.writeValueAsString(soundLevelMetricsPackage), objectMapper.writeValueAsString(result));
    assertEquals(0, runtimeException.getSuppressed().length);
  }

  @Test
  void convertSoundPropagationModels() throws TranslationException, JsonProcessingException {
    SoundPropagationModelsPackage soundPropagationModelsPackage = SoundPropagationModelsPackage.builder()
        .uuid(UUID.randomUUID())
        .dataCollectionName("dataCollectionName")
        .temperaturePath(Paths.get("temperature"))
        .biologicalPath(Paths.get("biological"))
        .otherPath(Paths.get("other"))
        .documentsPath(Paths.get("documents"))
        .calibrationDocumentsPath(Paths.get("calibrationDocuments"))
        .navigationPath(Paths.get("navigation"))
        .sourcePath(Paths.get("source"))
        .site("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projectName(List.of(
            "project-1", "project-2"
        ))
        .publishDate(LocalDate.parse("2020-01-01"))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        ))
        .sponsors(List.of(
            "organization-1", "organization-2"
        ))
        .funders(List.of(
            "organization-3", "organization-4"
        ))
        .platformName("platform")
        .instrumentType("instrument")
        .startTime(LocalDateTime.parse("2020-01-01T01:01:01"))
        .endTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .preDeploymentCalibrationDate(LocalDate.parse("2019-12-12"))
        .postDeploymentCalibrationDate(LocalDate.parse("2020-01-02"))
        .calibrationDescription("calibrationDescription")
        .title("deploymentTitle")
        .purpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .deploymentAlias("alternateDeploymentName")
        .locationDetail(StationaryMarineLocation.builder()
            .seaArea("sea-1")
            .deploymentLocation(MarineInstrumentLocation.builder()
                .latitude(1d)
                .longitude(2d)
                .seaFloorDepth(100f)
                .instrumentDepth(99f)
                .build())
            .recoveryLocation(MarineInstrumentLocation.builder()
                .latitude(2d)
                .longitude(3d)
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
        .audioStartTime(LocalDateTime.parse("2020-01-01T02:01:01"))
        .audioEndTime(LocalDateTime.parse("2020-01-01T02:30:01"))
        .build();

    SoundPropagationModelsPackageTranslator soundPropagationModelsTranslator = SoundPropagationModelsPackageTranslator.builder()
        .packageUUID("packageUUID")
        .dataCollectionName("dataCollectionNAME")
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
        .publicReleaseDate(DateTranslator.builder()
            .date("publicReleaseDate")
            .timeZone("timeZone")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder()
            .time("startTime")
            .timeZone("timeZone")
            .build())
        .endTime(DefaultTimeTranslator.builder()
            .time("endTime")
            .timeZone("timeZone")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("preDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("postDeploymentCalibrationDate")
            .timeZone("timeZone")
            .build())
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
        .audioStartTimeTranslator(DefaultTimeTranslator.builder()
            .time("audioStartTime")
            .timeZone("timeZone")
            .build())
        .audioEndTimeTranslator(DefaultTimeTranslator.builder()
            .time("audioEndTime")
            .timeZone("timeZone")
            .build())
        .build();

    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put(soundPropagationModelsTranslator.getPackageUUID(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getUuid().toString()), 1));
    map.put(soundPropagationModelsTranslator.getDataCollectionName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDataCollectionName()), 1));
    map.put(soundPropagationModelsTranslator.getTemperaturePath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getTemperaturePath().toString()), 2));
    map.put(soundPropagationModelsTranslator.getBiologicalPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getBiologicalPath().toString()), 3));
    map.put(soundPropagationModelsTranslator.getOtherPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getOtherPath().toString()), 4));
    map.put(soundPropagationModelsTranslator.getDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDocumentsPath().toString()), 5));
    map.put(soundPropagationModelsTranslator.getCalibrationDocumentsPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getCalibrationDocumentsPath().toString()), 6));
    map.put(soundPropagationModelsTranslator.getNavigationPath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getNavigationPath().toString()), 7));
    map.put(soundPropagationModelsTranslator.getSourcePath(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSourcePath().toString()), 8));
    map.put(soundPropagationModelsTranslator.getSiteOrCruiseName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getSite()), 9));
    map.put(soundPropagationModelsTranslator.getDeploymentId(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentId()), 10));
    map.put(soundPropagationModelsTranslator.getDatasetPackager(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDatasetPackager()), 11));
    map.put(soundPropagationModelsTranslator.getProjects(), new ValueWithColumnNumber(Optional.of(String.join(";", soundPropagationModelsPackage.getProjectName())), 12));
    map.put(soundPropagationModelsTranslator.getPublicReleaseDate().getDate(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPublishDate().toString()), 13));
    map.put(soundPropagationModelsTranslator.getPublicReleaseDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 13));
    map.put(soundPropagationModelsTranslator.getScientists(), new ValueWithColumnNumber(Optional.of(String.join(";", soundPropagationModelsPackage.getScientists())), 14));
    map.put(soundPropagationModelsTranslator.getSponsors(), new ValueWithColumnNumber(Optional.of(String.join(";", soundPropagationModelsPackage.getSponsors())), 15));
    map.put(soundPropagationModelsTranslator.getFunders(), new ValueWithColumnNumber(Optional.of(String.join(";", soundPropagationModelsPackage.getFunders())), 16));
    map.put(soundPropagationModelsTranslator.getPlatform(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPlatformName()), 17));
    map.put(soundPropagationModelsTranslator.getInstrument(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getInstrumentType()), 18));
    map.put((soundPropagationModelsTranslator.getStartTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getStartTime().toString()), 19));
    map.put((soundPropagationModelsTranslator.getEndTime()).getTime(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getEndTime().toString()), 20));
    map.put(soundPropagationModelsTranslator.getPreDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPreDeploymentCalibrationDate().toString()), 21));
    map.put(soundPropagationModelsTranslator.getPreDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 21));
    map.put(soundPropagationModelsTranslator.getPostDeploymentCalibrationDate().getDate(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPostDeploymentCalibrationDate().toString()), 22));
    map.put(soundPropagationModelsTranslator.getPostDeploymentCalibrationDate().getTimeZone(), new ValueWithColumnNumber(Optional.of("UTC"), 22));
    map.put(soundPropagationModelsTranslator.getCalibrationDescription(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getCalibrationDescription()), 23));
    map.put(soundPropagationModelsTranslator.getDeploymentTitle(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getTitle()), 24));
    map.put(soundPropagationModelsTranslator.getDeploymentPurpose(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getPurpose()), 25));
    map.put(soundPropagationModelsTranslator.getDeploymentDescription(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentDescription()), 26));
    map.put(soundPropagationModelsTranslator.getAlternateSiteName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getAlternateSiteName()), 27));
    map.put(soundPropagationModelsTranslator.getAlternateDeploymentName(), new ValueWithColumnNumber(Optional.of(soundPropagationModelsPackage.getDeploymentAlias()), 28));

    StationaryMarineLocationTranslator stationaryMarineLocationTranslator = (StationaryMarineLocationTranslator) soundPropagationModelsTranslator.getLocationDetailTranslator();
    map.put(stationaryMarineLocationTranslator.getSeaArea(), new ValueWithColumnNumber(Optional.of(((StationaryMarineLocation) soundPropagationModelsPackage.getLocationDetail()).getSeaArea()), 29));
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
    map.put((soundPropagationModelsTranslator.getAudioStartTimeTranslator()).getTime(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getAudioStartTime().toString()), 41));
    map.put((soundPropagationModelsTranslator.getAudioEndTimeTranslator()).getTime(), new ValueWithColumnNumber(Optional.of(
        soundPropagationModelsPackage.getAudioEndTime().toString()), 41));

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