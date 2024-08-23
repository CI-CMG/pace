package edu.colorado.cires.pace.packaging;

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
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
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
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.other.OtherSensor;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.passivePacker.data.AbstractObjectWithName;
import edu.colorado.cires.passivePacker.data.PassivePackerAudioSensor;
import edu.colorado.cires.passivePacker.data.PassivePackerCalibrationInfo;
import edu.colorado.cires.passivePacker.data.PassivePackerChannel;
import edu.colorado.cires.passivePacker.data.PassivePackerDatasetDetails;
import edu.colorado.cires.passivePacker.data.PassivePackerDeployment;
import edu.colorado.cires.passivePacker.data.PassivePackerDepthSensor;
import edu.colorado.cires.passivePacker.data.PassivePackerDutyCycle;
import edu.colorado.cires.passivePacker.data.PassivePackerGain;
import edu.colorado.cires.passivePacker.data.PassivePackerLocation;
import edu.colorado.cires.passivePacker.data.PassivePackerMarineInstrumentLocation;
import edu.colorado.cires.passivePacker.data.PassivePackerMobileMarineLocation;
import edu.colorado.cires.passivePacker.data.PassivePackerMultipointStationaryMarineLocation;
import edu.colorado.cires.passivePacker.data.PassivePackerOtherSensor;
import edu.colorado.cires.passivePacker.data.PassivePackerPackage;
import edu.colorado.cires.passivePacker.data.PassivePackerPerson;
import edu.colorado.cires.passivePacker.data.PassivePackerQualityDetails;
import edu.colorado.cires.passivePacker.data.PassivePackerQualityEntry;
import edu.colorado.cires.passivePacker.data.PassivePackerSampleRate;
import edu.colorado.cires.passivePacker.data.PassivePackerSamplingDetails;
import edu.colorado.cires.passivePacker.data.PassivePackerSensor;
import edu.colorado.cires.passivePacker.data.PassivePackerSensorType;
import edu.colorado.cires.passivePacker.data.PassivePackerStationaryMarineLocation;
import edu.colorado.cires.passivePacker.data.PassivePackerStationaryTerrestrialLocation;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.stream.IntStreams;

public class PassivePackerFactory {
  
  private final PersonRepository personRepository;
  private final OrganizationRepository organizationRepository;
  private final SensorRepository sensorRepository;
  private final DetectionTypeRepository detectionTypeRepository;

  public PassivePackerFactory(PersonRepository personRepository, OrganizationRepository organizationRepository, SensorRepository sensorRepository,
      DetectionTypeRepository detectionTypeRepository) {
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
    this.sensorRepository = sensorRepository;
    this.detectionTypeRepository = detectionTypeRepository;
  }

  public PassivePackerPackage createPackage(Package aPackage) throws NotFoundException, DatastoreException {
    PassivePackerPackage passivePackerPackage = PassivePackerPackage.builder()
        .dataCollectionName(aPackage.getPackageId())
        .publishDate(String.valueOf(aPackage.getPublicReleaseDate()))
        .projectName(aPackage.getProjects())
        .deploymentName(aPackage.getDeploymentId())
        .deploymentAlias(aPackage.getAlternateDeploymentName())
        .site(aPackage.getSiteOrCruiseName())
        .siteAliases(Collections.singletonList(aPackage.getAlternateSiteName()))
        .title(aPackage.getDeploymentTitle())
        .purpose(aPackage.getDeploymentPurpose())
        .description(aPackage.getDeploymentDescription())
        .platformName(aPackage.getPlatform())
        .instrumentType(aPackage.getInstrument())
        .metadataAuthor(getPerson(aPackage.getDatasetPackager()))
        .scientists(getPeople(aPackage.getScientists()))
        .sponsors(getOrganizations(aPackage.getSponsors()))
        .funders(getOrganizations(aPackage.getFunders()))
        .calibrationInfo(getCalibrationInfo(aPackage))
        .datasetDetails(getDatasetDetails(aPackage))
        .deployment(getDeployment(aPackage))
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      Map<PassivePackerSensorType, List<PassivePackerSensor>> sensors = getSensors(audioDataPackage);
      passivePackerPackage = passivePackerPackage.toBuilder()
          .sensors(sensors)
          .channels(getChannels(audioDataPackage, sensors))
          .qualityDetails(getQualityDetails(audioDataPackage))
          .instrumentId(audioDataPackage.getInstrumentId())
          .build();
    } else if (aPackage instanceof SoundLevelMetricsPackage soundLevelMetricsPackage) {
      passivePackerPackage = passivePackerPackage.toBuilder()
          .qualityDetails(getQualityDetails(soundLevelMetricsPackage))
          .build();
    } else if (aPackage instanceof DetectionsPackage detectionsPackage) {
      passivePackerPackage = passivePackerPackage.toBuilder()
          .qualityDetails(getQualityDetails(detectionsPackage))
          .build();
    }

