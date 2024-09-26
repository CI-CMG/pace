package edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.SoftwareDependentPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * SoundPropagationModelsPackageTranslator extends SoftwareDependentPackageTranslator, holds
 * modeled frequency, audio start time translator, and audio end time translator
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundPropagationModelsPackageTranslator extends SoftwareDependentPackageTranslator {

  private final String modeledFrequency;
  
  private final TimeTranslator audioStartTimeTranslator;
  private final TimeTranslator audioEndTimeTranslator;

  /**
   * Returns a translator builder with the provided data
   *
   * @param packageTranslator translator data to be applied
   * @return SoundPropagationModelsPackageTranslatorBuilder with provided data applied
   */
  public static SoundPropagationModelsPackageTranslatorBuilder<?, ?> toBuilder(PackageTranslator packageTranslator) {
    return SoundPropagationModelsPackageTranslator.builder()
        .uuid(packageTranslator.getUuid())
        .dataCollectionName(packageTranslator.getDataCollectionName())
        .name(packageTranslator.getName())
        .packageUUID(packageTranslator.getPackageUUID())
        .temperaturePath(packageTranslator.getTemperaturePath())
        .biologicalPath(packageTranslator.getBiologicalPath())
        .otherPath(packageTranslator.getOtherPath())
        .documentsPath(packageTranslator.getDocumentsPath())
        .calibrationDocumentsPath(packageTranslator.getCalibrationDocumentsPath())
        .sourcePath(packageTranslator.getSourcePath())
        .siteOrCruiseName(packageTranslator.getSiteOrCruiseName())
        .deploymentId(packageTranslator.getDeploymentId())
        .datasetPackager(packageTranslator.getDatasetPackager())
        .projects(packageTranslator.getProjects())
        .publicReleaseDate(packageTranslator.getPublicReleaseDate())
        .scientists(packageTranslator.getScientists())
        .sponsors(packageTranslator.getSponsors())
        .funders(packageTranslator.getFunders())
        .platform(packageTranslator.getPlatform())
        .instrument(packageTranslator.getInstrument())
        .startTime(packageTranslator.getStartTime())
        .endTime(packageTranslator.getEndTime())
        .preDeploymentCalibrationDate(packageTranslator.getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(packageTranslator.getPostDeploymentCalibrationDate())
        .calibrationDescription(packageTranslator.getCalibrationDescription())
        .deploymentTitle(packageTranslator.getDeploymentTitle())
        .deploymentPurpose(packageTranslator.getDeploymentPurpose())
        .deploymentDescription(packageTranslator.getDeploymentDescription())
        .alternateSiteName(packageTranslator.getAlternateSiteName())
        .alternateDeploymentName(packageTranslator.getAlternateDeploymentName())
        .locationDetailTranslator(packageTranslator.getLocationDetailTranslator());
  }

}
