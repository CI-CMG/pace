package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.floatFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.integerFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.latitudeFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.localDateFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.localDateTimeFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.longitudeFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.pathFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.propertyFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromProperty;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringListFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.pathListFromMap;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.ProcessingLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
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
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.LocationDetailTranslator;
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
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class PackageConverter extends Converter<PackageTranslator, Package> {

  @Override
  public Package convert(PackageTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    if (translator instanceof AudioPackageTranslator audioPackageTranslator) {
      return audioPackageFromMap(
          audioPackageTranslator, properties, row, runtimeException
      );
    } else if (translator instanceof CPODPackageTranslator cpodPackageTranslator) {
      return cpodPackageFromMap(
          cpodPackageTranslator, properties, row, runtimeException
      );
    } else if (translator instanceof DetectionsPackageTranslator detectionsPackageTranslator) {
      return detectionsPackageFromMap(detectionsPackageTranslator, properties, row, runtimeException
      );
    } else if (translator instanceof SoundClipsPackageTranslator soundClipsPackageTranslator) {
      return soundClipsPackageFromMap(soundClipsPackageTranslator, properties, row, runtimeException
      );
    } else if (translator instanceof SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator) {
      return soundLevelMetricsPackageFromMap(soundLevelMetricsPackageTranslator, properties, row, runtimeException
      );
    } else if (translator instanceof SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator) {
      return soundPropagationModelsPackageFromMap(soundPropagationModelsPackageTranslator, properties, row, runtimeException
      );
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", translator.getClass().getSimpleName()
      ));
    }
  }

  private static CPODPackage cpodPackageFromMap(CPODPackageTranslator cpodPackageTranslator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException)
      throws TranslationException {
    QualityControlDetailTranslator qualityControlDetailTranslator = cpodPackageTranslator.getQualityControlDetailTranslator();
    return CPODPackage.builder()
        .uuid(uuidFromMap(properties, "UUID", cpodPackageTranslator.getPackageUUID(), row, runtimeException))
        .dataCollectionName(stringFromMap(properties, cpodPackageTranslator.getDataCollectionName()))
        .processingLevel(processingLevelFromMap(properties, cpodPackageTranslator.getProcessingLevel(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, "Temperature Path", cpodPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, "Biological Path", cpodPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, "Other Path", cpodPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, "Documents Path", cpodPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, "Calibration Documents Path", cpodPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, "Source Path", cpodPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, cpodPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, cpodPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, cpodPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, cpodPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, "Public Release Date", cpodPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, cpodPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, cpodPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, cpodPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, cpodPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, cpodPackageTranslator.getInstrument()))
        .instrumentId(stringFromMap(properties, cpodPackageTranslator.getInstrumentId()))
        .startTime(localDateTimeFromMap(properties, "Start Time", cpodPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", cpodPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, "Pre-Deployment Calibration Date", cpodPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, "Post-Deployment Calibration Date", cpodPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, cpodPackageTranslator.getCalibrationDescription()))
        .hydrophoneSensitivity(floatFromMap(properties, "Hydrophone Sensitivity", cpodPackageTranslator.getHydrophoneSensitivity(), row, runtimeException))
        .frequencyRange(stringFromMap(properties, cpodPackageTranslator.getFrequencyRange()))
        .gain(floatFromMap(properties, "Gain", cpodPackageTranslator.getGain(), row, runtimeException))
        .deploymentTitle(stringFromMap(properties, cpodPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, cpodPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, cpodPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, cpodPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, cpodPackageTranslator.getAlternateDeploymentName()))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator == null ? Collections.emptyList() : qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .deploymentTime(localDateTimeFromMap(properties, "Deployment Time", cpodPackageTranslator.getDeploymentTime(), row, runtimeException))
        .recoveryTime(localDateTimeFromMap(properties, "Recovery Time", cpodPackageTranslator.getRecoveryTime(), row, runtimeException))
        .audioStartTime(localDateTimeFromMap(properties, "Audio Start Time", cpodPackageTranslator.getAudioStartTime(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, "Audio End Time", cpodPackageTranslator.getAudioEndTime(), row, runtimeException))
        .comments(stringFromMap(properties, cpodPackageTranslator.getComments()))
        .sensors(packageSensorsFromMap(properties, cpodPackageTranslator.getSensors(), row, runtimeException))
        .channels(channelsFromMap(cpodPackageTranslator.getChannelTranslators(), properties, row, runtimeException))
        .locationDetail(locationDetailFromMap(cpodPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .build();
  }

  private static List<PackageSensor<String>> packageSensorsFromMap(Map<String, ValueWithColumnNumber> properties, List<PackageSensorTranslator> sensors,
      int row, RuntimeException runtimeException) {
    return sensors.stream()
        .map(translator -> packageSensorFromMap(properties, translator, row, runtimeException))
        .toList();
  }
  
  private static PackageSensor<String> packageSensorFromMap(Map<String, ValueWithColumnNumber> properties, PackageSensorTranslator translator, int row, RuntimeException runtimeException) {
    return PackageSensor.<String>builder()
        .sensor(stringFromMap(properties, translator.getName()))
        .position(positionFromMap(translator.getPosition(), properties, row, runtimeException))
        .build();
  }

  private static Position positionFromMap(PositionTranslator positionTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Position.builder()
        .x(floatFromMap(properties, "Sensor Position (X)", positionTranslator.getX(), row, runtimeException))
        .y(floatFromMap(properties, "Sensor Position (Y)", positionTranslator.getY(), row, runtimeException))
        .z(floatFromMap(properties, "Sensor Position (Z)", positionTranslator.getZ(), row, runtimeException))
        .build();
  }

  private static AudioPackage audioPackageFromMap(AudioPackageTranslator audioPackageTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    QualityControlDetailTranslator qualityControlDetailTranslator = audioPackageTranslator.getQualityControlDetailTranslator();
    
    return AudioPackage.builder()
        .uuid(uuidFromMap(properties, "UUID", audioPackageTranslator.getPackageUUID(), row, runtimeException))
        .dataCollectionName(stringFromMap(properties, audioPackageTranslator.getDataCollectionName()))
        .processingLevel(processingLevelFromMap(properties, audioPackageTranslator.getProcessingLevel(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, "Temperature Path", audioPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, "Biological Path", audioPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, "Other Path", audioPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, "Documents Path", audioPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, "Calibration Documents Path", audioPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, "Source Path", audioPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, audioPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, audioPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, audioPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, audioPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, "Public Release Date", audioPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, audioPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, audioPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, audioPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, audioPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, audioPackageTranslator.getInstrument()))
        .instrumentId(stringFromMap(properties, audioPackageTranslator.getInstrumentId()))
        .startTime(localDateTimeFromMap(properties, "Start Time", audioPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", audioPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, "Pre-Deployment Calibration Date", audioPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, "Post-Deployment Calibration Date", audioPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, audioPackageTranslator.getCalibrationDescription()))
        .hydrophoneSensitivity(floatFromMap(properties, "Hydrophone Sensitivity", audioPackageTranslator.getHydrophoneSensitivity(), row, runtimeException))
        .frequencyRange(stringFromMap(properties, audioPackageTranslator.getFrequencyRange()))
        .gain(floatFromMap(properties, "Gain", audioPackageTranslator.getGain(), row, runtimeException))
        .deploymentTitle(stringFromMap(properties, audioPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, audioPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, audioPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, audioPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, audioPackageTranslator.getAlternateDeploymentName()))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator == null ? Collections.emptyList() : qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .deploymentTime(localDateTimeFromMap(properties, "Deployment Time", audioPackageTranslator.getDeploymentTime(), row, runtimeException))
        .recoveryTime(localDateTimeFromMap(properties, "Recovery Time", audioPackageTranslator.getRecoveryTime(), row, runtimeException))
        .audioStartTime(localDateTimeFromMap(properties, "Audio Start Time", audioPackageTranslator.getAudioStartTime(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, "Audio End Time", audioPackageTranslator.getAudioEndTime(), row, runtimeException))
        .comments(stringFromMap(properties, audioPackageTranslator.getComments()))
        .sensors(packageSensorsFromMap(properties, audioPackageTranslator.getSensors(), row, runtimeException))
        .channels(channelsFromMap(audioPackageTranslator.getChannelTranslators(), properties, row, runtimeException))
        .locationDetail(locationDetailFromMap(
            audioPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException
        ))
        .build();
  }

  private static DetectionsPackage detectionsPackageFromMap(DetectionsPackageTranslator detectionsPackageTranslator, Map<String, ValueWithColumnNumber> properties,
      int row, RuntimeException runtimeException)
      throws TranslationException {
    QualityControlDetailTranslator qualityControlDetailTranslator = detectionsPackageTranslator.getQualityControlDetailTranslator();
    return DetectionsPackage.builder()
        .uuid(uuidFromMap(properties, "UUID", detectionsPackageTranslator.getPackageUUID(), row, runtimeException))
        .dataCollectionName(stringFromMap(properties, detectionsPackageTranslator.getDataCollectionName()))
        .processingLevel(processingLevelFromMap(properties, detectionsPackageTranslator.getProcessingLevel(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, "Temperature Path", detectionsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, "Biological Path", detectionsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, "Other Path", detectionsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, "Documents Path", detectionsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, "Calibration Documents Path", detectionsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, "Source Path", detectionsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, detectionsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, detectionsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, detectionsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, detectionsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, "Public Release Date", detectionsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, detectionsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, detectionsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, detectionsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, detectionsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, detectionsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, "Start Time", detectionsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", detectionsPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, "Pre-Deployment Calibration Date", detectionsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, "Post-Deployment Calibration Date", detectionsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, detectionsPackageTranslator.getCalibrationDescription()))
        .deploymentTitle(stringFromMap(properties, detectionsPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, detectionsPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, detectionsPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, detectionsPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, detectionsPackageTranslator.getAlternateDeploymentName()))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator == null ? Collections.emptyList() : qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .softwareNames(stringFromMap(properties, detectionsPackageTranslator.getSoftwareNames()))
        .softwareVersions(stringFromMap(properties, detectionsPackageTranslator.getSoftwareVersions()))
        .softwareProtocolCitation(stringFromMap(properties, detectionsPackageTranslator.getSoftwareProtocolCitation()))
        .softwareDescription(stringFromMap(properties, detectionsPackageTranslator.getSoftwareDescription()))
        .softwareProcessingDescription(stringFromMap(properties, detectionsPackageTranslator.getSoftwareProcessingDescription()))
        .analysisTimeZone(integerFromMap(properties, "Analysis Time Zone", detectionsPackageTranslator.getAnalysisTimeZone(), row, runtimeException))
        .analysisEffort(integerFromMap(properties, "Analysis Effort", detectionsPackageTranslator.getAnalysisEffort(), row, runtimeException))
        .sampleRate(floatFromMap(properties, "Sample Rate", detectionsPackageTranslator.getSampleRate(), row, runtimeException))
        .minFrequency(floatFromMap(properties, "Min Frequency", detectionsPackageTranslator.getMinFrequency(), row, runtimeException))
        .maxFrequency(floatFromMap(properties, "Max Frequency", detectionsPackageTranslator.getMaxFrequency(), row, runtimeException))
        .locationDetail(locationDetailFromMap(detectionsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .soundSource(stringFromMap(properties, detectionsPackageTranslator.getSoundSource()))
        .build();
  }

  private static LocationDetail locationDetailFromMap(LocationDetailTranslator locationDetailTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    if (locationDetailTranslator == null) {
      return null;
    }
    if (locationDetailTranslator instanceof StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator) {
      return stationaryTerrestrialLocationFromMap(stationaryTerrestrialLocationTranslator, properties, row, runtimeException);
    } else if (locationDetailTranslator instanceof MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator) {
      return multiPointStationaryMarineLocationFromMap(multipointStationaryMarineLocationTranslator, properties, row, runtimeException);
    } else if (locationDetailTranslator instanceof MobileMarineLocationTranslator mobileMarineLocationTranslator) {
      return mobileMarineLocationFromMap(mobileMarineLocationTranslator, properties, row, runtimeException);
    } else if (locationDetailTranslator instanceof StationaryMarineLocationTranslator stationaryMarineLocationTranslator) {
      return stationaryMarineLocationFromMap(stationaryMarineLocationTranslator, properties, row, runtimeException);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", locationDetailTranslator.getClass().getSimpleName()
      ));
    }
  }

  private static MarineInstrumentLocation marineInstrumentLocationFromMap(MarineInstrumentLocationTranslator marineInstrumentLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return MarineInstrumentLocation.builder()
        .latitude(latitudeFromMap(properties, marineInstrumentLocationTranslator.getLatitude(), row, runtimeException))
        .longitude(longitudeFromMap(properties, marineInstrumentLocationTranslator.getLongitude(), row, runtimeException))
        .instrumentDepth(floatFromMap(properties, "Instrument Depth", marineInstrumentLocationTranslator.getInstrumentDepth(), row, runtimeException))
        .seaFloorDepth(floatFromMap(properties, "Sea Floor Depth", marineInstrumentLocationTranslator.getSeaFloorDepth(), row, runtimeException))
        .build();
  }

  private static StationaryTerrestrialLocation stationaryTerrestrialLocationFromMap(StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return StationaryTerrestrialLocation.builder()
        .latitude(latitudeFromMap(properties, stationaryTerrestrialLocationTranslator.getLatitude(), row, runtimeException))
        .longitude(longitudeFromMap(properties, stationaryTerrestrialLocationTranslator.getLongitude(), row, runtimeException))
        .instrumentElevation(floatFromMap(properties, "Instrument Elevation", stationaryTerrestrialLocationTranslator.getInstrumentElevation(), row, runtimeException))
        .surfaceElevation(floatFromMap(properties, "Surface Elevation", stationaryTerrestrialLocationTranslator.getSurfaceElevation(), row, runtimeException))
        .build();
  }

  private static MultiPointStationaryMarineLocation multiPointStationaryMarineLocationFromMap(MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return MultiPointStationaryMarineLocation.builder()
        .seaArea(stringFromMap(properties, multipointStationaryMarineLocationTranslator.getSeaArea()))
        .locations(multipointStationaryMarineLocationTranslator.getLocationTranslators().stream()
            .map(marineInstrumentLocationTranslator -> marineInstrumentLocationFromMap(marineInstrumentLocationTranslator, properties, row, runtimeException))
            .toList())
        .build();
  }

  private static MobileMarineLocation mobileMarineLocationFromMap(MobileMarineLocationTranslator mobileMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return MobileMarineLocation.builder()
        .locationDerivationDescription(stringFromMap(properties, mobileMarineLocationTranslator.getLocationDerivationDescription()))
        .vessel(stringFromMap(properties, mobileMarineLocationTranslator.getVessel()))
        .seaArea(stringFromMap(properties, mobileMarineLocationTranslator.getSeaArea()))
        .fileList(pathListFromMap(properties, "File List", mobileMarineLocationTranslator.getSingleStringFiles(), row, runtimeException))
        .build();
  }

  private static StationaryMarineLocation stationaryMarineLocationFromMap(StationaryMarineLocationTranslator stationaryMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return StationaryMarineLocation.builder()
        .deploymentLocation(marineInstrumentLocationFromMap(stationaryMarineLocationTranslator.getDeploymentLocationTranslator(), properties, row, runtimeException))
        .recoveryLocation(marineInstrumentLocationFromMap(stationaryMarineLocationTranslator.getRecoveryLocationTranslator(), properties, row, runtimeException))
        .seaArea(stringFromMap(properties, stationaryMarineLocationTranslator.getSeaArea()))
        .build();
  }

  private static SoundClipsPackage soundClipsPackageFromMap(SoundClipsPackageTranslator soundClipsPackageTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    return SoundClipsPackage.builder()
        .uuid(uuidFromMap(properties, "UUID", soundClipsPackageTranslator.getPackageUUID(), row, runtimeException))
        .dataCollectionName(stringFromMap(properties, soundClipsPackageTranslator.getDataCollectionName()))
        .processingLevel(processingLevelFromMap(properties, soundClipsPackageTranslator.getProcessingLevel(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, "Temperature Path", soundClipsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, "Biological Path", soundClipsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, "Other Path", soundClipsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, "Documents Path", soundClipsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, "Calibration Documents Path", soundClipsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, "Source Path", soundClipsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, soundClipsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, soundClipsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, soundClipsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, soundClipsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, "Public Release Date", soundClipsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, soundClipsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, soundClipsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, soundClipsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, soundClipsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, soundClipsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, "Start Time", soundClipsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", soundClipsPackageTranslator.getEndTime(), row, runtimeException))
        .audioStartTime(localDateTimeFromMap(properties, "Audio Start Time", soundClipsPackageTranslator.getAudioStartTime(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, "Audio End Time", soundClipsPackageTranslator.getAudioEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, "Pre-Deployment Calibration Date", soundClipsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, "Post-Deployment Calibration Date", soundClipsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, soundClipsPackageTranslator.getCalibrationDescription()))
        .deploymentTitle(stringFromMap(properties, soundClipsPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, soundClipsPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, soundClipsPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, soundClipsPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, soundClipsPackageTranslator.getAlternateDeploymentName()))
        .softwareNames(stringFromMap(properties, soundClipsPackageTranslator.getSoftwareNames()))
        .softwareVersions(stringFromMap(properties, soundClipsPackageTranslator.getSoftwareVersions()))
        .softwareProtocolCitation(stringFromMap(properties, soundClipsPackageTranslator.getSoftwareProtocolCitation()))
        .softwareDescription(stringFromMap(properties, soundClipsPackageTranslator.getSoftwareDescription()))
        .softwareProcessingDescription(stringFromMap(properties, soundClipsPackageTranslator.getSoftwareProcessingDescription()))
        .locationDetail(locationDetailFromMap(soundClipsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .build();
  }

  private static SoundLevelMetricsPackage soundLevelMetricsPackageFromMap(SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator, Map<String, ValueWithColumnNumber> properties,
      int row, RuntimeException runtimeException) throws TranslationException {
    QualityControlDetailTranslator qualityControlDetailTranslator = soundLevelMetricsPackageTranslator.getQualityControlDetailTranslator();
    return SoundLevelMetricsPackage.builder()
        .uuid(uuidFromMap(properties, "UUID", soundLevelMetricsPackageTranslator.getPackageUUID(), row, runtimeException))
        .dataCollectionName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDataCollectionName()))
        .processingLevel(processingLevelFromMap(properties, soundLevelMetricsPackageTranslator.getProcessingLevel(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, "Temperature Path", soundLevelMetricsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, "Biological Path", soundLevelMetricsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, "Other Path", soundLevelMetricsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, "Documents Path", soundLevelMetricsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, "Calibration Documents Path", soundLevelMetricsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, "Source Path", soundLevelMetricsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, "Public Release Date", soundLevelMetricsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, soundLevelMetricsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, soundLevelMetricsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, "Start Time", soundLevelMetricsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", soundLevelMetricsPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, "Pre-Deployment Calibration Date", soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, "Post-Deployment Calibration Date", soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, soundLevelMetricsPackageTranslator.getCalibrationDescription()))
        .deploymentTitle(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getAlternateDeploymentName()))
        .audioStartTime(localDateTimeFromMap(properties, "Audio Start Time", soundLevelMetricsPackageTranslator.getAudioStartTimeTranslator(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, "Audio End Time", soundLevelMetricsPackageTranslator.getAudioEndTimeTranslator(), row, runtimeException))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator == null ? null : qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator == null ? Collections.emptyList() : qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .analysisTimeZone(integerFromMap(properties, "Analysis Time Zone", soundLevelMetricsPackageTranslator.getAnalysisTimeZone(), row, runtimeException))
        .analysisEffort(integerFromMap(properties, "Analysis Effort", soundLevelMetricsPackageTranslator.getAnalysisEffort(), row, runtimeException))
        .sampleRate(floatFromMap(properties, "Sample Rate", soundLevelMetricsPackageTranslator.getSampleRate(), row, runtimeException))
        .minFrequency(floatFromMap(properties, "Min Frequency", soundLevelMetricsPackageTranslator.getMinFrequency(), row, runtimeException))
        .maxFrequency(floatFromMap(properties, "Max Frequency", soundLevelMetricsPackageTranslator.getMaxFrequency(), row, runtimeException))
        .softwareNames(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSoftwareNames()))
        .softwareVersions(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSoftwareVersions()))
        .softwareProtocolCitation(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSoftwareProtocolCitation()))
        .softwareDescription(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSoftwareDescription()))
        .softwareProcessingDescription(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSoftwareProcessingDescription()))
        .locationDetail(locationDetailFromMap(soundLevelMetricsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .build();
  }

  private static SoundPropagationModelsPackage soundPropagationModelsPackageFromMap(SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator,
      Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) throws TranslationException {
    return SoundPropagationModelsPackage.builder()
        .uuid(uuidFromMap(properties, "UUID", soundPropagationModelsPackageTranslator.getPackageUUID(), row, runtimeException))
        .dataCollectionName(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDataCollectionName()))
        .processingLevel(processingLevelFromMap(properties, soundPropagationModelsPackageTranslator.getProcessingLevel(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, "Temperature Path", soundPropagationModelsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, "Biological Path", soundPropagationModelsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, "Other Path", soundPropagationModelsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, "Documents Path", soundPropagationModelsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, "Calibration Documents Path", soundPropagationModelsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, "Source Path", soundPropagationModelsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, "Public Release Date", soundPropagationModelsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, soundPropagationModelsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, soundPropagationModelsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, "Start Time", soundPropagationModelsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", soundPropagationModelsPackageTranslator.getEndTime(), row, runtimeException))
        .modeledFrequency(floatFromMap(properties, "Modeled Frequency", soundPropagationModelsPackageTranslator.getModeledFrequency(), row, runtimeException))
        .softwareNames(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareNames()))
        .softwareVersions(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareVersions()))
        .softwareProtocolCitation(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareProtocolCitation()))
        .softwareDescription(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareDescription()))
        .softwareProcessingDescription(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareProcessingDescription()))
        .audioStartTime(localDateTimeFromMap(properties, "Audio Start Time", soundPropagationModelsPackageTranslator.getAudioStartTimeTranslator(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, "Audio End Time", soundPropagationModelsPackageTranslator.getAudioEndTimeTranslator(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, "Pre-Deployment Calibration Date", soundPropagationModelsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, "Post-Deployment Calibration Date", soundPropagationModelsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, soundPropagationModelsPackageTranslator.getCalibrationDescription()))
        .deploymentTitle(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, soundPropagationModelsPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, soundPropagationModelsPackageTranslator.getAlternateDeploymentName()))
        .locationDetail(locationDetailFromMap(soundPropagationModelsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .build();
  }

  private static DataQualityEntry dataQualityEntryFromMap(DataQualityEntryTranslator dataQualityEntryTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return DataQualityEntry.builder()
        .startTime(localDateTimeFromMap(properties, "Start Time", dataQualityEntryTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", dataQualityEntryTranslator.getEndTime(), row, runtimeException))
        .minFrequency(floatFromMap(properties, "Min Frequency", dataQualityEntryTranslator.getMinFrequency(), row, runtimeException))
        .maxFrequency(floatFromMap(properties, "Max Frequency", dataQualityEntryTranslator.getMaxFrequency(), row, runtimeException))
        .qualityLevel(qualityLevelFromMap(properties, dataQualityEntryTranslator.getQualityLevel(), row, runtimeException))
        .comments(stringFromMap(properties, dataQualityEntryTranslator.getComments()))
        .channelNumbers(getChannelNumbers(properties, dataQualityEntryTranslator.getChannelNumbers(), row, runtimeException))
        .build();
  }

  private static List<Integer> getChannelNumbers(Map<String, ValueWithColumnNumber> properties, String property, int row,
      RuntimeException runtimeException) {
    String rawValue = stringFromMap(properties, property);
    if (StringUtils.isBlank(rawValue)) {
      return Collections.emptyList();
    }
    return Arrays.stream(rawValue.split(";"))
        .map(v -> {
          try {
            return Integer.parseInt(v);
          } catch (Throwable t) {
            ValueWithColumnNumber propertyValue = properties.get(property);
            if (property == null) {
              return null;
            }
            runtimeException.addSuppressed(new FieldException(
                "channelNumber", "Channel Number", "Invalid integer format", propertyValue.column(), row
            ));
            return null;
          }
        }).filter(Objects::nonNull)
        .toList();
  }

  private static ProcessingLevel processingLevelFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, propertyName);
    String property = stringFromProperty(valueWithColumnNumber);

    if (property == null) {
      return null;
    }

    try {
      return ProcessingLevel.valueOf(property);
    } catch (IllegalArgumentException e) {
      runtimeException.addSuppressed(new FieldException(
          propertyName, "Processing Level", String.format(
              "Unsupported processing level - %s", property
      ), valueWithColumnNumber.column(), row
      ));
      return null;
    }
  }

  private static QualityLevel qualityLevelFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, propertyName);
    String property = stringFromProperty(valueWithColumnNumber);

    if (property == null) {
      return null;
    }

    try {
      return QualityLevel.fromName(property);
    } catch (IllegalStateException e) {
      runtimeException.addSuppressed(new FieldException(
          propertyName, "Quality Level", e.getMessage(), valueWithColumnNumber.column(), row
      ));
      return null;
    }
  }

  private static List<DataQualityEntry> dataQualityEntriesFromMap(List<DataQualityEntryTranslator> dataQualityEntryTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return dataQualityEntryTranslators.stream()
        .map(dataQualityEntryTranslator -> dataQualityEntryFromMap(dataQualityEntryTranslator, properties, row, runtimeException))
        .toList();
  }

  private static Channel<String> channelFromMap(ChannelTranslator channelTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    return Channel.<String>builder()
        .sensor(packageSensorFromMap(properties, channelTranslator.getSensor(), row, runtimeException))
        .startTime(localDateTimeFromMap(properties, "Start Time", channelTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", channelTranslator.getEndTime(), row, runtimeException))
        .sampleRates(sampleRatesFromMap(channelTranslator.getSampleRates(), properties, row, runtimeException))
        .dutyCycles(dutyCyclesFromMap(channelTranslator.getDutyCycles(), properties, row, runtimeException))
        .gains(gainsFromMap(channelTranslator.getGains(), properties, row, runtimeException))
        .build();
  }

  private static SampleRate sampleRateFromMap(SampleRateTranslator sampleRateTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return SampleRate.builder()
        .startTime(localDateTimeFromMap(properties, "Start Time", sampleRateTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", sampleRateTranslator.getEndTime(), row, runtimeException))
        .sampleRate(floatFromMap(properties, "Sample Rate", sampleRateTranslator.getSampleRate(), row, runtimeException))
        .sampleBits(integerFromMap(properties, "Sample Bits", sampleRateTranslator.getSampleBits(), row, runtimeException))
        .build();
  }

  private static List<SampleRate> sampleRatesFromMap(List<SampleRateTranslator> sampleRateTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return sampleRateTranslators.stream()
        .map(sampleRateTranslator -> sampleRateFromMap(sampleRateTranslator, properties, row, runtimeException))
        .toList();
  }

  private static DutyCycle dutyCycleFromMap(DutyCycleTranslator dutyCycleTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return DutyCycle.builder()
        .startTime(localDateTimeFromMap(properties, "Start Time", dutyCycleTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "Start Time", dutyCycleTranslator.getEndTime(), row, runtimeException))
        .duration(floatFromMap(properties, "Duration", dutyCycleTranslator.getDuration(), row, runtimeException))
        .interval(floatFromMap(properties, "Interval", dutyCycleTranslator.getInterval(), row, runtimeException))
        .build();
  }

  private static List<DutyCycle> dutyCyclesFromMap(List<DutyCycleTranslator> dutyCycleTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return dutyCycleTranslators.stream()
        .map(dutyCycleTranslator -> dutyCycleFromMap(dutyCycleTranslator, properties, row, runtimeException))
        .toList();
  }

  private static Gain gainFromMap(GainTranslator gainTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Gain.builder()
        .startTime(localDateTimeFromMap(properties, "Start Time", gainTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, "End Time", gainTranslator.getEndTime(), row, runtimeException))
        .gain(floatFromMap(properties, "Gain", gainTranslator.getGain(), row, runtimeException))
        .build();
  }

  private static List<Gain> gainsFromMap(List<GainTranslator> gainTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return gainTranslators.stream()
        .map(gainTranslator -> gainFromMap(gainTranslator, properties, row, runtimeException))
        .toList();
  }

  private static List<Channel<String>> channelsFromMap(List<ChannelTranslator> channelTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return channelTranslators.stream()
        .map(channelTranslator -> {
          try {
            return channelFromMap(channelTranslator, properties, row, runtimeException);
          } catch (TranslationException e) {
            runtimeException.addSuppressed(e);
            return null;
          }
        }).filter(Objects::nonNull)
        .toList();
  }
}
