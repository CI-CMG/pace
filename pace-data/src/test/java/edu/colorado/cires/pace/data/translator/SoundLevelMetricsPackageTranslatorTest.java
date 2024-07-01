package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class SoundLevelMetricsPackageTranslatorTest {
  
  @Test
  void testToBuilderFromPackageTranslator() {
    PackageTranslator packageTranslator = PackageTranslator.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .packageUUID("package-uuid")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate(DateTranslator.builder()
            .timeZone("UTC")
            .date("date")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTimeTranslator(DefaultTimeTranslator.builder()
            .time("time")
            .timeZone("timeZone")
            .build())
        .endTimeTranslator(DefaultTimeTranslator.builder()
            .time("time")
            .timeZone("timeZone")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date("date")
            .timeZone("timeZone")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date("date")
            .timeZone("timeZone")
            .build())
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(StationaryTerrestrialLocationTranslator.builder()
            .surfaceElevation("surfaceElevation")
            .instrumentElevation("instrumentElevation")
            .latitude("latitude")
            .longitude("longitude")
            .build())
        .build();

    SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator = SoundLevelMetricsPackageTranslator.toBuilder(packageTranslator).build();

    assertEquals(packageTranslator.getUuid(), soundLevelMetricsPackageTranslator.getUuid());
    assertEquals(packageTranslator.getName(), soundLevelMetricsPackageTranslator.getName());
    assertEquals(packageTranslator.getPackageUUID(), soundLevelMetricsPackageTranslator.getPackageUUID());
    assertEquals(packageTranslator.getTemperaturePath(), soundLevelMetricsPackageTranslator.getTemperaturePath());
    assertEquals(packageTranslator.getBiologicalPath(), soundLevelMetricsPackageTranslator.getBiologicalPath());
    assertEquals(packageTranslator.getOtherPath(), soundLevelMetricsPackageTranslator.getOtherPath());
    assertEquals(packageTranslator.getDocumentsPath(), soundLevelMetricsPackageTranslator.getDocumentsPath());
    assertEquals(packageTranslator.getCalibrationDocumentsPath(), soundLevelMetricsPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(packageTranslator.getNavigationPath(), soundLevelMetricsPackageTranslator.getNavigationPath());
    assertEquals(packageTranslator.getSourcePath(), soundLevelMetricsPackageTranslator.getSourcePath());
    assertEquals(packageTranslator.getSiteOrCruiseName(), soundLevelMetricsPackageTranslator.getSiteOrCruiseName());
    assertEquals(packageTranslator.getDeploymentId(), soundLevelMetricsPackageTranslator.getDeploymentId());
    assertEquals(packageTranslator.getDatasetPackager(), soundLevelMetricsPackageTranslator.getDatasetPackager());
    assertEquals(packageTranslator.getProjects(), soundLevelMetricsPackageTranslator.getProjects());
    assertEquals(packageTranslator.getPublicReleaseDate().getDate(), soundLevelMetricsPackageTranslator.getPublicReleaseDate().getDate());
    assertEquals(packageTranslator.getPublicReleaseDate().getTimeZone(), soundLevelMetricsPackageTranslator.getPublicReleaseDate().getTimeZone());
    assertEquals(packageTranslator.getScientists(), soundLevelMetricsPackageTranslator.getScientists());
    assertEquals(packageTranslator.getSponsors(), soundLevelMetricsPackageTranslator.getSponsors());
    assertEquals(packageTranslator.getFunders(), soundLevelMetricsPackageTranslator.getFunders());
    assertEquals(packageTranslator.getPlatform(), soundLevelMetricsPackageTranslator.getPlatform());
    assertEquals(packageTranslator.getInstrument(), soundLevelMetricsPackageTranslator.getInstrument());
    assertEquals(packageTranslator.getStartTimeTranslator().getTimeZone(), soundLevelMetricsPackageTranslator.getStartTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) soundLevelMetricsPackageTranslator.getStartTimeTranslator()).getTime());
    assertEquals(packageTranslator.getEndTimeTranslator().getTimeZone(), soundLevelMetricsPackageTranslator.getEndTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) soundLevelMetricsPackageTranslator.getEndTimeTranslator()).getTime());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getDate(), soundLevelMetricsPackageTranslator.getPreDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getDate(), soundLevelMetricsPackageTranslator.getPostDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getCalibrationDescription(), soundLevelMetricsPackageTranslator.getCalibrationDescription());
    assertEquals(packageTranslator.getDeploymentTitle(), soundLevelMetricsPackageTranslator.getDeploymentTitle());
    assertEquals(packageTranslator.getDeploymentDescription(), soundLevelMetricsPackageTranslator.getDeploymentDescription());
    assertEquals(packageTranslator.getDeploymentPurpose(), soundLevelMetricsPackageTranslator.getDeploymentPurpose());
    assertEquals(packageTranslator.getAlternateSiteName(), soundLevelMetricsPackageTranslator.getAlternateSiteName());
    assertEquals(packageTranslator.getAlternateDeploymentName(), soundLevelMetricsPackageTranslator.getAlternateDeploymentName());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getSurfaceElevation(), ((StationaryTerrestrialLocationTranslator) soundLevelMetricsPackageTranslator.getLocationDetailTranslator()).getSurfaceElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getInstrumentElevation(), ((StationaryTerrestrialLocationTranslator) soundLevelMetricsPackageTranslator.getLocationDetailTranslator()).getInstrumentElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLatitude(), ((StationaryTerrestrialLocationTranslator) soundLevelMetricsPackageTranslator.getLocationDetailTranslator()).getLatitude());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLongitude(), ((StationaryTerrestrialLocationTranslator) soundLevelMetricsPackageTranslator.getLocationDetailTranslator()).getLongitude());
  }

}