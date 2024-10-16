package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundClips.translator.SoundClipsPackageTranslator;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 * SoundClipsForm extends JPanel and provides structure relevant
 * to sound clips forms
 */
public class SoundClipsForm extends JPanel {
  private final TimeTranslatorForm audioStartTimeForm;
  private final TimeTranslatorForm audioEndTimeForm;
  private final TimeTranslatorForm clipStartTimeForm;
  private final TimeTranslatorForm clipEndTimeForm;

  /**
   * Creates sound clips form
   * @param headerOptions headers to select from during mapping
   * @param initialTranslator translator to build upon
   */
  public SoundClipsForm(String[] headerOptions, SoundClipsPackageTranslator initialTranslator) {
    this.audioStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioStartTime());
    this.audioEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioEndTime());
    this.clipStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.clipEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());

    addFields();
  }

  private void addFields() {
    setLayout(new GridBagLayout());
    
    audioStartTimeForm.setBorder(createEtchedBorder("Audio Start Time"));
    add(audioStartTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    audioEndTimeForm.setBorder(createEtchedBorder("Audio End Time"));
    add(audioEndTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 2; c.weightx = 1;
    }));
    clipStartTimeForm.setBorder(createEtchedBorder("Clip Start Time"));
    add(clipStartTimeForm, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    clipEndTimeForm.setBorder(createEtchedBorder("Clip End Time"));
    add(clipEndTimeForm, configureLayout((c) -> { c.gridx = 1; c.gridy = 3; c.weightx = 1; }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weighty = 1;
    }));
  }

  /**
   * Changes headers to select from during mapping
   * @param headerOptions new headers to select from
   */
  public void updateHeaderOptions(String[] headerOptions) {
    audioStartTimeForm.updateHeaderOptions(headerOptions);
    audioEndTimeForm.updateHeaderOptions(headerOptions);
    clipStartTimeForm.updateHeaderOptions(headerOptions);
    clipEndTimeForm.updateHeaderOptions(headerOptions);
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
  public TimeTranslator getStartTime() { return clipStartTimeForm.toTranslator(); }

  /**
   * Returns end time
   * @return TimeTranslator end time
   */
  public TimeTranslator getEndTime() { return clipStartTimeForm.toTranslator(); }
}
