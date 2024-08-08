package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.Collections;

public class DetectionsPackageTranslatorTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return DetectionsPackageTranslator.builder()
        .name(String.format("name-%s", suffix))
        .packageUUID(String.format("package-uuid-%s", suffix))
        .temperaturePath(String.format("temperature-path-%s", suffix))
        .biologicalPath(String.format("biological-path-%s", suffix))
        .otherPath(String.format("other-path-%s", suffix))
        .documentsPath(String.format("documents-path-%s", suffix))
        .calibrationDocumentsPath(String.format("calibration-documents-path-%s", suffix))
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
        
        .soundSource(String.format("sound-source-%s", suffix))
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst(String.format("quality-analyst-%s", suffix))
            .qualityAnalysisObjectives(String.format("quality-analysis-objectives-%s", suffix))
            .qualityAnalysisMethod(String.format("analysis-method-%s", suffix))
            .qualityAssessmentDescription(String.format("quality-assessment-description-%s", suffix))
            .qualityEntryTranslators(Collections.singletonList(DataQualityEntryTranslator.builder()
                .startTime(DefaultTimeTranslator.builder()
                    .time(String.format("quality-start-time-%s", suffix))
                    .build())
                .endTime(DefaultTimeTranslator.builder()
                    .time(String.format("quality-end-time-%s", suffix))
                    .build())
                .minFrequency(String.format("min-frequency-%s", suffix))
                .maxFrequency(String.format("max-frequency-%s", suffix))
                .qualityLevel(String.format("quality-level-%s", suffix))
                .comments(String.format("comments-%s", suffix))
                .build()))
            .build())
        .softwareNames(String.format("softwareNames-%s", suffix))
        .softwareVersions(String.format("softwareVersions-%s", suffix))
        .softwareProtocolCitation(String.format("softwareProtocolCitation-%s", suffix))
        .softwareDescription(String.format("softwareDescription-%s", suffix))
        .softwareProcessingDescription(String.format("softwareProcessingDescription-%s", suffix))
        .analysisTimeZone(String.format("analysisTimeZone-%s", suffix))
        .analysisEffort(String.format("analysisEffort-%s", suffix))
        .sampleRate(String.format("sampleRate-%s", suffix))
        .minFrequency(String.format("minFrequency-%s", suffix))
        .maxFrequency(String.format("maxFrequency-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((DetectionsPackageTranslator) object).toBuilder()
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
    assertEquals(((DefaultTimeTranslator) expectedPackageTranslator.getStartTime()).getTime(), ((DefaultTimeTranslator) actualPackageTranslator.getStartTime()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedPackageTranslator.getEndTime()).getTime(), ((DefaultTimeTranslator) actualPackageTranslator.getEndTime()).getTime());
    assertEquals(expectedPackageTranslator.getPreDeploymentCalibrationDate(), actualPackageTranslator.getPreDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getPostDeploymentCalibrationDate(), actualPackageTranslator.getPostDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getCalibrationDescription(), actualPackageTranslator.getCalibrationDescription());
    assertEquals(expectedPackageTranslator.getDeploymentTitle(), actualPackageTranslator.getDeploymentTitle());
    assertEquals(expectedPackageTranslator.getDeploymentPurpose(), actualPackageTranslator.getDeploymentPurpose());
    assertEquals(expectedPackageTranslator.getDeploymentDescription(), actualPackageTranslator.getDeploymentDescription());
    assertEquals(expectedPackageTranslator.getAlternateSiteName(), actualPackageTranslator.getAlternateSiteName());
    assertEquals(expectedPackageTranslator.getAlternateDeploymentName(), actualPackageTranslator.getAlternateDeploymentName());

    DetectionsPackageTranslator expectedDetectionsTranslator = (DetectionsPackageTranslator) expected;
    DetectionsPackageTranslator actualDetectionsTranslator = (DetectionsPackageTranslator) actual;
    assertEquals(expectedDetectionsTranslator.getSoundSource(), actualDetectionsTranslator.getSoundSource());
    assertEquals(expectedDetectionsTranslator.getSoftwareNames(), actualDetectionsTranslator.getSoftwareNames());
    assertEquals(expectedDetectionsTranslator.getSoftwareVersions(), actualDetectionsTranslator.getSoftwareVersions());
    assertEquals(expectedDetectionsTranslator.getSoftwareProtocolCitation(), actualDetectionsTranslator.getSoftwareProtocolCitation());
    assertEquals(expectedDetectionsTranslator.getSoftwareDescription(), actualDetectionsTranslator.getSoftwareDescription());
    assertEquals(expectedDetectionsTranslator.getSoftwareProcessingDescription(), actualDetectionsTranslator.getSoftwareProcessingDescription());
    assertEquals(expectedDetectionsTranslator.getAnalysisTimeZone(), actualDetectionsTranslator.getAnalysisTimeZone());
    assertEquals(expectedDetectionsTranslator.getAnalysisEffort(), actualDetectionsTranslator.getAnalysisEffort());
    assertEquals(expectedDetectionsTranslator.getSampleRate(), actualDetectionsTranslator.getSampleRate());
    assertEquals(expectedDetectionsTranslator.getMinFrequency(), actualDetectionsTranslator.getMinFrequency());
    assertEquals(expectedDetectionsTranslator.getMaxFrequency(), actualDetectionsTranslator.getMaxFrequency());
    
    
    QualityControlDetailTranslator expectedQCTranslator = expectedDetectionsTranslator.getQualityControlDetailTranslator();
    QualityControlDetailTranslator actualQCTranslator = actualDetectionsTranslator.getQualityControlDetailTranslator();
    assertEquals(expectedQCTranslator.getQualityAnalyst(), actualQCTranslator.getQualityAnalyst());
    assertEquals(expectedQCTranslator.getQualityAnalysisObjectives(), actualQCTranslator.getQualityAnalysisObjectives());
    assertEquals(expectedQCTranslator.getQualityAnalysisMethod(), actualQCTranslator.getQualityAnalysisMethod());
    assertEquals(expectedQCTranslator.getQualityAssessmentDescription(), actualQCTranslator.getQualityAssessmentDescription());

    DataQualityEntryTranslator expectedEntryTranslator = expectedQCTranslator.getQualityEntryTranslators().get(0);
    DataQualityEntryTranslator actualEntryTranslator = actualQCTranslator.getQualityEntryTranslators().get(0);
    assertEquals(((DefaultTimeTranslator) expectedEntryTranslator.getStartTime()).getTime(), ((DefaultTimeTranslator) actualEntryTranslator.getStartTime()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedEntryTranslator.getEndTime()).getTime(), ((DefaultTimeTranslator) actualEntryTranslator.getEndTime()).getTime());
    assertEquals(expectedEntryTranslator.getMinFrequency(), actualEntryTranslator.getMinFrequency());
    assertEquals(expectedEntryTranslator.getMaxFrequency(), actualEntryTranslator.getMaxFrequency());
    assertEquals(expectedEntryTranslator.getQualityLevel(), actualEntryTranslator.getQualityLevel());
    assertEquals(expectedEntryTranslator.getComments(), actualEntryTranslator.getComments());
  }
}
