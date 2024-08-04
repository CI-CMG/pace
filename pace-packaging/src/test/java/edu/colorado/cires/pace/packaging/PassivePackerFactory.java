package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.base.AbstractObjectWithName;
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
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerAudioDatasetDetails;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerAudioDeployment;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerAudioPackage;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerAudioCalibrationInfo;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerCalibrationInfo;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerChannel;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerDatasetDetails;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerDeployment;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerDutyCycle;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerGain;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerLocation;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerPackage;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerPerson;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerQualityDetails;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerSampleRate;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerSamplingDetails;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.passivePacker.PassivePackerUtils;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PassivePackerFactory {
  
  private final PersonRepository personRepository;
  private final OrganizationRepository organizationRepository;

  public PassivePackerFactory(PersonRepository personRepository, OrganizationRepository organizationRepository) {
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
  }

  public PassivePackerPackage createPackage(Package aPackage) throws NotFoundException, DatastoreException {
    PassivePackerPackage passivePackerPackage = PassivePackerPackage.builder()
        .dataCollectionName(aPackage.getDataCollectionName())
        .publishDate(aPackage.getPublishDate())
        .projectName(aPackage.getProjectName())
        .deploymentName(aPackage.getDeploymentId())
        .deploymentAlias(aPackage.getDeploymentAlias())
        .site(aPackage.getSite())
        .siteAliases(Collections.singletonList(aPackage.getAlternateSiteName()))
        .title(aPackage.getTitle())
        .purpose(aPackage.getPurpose())
        .description(aPackage.getDeploymentDescription())
        .platformName(aPackage.getPlatformName())
        .instrumentType(aPackage.getInstrumentType())
        .metadataAuthor(getPerson(aPackage.getDatasetPackager()))
        .scientists(getPeople(aPackage.getScientists()))
        .sponsors(getOrganizations(aPackage.getSponsors()))
        .funders(getOrganizations(aPackage.getFunders()))
        .calibrationInfo(getCalibrationInfo(aPackage))
        .datasetDetails(getDatasetDetails(aPackage))
        .deployment(getDeployment(aPackage))
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      passivePackerPackage = addAudioFields(passivePackerPackage, audioDataPackage);
    }
    
    return passivePackerPackage;
  }

  private PassivePackerDeployment getDeployment(Package aPackage) {
    PassivePackerDeployment deployment = PassivePackerDeployment.builder()
        .location(getLocation(aPackage.getLocationDetail()))
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      deployment = addAudioDeploymentFields(audioDataPackage, deployment);
    }
    
    return deployment;
  }

  private PassivePackerDeployment addAudioDeploymentFields(AudioDataPackage audioDataPackage, PassivePackerDeployment deployment) {
    return PassivePackerUtils.fromBaseDeployment(deployment, PassivePackerAudioDeployment.builder()).toBuilder()
        .audioStart(audioDataPackage.getAudioStartTime())
        .audioEnd(audioDataPackage.getAudioEndTime())
        .deploymentTime(audioDataPackage.getDeploymentTime())
        .recoveryTime(audioDataPackage.getRecoveryTime())
        .build();
  }

  private PassivePackerLocation getLocation(LocationDetail locationDetail) {
    PassivePackerLocation location = PassivePackerLocation.builder()
        .deployType(getDeployType(locationDetail))
        .build();
    
    if (locationDetail instanceof StationaryMarineLocation stationaryMarineLocation) {
      return addStationaryMarineLocationFields(stationaryMarineLocation, location);
    }
    
    return null;
  }

  private PassivePackerLocation addStationaryMarineLocationFields(StationaryMarineLocation stationaryMarineLocation, PassivePackerLocation location) {
    MarineInstrumentLocation deploymentLocation = stationaryMarineLocation.getDeploymentLocation();
    MarineInstrumentLocation recoveryLocation = stationaryMarineLocation.getRecoveryLocation();
    return PassivePackerUtils.fromBaseLocation(location, PassivePackerStationaryMarineLocation.builder()).toBuilder()
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

  private String getDeployType(LocationDetail locationDetail) {
    if (locationDetail instanceof StationaryMarineLocation) {
      return "Stationary Marine";
    }
    return null;
  }

  private PassivePackerDatasetDetails getDatasetDetails(Package aPackage) {
    PassivePackerDatasetDetails datasetDetails = PassivePackerDatasetDetails.builder()
        .type("Raw")
        .subType(subtypeFromPackage(aPackage))
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      datasetDetails = addAudioDetailFields(audioDataPackage, datasetDetails);
    }
    
    return datasetDetails;
  }

  private PassivePackerDatasetDetails addAudioDetailFields(AudioDataPackage audioDataPackage, PassivePackerDatasetDetails datasetDetails) {
    return PassivePackerUtils.fromBaseDatasetDetails(datasetDetails, PassivePackerAudioDatasetDetails.builder()).toBuilder()
        .sourcePath(audioDataPackage.getSourcePath().toString())
        .dataComment(audioDataPackage.getComments())
        .build();
  }

  private String subtypeFromPackage(Package aPackage) {
    if (aPackage instanceof AudioPackage) {
      return "Audio";
    }
    
    if (aPackage instanceof CPODPackage) {
      return "C-POD";
    }
    
    if (aPackage instanceof SoundLevelMetricsPackage) {
      return "Sound level metrics";
    }
    
    if (aPackage instanceof SoundPropagationModelsPackage) {
      return "Sound propagation models";
    }
    
    if (aPackage instanceof DetectionsPackage) {
      return "Detections";
    }
    
    if (aPackage instanceof SoundClipsPackage) {
      return "Sound clips package";
    }
    
    return null;
  }

  private PassivePackerCalibrationInfo getCalibrationInfo(Package aPackage) {
    String calState = null;
    LocalDate calDate = null;
    
    if (aPackage.getPreDeploymentCalibrationDate() != null && aPackage.getPostDeploymentCalibrationDate() == null) {
      calState = "Calibrated Pre-Deployment";
      calDate = aPackage.getPreDeploymentCalibrationDate();
    } else if (aPackage.getPostDeploymentCalibrationDate() != null && aPackage.getPreDeploymentCalibrationDate() == null) {
      calState = "Calibrated Post-Deployment";
      calDate = aPackage.getPostDeploymentCalibrationDate();
    } else if (aPackage.getPreDeploymentCalibrationDate() == null) {
      calState = "Factory Calibrated";
    }

    PassivePackerCalibrationInfo calibrationInfo = PassivePackerCalibrationInfo.builder()
        .calDate(calDate)
        .calState(calState)
        .calDocsPath(aPackage.getCalibrationDocumentsPath().toString())
        .comment(aPackage.getCalibrationDescription())
        .build();
    
    if (aPackage instanceof AudioDataPackage audioDataPackage) {
      calibrationInfo = addAudioCalibrationFields(calibrationInfo, audioDataPackage);
    }
    
    return calibrationInfo;
  }

  private PassivePackerCalibrationInfo addAudioCalibrationFields(PassivePackerCalibrationInfo calibrationInfo, AudioDataPackage audioDataPackage) {
    return PassivePackerUtils.fromBaseCalibrationInfo(calibrationInfo, PassivePackerAudioCalibrationInfo.builder()).toBuilder()
        .sensitivity(String.valueOf(audioDataPackage.getHydrophoneSensitivity()))
        .frequency(String.valueOf(audioDataPackage.getFrequencyRange()))
        .gain(String.valueOf(audioDataPackage.getGain()))
        .build();
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

  private PassivePackerPackage addAudioFields(PassivePackerPackage passivePackerPackage, AudioDataPackage audioDataPackage)
      throws NotFoundException, DatastoreException {
    return PassivePackerUtils.fromBasePackage(passivePackerPackage, PassivePackerAudioPackage.builder()).toBuilder()
        .channels(getChannels(audioDataPackage))
        .qualityDetails(getQualityDetails(audioDataPackage))
        .instrumentId(audioDataPackage.getInstrumentId())
        .build();
  }

  private PassivePackerQualityDetails getQualityDetails(AudioDataPackage audioDataPackage) throws NotFoundException, DatastoreException {
    Person person = personRepository.getByUniqueField(audioDataPackage.getQualityAnalyst());
    return PassivePackerQualityDetails.builder()
        .analyst(audioDataPackage.getQualityAnalyst())
        .analystUuid(person.getUuid())
        .description(audioDataPackage.getQualityAssessmentDescription())
        .method(audioDataPackage.getQualityAnalysisMethod())
        .objectives(audioDataPackage.getQualityAnalysisObjectives())
        .qualityDetails(getQualityEntries(audioDataPackage.getQualityEntries()))
        .build();
  }

  private List<PassivePackerQualityEntry> getQualityEntries(List<DataQualityEntry> qualityEntries) {
    return qualityEntries.stream()
        .map(e -> (PassivePackerQualityEntry) PassivePackerQualityEntry.builder()
            .start(e.getStartTime())
            .end(e.getEndTime())
            .quality(e.getQualityLevel().getName())
            .lowFreq(String.valueOf(e.getMinFrequency()))
            .highFreq(String.valueOf(e.getMaxFrequency()))
            .comments(e.getComments())
            .channels("1,2,3")
            .build())
        .toList();
  }

  private Map<Integer, PassivePackerChannel> getChannels(AudioDataPackage audioDataPackage) {
    List<Channel<String>> channels = audioDataPackage.getChannels();
    
    return IntStream.range(0, channels.size()).boxed()
        .collect(Collectors.toMap(
            i -> i + 1,
            i -> {
              Channel<String> channel = channels.get(i);
              return PassivePackerChannel.builder()
                  .channelStart(channel.getStartTime())
                  .channelEnd(channel.getEndTime())
                  .sensor(channel.getSensor().getSensor())
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
            .start(dc.getStartTime())
            .end(dc.getEndTime())
            .duration(String.valueOf(dc.getDuration()))
            .interval(String.valueOf(dc.getInterval()))
            .build())
        .toList();
  }

  private List<PassivePackerGain> getGains(List<Gain> gains) {
    return gains.stream()
        .map(g -> (PassivePackerGain) PassivePackerGain.builder()
            .start(g.getStartTime())
            .end(g.getEndTime())
            .gain(String.valueOf(g.getGain()))
            .build())
        .toList();
  }

  private List<PassivePackerSampleRate> getSampling(List<SampleRate> sampleRates) {
    return sampleRates.stream()
        .map(sr -> (PassivePackerSampleRate) PassivePackerSampleRate.builder()
            .start(sr.getStartTime())
            .end(sr.getEndTime())
            .sampleRate(String.valueOf(sr.getSampleRate()))
            .sampleBits(String.valueOf(sr.getSampleBits()))
            .build())
        .toList();
  }

}
