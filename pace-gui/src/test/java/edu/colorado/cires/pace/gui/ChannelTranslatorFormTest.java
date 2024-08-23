package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PositionTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

public class ChannelTranslatorFormTest extends AuxiliaryTranslatorFormTest<ChannelTranslator, ChannelTranslatorForm> {

  @Override
  protected ChannelTranslatorForm createForm(ChannelTranslator translator, String[] headerOptions) {
    return new ChannelTranslatorForm(headerOptions, translator, (f) -> {});
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Start Time", "End Time",
        "Sample Rate Start Time", "Sample Rate End Time", "Sample Rate Sample Rate", "Sample Rate Sample Bits",
        "Duty Cycle Start Time", "Duty Cycle End Time", "Duty Cycle Duration", "Duty Cycle Interval",
        "Gain Start Time", "Gain End Time", "Gain Gain",
        "Time Zone"
    };
  }

  @Override
  public void testForm() {
    super.testForm();

    clickButton(
        getSampleRateForm(
            getPanel("sampleRates"),
            0
        ),
        "Remove Sample Rate"
    );
    clickButton(
        getDutyCycleForm(
            getPanel("dutyCycles"),
            0
        ),
        "Remove Duty Cycle"
    );
    clickButton(
        getGainForm(
            getPanel("gains"),
            0
        ),
        "Remove Gain"
    );
    
    assertTranslatorEqualsHeaderOptionsWithoutNestedItems(
        getTranslator()
    );
  }

  @Override
  protected void populateInitialForm(ChannelTranslatorForm form) {
    selectTimeOptions("startTime", "Start Time", "Time Zone");
    selectTimeOptions("endTime", "End Time", "Time Zone");
    JPanel sampleRates = getPanel("sampleRates");
    clickButton(sampleRates, "Add Sample Rate");
    selectSampleRateOptions(0, sampleRates, "Sample Rate Start Time", "Sample Rate End Time", "Sample Rate Sample Rate", "Sample Rate Sample Bits", "Time Zone");
    JPanel dutyCycles = getPanel("dutyCycles");
    clickButton(dutyCycles, "Add Duty Cycle");
    selectDutyCycleOptions(0, dutyCycles, "Duty Cycle Start Time", "Duty Cycle End Time", "Duty Cycle Duration", "Duty Cycle Interval", "Time Zone");
    JPanel gains = getPanel("gains");
    clickButton(gains, "Add Gain");
    selectGainOptions(0, gains, "Gain Start Time", "Gain End Time", "Gain Gain", "Time Zone");
  }

  private void selectSampleRateOptions(int sampleRateNumber, JPanel sampleRates, String sampleRateStartTime, String sampleRateEndTime, String sampleRateSampleRate,
      String sampleRateSampleBits, String timeZone) {
    SampleRateForm sampleRateForm = getSampleRateForm(sampleRates, sampleRateNumber);
    selectTimeOptions(sampleRateForm, "startTime", sampleRateStartTime, timeZone);
    selectTimeOptions(sampleRateForm, "endTime", sampleRateEndTime, timeZone);
    selectComboBoxOption(sampleRateForm, "sampleRate", sampleRateSampleRate);
    selectComboBoxOption(sampleRateForm, "sampleBits", sampleRateSampleBits);
  }
  
  private SampleRateForm getSampleRateForm(JPanel sampleRates, int sampleRateNumber) {
    List<SampleRateForm> sampleRateForms = Arrays.stream(sampleRates.getComponents())
        .filter(c -> c instanceof JPanel)
        .map(c -> Arrays.asList(((JPanel) c).getComponents()))
        .flatMap(List::stream)
        .filter(c -> c instanceof CollapsiblePanel<?>)
        .map(c -> (CollapsiblePanel<?>) c)
        .map(CollapsiblePanel::getContentPanel)
        .filter(c -> c instanceof SampleRateForm)
        .map(c -> (SampleRateForm) c)
        .toList();

    return sampleRateForms.get(sampleRateNumber);
  }
  
  private void selectDutyCycleOptions(int dutyCycleNumber, JPanel dutyCycles, String dutyCycleStartTime, String dutyCycleEndTime, String dutyCycleDuration, String dutyCycleInterval, String timeZone) {
    DutyCycleForm dutyCycleForm = getDutyCycleForm(dutyCycles, dutyCycleNumber);
    selectTimeOptions(dutyCycleForm, "startTime", dutyCycleStartTime, timeZone);
    selectTimeOptions(dutyCycleForm, "endTime", dutyCycleEndTime, timeZone);
    selectComboBoxOption(dutyCycleForm, "duration", dutyCycleDuration);
    selectComboBoxOption(dutyCycleForm, "interval", dutyCycleInterval);
  }

  private DutyCycleForm getDutyCycleForm(JPanel dutyCycles, int dutyCycleNumber) {
    List<DutyCycleForm> dutyCycleForms = Arrays.stream(dutyCycles.getComponents())
        .filter(c -> c instanceof JPanel)
        .map(c -> Arrays.asList(((JPanel) c).getComponents()))
        .flatMap(List::stream)
        .filter(c -> c instanceof CollapsiblePanel<?>)
        .map(c -> (CollapsiblePanel<?>) c)
        .map(CollapsiblePanel::getContentPanel)
        .filter(c -> c instanceof DutyCycleForm)
        .map(c -> (DutyCycleForm) c)
        .toList();

    return dutyCycleForms.get(dutyCycleNumber);
  }

  private void selectGainOptions(int gainNumber, JPanel gains, String gainStartTime, String gainEndTime, String gainGain, String timeZone) {
    GainForm gainForm = getGainForm(gains, gainNumber);
    selectTimeOptions(gainForm, "startTime", gainStartTime, timeZone);
    selectTimeOptions(gainForm, "endTime", gainEndTime, timeZone);
    selectComboBoxOption(gainForm, "gain", gainGain);
  }

  private GainForm getGainForm(JPanel gains, int gainNumber) {
    List<GainForm> gainForms = Arrays.stream(gains.getComponents())
        .filter(c -> c instanceof JPanel)
        .map(c -> Arrays.asList(((JPanel) c).getComponents()))
        .flatMap(List::stream)
        .filter(c -> c instanceof CollapsiblePanel<?>)
        .map(c -> (CollapsiblePanel<?>) c)
        .map(CollapsiblePanel::getContentPanel)
        .filter(c -> c instanceof GainForm)
        .map(c -> ((GainForm) c))
        .toList();

    return gainForms.get(gainNumber);
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(ChannelTranslator translator) {
    TimeTranslator timeTranslator = translator.getStartTime();
    assertEquals("Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertEquals("End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    assertEquals(1, translator.getSampleRates().size());
    SampleRateTranslator sampleRateTranslator = translator.getSampleRates().get(0);
    assertEquals("Sample Rate Sample Rate", sampleRateTranslator.getSampleRate());
    assertEquals("Sample Rate Sample Bits", sampleRateTranslator.getSampleBits());
    timeTranslator = sampleRateTranslator.getStartTime();
    assertEquals("Sample Rate Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = sampleRateTranslator.getEndTime();
    assertEquals("Sample Rate End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    assertEquals(1, translator.getDutyCycles().size());
    DutyCycleTranslator dutyCycleTranslator = translator.getDutyCycles().get(0);
    assertEquals("Duty Cycle Duration", dutyCycleTranslator.getDuration());
    assertEquals("Duty Cycle Interval", dutyCycleTranslator.getInterval());
    timeTranslator = dutyCycleTranslator.getStartTime();
    assertEquals("Duty Cycle Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = dutyCycleTranslator.getEndTime();
    assertEquals("Duty Cycle End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    assertEquals(1, translator.getGains().size());
    GainTranslator gainTranslator = translator.getGains().get(0);
    assertEquals("Gain Gain", gainTranslator.getGain());
    timeTranslator = gainTranslator.getStartTime();
    assertEquals("Gain Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = gainTranslator.getEndTime();
    assertEquals("Gain End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(ChannelTranslator translator) {
    TimeTranslator timeTranslator = translator.getStartTime();
    assertEquals("Start Time", timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    assertEquals(1, translator.getSampleRates().size());
    SampleRateTranslator sampleRateTranslator = translator.getSampleRates().get(0);
    assertNull(sampleRateTranslator.getSampleRate());
    assertNull(sampleRateTranslator.getSampleBits());
    timeTranslator = sampleRateTranslator.getStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = sampleRateTranslator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    assertEquals(1, translator.getDutyCycles().size());
    DutyCycleTranslator dutyCycleTranslator = translator.getDutyCycles().get(0);
    assertNull(dutyCycleTranslator.getDuration());
    assertNull(dutyCycleTranslator.getInterval());
    timeTranslator = dutyCycleTranslator.getStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = dutyCycleTranslator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    assertEquals(1, translator.getGains().size());
    GainTranslator gainTranslator = translator.getGains().get(0);
    assertNull(gainTranslator.getGain());
    timeTranslator = gainTranslator.getStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = gainTranslator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
  }

  protected void assertTranslatorEqualsHeaderOptionsWithoutNestedItems(ChannelTranslator translator) {
    TimeTranslator timeTranslator = translator.getStartTime();
    assertEquals("Start Time", timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    assertEquals(0, translator.getSampleRates().size());
    assertEquals(0, translator.getDutyCycles().size());
    assertEquals(0, translator.getGains().size());
  }
}
