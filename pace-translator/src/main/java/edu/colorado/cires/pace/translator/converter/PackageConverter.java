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

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        .uuid(uuidFromMap(properties, cpodPackageTranslator.getPackageUUID(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, cpodPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, cpodPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, cpodPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, cpodPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, cpodPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .navigationPath(pathFromMap(properties, cpodPackageTranslator.getNavigationPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, cpodPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, cpodPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, cpodPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, cpodPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, cpodPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, cpodPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, cpodPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, cpodPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, cpodPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, cpodPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, cpodPackageTranslator.getInstrument()))
        .instrumentId(stringFromMap(properties, cpodPackageTranslator.getInstrumentId()))
        .startTime(localDateTimeFromMap(properties, cpodPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, cpodPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, cpodPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, cpodPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, cpodPackageTranslator.getCalibrationDescription()))
        .hydrophoneSensitivity(floatFromMap(properties, cpodPackageTranslator.getHydrophoneSensitivity(), row, runtimeException))
        .frequencyRange(floatFromMap(properties, cpodPackageTranslator.getFrequencyRange(), row, runtimeException))
        .gain(floatFromMap(properties, cpodPackageTranslator.getGain(), row, runtimeException))
        .deploymentTitle(stringFromMap(properties, cpodPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, cpodPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, cpodPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, cpodPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, cpodPackageTranslator.getAlternateDeploymentName()))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .deploymentTime(localDateTimeFromMap(properties, cpodPackageTranslator.getDeploymentTime(), row, runtimeException))
        .recoveryTime(localDateTimeFromMap(properties, cpodPackageTranslator.getRecoveryTime(), row, runtimeException))
        .comments(stringFromMap(properties, cpodPackageTranslator.getComments()))
        .sensors(packageSensorsFromMap(properties, cpodPackageTranslator.getSensors(), row, runtimeException))
        .channels(channelsFromMap(cpodPackageTranslator.getChannelTranslators(), properties, row, runtimeException))
        .locationDetail(locationDetailFromMap(cpodPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .build();
  }

  private static List<PackageSensor> packageSensorsFromMap(Map<String, ValueWithColumnNumber> properties, List<PackageSensorTranslator> sensors,
      int row, RuntimeException runtimeException) {
    return sensors.stream()
        .map(translator -> packageSensorFromMap(properties, translator, row, runtimeException))
        .toList();
  }
  
  private static PackageSensor packageSensorFromMap(Map<String, ValueWithColumnNumber> properties, PackageSensorTranslator translator, int row, RuntimeException runtimeException) {
    return PackageSensor.builder()
        .name(stringFromMap(properties, translator.getName()))
        .position(positionFromMap(translator.getPosition(), properties, row, runtimeException))
        .build();
  }

  private static Position positionFromMap(PositionTranslator positionTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Position.builder()
        .x(floatFromMap(properties, positionTranslator.getX(), row, runtimeException))
        .y(floatFromMap(properties, positionTranslator.getY(), row, runtimeException))
        .z(floatFromMap(properties, positionTranslator.getZ(), row, runtimeException))
        .build();
  }

  private static AudioPackage audioPackageFromMap(AudioPackageTranslator audioPackageTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    QualityControlDetailTranslator qualityControlDetailTranslator = audioPackageTranslator.getQualityControlDetailTranslator();
    
    return AudioPackage.builder()
        .uuid(uuidFromMap(properties, audioPackageTranslator.getPackageUUID(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, audioPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, audioPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, audioPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, audioPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, audioPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .navigationPath(pathFromMap(properties, audioPackageTranslator.getNavigationPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, audioPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, audioPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, audioPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, audioPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, audioPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, audioPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, audioPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, audioPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, audioPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, audioPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, audioPackageTranslator.getInstrument()))
        .instrumentId(stringFromMap(properties, audioPackageTranslator.getInstrumentId()))
        .startTime(localDateTimeFromMap(properties, audioPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, audioPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, audioPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, audioPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, audioPackageTranslator.getCalibrationDescription()))
        .hydrophoneSensitivity(floatFromMap(properties, audioPackageTranslator.getHydrophoneSensitivity(), row, runtimeException))
        .frequencyRange(floatFromMap(properties, audioPackageTranslator.getFrequencyRange(), row, runtimeException))
        .gain(floatFromMap(properties, audioPackageTranslator.getGain(), row, runtimeException))
        .deploymentTitle(stringFromMap(properties, audioPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, audioPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, audioPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, audioPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, audioPackageTranslator.getAlternateDeploymentName()))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .deploymentTime(localDateTimeFromMap(properties, audioPackageTranslator.getDeploymentTime(), row, runtimeException))
        .recoveryTime(localDateTimeFromMap(properties, audioPackageTranslator.getRecoveryTime(), row, runtimeException))
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
        .uuid(uuidFromMap(properties, detectionsPackageTranslator.getPackageUUID(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, detectionsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, detectionsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, detectionsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, detectionsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, detectionsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .navigationPath(pathFromMap(properties, detectionsPackageTranslator.getNavigationPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, detectionsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, detectionsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, detectionsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, detectionsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, detectionsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, detectionsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, detectionsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, detectionsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, detectionsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, detectionsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, detectionsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, detectionsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, detectionsPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, detectionsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, detectionsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, detectionsPackageTranslator.getCalibrationDescription()))
        .deploymentTitle(stringFromMap(properties, detectionsPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, detectionsPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, detectionsPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, detectionsPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, detectionsPackageTranslator.getAlternateDeploymentName()))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .softwareNames(stringFromMap(properties, detectionsPackageTranslator.getSoftwareNames()))
        .softwareVersions(stringFromMap(properties, detectionsPackageTranslator.getSoftwareVersions()))
        .softwareProtocolCitation(stringFromMap(properties, detectionsPackageTranslator.getSoftwareProtocolCitation()))
        .softwareDescription(stringFromMap(properties, detectionsPackageTranslator.getSoftwareDescription()))
        .softwareProcessingDescription(stringFromMap(properties, detectionsPackageTranslator.getSoftwareProcessingDescription()))
        .analysisTimeZone(integerFromMap(properties, detectionsPackageTranslator.getAnalysisTimeZone(), row, runtimeException))
        .analysisEffort(integerFromMap(properties, detectionsPackageTranslator.getAnalysisEffort(), row, runtimeException))
        .sampleRate(floatFromMap(properties, detectionsPackageTranslator.getSampleRate(), row, runtimeException))
        .minFrequency(floatFromMap(properties, detectionsPackageTranslator.getMinFrequency(), row, runtimeException))
        .maxFrequency(floatFromMap(properties, detectionsPackageTranslator.getMaxFrequency(), row, runtimeException))
        .locationDetail(locationDetailFromMap(detectionsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException))
        .soundSource(stringFromMap(properties, detectionsPackageTranslator.getSoundSource()))
        .build();
  }

  private static LocationDetail locationDetailFromMap(LocationDetailTranslator locationDetailTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    if (locationDetailTranslator instanceof StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator) {
      return stationaryTerrestrialLocationFromMap(stationaryTerrestrialLocationTranslator, properties, row, runtimeException);
    } else if (locationDetailTranslator instanceof MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator) {
      return multiPointStationaryMarineLocationFromMap(multipointStationaryMarineLocationTranslator, properties, row, runtimeException);
    } else if (locationDetailTranslator instanceof MobileMarineLocationTranslator mobileMarineLocationTranslator) {
      return mobileMarineLocationFromMap(mobileMarineLocationTranslator, properties);
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
        .instrumentDepth(floatFromMap(properties, marineInstrumentLocationTranslator.getInstrumentDepth(), row, runtimeException))
        .seaFloorDepth(floatFromMap(properties, marineInstrumentLocationTranslator.getSeaFloorDepth(), row, runtimeException))
        .build();
  }

  private static StationaryTerrestrialLocation stationaryTerrestrialLocationFromMap(StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return StationaryTerrestrialLocation.builder()
        .latitude(latitudeFromMap(properties, stationaryTerrestrialLocationTranslator.getLatitude(), row, runtimeException))
        .longitude(longitudeFromMap(properties, stationaryTerrestrialLocationTranslator.getLongitude(), row, runtimeException))
        .instrumentElevation(floatFromMap(properties, stationaryTerrestrialLocationTranslator.getInstrumentElevation(), row, runtimeException))
        .surfaceElevation(floatFromMap(properties, stationaryTerrestrialLocationTranslator.getSurfaceElevation(), row, runtimeException))
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

  private static MobileMarineLocation mobileMarineLocationFromMap(MobileMarineLocationTranslator mobileMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties) {
    return MobileMarineLocation.builder()
        .locationDerivationDescription(stringFromMap(properties, mobileMarineLocationTranslator.getLocationDerivationDescription()))
        .vessel(stringFromMap(properties, mobileMarineLocationTranslator.getVessel()))
        .seaArea(stringFromMap(properties, mobileMarineLocationTranslator.getSeaArea()))
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
        .uuid(uuidFromMap(properties, soundClipsPackageTranslator.getPackageUUID(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, soundClipsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, soundClipsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, soundClipsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, soundClipsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, soundClipsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, soundClipsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .navigationPath(pathFromMap(properties, soundClipsPackageTranslator.getNavigationPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, soundClipsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, soundClipsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, soundClipsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, soundClipsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, soundClipsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, soundClipsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, soundClipsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, soundClipsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, soundClipsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, soundClipsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, soundClipsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, soundClipsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, soundClipsPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, soundClipsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, soundClipsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
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
        .uuid(uuidFromMap(properties, soundLevelMetricsPackageTranslator.getPackageUUID(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .navigationPath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getNavigationPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, soundLevelMetricsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, soundLevelMetricsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, soundLevelMetricsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, soundLevelMetricsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, soundLevelMetricsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, soundLevelMetricsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, soundLevelMetricsPackageTranslator.getEndTime(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
        .calibrationDescription(stringFromMap(properties, soundLevelMetricsPackageTranslator.getCalibrationDescription()))
        .deploymentTitle(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentTitle()))
        .deploymentPurpose(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentPurpose()))
        .deploymentDescription(stringFromMap(properties, soundLevelMetricsPackageTranslator.getDeploymentDescription()))
        .alternateSiteName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getAlternateSiteName()))
        .alternateDeploymentName(stringFromMap(properties, soundLevelMetricsPackageTranslator.getAlternateDeploymentName()))
        .audioStartTime(localDateTimeFromMap(properties, soundLevelMetricsPackageTranslator.getAudioStartTimeTranslator(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, soundLevelMetricsPackageTranslator.getAudioEndTimeTranslator(), row, runtimeException))
        .qualityAnalyst(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst()))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .analysisTimeZone(integerFromMap(properties, soundLevelMetricsPackageTranslator.getAnalysisTimeZone(), row, runtimeException))
        .analysisEffort(integerFromMap(properties, soundLevelMetricsPackageTranslator.getAnalysisEffort(), row, runtimeException))
        .sampleRate(floatFromMap(properties, soundLevelMetricsPackageTranslator.getSampleRate(), row, runtimeException))
        .minFrequency(floatFromMap(properties, soundLevelMetricsPackageTranslator.getMinFrequency(), row, runtimeException))
        .maxFrequency(floatFromMap(properties, soundLevelMetricsPackageTranslator.getMaxFrequency(), row, runtimeException))
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
        .uuid(uuidFromMap(properties, soundPropagationModelsPackageTranslator.getPackageUUID(), row, runtimeException))
        .temperaturePath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getTemperaturePath(), row, runtimeException))
        .biologicalPath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getBiologicalPath(), row, runtimeException))
        .otherPath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getOtherPath(), row, runtimeException))
        .documentsPath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getDocumentsPath(), row, runtimeException))
        .calibrationDocumentsPath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getCalibrationDocumentsPath(), row, runtimeException))
        .navigationPath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getNavigationPath(), row, runtimeException))
        .sourcePath(pathFromMap(properties, soundPropagationModelsPackageTranslator.getSourcePath(), row, runtimeException))
        .siteOrCruiseName(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSiteOrCruiseName()))
        .deploymentId(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDeploymentId()))
        .datasetPackager(stringFromMap(properties, soundPropagationModelsPackageTranslator.getDatasetPackager()))
        .projects(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getProjects()))
        .publicReleaseDate(localDateFromMap(properties, soundPropagationModelsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getScientists()))
        .sponsors(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getSponsors()))
        .funders(stringListFromMap(properties, soundPropagationModelsPackageTranslator.getFunders()))
        .platform(stringFromMap(properties, soundPropagationModelsPackageTranslator.getPlatform()))
        .instrument(stringFromMap(properties, soundPropagationModelsPackageTranslator.getInstrument()))
        .startTime(localDateTimeFromMap(properties, soundPropagationModelsPackageTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, soundPropagationModelsPackageTranslator.getEndTime(), row, runtimeException))
        .modeledFrequency(floatFromMap(properties, soundPropagationModelsPackageTranslator.getModeledFrequency(), row, runtimeException))
        .softwareNames(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareNames()))
        .softwareVersions(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareVersions()))
        .softwareProtocolCitation(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareProtocolCitation()))
        .softwareDescription(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareDescription()))
        .softwareProcessingDescription(stringFromMap(properties, soundPropagationModelsPackageTranslator.getSoftwareProcessingDescription()))
        .audioStartTime(localDateTimeFromMap(properties, soundPropagationModelsPackageTranslator.getAudioStartTimeTranslator(), row, runtimeException))
        .audioEndTime(localDateTimeFromMap(properties, soundPropagationModelsPackageTranslator.getAudioEndTimeTranslator(), row, runtimeException))
        .preDeploymentCalibrationDate(localDateFromMap(properties, soundPropagationModelsPackageTranslator.getPreDeploymentCalibrationDate(), row, runtimeException))
        .postDeploymentCalibrationDate(localDateFromMap(properties, soundPropagationModelsPackageTranslator.getPostDeploymentCalibrationDate(), row, runtimeException))
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
        .startTime(localDateTimeFromMap(properties, dataQualityEntryTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, dataQualityEntryTranslator.getEndTime(), row, runtimeException))
        .minFrequency(floatFromMap(properties, dataQualityEntryTranslator.getMinFrequency(), row, runtimeException))
        .maxFrequency(floatFromMap(properties, dataQualityEntryTranslator.getMaxFrequency(), row, runtimeException))
        .qualityLevel(qualityLevelFromMap(properties, dataQualityEntryTranslator.getQualityLevel(), row, runtimeException))
        .comments(stringFromMap(properties, dataQualityEntryTranslator.getComments()))
        .build();
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
          propertyName, e.getMessage(), valueWithColumnNumber.column(), row
      ));
      return null;
    }
  }

  private static List<DataQualityEntry> dataQualityEntriesFromMap(List<DataQualityEntryTranslator> dataQualityEntryTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return dataQualityEntryTranslators.stream()
        .map(dataQualityEntryTranslator -> dataQualityEntryFromMap(dataQualityEntryTranslator, properties, row, runtimeException))
        .toList();
  }

  private static Channel channelFromMap(ChannelTranslator channelTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    return Channel.builder()
        .sensor(packageSensorFromMap(properties, channelTranslator.getSensor(), row, runtimeException))
        .startTime(localDateTimeFromMap(properties, channelTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, channelTranslator.getEndTime(), row, runtimeException))
        .sampleRates(sampleRatesFromMap(channelTranslator.getSampleRates(), properties, row, runtimeException))
        .dutyCycles(dutyCyclesFromMap(channelTranslator.getDutyCycles(), properties, row, runtimeException))
        .gains(gainsFromMap(channelTranslator.getGains(), properties, row, runtimeException))
        .build();
  }

  private static SampleRate sampleRateFromMap(SampleRateTranslator sampleRateTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return SampleRate.builder()
        .startTime(localDateTimeFromMap(properties, sampleRateTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, sampleRateTranslator.getEndTime(), row, runtimeException))
        .sampleRate(floatFromMap(properties, sampleRateTranslator.getSampleRate(), row, runtimeException))
        .sampleBits(integerFromMap(properties, sampleRateTranslator.getSampleBits(), row, runtimeException))
        .build();
  }

  private static List<SampleRate> sampleRatesFromMap(List<SampleRateTranslator> sampleRateTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return sampleRateTranslators.stream()
        .map(sampleRateTranslator -> sampleRateFromMap(sampleRateTranslator, properties, row, runtimeException))
        .toList();
  }

  private static DutyCycle dutyCycleFromMap(DutyCycleTranslator dutyCycleTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return DutyCycle.builder()
        .startTime(localDateTimeFromMap(properties, dutyCycleTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, dutyCycleTranslator.getEndTime(), row, runtimeException))
        .duration(floatFromMap(properties, dutyCycleTranslator.getDuration(), row, runtimeException))
        .interval(floatFromMap(properties, dutyCycleTranslator.getInterval(), row, runtimeException))
        .build();
  }

  private static List<DutyCycle> dutyCyclesFromMap(List<DutyCycleTranslator> dutyCycleTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return dutyCycleTranslators.stream()
        .map(dutyCycleTranslator -> dutyCycleFromMap(dutyCycleTranslator, properties, row, runtimeException))
        .toList();
  }

  private static Gain gainFromMap(GainTranslator gainTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Gain.builder()
        .startTime(localDateTimeFromMap(properties, gainTranslator.getStartTime(), row, runtimeException))
        .endTime(localDateTimeFromMap(properties, gainTranslator.getEndTime(), row, runtimeException))
        .gain(floatFromMap(properties, gainTranslator.getGain(), row, runtimeException))
        .build();
  }

  private static List<Gain> gainsFromMap(List<GainTranslator> gainTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return gainTranslators.stream()
        .map(gainTranslator -> gainFromMap(gainTranslator, properties, row, runtimeException))
        .toList();
  }

  private static List<Channel> channelsFromMap(List<ChannelTranslator> channelTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
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
