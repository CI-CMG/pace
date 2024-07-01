package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class CPODPackageTranslatorTest {
  
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

    CPODPackageTranslator cpodPackageTranslator = CPODPackageTranslator.toBuilder(packageTranslator).build();

    assertEquals(packageTranslator.getUuid(), cpodPackageTranslator.getUuid());
    assertEquals(packageTranslator.getName(), cpodPackageTranslator.getName());
    assertEquals(packageTranslator.getPackageUUID(), cpodPackageTranslator.getPackageUUID());
    assertEquals(packageTranslator.getTemperaturePath(), cpodPackageTranslator.getTemperaturePath());
    assertEquals(packageTranslator.getBiologicalPath(), cpodPackageTranslator.getBiologicalPath());
    assertEquals(packageTranslator.getOtherPath(), cpodPackageTranslator.getOtherPath());
    assertEquals(packageTranslator.getDocumentsPath(), cpodPackageTranslator.getDocumentsPath());
    assertEquals(packageTranslator.getCalibrationDocumentsPath(), cpodPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(packageTranslator.getNavigationPath(), cpodPackageTranslator.getNavigationPath());
    assertEquals(packageTranslator.getSourcePath(), cpodPackageTranslator.getSourcePath());
    assertEquals(packageTranslator.getSiteOrCruiseName(), cpodPackageTranslator.getSiteOrCruiseName());
    assertEquals(packageTranslator.getDeploymentId(), cpodPackageTranslator.getDeploymentId());
    assertEquals(packageTranslator.getDatasetPackager(), cpodPackageTranslator.getDatasetPackager());
    assertEquals(packageTranslator.getProjects(), cpodPackageTranslator.getProjects());
    assertEquals(packageTranslator.getPublicReleaseDate().getDate(), cpodPackageTranslator.getPublicReleaseDate().getDate());
    assertEquals(packageTranslator.getPublicReleaseDate().getTimeZone(), cpodPackageTranslator.getPublicReleaseDate().getTimeZone());
    assertEquals(packageTranslator.getScientists(), cpodPackageTranslator.getScientists());
    assertEquals(packageTranslator.getSponsors(), cpodPackageTranslator.getSponsors());
    assertEquals(packageTranslator.getFunders(), cpodPackageTranslator.getFunders());
    assertEquals(packageTranslator.getPlatform(), cpodPackageTranslator.getPlatform());
    assertEquals(packageTranslator.getInstrument(), cpodPackageTranslator.getInstrument());
    assertEquals(packageTranslator.getStartTimeTranslator().getTimeZone(), cpodPackageTranslator.getStartTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) cpodPackageTranslator.getStartTimeTranslator()).getTime());
    assertEquals(packageTranslator.getEndTimeTranslator().getTimeZone(), cpodPackageTranslator.getEndTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) cpodPackageTranslator.getEndTimeTranslator()).getTime());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), cpodPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getDate(), cpodPackageTranslator.getPreDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), cpodPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getDate(), cpodPackageTranslator.getPostDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getCalibrationDescription(), cpodPackageTranslator.getCalibrationDescription());
    assertEquals(packageTranslator.getDeploymentTitle(), cpodPackageTranslator.getDeploymentTitle());
    assertEquals(packageTranslator.getDeploymentDescription(), cpodPackageTranslator.getDeploymentDescription());
    assertEquals(packageTranslator.getDeploymentPurpose(), cpodPackageTranslator.getDeploymentPurpose());
    assertEquals(packageTranslator.getAlternateSiteName(), cpodPackageTranslator.getAlternateSiteName());
    assertEquals(packageTranslator.getAlternateDeploymentName(), cpodPackageTranslator.getAlternateDeploymentName());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getSurfaceElevation(), ((StationaryTerrestrialLocationTranslator) cpodPackageTranslator.getLocationDetailTranslator()).getSurfaceElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getInstrumentElevation(), ((StationaryTerrestrialLocationTranslator) cpodPackageTranslator.getLocationDetailTranslator()).getInstrumentElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLatitude(), ((StationaryTerrestrialLocationTranslator) cpodPackageTranslator.getLocationDetailTranslator()).getLatitude());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLongitude(), ((StationaryTerrestrialLocationTranslator) cpodPackageTranslator.getLocationDetailTranslator()).getLongitude());
  }

}