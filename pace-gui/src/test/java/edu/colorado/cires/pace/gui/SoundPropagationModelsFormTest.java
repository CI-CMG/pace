package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;

public class SoundPropagationModelsFormTest extends AuxiliaryTranslatorFormTest<SoundPropagationModelsPackageTranslator, SoundPropagationModelsForm> {

  @Override
  protected SoundPropagationModelsForm createForm(SoundPropagationModelsPackageTranslator translator, String[] headerOptions) {
    return new SoundPropagationModelsForm(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Modeled Frequency", "Audio Start Time", "Audio End Time", "Time Zone"
    };
  }

  @Override
  protected void populateInitialForm(SoundPropagationModelsForm form) {
    selectComboBoxOption("modeledFrequency", "Modeled Frequency");
    selectTimeOptions("audioStartTime", "Audio Start Time", "Time Zone");
    selectTimeOptions("audioEndTime", "Audio End Time", "Time Zone");
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(SoundPropagationModelsPackageTranslator translator) {
    assertEquals("Modeled Frequency", translator.getModeledFrequency());
    TimeTranslator timeTranslator = translator.getAudioStartTimeTranslator();
    assertEquals("Audio Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioEndTimeTranslator();
    assertEquals("Audio End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(SoundPropagationModelsPackageTranslator translator) {
    assertEquals("Modeled Frequency", translator.getModeledFrequency());
    TimeTranslator timeTranslator = translator.getAudioStartTimeTranslator();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioEndTimeTranslator();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
  }
}