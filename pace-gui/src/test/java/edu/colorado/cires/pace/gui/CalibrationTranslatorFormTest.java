package edu.colorado.cires.pace.gui;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;

public class CalibrationTranslatorFormTest extends AuxiliaryTranslatorFormTest<PackageTranslator, CalibrationTranslatorForm> {

  @Override
  protected CalibrationTranslatorForm createForm(PackageTranslator translator, String[] headerOptions) {
    return new CalibrationTranslatorForm(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Calibration Documents Path", "Calibration Description", "Pre-Deployment Calibration Date", "Post-Deployment Calibration Date",
        "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(CalibrationTranslatorForm form) {
    selectComboBoxOption("calibrationDocumentsPath", "Calibration Documents Path");
    selectComboBoxOption("calibrationDescription", "Calibration Description");
    selectDateOptions("preDeploymentCalibrationDate", "Pre-Deployment Calibration Date", "Time Zone");
    selectDateOptions("postDeploymentCalibrationDate", "Post-Deployment Calibration Date", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(PackageTranslator translator) {
    assertEquals("Calibration Documents Path", translator.getCalibrationDocumentsPath());
    assertEquals("Calibration Description", translator.getCalibrationDescription());
    DateTranslator dateTranslator = translator.getPreDeploymentCalibrationDate();
    assertEquals("Pre-Deployment Calibration Date", dateTranslator.getDate());
    assertEquals("Time Zone", dateTranslator.getTimeZone());
    dateTranslator = translator.getPostDeploymentCalibrationDate();
    assertEquals("Post-Deployment Calibration Date", dateTranslator.getDate());
    assertEquals("Time Zone", dateTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(PackageTranslator translator) {
    assertEquals("Calibration Documents Path", translator.getCalibrationDocumentsPath());
    assertNull(translator.getCalibrationDescription());
    DateTranslator dateTranslator = translator.getPreDeploymentCalibrationDate();
    assertNull(dateTranslator.getDate());
    assertNull(dateTranslator.getTimeZone());
    dateTranslator = translator.getPostDeploymentCalibrationDate();
    assertNull(dateTranslator.getDate());
    assertNull(dateTranslator.getTimeZone());
  }
}