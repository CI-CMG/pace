package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.delimitedObjectsFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.floatFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.integerFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.latitudeFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.localDateFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.localDateTimeFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.longitudeFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.objectFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.pathFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.propertyFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromProperty;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.data.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.translator.GainTranslator;
import edu.colorado.cires.pace.data.translator.LocationDetailTranslator;
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
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PackageConverter extends Converter<PackageTranslator, Package> {
  
  private final PersonRepository personRepository;
  private final ProjectRepository projectRepository;
  private final OrganizationRepository organizationRepository;
  private final PlatformRepository platformRepository;
  private final InstrumentRepository instrumentRepository;
  private final SeaRepository seaRepository;
  private final ShipRepository shipRepository;
  private final DetectionTypeRepository detectionTypeRepository;
  private final SensorRepository sensorRepository;

  public PackageConverter(PersonRepository personRepository, ProjectRepository projectRepository,
      OrganizationRepository organizationRepository, PlatformRepository platformRepository, InstrumentRepository instrumentRepository,
      SeaRepository seaRepository, ShipRepository shipRepository, DetectionTypeRepository detectionTypeRepository, SensorRepository sensorRepository) {
    this.personRepository = personRepository;
    this.projectRepository = projectRepository;
    this.organizationRepository = organizationRepository;
    this.platformRepository = platformRepository;
    this.instrumentRepository = instrumentRepository;
    this.seaRepository = seaRepository;
    this.shipRepository = shipRepository;
    this.detectionTypeRepository = detectionTypeRepository;
    this.sensorRepository = sensorRepository;
  }

  @Override
  public Package convert(PackageTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException)
      throws TranslationException {
    if (translator instanceof AudioPackageTranslator audioPackageTranslator) {
      return audioPackageFromMap(
          audioPackageTranslator, properties, row, runtimeException, personRepository, projectRepository, organizationRepository,
          platformRepository, instrumentRepository, seaRepository, shipRepository, sensorRepository
      );
    } else if (translator instanceof CPODPackageTranslator cpodPackageTranslator) {
      return cpodPackageFromMap(
          cpodPackageTranslator, properties, row, runtimeException, personRepository, projectRepository, organizationRepository,
          platformRepository, instrumentRepository, seaRepository, shipRepository, sensorRepository
      );
    } else if (translator instanceof DetectionsPackageTranslator detectionsPackageTranslator) {
      return detectionsPackageFromMap(detectionsPackageTranslator, properties, row, runtimeException, personRepository, projectRepository,
          organizationRepository, platformRepository, instrumentRepository, seaRepository, shipRepository, detectionTypeRepository);
    } else if (translator instanceof SoundClipsPackageTranslator soundClipsPackageTranslator) {
      return soundClipsPackageFromMap(soundClipsPackageTranslator, properties, row, runtimeException, personRepository, projectRepository,
          organizationRepository, platformRepository, instrumentRepository, seaRepository, shipRepository);
    } else if (translator instanceof SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator) {
      return soundLevelMetricsPackageFromMap(soundLevelMetricsPackageTranslator, properties, row, runtimeException, personRepository, projectRepository,
          organizationRepository, platformRepository, instrumentRepository, seaRepository, shipRepository);
    } else if (translator instanceof SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator) {
      return soundPropagationModelsPackageFromMap(soundPropagationModelsPackageTranslator, properties, row, runtimeException, personRepository, projectRepository,
          organizationRepository, platformRepository, instrumentRepository, seaRepository, shipRepository);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", translator.getClass().getSimpleName()
      ));
    }
  }

  private static CPODPackage cpodPackageFromMap(CPODPackageTranslator cpodPackageTranslator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException, PersonRepository personRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository,
      PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SeaRepository seaRepository, ShipRepository shipRepository, SensorRepository sensorRepository)
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
        .datasetPackager(objectFromMap(properties, cpodPackageTranslator.getDatasetPackager(), personRepository, row, runtimeException))
        .projects(delimitedObjectsFromMap(properties, cpodPackageTranslator.getProjects(), row, runtimeException, projectRepository))
        .publicReleaseDate(localDateFromMap(properties, cpodPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(delimitedObjectsFromMap(properties, cpodPackageTranslator.getScientists(), row, runtimeException, personRepository))
        .sponsors(delimitedObjectsFromMap(properties, cpodPackageTranslator.getSponsors(), row, runtimeException, organizationRepository))
        .funders(delimitedObjectsFromMap(properties, cpodPackageTranslator.getFunders(), row, runtimeException, organizationRepository))
        .platform(objectFromMap(properties, cpodPackageTranslator.getPlatform(), platformRepository, row, runtimeException))
        .instrument(objectFromMap(properties, cpodPackageTranslator.getInstrument(), instrumentRepository, row, runtimeException))
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
        .qualityAnalyst(objectFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst(), personRepository, row, runtimeException))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .deploymentTime(localDateTimeFromMap(properties, cpodPackageTranslator.getDeploymentTime(), row, runtimeException))
        .recoveryTime(localDateTimeFromMap(properties, cpodPackageTranslator.getRecoveryTime(), row, runtimeException))
        .comments(stringFromMap(properties, cpodPackageTranslator.getComments()))
        .sensors(delimitedObjectsFromMap(properties, cpodPackageTranslator.getSensors(), row, runtimeException, sensorRepository))
        .channels(channelsFromMap(cpodPackageTranslator.getChannelTranslators(), properties, row, runtimeException, sensorRepository))
        .locationDetail(locationDetailFromMap(cpodPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException, seaRepository, shipRepository))
        .build();
  }

  private static AudioPackage audioPackageFromMap(AudioPackageTranslator audioPackageTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, PersonRepository personRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository, PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SeaRepository seaRepository, ShipRepository shipRepository, SensorRepository sensorRepository)
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
        .datasetPackager(objectFromMap(properties, audioPackageTranslator.getDatasetPackager(), personRepository, row, runtimeException))
        .projects(delimitedObjectsFromMap(properties, audioPackageTranslator.getProjects(), row, runtimeException, projectRepository))
        .publicReleaseDate(localDateFromMap(properties, audioPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(delimitedObjectsFromMap(properties, audioPackageTranslator.getScientists(), row, runtimeException, personRepository))
        .sponsors(delimitedObjectsFromMap(properties, audioPackageTranslator.getSponsors(), row, runtimeException, organizationRepository))
        .funders(delimitedObjectsFromMap(properties, audioPackageTranslator.getFunders(), row, runtimeException, organizationRepository))
        .platform(objectFromMap(properties, audioPackageTranslator.getPlatform(), platformRepository, row, runtimeException))
        .instrument(objectFromMap(properties, audioPackageTranslator.getInstrument(), instrumentRepository, row, runtimeException))
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
        .qualityAnalyst(objectFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst(), personRepository, row, runtimeException))
        .qualityAnalysisObjectives(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisObjectives()))
        .qualityAnalysisMethod(stringFromMap(properties, qualityControlDetailTranslator.getQualityAnalysisMethod()))
        .qualityAssessmentDescription(stringFromMap(properties, qualityControlDetailTranslator.getQualityAssessmentDescription()))
        .qualityEntries(dataQualityEntriesFromMap(qualityControlDetailTranslator.getQualityEntryTranslators(), properties, row, runtimeException))
        .deploymentTime(localDateTimeFromMap(properties, audioPackageTranslator.getDeploymentTime(), row, runtimeException))
        .recoveryTime(localDateTimeFromMap(properties, audioPackageTranslator.getRecoveryTime(), row, runtimeException))
        .comments(stringFromMap(properties, audioPackageTranslator.getComments()))
        .sensors(delimitedObjectsFromMap(properties, audioPackageTranslator.getSensors(), row, runtimeException, sensorRepository))
        .channels(channelsFromMap(audioPackageTranslator.getChannelTranslators(), properties, row, runtimeException, sensorRepository))
        .locationDetail(locationDetailFromMap(
            audioPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException, seaRepository, shipRepository
        ))
        .build();
  }

  private static DetectionsPackage detectionsPackageFromMap(DetectionsPackageTranslator detectionsPackageTranslator, Map<String, ValueWithColumnNumber> properties,
      int row, RuntimeException runtimeException, PersonRepository personRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository,
      PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SeaRepository seaRepository, ShipRepository shipRepository, DetectionTypeRepository detectionTypeRepository)
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
        .datasetPackager(objectFromMap(properties, detectionsPackageTranslator.getDatasetPackager(), personRepository, row, runtimeException))
        .projects(delimitedObjectsFromMap(properties, detectionsPackageTranslator.getProjects(), row, runtimeException, projectRepository))
        .publicReleaseDate(localDateFromMap(properties, detectionsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(delimitedObjectsFromMap(properties, detectionsPackageTranslator.getScientists(), row, runtimeException, personRepository))
        .sponsors(delimitedObjectsFromMap(properties, detectionsPackageTranslator.getSponsors(), row, runtimeException, organizationRepository))
        .funders(delimitedObjectsFromMap(properties, detectionsPackageTranslator.getFunders(), row, runtimeException, organizationRepository))
        .platform(objectFromMap(properties, detectionsPackageTranslator.getPlatform(), platformRepository, row, runtimeException))
        .instrument(objectFromMap(properties, detectionsPackageTranslator.getInstrument(), instrumentRepository, row, runtimeException))
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
        .qualityAnalyst(objectFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst(), personRepository, row, runtimeException))
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
        .locationDetail(locationDetailFromMap(detectionsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException, seaRepository, shipRepository))
        .soundSource(objectFromMap(properties, detectionsPackageTranslator.getSoundSource(), detectionTypeRepository, row, runtimeException))
        .build();
  }

  private static LocationDetail locationDetailFromMap(LocationDetailTranslator locationDetailTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, CRUDRepository<?>... repositories)
      throws TranslationException {
    if (locationDetailTranslator instanceof StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator) {
      return stationaryTerrestrialLocationFromMap(stationaryTerrestrialLocationTranslator, properties, row, runtimeException);
    } else if (locationDetailTranslator instanceof MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator) {
      SeaRepository seaRepository = (SeaRepository) Arrays.stream(repositories)
          .filter(r -> r instanceof SeaRepository)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Sea repository not provided at runtime"));
      return multiPointStationaryMarineLocationFromMap(multipointStationaryMarineLocationTranslator, properties, row, runtimeException, seaRepository);
    } else if (locationDetailTranslator instanceof MobileMarineLocationTranslator mobileMarineLocationTranslator) {
      SeaRepository seaRepository = (SeaRepository) Arrays.stream(repositories)
          .filter(r -> r instanceof SeaRepository)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Sea repository not provided at runtime"));
      ShipRepository shipRepository = (ShipRepository) Arrays.stream(repositories)
          .filter(r -> r instanceof ShipRepository)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Ship repository not provided at runtime"));
      return mobileMarineLocationFromMap(mobileMarineLocationTranslator, properties, row, runtimeException, shipRepository, seaRepository);
    } else if (locationDetailTranslator instanceof StationaryMarineLocationTranslator stationaryMarineLocationTranslator) {
      SeaRepository seaRepository = (SeaRepository) Arrays.stream(repositories)
          .filter(r -> r instanceof SeaRepository)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Sea repository not provided at runtime"));
      return stationaryMarineLocationFromMap(stationaryMarineLocationTranslator, properties, row, runtimeException, seaRepository);
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

  private static MultiPointStationaryMarineLocation multiPointStationaryMarineLocationFromMap(MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, SeaRepository seaRepository) {
    return MultiPointStationaryMarineLocation.builder()
        .seaArea(objectFromMap(properties, multipointStationaryMarineLocationTranslator.getSeaArea(), seaRepository, row, runtimeException))
        .locations(multipointStationaryMarineLocationTranslator.getLocationTranslators().stream()
            .map(marineInstrumentLocationTranslator -> marineInstrumentLocationFromMap(marineInstrumentLocationTranslator, properties, row, runtimeException))
            .toList())
        .build();
  }

  private static MobileMarineLocation mobileMarineLocationFromMap(MobileMarineLocationTranslator mobileMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, ShipRepository shipRepository, SeaRepository seaRepository) {
    return MobileMarineLocation.builder()
        .locationDerivationDescription(stringFromMap(properties, mobileMarineLocationTranslator.getLocationDerivationDescription()))
        .vessel(objectFromMap(properties, mobileMarineLocationTranslator.getVessel(), shipRepository, row, runtimeException))
        .seaArea(objectFromMap(properties, mobileMarineLocationTranslator.getSeaArea(), seaRepository, row, runtimeException))
        .build();
  }

  private static StationaryMarineLocation stationaryMarineLocationFromMap(StationaryMarineLocationTranslator stationaryMarineLocationTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, SeaRepository seaRepository) {
    return StationaryMarineLocation.builder()
        .deploymentLocation(marineInstrumentLocationFromMap(stationaryMarineLocationTranslator.getDeploymentLocationTranslator(), properties, row, runtimeException))
        .recoveryLocation(marineInstrumentLocationFromMap(stationaryMarineLocationTranslator.getRecoveryLocationTranslator(), properties, row, runtimeException))
        .seaArea(objectFromMap(properties, stationaryMarineLocationTranslator.getSeaArea(), seaRepository, row, runtimeException))
        .build();
  }

  private static SoundClipsPackage soundClipsPackageFromMap(SoundClipsPackageTranslator soundClipsPackageTranslator, Map<String, ValueWithColumnNumber> properties,
      int row, RuntimeException runtimeException, PersonRepository personRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository,
      PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SeaRepository seaRepository, ShipRepository shipRepository)
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
        .datasetPackager(objectFromMap(properties, soundClipsPackageTranslator.getDatasetPackager(), personRepository, row, runtimeException))
        .projects(delimitedObjectsFromMap(properties, soundClipsPackageTranslator.getProjects(), row, runtimeException, projectRepository))
        .publicReleaseDate(localDateFromMap(properties, soundClipsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(delimitedObjectsFromMap(properties, soundClipsPackageTranslator.getScientists(), row, runtimeException, personRepository))
        .sponsors(delimitedObjectsFromMap(properties, soundClipsPackageTranslator.getSponsors(), row, runtimeException, organizationRepository))
        .funders(delimitedObjectsFromMap(properties, soundClipsPackageTranslator.getFunders(), row, runtimeException, organizationRepository))
        .platform(objectFromMap(properties, soundClipsPackageTranslator.getPlatform(), platformRepository, row, runtimeException))
        .instrument(objectFromMap(properties, soundClipsPackageTranslator.getInstrument(), instrumentRepository, row, runtimeException))
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
        .locationDetail(locationDetailFromMap(soundClipsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException, seaRepository, shipRepository))
        .build();
  }

  private static SoundLevelMetricsPackage soundLevelMetricsPackageFromMap(SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator, Map<String, ValueWithColumnNumber> properties,
      int row, RuntimeException runtimeException, PersonRepository personRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository,
      PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SeaRepository seaRepository, ShipRepository shipRepository) throws TranslationException {
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
        .datasetPackager(objectFromMap(properties, soundLevelMetricsPackageTranslator.getDatasetPackager(), personRepository, row, runtimeException))
        .projects(delimitedObjectsFromMap(properties, soundLevelMetricsPackageTranslator.getProjects(), row, runtimeException, projectRepository))
        .publicReleaseDate(localDateFromMap(properties, soundLevelMetricsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(delimitedObjectsFromMap(properties, soundLevelMetricsPackageTranslator.getScientists(), row, runtimeException, personRepository))
        .sponsors(delimitedObjectsFromMap(properties, soundLevelMetricsPackageTranslator.getSponsors(), row, runtimeException, organizationRepository))
        .funders(delimitedObjectsFromMap(properties, soundLevelMetricsPackageTranslator.getFunders(), row, runtimeException, organizationRepository))
        .platform(objectFromMap(properties, soundLevelMetricsPackageTranslator.getPlatform(), platformRepository, row, runtimeException))
        .instrument(objectFromMap(properties, soundLevelMetricsPackageTranslator.getInstrument(), instrumentRepository, row, runtimeException))
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
        .qualityAnalyst(objectFromMap(properties, qualityControlDetailTranslator.getQualityAnalyst(), personRepository, row, runtimeException))
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
        .locationDetail(locationDetailFromMap(soundLevelMetricsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException, seaRepository, shipRepository))
        .build();
  }

  private static SoundPropagationModelsPackage soundPropagationModelsPackageFromMap(SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator,
      Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, PersonRepository personRepository, ProjectRepository projectRepository,
      OrganizationRepository organizationRepository, PlatformRepository platformRepository, InstrumentRepository instrumentRepository, SeaRepository seaRepository,
      ShipRepository shipRepository) throws TranslationException {
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
        .datasetPackager(objectFromMap(properties, soundPropagationModelsPackageTranslator.getDatasetPackager(), personRepository, row, runtimeException))
        .projects(delimitedObjectsFromMap(properties, soundPropagationModelsPackageTranslator.getProjects(), row, runtimeException, projectRepository))
        .publicReleaseDate(localDateFromMap(properties, soundPropagationModelsPackageTranslator.getPublicReleaseDate(), row, runtimeException))
        .scientists(delimitedObjectsFromMap(properties, soundPropagationModelsPackageTranslator.getScientists(), row, runtimeException, personRepository))
        .sponsors(delimitedObjectsFromMap(properties, soundPropagationModelsPackageTranslator.getSponsors(), row, runtimeException, organizationRepository))
        .funders(delimitedObjectsFromMap(properties, soundPropagationModelsPackageTranslator.getFunders(), row, runtimeException, organizationRepository))
        .platform(objectFromMap(properties, soundPropagationModelsPackageTranslator.getPlatform(), platformRepository, row, runtimeException))
        .instrument(objectFromMap(properties, soundPropagationModelsPackageTranslator.getInstrument(), instrumentRepository, row, runtimeException))
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
        .locationDetail(locationDetailFromMap(soundPropagationModelsPackageTranslator.getLocationDetailTranslator(), properties, row, runtimeException, seaRepository, shipRepository))
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

  private static Channel channelFromMap(ChannelTranslator channelTranslator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, SensorRepository sensorRepository)
      throws TranslationException {
    return Channel.builder()
        .sensor(objectFromMap(properties, channelTranslator.getSensor(), sensorRepository, row, runtimeException))
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

  private static List<Channel> channelsFromMap(List<ChannelTranslator> channelTranslators, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException, SensorRepository sensorRepository) {
    return channelTranslators.stream()
        .map(channelTranslator -> {
          try {
            return channelFromMap(channelTranslator, properties, row, runtimeException, sensorRepository);
          } catch (TranslationException e) {
            runtimeException.addSuppressed(e);
            return null;
          }
        }).filter(Objects::nonNull)
        .toList();
  }
}
