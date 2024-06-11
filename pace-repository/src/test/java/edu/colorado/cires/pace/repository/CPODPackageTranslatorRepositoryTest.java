package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.translator.GainTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.data.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.data.translator.Translator;
import java.util.Collections;

public class CPODPackageTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return CPODPackageTranslator.builder()
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
        .startTimeTranslator(DefaultTimeTranslator.builder()
            .time(String.format("start-time-%s", suffix))
            .build())
        .endTimeTranslator(DefaultTimeTranslator.builder()
            .time(String.format("end-time-%s", suffix))
            .build())
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

        .instrumentId(String.format("instrumentId-%s", suffix))
        .hydrophoneSensitivity(String.format("hydrophoneSensitivity-%s", suffix))
        .frequencyRange(String.format("frequencyRange-%s", suffix))
        .gain(String.format("gain-%s", suffix))
        .deploymentTimeTranslator(DefaultTimeTranslator.builder()
            .time(String.format("deploymentTime-%s", suffix))
            .build())
        .recoveryTimeTranslator(DefaultTimeTranslator.builder()
            .time(String.format("recoveryTime-%s", suffix))
            .build())
        .comments(String.format("comments-%s", suffix))
        .sensors(String.format("sensors-%s", suffix))
        .qualityControlDetailTranslator(QualityControlDetailTranslator.builder()
            .qualityAnalyst(String.format("quality-analyst-%s", suffix))
            .qualityAnalysisObjectives(String.format("quality-analysis-objectives-%s", suffix))
            .qualityAnalysisMethod(String.format("analysis-method-%s", suffix))
            .qualityAssessmentDescription(String.format("quality-assessment-description-%s", suffix))
            .qualityEntryTranslators(Collections.singletonList(DataQualityEntryTranslator.builder()
                .startTimeTranslator(DefaultTimeTranslator.builder()
                    .time(String.format("quality-start-time-%s", suffix))
                    .build())
                .endTimeTranslator(DefaultTimeTranslator.builder()
                    .time(String.format("quality-end-time-%s", suffix))
                    .build())
                .minFrequency(String.format("min-frequency-%s", suffix))
                .maxFrequency(String.format("max-frequency-%s", suffix))
                .qualityLevel(String.format("quality-level-%s", suffix))
                .comments(String.format("comments-%s", suffix))
                .build()))
            .build())
        .channelTranslators(Collections.singletonList(ChannelTranslator.builder()
            .sensor(String.format("channel-sensor-%s", suffix))
            .startTimeTranslator(DefaultTimeTranslator.builder()
                .time(String.format("channel-start-time-%s", suffix))
                .build())
            .endTimeTranslator(DefaultTimeTranslator.builder()
                .time(String.format("channel-end-time-%s", suffix))
                .build())
            .sampleRateTranslators(Collections.singletonList(SampleRateTranslator.builder()
                .startTimeTranslator(DefaultTimeTranslator.builder()
                    .time(String.format("startTime-%s", suffix))
                    .build())
                .endTimeTranslator(DefaultTimeTranslator.builder()
                    .time(String.format("endTime-%s", suffix))
                    .build())
                .sampleRate(String.format("sampleRate-%s", suffix))
                .sampleBits(String.format("sampleBits-%s", suffix))
                .build()))
            .dutyCycleTranslators(Collections.singletonList(DutyCycleTranslator.builder()
                .startTimeTranslator(DefaultTimeTranslator.builder()
                    .time(String.format("startTime-%s", suffix))
                    .build())
                .endTimeTranslator(DefaultTimeTranslator.builder()
                    .time(String.format("endTime-%s", suffix))
                    .build())
                .duration(String.format("duration-%s", suffix))
                .interval(String.format("interval-%s", suffix))
                .build()))
            .gainTranslators(Collections.singletonList(
                GainTranslator.builder()
                    .startTimeTranslator(DefaultTimeTranslator.builder()
                        .time(String.format("startTime-%s", suffix))
                        .build())
                    .endTimeTranslator(DefaultTimeTranslator.builder()
                        .time(String.format("endTime-%s", suffix))
                        .build())
                    .gain(String.format("gain-%s", suffix))
                    .build()
            ))
            .build()))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((CPODPackageTranslator) object).toBuilder()
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
    assertEquals(((DefaultTimeTranslator) expectedPackageTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualPackageTranslator.getStartTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedPackageTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualPackageTranslator.getEndTimeTranslator()).getTime());
    assertEquals(expectedPackageTranslator.getPreDeploymentCalibrationDate(), actualPackageTranslator.getPreDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getPostDeploymentCalibrationDate(), actualPackageTranslator.getPostDeploymentCalibrationDate());
    assertEquals(expectedPackageTranslator.getCalibrationDescription(), actualPackageTranslator.getCalibrationDescription());
    assertEquals(expectedPackageTranslator.getDeploymentTitle(), actualPackageTranslator.getDeploymentTitle());
    assertEquals(expectedPackageTranslator.getDeploymentPurpose(), actualPackageTranslator.getDeploymentPurpose());
    assertEquals(expectedPackageTranslator.getDeploymentDescription(), actualPackageTranslator.getDeploymentDescription());
    assertEquals(expectedPackageTranslator.getAlternateSiteName(), actualPackageTranslator.getAlternateSiteName());
    assertEquals(expectedPackageTranslator.getAlternateDeploymentName(), actualPackageTranslator.getAlternateDeploymentName());

    CPODPackageTranslator expectedCPODTranslator = (CPODPackageTranslator) expectedPackageTranslator;
    CPODPackageTranslator actualCPODTranslator = (CPODPackageTranslator) actualPackageTranslator;
    assertEquals(expectedCPODTranslator.getInstrumentId(), actualCPODTranslator.getInstrumentId());
    assertEquals(expectedCPODTranslator.getHydrophoneSensitivity(), actualCPODTranslator.getHydrophoneSensitivity());
    assertEquals(expectedCPODTranslator.getFrequencyRange(), actualCPODTranslator.getFrequencyRange());
    assertEquals(expectedCPODTranslator.getGain(), actualCPODTranslator.getGain());
    assertEquals(((DefaultTimeTranslator) expectedCPODTranslator.getDeploymentTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualCPODTranslator.getDeploymentTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedCPODTranslator.getRecoveryTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualCPODTranslator.getRecoveryTimeTranslator()).getTime());
    assertEquals(expectedCPODTranslator.getComments(), actualCPODTranslator.getComments());
    assertEquals(expectedCPODTranslator.getSensors(), actualCPODTranslator.getSensors());

    QualityControlDetailTranslator expectedQCTranslator = expectedCPODTranslator.getQualityControlDetailTranslator();
    QualityControlDetailTranslator actualQCTranslator = actualCPODTranslator.getQualityControlDetailTranslator();
    assertEquals(expectedQCTranslator.getQualityAnalyst(), actualQCTranslator.getQualityAnalyst());
    assertEquals(expectedQCTranslator.getQualityAnalysisObjectives(), actualQCTranslator.getQualityAnalysisObjectives());
    assertEquals(expectedQCTranslator.getQualityAnalysisMethod(), actualQCTranslator.getQualityAnalysisMethod());
    assertEquals(expectedQCTranslator.getQualityAssessmentDescription(), actualQCTranslator.getQualityAssessmentDescription());

    DataQualityEntryTranslator expectedEntryTranslator = expectedQCTranslator.getQualityEntryTranslators().get(0);
    DataQualityEntryTranslator actualEntryTranslator = actualQCTranslator.getQualityEntryTranslators().get(0);
    assertEquals(((DefaultTimeTranslator) expectedEntryTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualEntryTranslator.getStartTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedEntryTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualEntryTranslator.getEndTimeTranslator()).getTime());
    assertEquals(expectedEntryTranslator.getMinFrequency(), actualEntryTranslator.getMinFrequency());
    assertEquals(expectedEntryTranslator.getMaxFrequency(), actualEntryTranslator.getMaxFrequency());
    assertEquals(expectedEntryTranslator.getQualityLevel(), actualEntryTranslator.getQualityLevel());
    assertEquals(expectedEntryTranslator.getComments(), actualEntryTranslator.getComments());

    ChannelTranslator expectedChannelTranslator = expectedCPODTranslator.getChannelTranslators().get(0);
    ChannelTranslator actualChannelTranslator = actualCPODTranslator.getChannelTranslators().get(0);
    assertEquals(expectedChannelTranslator.getSensor(), actualChannelTranslator.getSensor());
    assertEquals(((DefaultTimeTranslator) expectedChannelTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualChannelTranslator.getStartTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedChannelTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualChannelTranslator.getEndTimeTranslator()).getTime());

    SampleRateTranslator expectedSampleRateTranslator = expectedChannelTranslator.getSampleRateTranslators().get(0);
    SampleRateTranslator actualSampleRateTranslator = actualChannelTranslator.getSampleRateTranslators().get(0);
    assertEquals(((DefaultTimeTranslator) expectedSampleRateTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualSampleRateTranslator.getStartTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedSampleRateTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualSampleRateTranslator.getEndTimeTranslator()).getTime());
    assertEquals(expectedSampleRateTranslator.getSampleRate(), actualSampleRateTranslator.getSampleRate());
    assertEquals(expectedSampleRateTranslator.getSampleBits(), actualSampleRateTranslator.getSampleBits());

    DutyCycleTranslator expectedDutyCycleTranslator = expectedChannelTranslator.getDutyCycleTranslators().get(0);
    DutyCycleTranslator actualDutyCycleTranslator = actualChannelTranslator.getDutyCycleTranslators().get(0);
    assertEquals(((DefaultTimeTranslator) expectedDutyCycleTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualDutyCycleTranslator.getStartTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedDutyCycleTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualDutyCycleTranslator.getEndTimeTranslator()).getTime());
    assertEquals(expectedDutyCycleTranslator.getDuration(), actualDutyCycleTranslator.getDuration());
    assertEquals(expectedDutyCycleTranslator.getInterval(), actualDutyCycleTranslator.getInterval());

    GainTranslator expectedGainTranslator = expectedChannelTranslator.getGainTranslators().get(0);
    GainTranslator actualGainTranslator = actualChannelTranslator.getGainTranslators().get(0);
    assertEquals(((DefaultTimeTranslator) expectedGainTranslator.getStartTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualGainTranslator.getStartTimeTranslator()).getTime());
    assertEquals(((DefaultTimeTranslator) expectedGainTranslator.getEndTimeTranslator()).getTime(), ((DefaultTimeTranslator) actualGainTranslator.getEndTimeTranslator()).getTime());
    assertEquals(expectedGainTranslator.getGain(), actualGainTranslator.getGain());
  }
}
