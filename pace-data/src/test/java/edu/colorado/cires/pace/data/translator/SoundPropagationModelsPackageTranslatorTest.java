package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class SoundPropagationModelsPackageTranslatorTest {
  
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
        .startTime(DefaultTimeTranslator.builder()
            .time("time")
            .timeZone("timeZone")
            .build())
        .endTime(DefaultTimeTranslator.builder()
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

    SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator = SoundPropagationModelsPackageTranslator.toBuilder(packageTranslator).build();

    assertEquals(packageTranslator.getUuid(), soundPropagationModelsPackageTranslator.getUuid());
    assertEquals(packageTranslator.getName(), soundPropagationModelsPackageTranslator.getName());
    assertEquals(packageTranslator.getPackageUUID(), soundPropagationModelsPackageTranslator.getPackageUUID());
    assertEquals(packageTranslator.getTemperaturePath(), soundPropagationModelsPackageTranslator.getTemperaturePath());
    assertEquals(packageTranslator.getBiologicalPath(), soundPropagationModelsPackageTranslator.getBiologicalPath());
    assertEquals(packageTranslator.getOtherPath(), soundPropagationModelsPackageTranslator.getOtherPath());
    assertEquals(packageTranslator.getDocumentsPath(), soundPropagationModelsPackageTranslator.getDocumentsPath());
    assertEquals(packageTranslator.getCalibrationDocumentsPath(), soundPropagationModelsPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(packageTranslator.getNavigationPath(), soundPropagationModelsPackageTranslator.getNavigationPath());
    assertEquals(packageTranslator.getSourcePath(), soundPropagationModelsPackageTranslator.getSourcePath());
    assertEquals(packageTranslator.getSiteOrCruiseName(), soundPropagationModelsPackageTranslator.getSiteOrCruiseName());
    assertEquals(packageTranslator.getDeploymentId(), soundPropagationModelsPackageTranslator.getDeploymentId());
    assertEquals(packageTranslator.getDatasetPackager(), soundPropagationModelsPackageTranslator.getDatasetPackager());
    assertEquals(packageTranslator.getProjects(), soundPropagationModelsPackageTranslator.getProjects());
    assertEquals(packageTranslator.getPublicReleaseDate().getDate(), soundPropagationModelsPackageTranslator.getPublicReleaseDate().getDate());
    assertEquals(packageTranslator.getPublicReleaseDate().getTimeZone(), soundPropagationModelsPackageTranslator.getPublicReleaseDate().getTimeZone());
    assertEquals(packageTranslator.getScientists(), soundPropagationModelsPackageTranslator.getScientists());
    assertEquals(packageTranslator.getSponsors(), soundPropagationModelsPackageTranslator.getSponsors());
    assertEquals(packageTranslator.getFunders(), soundPropagationModelsPackageTranslator.getFunders());
    assertEquals(packageTranslator.getPlatform(), soundPropagationModelsPackageTranslator.getPlatform());
    assertEquals(packageTranslator.getInstrument(), soundPropagationModelsPackageTranslator.getInstrument());
    assertEquals(packageTranslator.getStartTime().getTimeZone(), soundPropagationModelsPackageTranslator.getStartTime().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getStartTime()).getTime(), ((DefaultTimeTranslator) soundPropagationModelsPackageTranslator.getStartTime()).getTime());
    assertEquals(packageTranslator.getEndTime().getTimeZone(), soundPropagationModelsPackageTranslator.getEndTime().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getEndTime()).getTime(), ((DefaultTimeTranslator) soundPropagationModelsPackageTranslator.getEndTime()).getTime());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), soundPropagationModelsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getDate(), soundPropagationModelsPackageTranslator.getPreDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), soundPropagationModelsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getDate(), soundPropagationModelsPackageTranslator.getPostDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getCalibrationDescription(), soundPropagationModelsPackageTranslator.getCalibrationDescription());
    assertEquals(packageTranslator.getDeploymentTitle(), soundPropagationModelsPackageTranslator.getDeploymentTitle());
    assertEquals(packageTranslator.getDeploymentDescription(), soundPropagationModelsPackageTranslator.getDeploymentDescription());
    assertEquals(packageTranslator.getDeploymentPurpose(), soundPropagationModelsPackageTranslator.getDeploymentPurpose());
    assertEquals(packageTranslator.getAlternateSiteName(), soundPropagationModelsPackageTranslator.getAlternateSiteName());
    assertEquals(packageTranslator.getAlternateDeploymentName(), soundPropagationModelsPackageTranslator.getAlternateDeploymentName());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getSurfaceElevation(), ((StationaryTerrestrialLocationTranslator) soundPropagationModelsPackageTranslator.getLocationDetailTranslator()).getSurfaceElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getInstrumentElevation(), ((StationaryTerrestrialLocationTranslator) soundPropagationModelsPackageTranslator.getLocationDetailTranslator()).getInstrumentElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLatitude(), ((StationaryTerrestrialLocationTranslator) soundPropagationModelsPackageTranslator.getLocationDetailTranslator()).getLatitude());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLongitude(), ((StationaryTerrestrialLocationTranslator) soundPropagationModelsPackageTranslator.getLocationDetailTranslator()).getLongitude());
  }

}