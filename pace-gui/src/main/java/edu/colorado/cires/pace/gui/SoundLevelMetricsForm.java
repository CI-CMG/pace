package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;

import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 * SoundLevelMetricsForm extends JPanel and provides structure
 * relevant to sound level metrics forms
 */
public class SoundLevelMetricsForm extends JPanel {
  
  private final TimeTranslatorForm audioStartTimeForm;
  private final TimeTranslatorForm audioEndTimeForm;
  private final TimeTranslatorForm analysisStartTimeForm;
  private final TimeTranslatorForm analysisEndTimeForm;

  /**
   * Creates a sound level metrics form
   * @param headerOptions headers to select from during mapping
   * @param initialTranslator translator to build upon
   */
  public SoundLevelMetricsForm(String[] headerOptions, SoundLevelMetricsPackageTranslator initialTranslator) {
    this.audioStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioStartTimeTranslator());
    this.audioEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioEndTimeTranslator());
    this.analysisStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.analysisEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());
    addFields();
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    audioStartTimeForm.setBorder(createEtchedBorder("Audio Start Time"));
    add(audioStartTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    audioEndTimeForm.setBorder(createEtchedBorder("Audio End Time"));
    add(audioEndTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 0; c.weightx = 1;
    }));
    analysisStartTimeForm.setBorder(createEtchedBorder("Analysis Start Time"));
    add(analysisStartTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1;
    }));
    analysisEndTimeForm.setBorder(createEtchedBorder("Analysis End Time"));
    add(analysisEndTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 1; c.weightx = 1;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weighty = 1;
    }));
  }

  /**
   * Changes headers to select from during mapping
   * @param headerOptions new headers to select from
   */
  public void updateHeaderOptions(String[] headerOptions) {
    audioStartTimeForm.updateHeaderOptions(headerOptions);
    audioEndTimeForm.updateHeaderOptions(headerOptions);
    analysisStartTimeForm.updateHeaderOptions(headerOptions);
    analysisEndTimeForm.updateHeaderOptions(headerOptions);
  }

  /**
   * Returns audio start time translator
   * @return TimeTranslator audio start time translator
   */
  public TimeTranslator getAudioStartTimeTranslator() {
    return audioStartTimeForm.toTranslator();
  }

  /**
   * Returns audio end time translator
   * @return TimeTranslator audio end time translator
   */
  public TimeTranslator getAudioEndTimeTranslator() {
    return audioEndTimeForm.toTranslator();
  }

  /**
   * Returns start time
   * @return TimeTranslator start time
   */
  public TimeTranslator getStartTime() { return analysisStartTimeForm.toTranslator(); }

  /**
   * Returns end time
   * @return TimeTranslator end time
   */
  public TimeTranslator getEndTime() { return analysisStartTimeForm.toTranslator(); }
}
