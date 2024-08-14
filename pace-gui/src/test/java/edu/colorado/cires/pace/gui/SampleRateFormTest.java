package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;

public class SampleRateFormTest extends AuxiliaryTranslatorFormTest<SampleRateTranslator, SampleRateForm> {

  @Override
  protected SampleRateForm createForm(SampleRateTranslator translator, String[] headerOptions) {
    return new SampleRateForm(headerOptions, translator, (f) -> {});
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Sample Bits", "Start Time", "End Time", "Time Zone", "Sample Rate"
    };
  }

  @Override
  protected void populateInitialForm(SampleRateForm form) {
    selectComboBoxOption("sampleRate", "Sample Rate");
    selectComboBoxOption("sampleBits", "Sample Bits");
    selectTimeOptions("startTime", "Start Time", "Time Zone");
    selectTimeOptions("endTime", "End Time", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(SampleRateTranslator translator) {
    assertEquals("Sample Rate", translator.getSampleRate());
    assertEquals("Sample Bits", translator.getSampleBits());
    TimeTranslator timeTranslator = translator.getStartTime();
    assertEquals("Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertEquals("End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(SampleRateTranslator translator) {
    assertNull(translator.getSampleRate());
    assertEquals("Sample Bits", translator.getSampleBits());
    TimeTranslator timeTranslator = translator.getStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
  }
}
