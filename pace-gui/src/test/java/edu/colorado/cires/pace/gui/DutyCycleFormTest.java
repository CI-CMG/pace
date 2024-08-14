package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;

public class DutyCycleFormTest extends AuxiliaryTranslatorFormTest<DutyCycleTranslator, DutyCycleForm> {

  @Override
  protected DutyCycleForm createForm(DutyCycleTranslator translator, String[] headerOptions) {
    return new DutyCycleForm(headerOptions, translator, (f) -> {});
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Duration", "Interval", "Start Time", "End Time", "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(DutyCycleForm form) {
    selectTimeOptions("startTime", "Start Time", "Time Zone");
    selectTimeOptions("endTime", "End Time", "Time Zone");
    selectComboBoxOption("interval", "Interval");
    selectComboBoxOption("duration", "Duration");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(DutyCycleTranslator translator) {
    assertEquals("Duration", translator.getDuration());
    assertEquals("Interval", translator.getInterval());
    TimeTranslator timeTranslator = translator.getStartTime();
    assertEquals("Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertEquals("End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(DutyCycleTranslator translator) {
    assertEquals("Duration", translator.getDuration());
    assertNull(translator.getInterval());
    TimeTranslator timeTranslator = translator.getStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
  }
}
