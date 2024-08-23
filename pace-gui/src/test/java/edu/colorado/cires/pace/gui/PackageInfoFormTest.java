package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;

public class PackageInfoFormTest extends AuxiliaryTranslatorFormTest<PackageTranslator, PackageInfoForm> {

  @Override
  protected PackageInfoForm createForm(PackageTranslator translator, String[] headerOptions) {
    return new PackageInfoForm(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "UUID", "Data Collection Name", "Site Or Cruise Name", "Deployment ID", "Projects", "Platform", "Instrument", "Deployment Title",
        "Deployment Purpose", "Deployment Description", "Alternate Site Name", "Alternate Deployment Name", "Processing Level",
        "Start Time", "End Time", "Public Release Date", "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(PackageInfoForm form) {
    selectComboBoxOption("uuid", "UUID");
    selectComboBoxOption("dataCollectionName", "Data Collection Name");
    selectComboBoxOption("siteOrCruiseName", "Site Or Cruise Name");
    selectComboBoxOption("deploymentId", "Deployment ID");
    selectComboBoxOption("projects", "Projects");
    selectComboBoxOption("platform", "Platform");
    selectComboBoxOption("instrument", "Instrument");
    selectComboBoxOption("deploymentTitle", "Deployment Title");
    selectComboBoxOption("deploymentPurpose", "Deployment Purpose");
    selectComboBoxOption("deploymentDescription", "Deployment Description");
    selectComboBoxOption("alternateSiteName", "Alternate Site Name");
    selectComboBoxOption("alternateDeploymentName", "Alternate Deployment Name");
    selectDateOptions("publicReleaseDate", "Public Release Date", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(PackageTranslator translator) {
    assertEquals("UUID", translator.getPackageUUID());
    assertEquals("Data Collection Name", translator.getDataCollectionName());
    assertEquals("Site Or Cruise Name", translator.getSiteOrCruiseName());
    assertEquals("Deployment ID", translator.getDeploymentId());
    assertEquals("Projects", translator.getProjects());
    assertEquals("Instrument", translator.getInstrument());
    assertEquals("Deployment Title", translator.getDeploymentTitle());
    assertEquals("Deployment Purpose", translator.getDeploymentPurpose());
    assertEquals("Deployment Description", translator.getDeploymentDescription());
    assertEquals("Alternate Site Name", translator.getAlternateSiteName());
    assertEquals("Alternate Deployment Name", translator.getAlternateDeploymentName());
    DateTranslator dateTranslator = translator.getPublicReleaseDate();
    assertEquals("Public Release Date", dateTranslator.getDate());
    assertEquals("Time Zone", dateTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(PackageTranslator translator) {
    assertEquals("UUID", translator.getPackageUUID());
    assertNull(translator.getDataCollectionName());
    assertNull(translator.getSiteOrCruiseName());
    assertNull(translator.getDeploymentId());
    assertNull(translator.getProjects());
    assertNull(translator.getInstrument());
    assertNull(translator.getDeploymentTitle());
    assertNull(translator.getDeploymentPurpose());
    assertNull(translator.getDeploymentDescription());
    assertNull(translator.getAlternateSiteName());
    assertNull(translator.getAlternateDeploymentName());
    DateTranslator dateTranslator = translator.getPublicReleaseDate();
    assertNull(dateTranslator.getDate());
    assertNull(dateTranslator.getTimeZone());
  }
}