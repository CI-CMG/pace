package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.DetailedAudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.DetailedCPODPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.detections.DetailedDetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.DetailedSoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.DetailedSoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.DetailedSoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.passivePacker.data.AbstractObjectWithName;
import java.util.ArrayList;
import java.util.List;

public final class PackageInflator {
  
  private final PersonRepository personRepository;
  private final OrganizationRepository organizationRepository;
  private final SensorRepository sensorRepository;
  private final DetectionTypeRepository detectionTypeRepository;

  public PackageInflator(PersonRepository personRepository, OrganizationRepository organizationRepository,
      SensorRepository sensorRepository,
      DetectionTypeRepository detectionTypeRepository) {
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
    this.sensorRepository = sensorRepository;
    this.detectionTypeRepository = detectionTypeRepository;
  }

  public DetailedPackage process(Package aPackage) throws NotFoundException, DatastoreException {
    if (aPackage instanceof AudioPackage audioPackage) {
      return process(audioPackage);
    } else if (aPackage instanceof CPODPackage cpodPackage) {
      return process(cpodPackage);
    } else if (aPackage instanceof DetectionsPackage detectionsPackage) {
      return process(detectionsPackage);
    } else if (aPackage instanceof SoundClipsPackage soundClipsPackage) {
      return process(soundClipsPackage);
    } else if (aPackage instanceof SoundLevelMetricsPackage soundLevelMetricsPackage) {
      return process(soundLevelMetricsPackage);
    } else if (aPackage instanceof SoundPropagationModelsPackage soundPropagationModelsPackage) {
      return process(soundPropagationModelsPackage);
    } else {
      throw new IllegalStateException(String.format(
          "Invalid package type: %s", aPackage.getClass().getSimpleName()
      ));
    }
  }
  
