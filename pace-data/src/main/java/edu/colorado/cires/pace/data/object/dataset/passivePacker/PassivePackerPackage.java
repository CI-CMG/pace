package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.colorado.cires.pace.data.object.base.AbstractObjectWithName;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerPackage {
  
  private final String dataCollectionName;
  private final String publishDate;
  private final List<String> projectName;
  private final String deploymentName;
  private final String deploymentAlias;
  private final String site;
  private final List<String> siteAliases;
  private final String title;
  private final String purpose;
  @JsonProperty("ABSTRACT")
  private final String description;
  private final String platformName;
  private final String instrumentType;
  private final PassivePackerPerson metadataAuthor;
  private final List<AbstractObjectWithName> scientists;
  private final List<AbstractObjectWithName> sponsors;
  private final List<AbstractObjectWithName> funders;
  private final PassivePackerCalibrationInfo calibrationInfo;
  private final PassivePackerDeployment deployment;
  private final PassivePackerDatasetDetails datasetDetails;
  private final PassivePackerQualityDetails qualityDetails;
  @Builder.Default
  private final String instrumentId = "";
  private final Map<Integer, PassivePackerChannel> channels;
  private final Map<PassivePackerSensorType, List<PassivePackerSensor>> sensors;
  
  public <P extends PassivePackerPackage, B extends PassivePackerPackage.PassivePackerPackageBuilder<P, ?>> P toInheritingType(B inheritingTypeBuilder) {
    return inheritingTypeBuilder
        .dataCollectionName(getDataCollectionName())
        .publishDate(getPublishDate())
        .projectName(getProjectName())
        .deploymentName(getDeploymentName())
        .deploymentAlias(getDeploymentAlias())
        .site(getSite())
        .siteAliases(getSiteAliases())
        .title(getTitle())
        .purpose(getPurpose())
        .description(getDescription())
        .platformName(getPlatformName())
        .instrumentType(getInstrumentType())
        .metadataAuthor(getMetadataAuthor())
        .scientists(getScientists())
        .sponsors(getSponsors())
        .funders(getFunders())
        .calibrationInfo(getCalibrationInfo())
        .datasetDetails(getDatasetDetails())
        .deployment(getDeployment())
        .instrumentId(getInstrumentId())
        .qualityDetails(getQualityDetails())
        .channels(getChannels())
        .sensors(getSensors())
        .build();
  }

}
