package edu.colorado.cires.pace.data.translator;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class PackageTranslator implements Translator {
  
  private final UUID uuid;
  private final String name;
  
  private final String packageUUID;
  private final String temperaturePath;
  private final String biologicalPath;
  private final String otherPath;
  private final String documentsPath;
  private final String calibrationDocumentsPath;
  private final String navigationPath;
  private final String sourcePath;
  private final String siteOrCruiseName;
  private final String deploymentId;
  private final String datasetPackager;
  private final String projects;
  private final String publicReleaseDate;
  private final String scientists;
  private final String sponsors;
  private final String funders;
  private final String platform;
  private final String instrument;
  private final TimeTranslator startTimeTranslator;
  private final TimeTranslator endTimeTranslator;
  private final String preDeploymentCalibrationDate;
  private final String postDeploymentCalibrationDate;
  private final String calibrationDescription;
  private final String deploymentTitle;
  private final String deploymentPurpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String alternateDeploymentName;
  private final LocationDetailTranslator locationDetailTranslator;
}