  private DetailedAudioPackage process(AudioPackage audioPackage) throws NotFoundException, DatastoreException {
    return DetailedAudioPackage.builder()
        .uuid(audioPackage.getUuid())
        .visible(audioPackage.isVisible())
        .processingLevel(audioPackage.getProcessingLevel())
        .temperaturePath(audioPackage.getTemperaturePath())
        .biologicalPath(audioPackage.getBiologicalPath())
        .otherPath(audioPackage.getOtherPath())
        .documentsPath(audioPackage.getDocumentsPath())
        .calibrationDocumentsPath(audioPackage.getCalibrationDocumentsPath())
        .sourcePath(audioPackage.getSourcePath())
        .dataCollectionName(audioPackage.getDataCollectionName())
        .siteOrCruiseName(audioPackage.getSiteOrCruiseName())
        .deploymentId(audioPackage.getDeploymentId())
        .publicReleaseDate(audioPackage.getPublicReleaseDate())
        .locationDetail(audioPackage.getLocationDetail())
        .deploymentTitle(audioPackage.getDeploymentTitle())
        .deploymentPurpose(audioPackage.getDeploymentPurpose())
        .deploymentDescription(audioPackage.getDeploymentDescription())
        .alternateSiteName(audioPackage.getAlternateSiteName())
        .alternateDeploymentName(audioPackage.getAlternateDeploymentName())
        .startTime(audioPackage.getStartTime())
        .endTime(audioPackage.getEndTime())
        .preDeploymentCalibrationDate(audioPackage.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(audioPackage.getPostDeploymentCalibrationDate())
        .calibrationDescription(audioPackage.getCalibrationDescription())
        .datasetPackager(getObject(personRepository, audioPackage.getDatasetPackager()))
        .scientists(getBaseObjects(personRepository, audioPackage.getScientists()))
        .projects(stringListToObjectList(audioPackage.getProjects()))
        .sponsors(getBaseObjects(organizationRepository, audioPackage.getSponsors()))
        .funders(getBaseObjects(organizationRepository, audioPackage.getFunders()))
        .platform(audioPackage.getPlatform())
        .instrument(audioPackage.getInstrument())
        .instrumentId(audioPackage.getInstrumentId())
        .deploymentTime(audioPackage.getDeploymentTime())
        .recoveryTime(audioPackage.getRecoveryTime())
        .comments(audioPackage.getComments())
        .sensors(getSensors(audioPackage.getSensors()))
        .channels(getChannels(audioPackage.getChannels()))
        .hydrophoneSensitivity(audioPackage.getHydrophoneSensitivity())
        .frequencyRange(audioPackage.getFrequencyRange())
        .gain(audioPackage.getGain())
        .qualityAnalyst(getObject(personRepository, audioPackage.getQualityAnalyst()))
        .qualityAnalysisObjectives(audioPackage.getQualityAnalysisObjectives())
        .qualityAnalysisMethod(audioPackage.getQualityAnalysisMethod())
        .qualityAssessmentDescription(audioPackage.getQualityAssessmentDescription())
        .qualityEntries(audioPackage.getQualityEntries())
        .build();
  }

  private DetailedCPODPackage process(CPODPackage cpodPackage) throws NotFoundException, DatastoreException {
    return DetailedCPODPackage.builder()
        .uuid(cpodPackage.getUuid())
        .visible(cpodPackage.isVisible())
        .processingLevel(cpodPackage.getProcessingLevel())
        .temperaturePath(cpodPackage.getTemperaturePath())
        .biologicalPath(cpodPackage.getBiologicalPath())
        .otherPath(cpodPackage.getOtherPath())
        .documentsPath(cpodPackage.getDocumentsPath())
        .calibrationDocumentsPath(cpodPackage.getCalibrationDocumentsPath())
        .sourcePath(cpodPackage.getSourcePath())
        .dataCollectionName(cpodPackage.getDataCollectionName())
        .siteOrCruiseName(cpodPackage.getSiteOrCruiseName())
        .deploymentId(cpodPackage.getDeploymentId())
        .publicReleaseDate(cpodPackage.getPublicReleaseDate())
        .locationDetail(cpodPackage.getLocationDetail())
        .deploymentTitle(cpodPackage.getDeploymentTitle())
        .deploymentPurpose(cpodPackage.getDeploymentPurpose())
        .deploymentDescription(cpodPackage.getDeploymentDescription())
        .alternateSiteName(cpodPackage.getAlternateSiteName())
        .alternateDeploymentName(cpodPackage.getAlternateDeploymentName())
        .startTime(cpodPackage.getStartTime())
        .endTime(cpodPackage.getEndTime())
        .preDeploymentCalibrationDate(cpodPackage.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(cpodPackage.getPostDeploymentCalibrationDate())
        .calibrationDescription(cpodPackage.getCalibrationDescription())
        .datasetPackager(getObject(personRepository, cpodPackage.getDatasetPackager()))
        .scientists(getBaseObjects(personRepository, cpodPackage.getScientists()))
        .projects(stringListToObjectList(cpodPackage.getProjects()))
        .sponsors(getBaseObjects(organizationRepository, cpodPackage.getSponsors()))
        .funders(getBaseObjects(organizationRepository, cpodPackage.getFunders()))
        .platform(cpodPackage.getPlatform())
        .instrument(cpodPackage.getInstrument())
        .instrumentId(cpodPackage.getInstrumentId())
        .deploymentTime(cpodPackage.getDeploymentTime())
        .recoveryTime(cpodPackage.getRecoveryTime())
        .comments(cpodPackage.getComments())
        .sensors(getSensors(cpodPackage.getSensors()))
        .channels(getChannels(cpodPackage.getChannels()))
        .hydrophoneSensitivity(cpodPackage.getHydrophoneSensitivity())
        .frequencyRange(cpodPackage.getFrequencyRange())
        .gain(cpodPackage.getGain())
        .qualityAnalyst(getObject(personRepository, cpodPackage.getQualityAnalyst()))
        .qualityAnalysisObjectives(cpodPackage.getQualityAnalysisObjectives())
        .qualityAnalysisMethod(cpodPackage.getQualityAnalysisMethod())
        .qualityAssessmentDescription(cpodPackage.getQualityAssessmentDescription())
        .qualityEntries(cpodPackage.getQualityEntries())
        .build();
  }

  private DetailedDetectionsPackage process(DetectionsPackage detectionsPackage) throws NotFoundException, DatastoreException {
    return DetailedDetectionsPackage.builder()
        .uuid(detectionsPackage.getUuid())
        .visible(detectionsPackage.isVisible())
        .processingLevel(detectionsPackage.getProcessingLevel())
        .temperaturePath(detectionsPackage.getTemperaturePath())
        .biologicalPath(detectionsPackage.getBiologicalPath())
        .otherPath(detectionsPackage.getOtherPath())
        .documentsPath(detectionsPackage.getDocumentsPath())
        .calibrationDocumentsPath(detectionsPackage.getCalibrationDocumentsPath())
        .sourcePath(detectionsPackage.getSourcePath())
        .dataCollectionName(detectionsPackage.getDataCollectionName())
        .siteOrCruiseName(detectionsPackage.getSiteOrCruiseName())
        .deploymentId(detectionsPackage.getDeploymentId())
        .publicReleaseDate(detectionsPackage.getPublicReleaseDate())
        .locationDetail(detectionsPackage.getLocationDetail())
        .deploymentTitle(detectionsPackage.getDeploymentTitle())
        .deploymentPurpose(detectionsPackage.getDeploymentPurpose())
        .deploymentDescription(detectionsPackage.getDeploymentDescription())
        .alternateSiteName(detectionsPackage.getAlternateSiteName())
        .alternateDeploymentName(detectionsPackage.getAlternateDeploymentName())
        .startTime(detectionsPackage.getStartTime())
        .endTime(detectionsPackage.getEndTime())
        .preDeploymentCalibrationDate(detectionsPackage.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(detectionsPackage.getPostDeploymentCalibrationDate())
        .calibrationDescription(detectionsPackage.getCalibrationDescription())
        .datasetPackager(getObject(personRepository, detectionsPackage.getDatasetPackager()))
        .scientists(getBaseObjects(personRepository, detectionsPackage.getScientists()))
        .projects(stringListToObjectList(detectionsPackage.getProjects()))
        .sponsors(getBaseObjects(organizationRepository, detectionsPackage.getSponsors()))
        .funders(getBaseObjects(organizationRepository, detectionsPackage.getFunders()))
        .platform(detectionsPackage.getPlatform())
        .instrument(detectionsPackage.getInstrument())
        .qualityAnalyst(getObject(personRepository, detectionsPackage.getQualityAnalyst()))
        .qualityAnalysisObjectives(detectionsPackage.getQualityAnalysisObjectives())
        .qualityAnalysisMethod(detectionsPackage.getQualityAnalysisMethod())
        .qualityAssessmentDescription(detectionsPackage.getQualityAssessmentDescription())
        .qualityEntries(detectionsPackage.getQualityEntries())
        .softwareNames(detectionsPackage.getSoftwareNames())
        .softwareVersions(detectionsPackage.getSoftwareVersions())
        .softwareProtocolCitation(detectionsPackage.getSoftwareProtocolCitation())
        .softwareDescription(detectionsPackage.getSoftwareDescription())
        .softwareProcessingDescription(detectionsPackage.getSoftwareProcessingDescription())
        .analysisTimeZone(detectionsPackage.getAnalysisTimeZone())
        .analysisEffort(detectionsPackage.getAnalysisEffort())
        .sampleRate(detectionsPackage.getSampleRate())
        .minFrequency(detectionsPackage.getMinFrequency())
        .maxFrequency(detectionsPackage.getMaxFrequency())
        .soundSource(getObject(detectionTypeRepository, detectionsPackage.getSoundSource()))
        .build();
  }

  private DetailedSoundClipsPackage process(SoundClipsPackage soundClipsPackage) throws NotFoundException, DatastoreException {
    return DetailedSoundClipsPackage.builder()
        .uuid(soundClipsPackage.getUuid())
        .visible(soundClipsPackage.isVisible())
        .processingLevel(soundClipsPackage.getProcessingLevel())
        .temperaturePath(soundClipsPackage.getTemperaturePath())
        .biologicalPath(soundClipsPackage.getBiologicalPath())
        .otherPath(soundClipsPackage.getOtherPath())
        .documentsPath(soundClipsPackage.getDocumentsPath())
        .calibrationDocumentsPath(soundClipsPackage.getCalibrationDocumentsPath())
        .sourcePath(soundClipsPackage.getSourcePath())
        .dataCollectionName(soundClipsPackage.getDataCollectionName())
        .siteOrCruiseName(soundClipsPackage.getSiteOrCruiseName())
        .deploymentId(soundClipsPackage.getDeploymentId())
        .publicReleaseDate(soundClipsPackage.getPublicReleaseDate())
        .locationDetail(soundClipsPackage.getLocationDetail())
        .deploymentTitle(soundClipsPackage.getDeploymentTitle())
        .deploymentPurpose(soundClipsPackage.getDeploymentPurpose())
        .deploymentDescription(soundClipsPackage.getDeploymentDescription())
        .alternateSiteName(soundClipsPackage.getAlternateSiteName())
        .alternateDeploymentName(soundClipsPackage.getAlternateDeploymentName())
        .startTime(soundClipsPackage.getStartTime())
        .endTime(soundClipsPackage.getEndTime())
        .preDeploymentCalibrationDate(soundClipsPackage.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(soundClipsPackage.getPostDeploymentCalibrationDate())
        .calibrationDescription(soundClipsPackage.getCalibrationDescription())
        .datasetPackager(getObject(personRepository, soundClipsPackage.getDatasetPackager()))
        .scientists(getBaseObjects(personRepository, soundClipsPackage.getScientists()))
        .projects(stringListToObjectList(soundClipsPackage.getProjects()))
        .sponsors(getBaseObjects(organizationRepository, soundClipsPackage.getSponsors()))
        .funders(getBaseObjects(organizationRepository, soundClipsPackage.getFunders()))
        .platform(soundClipsPackage.getPlatform())
        .instrument(soundClipsPackage.getInstrument())
        .softwareNames(soundClipsPackage.getSoftwareNames())
        .softwareVersions(soundClipsPackage.getSoftwareVersions())
        .softwareProtocolCitation(soundClipsPackage.getSoftwareProtocolCitation())
        .softwareDescription(soundClipsPackage.getSoftwareDescription())
        .softwareProcessingDescription(soundClipsPackage.getSoftwareProcessingDescription())
        .build();
  }

  private DetailedSoundLevelMetricsPackage process(SoundLevelMetricsPackage soundLevelMetricsPackage) throws NotFoundException, DatastoreException {
    return DetailedSoundLevelMetricsPackage.builder()
        .uuid(soundLevelMetricsPackage.getUuid())
        .visible(soundLevelMetricsPackage.isVisible())
        .processingLevel(soundLevelMetricsPackage.getProcessingLevel())
        .temperaturePath(soundLevelMetricsPackage.getTemperaturePath())
        .biologicalPath(soundLevelMetricsPackage.getBiologicalPath())
        .otherPath(soundLevelMetricsPackage.getOtherPath())
        .documentsPath(soundLevelMetricsPackage.getDocumentsPath())
        .calibrationDocumentsPath(soundLevelMetricsPackage.getCalibrationDocumentsPath())
        .sourcePath(soundLevelMetricsPackage.getSourcePath())
        .dataCollectionName(soundLevelMetricsPackage.getDataCollectionName())
        .siteOrCruiseName(soundLevelMetricsPackage.getSiteOrCruiseName())
        .deploymentId(soundLevelMetricsPackage.getDeploymentId())
        .publicReleaseDate(soundLevelMetricsPackage.getPublicReleaseDate())
        .locationDetail(soundLevelMetricsPackage.getLocationDetail())
        .deploymentTitle(soundLevelMetricsPackage.getDeploymentTitle())
        .deploymentPurpose(soundLevelMetricsPackage.getDeploymentPurpose())
        .deploymentDescription(soundLevelMetricsPackage.getDeploymentDescription())
        .alternateSiteName(soundLevelMetricsPackage.getAlternateSiteName())
        .alternateDeploymentName(soundLevelMetricsPackage.getAlternateDeploymentName())
        .startTime(soundLevelMetricsPackage.getStartTime())
        .endTime(soundLevelMetricsPackage.getEndTime())
        .preDeploymentCalibrationDate(soundLevelMetricsPackage.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(soundLevelMetricsPackage.getPostDeploymentCalibrationDate())
        .calibrationDescription(soundLevelMetricsPackage.getCalibrationDescription())
        .datasetPackager(getObject(personRepository, soundLevelMetricsPackage.getDatasetPackager()))
        .scientists(getBaseObjects(personRepository, soundLevelMetricsPackage.getScientists()))
        .projects(stringListToObjectList(soundLevelMetricsPackage.getProjects()))
        .sponsors(getBaseObjects(organizationRepository, soundLevelMetricsPackage.getSponsors()))
        .funders(getBaseObjects(organizationRepository, soundLevelMetricsPackage.getFunders()))
        .platform(soundLevelMetricsPackage.getPlatform())
        .instrument(soundLevelMetricsPackage.getInstrument())
        .qualityAnalyst(getObject(personRepository, soundLevelMetricsPackage.getQualityAnalyst()))
        .qualityAnalysisObjectives(soundLevelMetricsPackage.getQualityAnalysisObjectives())
        .qualityAnalysisMethod(soundLevelMetricsPackage.getQualityAnalysisMethod())
        .qualityAssessmentDescription(soundLevelMetricsPackage.getQualityAssessmentDescription())
        .qualityEntries(soundLevelMetricsPackage.getQualityEntries())
        .softwareNames(soundLevelMetricsPackage.getSoftwareNames())
        .softwareVersions(soundLevelMetricsPackage.getSoftwareVersions())
        .softwareProtocolCitation(soundLevelMetricsPackage.getSoftwareProtocolCitation())
        .softwareDescription(soundLevelMetricsPackage.getSoftwareDescription())
        .softwareProcessingDescription(soundLevelMetricsPackage.getSoftwareProcessingDescription())
        .analysisTimeZone(soundLevelMetricsPackage.getAnalysisTimeZone())
        .analysisEffort(soundLevelMetricsPackage.getAnalysisEffort())
        .sampleRate(soundLevelMetricsPackage.getSampleRate())
        .minFrequency(soundLevelMetricsPackage.getMinFrequency())
        .maxFrequency(soundLevelMetricsPackage.getMaxFrequency())
        .audioStartTime(soundLevelMetricsPackage.getAudioStartTime())
        .audioEndTime(soundLevelMetricsPackage.getAudioEndTime())
        .build();
  }

  private DetailedSoundPropagationModelsPackage process(SoundPropagationModelsPackage soundPropagationModelsPackage)
      throws NotFoundException, DatastoreException {
    return DetailedSoundPropagationModelsPackage.builder()
        .uuid(soundPropagationModelsPackage.getUuid())
        .visible(soundPropagationModelsPackage.isVisible())
        .processingLevel(soundPropagationModelsPackage.getProcessingLevel())
        .temperaturePath(soundPropagationModelsPackage.getTemperaturePath())
        .biologicalPath(soundPropagationModelsPackage.getBiologicalPath())
        .otherPath(soundPropagationModelsPackage.getOtherPath())
        .documentsPath(soundPropagationModelsPackage.getDocumentsPath())
        .calibrationDocumentsPath(soundPropagationModelsPackage.getCalibrationDocumentsPath())
        .sourcePath(soundPropagationModelsPackage.getSourcePath())
        .dataCollectionName(soundPropagationModelsPackage.getDataCollectionName())
        .siteOrCruiseName(soundPropagationModelsPackage.getSiteOrCruiseName())
        .deploymentId(soundPropagationModelsPackage.getDeploymentId())
        .publicReleaseDate(soundPropagationModelsPackage.getPublicReleaseDate())
        .locationDetail(soundPropagationModelsPackage.getLocationDetail())
        .deploymentTitle(soundPropagationModelsPackage.getDeploymentTitle())
        .deploymentPurpose(soundPropagationModelsPackage.getDeploymentPurpose())
        .deploymentDescription(soundPropagationModelsPackage.getDeploymentDescription())
        .alternateSiteName(soundPropagationModelsPackage.getAlternateSiteName())
        .alternateDeploymentName(soundPropagationModelsPackage.getAlternateDeploymentName())
        .startTime(soundPropagationModelsPackage.getStartTime())
        .endTime(soundPropagationModelsPackage.getEndTime())
        .preDeploymentCalibrationDate(soundPropagationModelsPackage.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(soundPropagationModelsPackage.getPostDeploymentCalibrationDate())
        .calibrationDescription(soundPropagationModelsPackage.getCalibrationDescription())
        .datasetPackager(getObject(personRepository, soundPropagationModelsPackage.getDatasetPackager()))
        .scientists(getBaseObjects(personRepository, soundPropagationModelsPackage.getScientists()))
        .projects(stringListToObjectList(soundPropagationModelsPackage.getProjects()))
        .sponsors(getBaseObjects(organizationRepository, soundPropagationModelsPackage.getSponsors()))
        .funders(getBaseObjects(organizationRepository, soundPropagationModelsPackage.getFunders()))
        .platform(soundPropagationModelsPackage.getPlatform())
        .instrument(soundPropagationModelsPackage.getInstrument())
        .softwareNames(soundPropagationModelsPackage.getSoftwareNames())
        .softwareVersions(soundPropagationModelsPackage.getSoftwareVersions())
        .softwareProtocolCitation(soundPropagationModelsPackage.getSoftwareProtocolCitation())
        .softwareDescription(soundPropagationModelsPackage.getSoftwareDescription())
        .softwareProcessingDescription(soundPropagationModelsPackage.getSoftwareProcessingDescription())
        .audioStartTime(soundPropagationModelsPackage.getAudioStartTime())
        .audioEndTime(soundPropagationModelsPackage.getAudioEndTime())
        .modeledFrequency(soundPropagationModelsPackage.getModeledFrequency())
        .build();
  }

  private List<Channel<AbstractObject>> getChannels(List<Channel<String>> channels) throws NotFoundException, DatastoreException {
    List<Channel<AbstractObject>> packageChannels = new ArrayList<>(0);

    for (Channel<String> channel : channels) {
      PackageSensor<AbstractObject> packageSensor = getSensor(channel.getSensor());
      packageChannels.add(Channel.<AbstractObject>builder()
              .sensor(packageSensor)
              .startTime(channel.getStartTime())
              .endTime(channel.getEndTime())
              .sampleRates(channel.getSampleRates())
              .dutyCycles(channel.getDutyCycles())
              .gains(channel.getGains())
          .build());
    }
    
    return packageChannels;
  }

  private List<PackageSensor<AbstractObject>> getSensors(List<PackageSensor<String>> sensors) throws NotFoundException, DatastoreException {
    List<PackageSensor<AbstractObject>> packageSensors = new ArrayList<>(0);

    for (PackageSensor<String> sensor : sensors) {
      packageSensors.add(getSensor(sensor));
    }
    
    return packageSensors;
  }
  
  private PackageSensor<AbstractObject> getSensor(PackageSensor<String> sensor) throws NotFoundException, DatastoreException {
    AbstractObject object = getObject(sensorRepository, sensor.getSensor());
    return PackageSensor.<AbstractObject>builder()
        .sensor(object)
        .position(sensor.getPosition())
        .build();
  }
  
  private AbstractObject getObject(CRUDRepository<?> repository, String uniqueField) throws NotFoundException, DatastoreException {
    return repository.getByUniqueField(uniqueField);
  }

  private List<AbstractObject> getAbstractObjects(CRUDRepository<?> repository, List<String> uniqueFields) throws NotFoundException, DatastoreException {
    List<AbstractObject> objects = new ArrayList<>(0);

    for (String uniqueField : uniqueFields) {
      objects.add(getObject(repository, uniqueField));
    }

    return objects;
  }

  private List<Object> getBaseObjects(CRUDRepository<?> repository, List<String> uniqueFields) throws NotFoundException, DatastoreException {
    return getAbstractObjects(repository, uniqueFields).stream()
            .map(o -> (Object) AbstractObjectWithName.builder()
                    .uuid(o.getUuid())
                    .name(o.getUniqueField())
                    .build()).toList();
  }

  private List<Object> stringListToObjectList(List<String> strings) {
    return strings.stream()
            .map(o -> (Object) o).toList();
  }

}
