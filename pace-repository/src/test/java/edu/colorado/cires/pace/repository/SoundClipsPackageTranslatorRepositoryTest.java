package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundClips.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;

public class SoundClipsPackageTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return SoundClipsPackageTranslator.builder()
        .name(String.format("name-%s", suffix))
        .packageUUID(String.format("package-uuid-%s", suffix))
        .temperaturePath(String.format("temperature-path-%s", suffix))
        .biologicalPath(String.format("biological-path-%s", suffix))
        .otherPath(String.format("other-path-%s", suffix))
        .documentsPath(String.format("documents-path-%s", suffix))
        .calibrationDocumentsPath(String.format("calibration-documents-path-%s", suffix))
        .navigationPath(String.format("navigation-path-%s", suffix))
        .sourcePath(String.format("source-path-%s", suffix))
        .siteOrCruiseName(String.format("site-or-cruise-name-%s", suffix))
        .deploymentId(String.format("deployment-id-%s", suffix))
        .datasetPackager(String.format("dataset-packager-%s", suffix))
        .projects(String.format("projects-%s", suffix))
        .publicReleaseDate(DateTranslator.builder()
            .date(String.format("public-release-date-%s", suffix))
            .timeZone(String.format("time-zone-%s", suffix))
            .build())
        .scientists(String.format("scientists-%s", suffix))
        .sponsors(String.format("sponsors-%s", suffix))
        .funders(String.format("funders-%s", suffix))
        .platform(String.format("platform-%s", suffix))
        .instrument(String.format("instrument-%s", suffix))
        .startTime(DefaultTimeTranslator.builder()
            .time(String.format("start-time-%s", suffix))
            .build())
        .endTime(DefaultTimeTranslator.builder()
            .time(String.format("end-time-%s", suffix))
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .date(String.format("pre-deployment-calibration-date-%s", suffix))
            .timeZone(String.format("time-zone-%s", suffix))
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .date(String.format("post-deployment-calibration-date-%s", suffix))
            .timeZone(String.format("time-zone-%s", suffix))
            .build())
        .calibrationDescription(String.format("calibration-description-%s", suffix))
        .deploymentTitle(String.format("deployment-title-%s", suffix))
        .deploymentPurpose(String.format("deployment-purpose-%s", suffix))
        .deploymentDescription(String.format("deployment-descriptions-%s", suffix))
        .alternateSiteName(String.format("alternate-site-name-%s", suffix))
        .alternateDeploymentName(String.format("alternate-deployment-name-%s", suffix))
        .locationDetailTranslator(StationaryTerrestrialLocationTranslator.builder()
            .latitude(String.format("latitude-%s", suffix))
            .longitude(String.format("longitude-%s", suffix))
            .surfaceElevation(String.format("surface-elevation-%s", suffix))
            .instrumentElevation(String.format("instrument-elevation-%s", suffix))
            .build())
        
        .softwareNames(String.format("software-names-%s", suffix))
        .softwareVersions(String.format("software-versions-%s", suffix))
        .softwareProtocolCitation(String.format("software-protocol-citation-%s", suffix))
        .softwareDescription(String.format("software-description-%s", suffix))
        .softwareProcessingDescription(String.format("software-processing-description-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((SoundClipsPackageTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());

    PackageTranslator expectedPackageTranslator = (PackageTranslator) expected;
    PackageTranslator actualPackageTranslator = (PackageTranslator) actual;
    assertEquals(expectedPackageTranslator.getName(), actualPackageTranslator.getName());
    assertEquals(expectedPackageTranslator.getPackageUUID(), actualPackageTranslator.getPackageUUID());
    assertEquals(expectedPackageTranslator.getTemperaturePath(), actualPackageTranslator.getTemperaturePath());
    assertEquals(expectedPackageTranslator.getBiologicalPath(), actualPackageTranslator.getBiologicalPath());
    assertEquals(expectedPackageTranslator.getOtherPath(), actualPackageTranslator.getOtherPath());
    assertEquals(expectedPackageTranslator.getDocumentsPath(), actualPackageTranslator.getDocumentsPath());
    assertEquals(expectedPackageTranslator.getCalibrationDocumentsPath(), actualPackageTranslator.getCalibrationDocumentsPath());
    assertEquals(expectedPackageTranslator.getNavigationPath(), actualPackageTranslator.getNavigationPath());
    assertEquals(expectedPackageTranslator.getSourcePath(), actualPackageTranslator.getSourcePath());
    assertEquals(expectedPackageTranslator.getSiteOrCruiseName(), actualPackageTranslator.getSiteOrCruiseName());
    assertEquals(expectedPackageTranslator.getDeploymentId(), actualPackageTranslator.getDeploymentId());
    assertEquals(expectedPackageTranslator.getDatasetPackager(), actualPackageTranslator.getDatasetPackager());
    assertEquals(expectedPackageTranslator.getProjects(), actualPackageTranslator.getProjects());
    assertEquals(expectedPackageTranslator.getPublicReleaseDate(), actualPackageTranslator.getPublicReleaseDate());
    assertEquals(expectedPackageTranslator.getScientists(), actualPackageTranslator.getScientists());
    assertEquals(expectedPackageTranslator.getSponsors(), actualPackageTranslator.getSponsors());
    assertEquals(expectedPackageTranslator.getFunders(), actualPackageTranslator.getFunders());
    assertEquals(expectedPackageTranslator.getPlatform(), actualPackageTranslator.getPlatform());
    assertEquals(expectedPackageTranslator.getInstrument(), actualPackageTranslator.getInstrument());
    assertEquals(expectedPackageTranslator.getStartTime().getTime(), actualPackageTranslator.getStartTime().getTime());
    assertEquals(expectedPackageTranslator.getEndTime().getTime(), actualPackageTranslator.getEndTime().getTime());
    assertEquals(expectedPackageTranslator.getPreDeploymentCalibrationDate(), actualPackageTranslator.getPreDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getPostDeploymentCalibrationDate(), actualPackageTranslator.getPostDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getCalibrationDescription(), actualPackageTranslator.getCalibrationDescription());
    assertEquals(expectedPackageTranslator.getDeploymentTitle(), actualPackageTranslator.getDeploymentTitle());
    assertEquals(expectedPackageTranslator.getDeploymentPurpose(), actualPackageTranslator.getDeploymentPurpose());
    assertEquals(expectedPackageTranslator.getDeploymentDescription(), actualPackageTranslator.getDeploymentDescription());
    assertEquals(expectedPackageTranslator.getAlternateSiteName(), actualPackageTranslator.getAlternateSiteName());
    assertEquals(expectedPackageTranslator.getAlternateDeploymentName(), actualPackageTranslator.getAlternateDeploymentName());
    
    SoundClipsPackageTranslator expectedSoundClipsTranslator = (SoundClipsPackageTranslator) expectedPackageTranslator;
    SoundClipsPackageTranslator actualSoundClipsTranslator = (SoundClipsPackageTranslator) actualPackageTranslator;
    assertEquals(expectedSoundClipsTranslator.getSoftwareNames(), actualSoundClipsTranslator.getSoftwareNames());
    assertEquals(expectedSoundClipsTranslator.getSoftwareVersions(), actualSoundClipsTranslator.getSoftwareVersions());
    assertEquals(expectedSoundClipsTranslator.getSoftwareProtocolCitation(), actualSoundClipsTranslator.getSoftwareProtocolCitation());
    assertEquals(expectedSoundClipsTranslator.getSoftwareDescription(), actualSoundClipsTranslator.getSoftwareDescription());
    assertEquals(expectedSoundClipsTranslator.getSoftwareProcessingDescription(), actualSoundClipsTranslator.getSoftwareProcessingDescription());
  }
}
