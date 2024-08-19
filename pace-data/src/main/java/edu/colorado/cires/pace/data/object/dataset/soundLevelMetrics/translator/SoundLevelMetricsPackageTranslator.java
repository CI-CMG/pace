package edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.SoundAnalysisPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundLevelMetricsPackageTranslator extends SoundAnalysisPackageTranslator {
  
  private final QualityControlDetailTranslator qualityControlDetailTranslator;

  private final TimeTranslator audioStartTimeTranslator;
  private final TimeTranslator audioEndTimeTranslator;

  public static SoundLevelMetricsPackageTranslatorBuilder<?, ?> toBuilder(PackageTranslator packageTranslator) {
    return SoundLevelMetricsPackageTranslator.builder()
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
