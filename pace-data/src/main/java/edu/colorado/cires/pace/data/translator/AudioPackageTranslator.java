package edu.colorado.cires.pace.data.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioPackageTranslator extends AudioDataPackageTranslator {
  
  public static AudioPackageTranslatorBuilder<?, ?> toBuilder(PackageTranslator packageTranslator) {
    return AudioPackageTranslator
        .builder()
        .uuid(packageTranslator.getUuid())
        .name(packageTranslator.getName())
        .packageUUID(packageTranslator.getPackageUUID())
        .temperaturePath(packageTranslator.getTemperaturePath())
        .biologicalPath(packageTranslator.getBiologicalPath())
        .otherPath(packageTranslator.getOtherPath())
        .documentsPath(packageTranslator.getDocumentsPath())
        .calibrationDocumentsPath(packageTranslator.getCalibrationDocumentsPath())
        .navigationPath(packageTranslator.getNavigationPath())
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
        .startTimeTranslator(packageTranslator.getStartTimeTranslator())
        .endTimeTranslator(packageTranslator.getEndTimeTranslator())
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
