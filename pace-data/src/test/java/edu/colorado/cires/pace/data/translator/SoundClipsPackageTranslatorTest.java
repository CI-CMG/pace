package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class SoundClipsPackageTranslatorTest {

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
    
    SoundClipsPackageTranslator soundClipsPackageTranslator = SoundClipsPackageTranslator.toBuilder(packageTranslator).build();
    
    assertEquals(packageTranslator.getUuid(), soundClipsPackageTranslator.getUuid());
    assertEquals(packageTranslator.getName(), soundClipsPackageTranslator.getName());
    assertEquals(packageTranslator.getPackageUUID(), soundClipsPackageTranslator.getPackageUUID());
    assertEquals(packageTranslator.getTemperaturePath(), soundClipsPackageTranslator.getTemperaturePath());
    assertEquals(packageTranslator.getBiologicalPath(), soundClipsPackageTranslator.getBiologicalPath());
    assertEquals(packageTranslator.getOtherPath(), soundClipsPackageTranslator.getOtherPath());
    assertEquals(packageTranslator.getDocumentsPath(), soundClipsPackageTranslator.getDocumentsPath());
    assertEquals(packageTranslator.getCalibrationDocumentsPath(), soundClipsPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(packageTranslator.getNavigationPath(), soundClipsPackageTranslator.getNavigationPath());
    assertEquals(packageTranslator.getSourcePath(), soundClipsPackageTranslator.getSourcePath());
    assertEquals(packageTranslator.getSiteOrCruiseName(), soundClipsPackageTranslator.getSiteOrCruiseName());
    assertEquals(packageTranslator.getDeploymentId(), soundClipsPackageTranslator.getDeploymentId());
    assertEquals(packageTranslator.getDatasetPackager(), soundClipsPackageTranslator.getDatasetPackager());
    assertEquals(packageTranslator.getProjects(), soundClipsPackageTranslator.getProjects());
    assertEquals(packageTranslator.getPublicReleaseDate().getDate(), soundClipsPackageTranslator.getPublicReleaseDate().getDate());
    assertEquals(packageTranslator.getPublicReleaseDate().getTimeZone(), soundClipsPackageTranslator.getPublicReleaseDate().getTimeZone());
    assertEquals(packageTranslator.getScientists(), soundClipsPackageTranslator.getScientists());
    assertEquals(packageTranslator.getSponsors(), soundClipsPackageTranslator.getSponsors());
    assertEquals(packageTranslator.getFunders(), soundClipsPackageTranslator.getFunders());
    assertEquals(packageTranslator.getPlatform(), soundClipsPackageTranslator.getPlatform());
    assertEquals(packageTranslator.getInstrument(), soundClipsPackageTranslator.getInstrument());
    assertEquals(packageTranslator.getStartTimeTranslator().getTimeZone(), soundClipsPackageTranslator.getStartTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) soundClipsPackageTranslator.getStartTimeTranslator()).getTime());
    assertEquals(packageTranslator.getEndTimeTranslator().getTimeZone(), soundClipsPackageTranslator.getEndTimeTranslator().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) soundClipsPackageTranslator.getEndTimeTranslator()).getTime());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), soundClipsPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getDate(), soundClipsPackageTranslator.getPreDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), soundClipsPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getDate(), soundClipsPackageTranslator.getPostDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getCalibrationDescription(), soundClipsPackageTranslator.getCalibrationDescription());
    assertEquals(packageTranslator.getDeploymentTitle(), soundClipsPackageTranslator.getDeploymentTitle());
    assertEquals(packageTranslator.getDeploymentDescription(), soundClipsPackageTranslator.getDeploymentDescription());
    assertEquals(packageTranslator.getDeploymentPurpose(), soundClipsPackageTranslator.getDeploymentPurpose());
    assertEquals(packageTranslator.getAlternateSiteName(), soundClipsPackageTranslator.getAlternateSiteName());
    assertEquals(packageTranslator.getAlternateDeploymentName(), soundClipsPackageTranslator.getAlternateDeploymentName());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getSurfaceElevation(), ((StationaryTerrestrialLocationTranslator) soundClipsPackageTranslator.getLocationDetailTranslator()).getSurfaceElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getInstrumentElevation(), ((StationaryTerrestrialLocationTranslator) soundClipsPackageTranslator.getLocationDetailTranslator()).getInstrumentElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLatitude(), ((StationaryTerrestrialLocationTranslator) soundClipsPackageTranslator.getLocationDetailTranslator()).getLatitude());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLongitude(), ((StationaryTerrestrialLocationTranslator) soundClipsPackageTranslator.getLocationDetailTranslator()).getLongitude());
  }

}