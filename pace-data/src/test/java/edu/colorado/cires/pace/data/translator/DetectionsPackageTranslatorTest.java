package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DetectionsPackageTranslatorTest {
  
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

    DetectionsPackageTranslator detectionsPackageTranslator = DetectionsPackageTranslator.toBuilder(packageTranslator).build();

    assertEquals(packageTranslator.getUuid(), detectionsPackageTranslator.getUuid());
    assertEquals(packageTranslator.getName(), detectionsPackageTranslator.getName());
    assertEquals(packageTranslator.getPackageUUID(), detectionsPackageTranslator.getPackageUUID());
    assertEquals(packageTranslator.getTemperaturePath(), detectionsPackageTranslator.getTemperaturePath());
    assertEquals(packageTranslator.getBiologicalPath(), detectionsPackageTranslator.getBiologicalPath());
    assertEquals(packageTranslator.getOtherPath(), detectionsPackageTranslator.getOtherPath());
    assertEquals(packageTranslator.getDocumentsPath(), detectionsPackageTranslator.getDocumentsPath());
    assertEquals(packageTranslator.getCalibrationDocumentsPath(), detectionsPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(packageTranslator.getNavigationPath(), detectionsPackageTranslator.getNavigationPath());
    assertEquals(packageTranslator.getSourcePath(), detectionsPackageTranslator.getSourcePath());
    assertEquals(packageTranslator.getSiteOrCruiseName(), detectionsPackageTranslator.getSiteOrCruiseName());
    assertEquals(packageTranslator.getDeploymentId(), detectionsPackageTranslator.getDeploymentId());
    assertEquals(packageTranslator.getDatasetPackager(), detectionsPackageTranslator.getDatasetPackager());
    assertEquals(packageTranslator.getProjects(), detectionsPackageTranslator.getProjects());
    assertEquals(packageTranslator.getPublicReleaseDate().getDate(), detectionsPackageTranslator.getPublicReleaseDate().getDate());
    assertEquals(packageTranslator.getPublicReleaseDate().getTimeZone(), detectionsPackageTranslator.getPublicReleaseDate().getTimeZone());
    assertEquals(packageTranslator.getScientists(), detectionsPackageTranslator.getScientists());
    assertEquals(packageTranslator.getSponsors(), detectionsPackageTranslator.getSponsors());
    assertEquals(packageTranslator.getFunders(), detectionsPackageTranslator.getFunders());
    assertEquals(packageTranslator.getPlatform(), detectionsPackageTranslator.getPlatform());
    assertEquals(packageTranslator.getInstrument(), detectionsPackageTranslator.getInstrument());
    assertEquals(packageTranslator.getStartTimeTranslator().getTimeZone(), detectionsPackageTranslator.getStartTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) detectionsPackageTranslator.getStartTimeTranslator()).getTime());
    assertEquals(packageTranslator.getEndTimeTranslator().getTimeZone(), detectionsPackageTranslator.getEndTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) detectionsPackageTranslator.getEndTimeTranslator()).getTime());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), detectionsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getDate(), detectionsPackageTranslator.getPreDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), detectionsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getDate(), detectionsPackageTranslator.getPostDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getCalibrationDescription(), detectionsPackageTranslator.getCalibrationDescription());
    assertEquals(packageTranslator.getDeploymentTitle(), detectionsPackageTranslator.getDeploymentTitle());
    assertEquals(packageTranslator.getDeploymentDescription(), detectionsPackageTranslator.getDeploymentDescription());
    assertEquals(packageTranslator.getDeploymentPurpose(), detectionsPackageTranslator.getDeploymentPurpose());
    assertEquals(packageTranslator.getAlternateSiteName(), detectionsPackageTranslator.getAlternateSiteName());
    assertEquals(packageTranslator.getAlternateDeploymentName(), detectionsPackageTranslator.getAlternateDeploymentName());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getSurfaceElevation(), ((StationaryTerrestrialLocationTranslator) detectionsPackageTranslator.getLocationDetailTranslator()).getSurfaceElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getInstrumentElevation(), ((StationaryTerrestrialLocationTranslator) detectionsPackageTranslator.getLocationDetailTranslator()).getInstrumentElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLatitude(), ((StationaryTerrestrialLocationTranslator) detectionsPackageTranslator.getLocationDetailTranslator()).getLatitude());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLongitude(), ((StationaryTerrestrialLocationTranslator) detectionsPackageTranslator.getLocationDetailTranslator()).getLongitude());
  }

}