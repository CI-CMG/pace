package edu.colorado.cires.pace.data.object.dataset.audio.translator;

import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * AudioPackageTranslator contains all the same fields as AudioDataPackageTranslator
 * in addition to having a builder
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioPackageTranslator extends AudioDataPackageTranslator {
  
  public static AudioPackageTranslatorBuilder<?, ?> toBuilder(PackageTranslator packageTranslator) {
    return AudioPackageTranslator
        .builder()
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
