package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.data.translator.SoundPropagationModelsPackageTranslator;
import edu.colorado.cires.pace.data.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class SoundPropagationModelsPackageTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return SoundPropagationModelsPackageTranslator.builder()
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
        .publicReleaseDate(String.format("public-release-date-%s", suffix))
        .scientists(String.format("scientists-%s", suffix))
        .sponsors(String.format("sponsors-%s", suffix))
        .funders(String.format("funders-%s", suffix))
        .platform(String.format("platform-%s", suffix))
        .instrument(String.format("instrument-%s", suffix))
        .startTime(String.format("start-time-%s", suffix))
        .endTime(String.format("end-time-%s", suffix))
        .preDeploymentCalibrationDate(String.format("pre-deployment-calibration-date-%s", suffix))
        .postDeploymentCalibrationDate(String.format("post-deployment-calibration-date-%s", suffix))
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

        .modeledFrequency(String.format("modeledFrequency-%s", suffix))
        .softwareNames(String.format("softwareNames-%s", suffix))
        .softwareVersions(String.format("softwareVersions-%s", suffix))
        .softwareProtocolCitation(String.format("softwareProtocolCitation-%s", suffix))
        .softwareDescription(String.format("softwareDescription-%s", suffix))
        .softwareProcessingDescription(String.format("softwareProcessingDescription-%s", suffix))
        .audioStartTime(String.format("audioStartTime-%s", suffix))
        .audioEndTime(String.format("audioEndTime-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((SoundPropagationModelsPackageTranslator) object).toBuilder()
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
    assertEquals(expectedPackageTranslator.getStartTime(), actualPackageTranslator.getStartTime());
    assertEquals(expectedPackageTranslator.getEndTime(), actualPackageTranslator.getEndTime());
    assertEquals(expectedPackageTranslator.getPreDeploymentCalibrationDate(), actualPackageTranslator.getPreDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getPostDeploymentCalibrationDate(), actualPackageTranslator.getPostDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getCalibrationDescription(), actualPackageTranslator.getCalibrationDescription());
    assertEquals(expectedPackageTranslator.getDeploymentTitle(), actualPackageTranslator.getDeploymentTitle());
    assertEquals(expectedPackageTranslator.getDeploymentPurpose(), actualPackageTranslator.getDeploymentPurpose());
    assertEquals(expectedPackageTranslator.getDeploymentDescription(), actualPackageTranslator.getDeploymentDescription());
    assertEquals(expectedPackageTranslator.getAlternateSiteName(), actualPackageTranslator.getAlternateSiteName());
    assertEquals(expectedPackageTranslator.getAlternateDeploymentName(), actualPackageTranslator.getAlternateDeploymentName());

    SoundPropagationModelsPackageTranslator expectedPropagationTranslator = (SoundPropagationModelsPackageTranslator) expectedPackageTranslator;
    SoundPropagationModelsPackageTranslator actualPropagationTranslator = (SoundPropagationModelsPackageTranslator) actualPackageTranslator;
    assertEquals(expectedPropagationTranslator.getModeledFrequency(), actualPropagationTranslator.getModeledFrequency());
    assertEquals(expectedPropagationTranslator.getSoftwareNames(), actualPropagationTranslator.getSoftwareNames());
    assertEquals(expectedPropagationTranslator.getSoftwareVersions(), actualPropagationTranslator.getSoftwareVersions());
    assertEquals(expectedPropagationTranslator.getSoftwareProtocolCitation(), actualPropagationTranslator.getSoftwareProtocolCitation());
    assertEquals(expectedPropagationTranslator.getSoftwareDescription(), actualPropagationTranslator.getSoftwareDescription());
    assertEquals(expectedPropagationTranslator.getSoftwareProcessingDescription(), actualPropagationTranslator.getSoftwareProcessingDescription());
    assertEquals(expectedPropagationTranslator.getAudioStartTime(), actualPropagationTranslator.getAudioStartTime());
    assertEquals(expectedPropagationTranslator.getAudioEndTime(), actualPropagationTranslator.getAudioEndTime());
  }
}
