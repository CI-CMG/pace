package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.colorado.cires.pace.data.object.base.AbstractObjectWithName;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Jacksonized
@JsonTypeInfo(use = Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(PassivePackerAudioPackage.class),
})
public class PassivePackerPackage {
  
  private final String dataCollectionName;
  private final LocalDate publishDate;
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

}
