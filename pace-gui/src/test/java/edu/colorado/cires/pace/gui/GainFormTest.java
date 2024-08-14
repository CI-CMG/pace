package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;

public class GainFormTest extends AuxiliaryTranslatorFormTest<GainTranslator, GainForm>{

  @Override
  protected GainForm createForm(GainTranslator translator, String[] headerOptions) {
    return new GainForm(headerOptions, translator, (f) -> {});
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Gain", "Start Time", "End Time", "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(GainForm form) {
    selectComboBoxOption("gain", "Gain");
    selectTimeOptions("startTime", "Start Time", "Time Zone");
    selectTimeOptions("endTime", "End Time", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(GainTranslator translator) {
    assertEquals("Gain", translator.getGain());
    TimeTranslator timeTranslator = translator.getStartTime();
    assertEquals("Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertEquals("End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(GainTranslator translator) {
    assertEquals("Gain", translator.getGain());
    TimeTranslator timeTranslator = translator.getStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
  }
}
