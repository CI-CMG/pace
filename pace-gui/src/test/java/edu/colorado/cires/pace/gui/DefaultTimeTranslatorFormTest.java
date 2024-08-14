package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;

public class DefaultTimeTranslatorFormTest extends AuxiliaryTranslatorFormTest<TimeTranslator, TimeTranslatorForm> {


  @Override
  protected TimeTranslatorForm createForm(TimeTranslator translator, String[] headerOptions) {
    return new TimeTranslatorForm(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Time", "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(TimeTranslatorForm form) {
    selectComboBoxOption("time", "Time");
    selectComboBoxOption("timeZone", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(TimeTranslator translator) {
    assertTrue(translator instanceof DefaultTimeTranslator);
    assertEquals("Time", translator.getTime());
    assertEquals("Time Zone", translator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(TimeTranslator translator) {
    assertTrue(translator instanceof DefaultTimeTranslator);
    assertEquals("Time", translator.getTime());
    assertNull(translator.getTimeZone());
  }
}
