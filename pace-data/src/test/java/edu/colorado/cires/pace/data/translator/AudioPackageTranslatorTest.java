package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AudioPackageTranslatorTest {
  
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

    AudioPackageTranslator audioPackageTranslator = AudioPackageTranslator.toBuilder(packageTranslator).build();

    assertEquals(packageTranslator.getUuid(), audioPackageTranslator.getUuid());
    assertEquals(packageTranslator.getName(), audioPackageTranslator.getName());
    assertEquals(packageTranslator.getPackageUUID(), audioPackageTranslator.getPackageUUID());
    assertEquals(packageTranslator.getTemperaturePath(), audioPackageTranslator.getTemperaturePath());
    assertEquals(packageTranslator.getBiologicalPath(), audioPackageTranslator.getBiologicalPath());
    assertEquals(packageTranslator.getOtherPath(), audioPackageTranslator.getOtherPath());
    assertEquals(packageTranslator.getDocumentsPath(), audioPackageTranslator.getDocumentsPath());
    assertEquals(packageTranslator.getCalibrationDocumentsPath(), audioPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(packageTranslator.getSourcePath(), audioPackageTranslator.getSourcePath());
    assertEquals(packageTranslator.getSiteOrCruiseName(), audioPackageTranslator.getSiteOrCruiseName());
    assertEquals(packageTranslator.getDeploymentId(), audioPackageTranslator.getDeploymentId());
    assertEquals(packageTranslator.getDatasetPackager(), audioPackageTranslator.getDatasetPackager());
    assertEquals(packageTranslator.getProjects(), audioPackageTranslator.getProjects());
    assertEquals(packageTranslator.getPublicReleaseDate().getDate(), audioPackageTranslator.getPublicReleaseDate().getDate());
    assertEquals(packageTranslator.getPublicReleaseDate().getTimeZone(), audioPackageTranslator.getPublicReleaseDate().getTimeZone());
    assertEquals(packageTranslator.getScientists(), audioPackageTranslator.getScientists());
    assertEquals(packageTranslator.getSponsors(), audioPackageTranslator.getSponsors());
    assertEquals(packageTranslator.getFunders(), audioPackageTranslator.getFunders());
    assertEquals(packageTranslator.getPlatform(), audioPackageTranslator.getPlatform());
    assertEquals(packageTranslator.getInstrument(), audioPackageTranslator.getInstrument());
    assertEquals(packageTranslator.getStartTime().getTimeZone(), audioPackageTranslator.getStartTime().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getStartTime()).getTime(), ((DefaultTimeTranslator) audioPackageTranslator.getStartTime()).getTime());
    assertEquals(packageTranslator.getEndTime().getTimeZone(), audioPackageTranslator.getEndTime().getTimeZone());
    assertEquals(((DefaultTimeTranslator) packageTranslator.getEndTime()).getTime(), ((DefaultTimeTranslator) audioPackageTranslator.getEndTime()).getTime());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getTimeZone(), audioPackageTranslator.getPreDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPreDeploymentCalibrationDate().getDate(), audioPackageTranslator.getPreDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getTimeZone(), audioPackageTranslator.getPostDeploymentCalibrationDate().getTimeZone());
    assertEquals(packageTranslator.getPostDeploymentCalibrationDate().getDate(), audioPackageTranslator.getPostDeploymentCalibrationDate().getDate());
    assertEquals(packageTranslator.getCalibrationDescription(), audioPackageTranslator.getCalibrationDescription());
    assertEquals(packageTranslator.getDeploymentTitle(), audioPackageTranslator.getDeploymentTitle());
    assertEquals(packageTranslator.getDeploymentDescription(), audioPackageTranslator.getDeploymentDescription());
    assertEquals(packageTranslator.getDeploymentPurpose(), audioPackageTranslator.getDeploymentPurpose());
    assertEquals(packageTranslator.getAlternateSiteName(), audioPackageTranslator.getAlternateSiteName());
    assertEquals(packageTranslator.getAlternateDeploymentName(), audioPackageTranslator.getAlternateDeploymentName());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getSurfaceElevation(), ((StationaryTerrestrialLocationTranslator) audioPackageTranslator.getLocationDetailTranslator()).getSurfaceElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getInstrumentElevation(), ((StationaryTerrestrialLocationTranslator) audioPackageTranslator.getLocationDetailTranslator()).getInstrumentElevation());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLatitude(), ((StationaryTerrestrialLocationTranslator) audioPackageTranslator.getLocationDetailTranslator()).getLatitude());
    assertEquals(((StationaryTerrestrialLocationTranslator) packageTranslator.getLocationDetailTranslator()).getLongitude(), ((StationaryTerrestrialLocationTranslator) audioPackageTranslator.getLocationDetailTranslator()).getLongitude());
  }

}