    return passivePackerPackage;
  }

  private PassivePackerDeployment getDeployment(Package aPackage) {
    PassivePackerDeployment deployment = PassivePackerDeployment.builder()
        .location(getLocation(aPackage.getLocationDetail()))
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      deployment = deployment.toBuilder()
          .audioStart(getAudioDateTime(audioDataPackage.getAudioStartTime()))
          .audioEnd(getAudioDateTime(audioDataPackage.getAudioEndTime()))
          .deploymentTime(serializeDateTime(audioDataPackage.getDeploymentTime()))
          .recoveryTime(serializeDateTime(audioDataPackage.getRecoveryTime()))
          .build();
    } else if (aPackage instanceof SoundLevelMetricsPackage soundLevelMetricsPackage) {
      deployment = deployment.toBuilder()
          .audioStart(getAudioDateTime(soundLevelMetricsPackage.getAudioStartTime()))
          .audioEnd(getAudioDateTime(soundLevelMetricsPackage.getAudioEndTime()))
          .build();
    } else if (aPackage instanceof SoundPropagationModelsPackage soundPropagationModelsPackage) {
      deployment = deployment.toBuilder()
          .audioStart(getAudioDateTime(soundPropagationModelsPackage.getAudioStartTime()))
          .audioEnd(getAudioDateTime(soundPropagationModelsPackage.getAudioEndTime()))
          .build();
    } else if (aPackage instanceof SoundClipsPackage soundClipsPackage) {
      deployment = deployment.toBuilder()
          .audioStart(getAudioDateTime(soundClipsPackage.getAudioStartTime()))
          .audioEnd(getAudioDateTime(soundClipsPackage.getAudioEndTime()))
          .build();
    }

    return deployment;
  }

  private String getAudioDateTime(LocalDateTime audioStartTime) {
    String serializedDate = serializeDateTime(audioStartTime);
    if (serializedDate == null) {
      return "";
    }
    
    return serializedDate;
  }

  private PassivePackerLocation getLocation(LocationDetail locationDetail) {
    PassivePackerLocation location = PassivePackerLocation.builder()
        .deployType(getDeployType(locationDetail))
        .build();
    
    if (locationDetail instanceof StationaryMarineLocation stationaryMarineLocation) {
      MarineInstrumentLocation deploymentLocation = stationaryMarineLocation.getDeploymentLocation();
      MarineInstrumentLocation recoveryLocation = stationaryMarineLocation.getRecoveryLocation();
      return location.toInheritingType(PassivePackerStationaryMarineLocation.builder()).toBuilder()
          .seaArea(stationaryMarineLocation.getSeaArea())
          .deployBottomDepth(String.valueOf(deploymentLocation.getSeaFloorDepth()))
          .deployInstrumentDepth(String.valueOf(deploymentLocation.getInstrumentDepth()))
          .deployLat(String.valueOf(deploymentLocation.getLatitude()))
          .deployLon(String.valueOf(deploymentLocation.getLongitude()))
          .recoverBottomDepth(String.valueOf(recoveryLocation.getSeaFloorDepth()))
          .recoverInstrumentDepth(String.valueOf(recoveryLocation.getInstrumentDepth()))
          .recoverLat(String.valueOf(recoveryLocation.getLatitude()))
          .recoverLon(String.valueOf(recoveryLocation.getLongitude()))
          .build();
    }
    
    if (locationDetail instanceof MobileMarineLocation mobileMarineLocation) {
      return location.toInheritingType(PassivePackerMobileMarineLocation.builder()).toBuilder()
          .seaArea(mobileMarineLocation.getSeaArea())
          .deployShip(mobileMarineLocation.getVessel())
          .positionDetails(mobileMarineLocation.getLocationDerivationDescription())
          .files(mobileMarineLocation.getFileList().stream()
              .map(Path::toString)
              .collect(Collectors.joining(",")))
          .build();
    }
    
    if (locationDetail instanceof MultiPointStationaryMarineLocation multiPointStationaryMarineLocation) {
      return location.toInheritingType(PassivePackerMultipointStationaryMarineLocation.builder()).toBuilder()
          .seaArea(multiPointStationaryMarineLocation.getSeaArea())
          .locations(getLocations(multiPointStationaryMarineLocation.getLocations()))
          .build();
    }
    
    if (locationDetail instanceof StationaryTerrestrialLocation stationaryTerrestrialLocation) {
      return location.toInheritingType(PassivePackerStationaryTerrestrialLocation.builder()).toBuilder()
          .lat(String.valueOf(stationaryTerrestrialLocation.getLatitude()))
          .lon(String.valueOf(stationaryTerrestrialLocation.getLongitude()))
          .instrumentElevation(String.valueOf(stationaryTerrestrialLocation.getInstrumentElevation()))
          .surfaceElevation(String.valueOf(stationaryTerrestrialLocation.getSurfaceElevation()))
          .build();
    }
    
    return null;
  }

  private List<PassivePackerMarineInstrumentLocation> getLocations(List<MarineInstrumentLocation> locations) {
    return locations.stream()
        .map(this::getMarineInstrumentLocation)
        .toList();
  }

  private PassivePackerMarineInstrumentLocation getMarineInstrumentLocation(MarineInstrumentLocation marineInstrumentLocation) {
    return PassivePackerMarineInstrumentLocation.builder()
        .latitude(String.valueOf(marineInstrumentLocation.getLatitude()))
        .longitude(String.valueOf(marineInstrumentLocation.getLongitude()))
        .instrumentDepth(String.valueOf(marineInstrumentLocation.getInstrumentDepth()))
        .bottomDepth(String.valueOf(marineInstrumentLocation.getSeaFloorDepth()))
        .build();
  }

  private String getDeployType(LocationDetail locationDetail) {
    if (locationDetail instanceof StationaryMarineLocation) {
      return "Stationary Marine";
    }
    if (locationDetail instanceof MobileMarineLocation) {
      return "Mobile Marine";
    }
    
    if (locationDetail instanceof MultiPointStationaryMarineLocation) {
      return "Multipoint Stationary Marine";
    }
    
    if (locationDetail instanceof StationaryTerrestrialLocation) {
      return "Stationary Terrestrial";
    }
    
    return null;
  }

  private PassivePackerDatasetDetails getDatasetDetails(Package aPackage) throws NotFoundException, DatastoreException {
    PassivePackerDatasetDetails datasetDetails = PassivePackerDatasetDetails.builder()
        .subType(subtypeFromPackage(aPackage))
        .sourcePath(String.valueOf(aPackage.getSourcePath()))
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      datasetDetails = datasetDetails.toBuilder()
          .dataComment(audioDataPackage.getComments())
          .build();
    } else if (aPackage instanceof SoundClipsPackage soundClipsPackage) {
      datasetDetails =  datasetDetails.toBuilder()
          .dataFiles(String.valueOf(soundClipsPackage.getSourcePath()))
          .softwareName(soundClipsPackage.getSoftwareNames())
          .softwareVersion(soundClipsPackage.getSoftwareVersions())
          .protocolReference(soundClipsPackage.getSoftwareProtocolCitation())
          .processingStatement(soundClipsPackage.getSoftwareProcessingDescription())
          .softwareStatement(soundClipsPackage.getSoftwareDescription())
          .clipsStart(serializeDateTime(soundClipsPackage.getStartTime()))
          .clipsEnd(serializeDateTime(soundClipsPackage.getEndTime()))
          .build();
    } else if (aPackage instanceof SoundLevelMetricsPackage soundLevelMetricsPackage) {
      datasetDetails = datasetDetails.toBuilder()
          .dataFiles(String.valueOf(soundLevelMetricsPackage.getSourcePath()))
          .analysisTimeZone(String.valueOf(soundLevelMetricsPackage.getAnalysisTimeZone()))
          .analysisEffort(String.valueOf(soundLevelMetricsPackage.getAnalysisEffort()))
          .minAnalysisFrequency(String.valueOf(soundLevelMetricsPackage.getMinFrequency()))
          .maxAnalysisFrequency(String.valueOf(soundLevelMetricsPackage.getMaxFrequency()))
          .analysisSampleRate(String.valueOf(soundLevelMetricsPackage.getSampleRate()))
          .softwareName(soundLevelMetricsPackage.getSoftwareNames())
          .softwareVersion(soundLevelMetricsPackage.getSoftwareVersions())
          .protocolReference(soundLevelMetricsPackage.getSoftwareProtocolCitation())
          .processingStatement(soundLevelMetricsPackage.getSoftwareProcessingDescription())
          .softwareStatement(soundLevelMetricsPackage.getSoftwareDescription())
          .analysisStart(serializeDateTime(soundLevelMetricsPackage.getStartTime()))
          .analysisEnd(serializeDateTime(soundLevelMetricsPackage.getEndTime()))
          .build();
    } else if (aPackage instanceof SoundPropagationModelsPackage soundPropagationModelsPackage) {
      datasetDetails = datasetDetails.toBuilder()
          .dataFiles(String.valueOf(soundPropagationModelsPackage.getSourcePath()))
          .softwareName(soundPropagationModelsPackage.getSoftwareNames())
          .softwareVersion(soundPropagationModelsPackage.getSoftwareVersions())
          .protocolReference(soundPropagationModelsPackage.getSoftwareProtocolCitation())
          .processingStatement(soundPropagationModelsPackage.getSoftwareProcessingDescription())
          .softwareStatement(soundPropagationModelsPackage.getSoftwareDescription())
          .modelStart(serializeDateTime(soundPropagationModelsPackage.getStartTime()))
          .modelEnd(serializeDateTime(soundPropagationModelsPackage.getEndTime()))
          .frequency(String.valueOf(soundPropagationModelsPackage.getModeledFrequency()))
          .build();
    } else if (aPackage instanceof DetectionsPackage detectionsPackage) {
      String sourceName = detectionsPackage.getSoundSource();
      String species = null;
      String scientificName = null;
      
      if (sourceName != null) {
        DetectionType detectionType = detectionTypeRepository.getByUniqueField(sourceName);
        
        species = detectionType.getSource();
        scientificName = detectionType.getScienceName();
      }
      
      datasetDetails = datasetDetails.toBuilder()
          .dataFiles(String.valueOf(detectionsPackage.getSourcePath()))
          .analysisStart(serializeDateTime(detectionsPackage.getStartTime()))
          .analysisEnd(serializeDateTime(detectionsPackage.getEndTime()))
          .analysisTimeZone(String.valueOf(detectionsPackage.getAnalysisTimeZone()))
          .analysisEffort(String.valueOf(detectionsPackage.getAnalysisEffort()))
          .minAnalysisFrequency(String.valueOf(detectionsPackage.getMinFrequency()))
          .maxAnalysisFrequency(String.valueOf(detectionsPackage.getMaxFrequency()))
          .analysisSampleRate(String.valueOf(detectionsPackage.getSampleRate()))
          .species(species)
          .scientificName(scientificName)
          .softwareName(detectionsPackage.getSoftwareNames())
          .softwareVersion(detectionsPackage.getSoftwareVersions())
          .protocolReference(detectionsPackage.getSoftwareProtocolCitation())
          .processingStatement(detectionsPackage.getSoftwareProcessingDescription())
          .softwareStatement(detectionsPackage.getSoftwareDescription())
          .build();
    }

    return datasetDetails;
  }

  private String subtypeFromPackage(Package aPackage) {
    if (aPackage instanceof AudioPackage) {
      return "Audio";
    }
    
    if (aPackage instanceof CPODPackage) {
      return "C-POD";
    }
    
    if (aPackage instanceof SoundLevelMetricsPackage) {
      return "Sound Level Metrics";
    }
    
    if (aPackage instanceof SoundPropagationModelsPackage) {
      return "Sound Propagation Models";
    }
    
    if (aPackage instanceof DetectionsPackage) {
      return "Detections";
    }
    
    if (aPackage instanceof SoundClipsPackage) {
      return "Sound Clips";
    }
    
    return null;
  }

  private PassivePackerCalibrationInfo getCalibrationInfo(Package aPackage) {
    String calState;
    LocalDate calDate = null;
    LocalDate calDate2 = null;
    
    if (aPackage.getPreDeploymentCalibrationDate() != null && aPackage.getPostDeploymentCalibrationDate() == null) {
      calState = "Calibrated Pre-Deployment";
      calDate = aPackage.getPreDeploymentCalibrationDate();
    } else if (aPackage.getPostDeploymentCalibrationDate() != null && aPackage.getPreDeploymentCalibrationDate() == null) {
      calState = "Calibrated Post-Deployment";
      calDate = aPackage.getPostDeploymentCalibrationDate();
    } else if (aPackage.getPreDeploymentCalibrationDate() == null) {
      calState = "Factory Calibrated";
    } else {
      calState = "Calibrated Pre & Post Deployment";
      calDate = aPackage.getPreDeploymentCalibrationDate();
      calDate2 = aPackage.getPostDeploymentCalibrationDate();
    }

    Path calibrationDocumentsPath = aPackage.getCalibrationDocumentsPath();
    PassivePackerCalibrationInfo calibrationInfo = PassivePackerCalibrationInfo.builder()
        .calDate(calDate == null ? null : String.valueOf(calDate))
        .calState(calState)
        .calDocsPath(calibrationDocumentsPath == null ? "" : calibrationDocumentsPath.toString())
        .comment(aPackage.getCalibrationDescription())
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      calibrationInfo = calibrationInfo.toBuilder()
          .sensitivity(String.valueOf(audioDataPackage.getHydrophoneSensitivity()))
          .frequency(String.valueOf(audioDataPackage.getFrequencyRange()))
          .gain(String.valueOf(audioDataPackage.getGain()))
          .calDate2(calDate2 == null ? null : String.valueOf(calDate2))
          .build();
    } else if (aPackage instanceof SoundClipsPackage || aPackage instanceof SoundLevelMetricsPackage 
        || aPackage instanceof SoundPropagationModelsPackage || aPackage instanceof DetectionsPackage) {
      calibrationInfo = calibrationInfo.toBuilder()
          .calState(aPackage.getPreDeploymentCalibrationDate() != null ? "Calibration applied before processing" : "Not calibrated / not applicable")
          .calDate(null)
          .build();
    }

    return calibrationInfo;
  }

  private List<AbstractObjectWithName> getPeople(List<String> names) throws NotFoundException, DatastoreException {
    List<AbstractObjectWithName> people = new ArrayList<>(0);

    for (String name : names) {
      PassivePackerPerson person = getPerson(name);
      people.add(AbstractObjectWithName.builder()
              .uuid(person.getUuid())
              .name(person.getName())
          .build());
    }
    
    return people;
  }
  
  private List<AbstractObjectWithName> getOrganizations(List<String> names) throws NotFoundException, DatastoreException {
    List<AbstractObjectWithName> organizations = new ArrayList<>(0);

    for (String name : names) {
      Organization organization = organizationRepository.getByUniqueField(name);
      organizations.add(AbstractObjectWithName.builder()
              .uuid(organization.getUuid())
              .name(organization.getName())
          .build());
    }
    
    return organizations;
  }

  private PassivePackerPerson getPerson(String name) throws NotFoundException, DatastoreException {
    Person person = personRepository.getByUniqueField(name);
    return PassivePackerPerson.builder()
        .uuid(person.getUuid())
        .name(person.getName())
        .position(person.getPosition())
        .organization(person.getOrganization())
        .street(person.getStreet())
        .city(person.getCity())
        .state(person.getState())
        .zip(person.getZip())
        .country(person.getCountry())
        .phone(person.getPhone())
        .email(person.getEmail())
        .build();
  }

  private Map<PassivePackerSensorType, List<PassivePackerSensor>> getSensors(AudioDataPackage audioDataPackage) {
    List<PackageSensor<String>> sensors = new ArrayList<>(audioDataPackage.getSensors());
    return IntStreams.range(sensors.size()).boxed()
        .map(i -> {
          try {
            return getSensor(sensors.get(i), i + 1);
          } catch (NotFoundException | DatastoreException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.groupingBy((s -> {
          if (s instanceof PassivePackerAudioSensor) {
            return PassivePackerSensorType.AUDIO;
          }
          
          if (s instanceof PassivePackerOtherSensor) {
            return PassivePackerSensorType.OTHER;
          }
          
          return PassivePackerSensorType.DEPTH;
        })));
  }

  private PassivePackerSensor getSensor(PackageSensor<String> packageSensor, int listPosition) throws NotFoundException, DatastoreException {
    Sensor sensor = sensorRepository.getByUniqueField(packageSensor.getSensor());
    Position position = packageSensor.getPosition();
    
    PassivePackerSensor passivePackerSensor = PassivePackerDepthSensor.builder()
        .type(getStringSensorType(sensor))
        .name(sensor.getName())
        .number(String.valueOf(listPosition))
        .positionX(String.valueOf(position.getX()))
        .positionY(String.valueOf(position.getY()))
        .positionZ(String.valueOf(position.getZ()))
        .description(sensor.getDescription())
        .id(sensor.getId())
        .build();
    
    if (sensor instanceof AudioSensor audioSensor) {
      passivePackerSensor = passivePackerSensor.toInheritingType(PassivePackerAudioSensor.builder()).toBuilder()
          .hydroId(audioSensor.getHydrophoneId())
          .preId(audioSensor.getPreampId())
          .build();
    }
    
    if (sensor instanceof OtherSensor otherSensor) {
      passivePackerSensor = passivePackerSensor.toInheritingType(PassivePackerOtherSensor.builder()).toBuilder()
          .properties(otherSensor.getProperties())
          .sensorType(otherSensor.getSensorType())
          .build();
    }
    
    return passivePackerSensor;
  }

  private String getStringSensorType(Sensor sensor) {
    if (sensor instanceof AudioSensor) {
      return "Audio Sensor";
    }
    
    if (sensor instanceof DepthSensor) {
      return "Depth Sensor";
    }
    
    if (sensor instanceof OtherSensor) {
      return "Other Sensor";
    }
    
    return null;
  }

  private PassivePackerQualityDetails getQualityDetails(DataQuality<String> dataQuality) throws NotFoundException, DatastoreException {
    Person person = personRepository.getByUniqueField(dataQuality.getQualityAnalyst());
    return PassivePackerQualityDetails.builder()
        .analyst(dataQuality.getQualityAnalyst())
        .analystUuid(person.getUuid())
        .description(dataQuality.getQualityAssessmentDescription())
        .method(dataQuality.getQualityAnalysisMethod())
        .objectives(dataQuality.getQualityAnalysisObjectives())
        .qualityDetails(getQualityEntries(dataQuality.getQualityEntries()))
        .build();
  }

  private List<PassivePackerQualityEntry> getQualityEntries(List<DataQualityEntry> qualityEntries) {
    return qualityEntries.stream()
        .map(e -> {
          String channelNumbers = e.getChannelNumbers().stream()
              .map(String::valueOf)
              .collect(Collectors.joining(","));
          return (PassivePackerQualityEntry) PassivePackerQualityEntry.builder()
              .start(serializeDateTime(e.getStartTime()))
              .end(serializeDateTime(e.getEndTime()))
              .quality(e.getQualityLevel() == null ? null : e.getQualityLevel().getName())
              .lowFreq(String.valueOf(e.getMinFrequency()))
              .highFreq(String.valueOf(e.getMaxFrequency()))
              .comments(e.getComments())
              .channels(
                  StringUtils.isBlank(channelNumbers) ? "1" : channelNumbers
              ).build();
        })
        .toList();
  }

  private Map<Integer, PassivePackerChannel> getChannels(AudioDataPackage audioDataPackage, Map<PassivePackerSensorType, List<PassivePackerSensor>> sensors) {
    List<Channel<String>> channels = audioDataPackage.getChannels();
    
    return IntStream.range(0, channels.size()).boxed()
        .collect(Collectors.toMap(
            i -> i + 1,
            i -> {
              Channel<String> channel = channels.get(i);
              return PassivePackerChannel.builder()
                  .channelStart(serializeDateTime(channel.getStartTime()))
                  .channelEnd(serializeDateTime(channel.getEndTime()))
                  .samplingDetails(getSamplingDetails(channel))
                  .build();
            }
        ));
  }
  
  private PassivePackerSamplingDetails getSamplingDetails(Channel<String> channel) {
    return PassivePackerSamplingDetails.builder()
        .sampling(getSampling(channel.getSampleRates()))
        .gain(getGains(channel.getGains()))
        .dutyCycle(getDutyCycles(channel.getDutyCycles()))
        .build();
  }

  private List<PassivePackerDutyCycle> getDutyCycles(List<DutyCycle> dutyCycles) {
    return dutyCycles.stream()
        .map(dc -> (PassivePackerDutyCycle) PassivePackerDutyCycle.builder()
            .start(serializeDateTime(dc.getStartTime()))
            .end(serializeDateTime(dc.getEndTime()))
            .duration(String.valueOf(dc.getDuration()))
            .interval(String.valueOf(dc.getInterval()))
            .build())
        .toList();
  }

  private List<PassivePackerGain> getGains(List<Gain> gains) {
    return gains.stream()
        .map(g -> (PassivePackerGain) PassivePackerGain.builder()
            .start(serializeDateTime(g.getStartTime()))
            .end(serializeDateTime(g.getEndTime()))
            .gain(String.valueOf(g.getGain()))
            .build())
        .toList();
  }

  private List<PassivePackerSampleRate> getSampling(List<SampleRate> sampleRates) {
    return sampleRates.stream()
        .map(sr -> (PassivePackerSampleRate) PassivePackerSampleRate.builder()
            .start(serializeDateTime(sr.getStartTime()))
            .end(serializeDateTime(sr.getEndTime()))
            .sampleRate(String.valueOf(sr.getSampleRate()))
            .sampleBits(String.valueOf(sr.getSampleBits()))
            .build())
        .toList();
  }
  
  private String serializeDateTime(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    
    return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

}
