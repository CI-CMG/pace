package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.SoftwareDependentPackageTranslator;

public class SoftwareFormTest extends AuxiliaryTranslatorFormTest<SoftwareDependentPackageTranslator, SoftwareForm<?>> {

  @Override
  protected SoftwareForm<?> createForm(SoftwareDependentPackageTranslator translator, String[] headerOptions) {
    return new SoftwareForm<>(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Software Names", "Software Versions", "Software Protocol Citation", 
        "Software Description", "Software Processing Description"
    };
  }

  @Override
  protected void populateInitialForm(SoftwareForm<?> form) {
    selectComboBoxOption("softwareNames", "Software Names");
    selectComboBoxOption("softwareVersions", "Software Versions");
    selectComboBoxOption("softwareProtocolCitation", "Software Protocol Citation");
    selectComboBoxOption("softwareDescription", "Software Description");
    selectComboBoxOption("softwareProcessingDescription", "Software Processing Description");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(SoftwareDependentPackageTranslator translator) {
    assertEquals("Software Names", translator.getSoftwareNames());
    assertEquals("Software Versions", translator.getSoftwareVersions());
    assertEquals("Software Protocol Citation", translator.getSoftwareProtocolCitation());
    assertEquals("Software Description", translator.getSoftwareDescription());
    assertEquals("Software Processing Description", translator.getSoftwareProcessingDescription());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(SoftwareDependentPackageTranslator translator) {
    assertEquals("Software Names", translator.getSoftwareNames());
    assertNull(translator.getSoftwareVersions());
    assertNull(translator.getSoftwareProtocolCitation());
    assertNull(translator.getSoftwareDescription());
    assertNull(translator.getSoftwareProcessingDescription());
  }
}