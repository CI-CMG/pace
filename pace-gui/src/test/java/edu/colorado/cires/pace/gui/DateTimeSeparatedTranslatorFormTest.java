package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;

public class DateTimeSeparatedTranslatorFormTest extends AuxiliaryTranslatorFormTest<TimeTranslator, TimeTranslatorForm> {

  @Override
  protected TimeTranslatorForm createForm(TimeTranslator translator, String[] headerOptions) {
    return new TimeTranslatorForm(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Date", "Time", "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(TimeTranslatorForm form) {
    selectComboBoxOption("timeFormat", "Date-Time Separated");
    selectComboBoxOption("date", "Date");
    selectComboBoxOption("time", "Time");
    selectComboBoxOption("timeZone", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(TimeTranslator translator) {
    assertTrue(translator instanceof DateTimeSeparatedTimeTranslator);
    DateTimeSeparatedTimeTranslator timeTranslator = (DateTimeSeparatedTimeTranslator) translator;
    assertEquals("Date", timeTranslator.getDate());
    assertEquals("Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(TimeTranslator translator) {
    assertTrue(translator instanceof DateTimeSeparatedTimeTranslator);
    DateTimeSeparatedTimeTranslator timeTranslator = (DateTimeSeparatedTimeTranslator) translator;
    assertEquals("Date", timeTranslator.getDate());
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
  }
}
