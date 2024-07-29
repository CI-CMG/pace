package edu.colorado.cires.pace.data.object.dataset.base.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.LocationDetailTranslator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PackageTranslator extends Translator {
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
  private final DateTranslator publicReleaseDate;
  private final String scientists;
  private final String sponsors;
  private final String funders;
  private final String platform;
  private final String instrument;
  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final DateTranslator preDeploymentCalibrationDate;
  private final DateTranslator postDeploymentCalibrationDate;
  private final String calibrationDescription;
  private final String deploymentTitle;
  private final String deploymentPurpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String alternateDeploymentName;
  private final LocationDetailTranslator locationDetailTranslator;

  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public PackageTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
