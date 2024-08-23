package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;

import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class SoundLevelMetricsForm extends JPanel {
  
  private final TimeTranslatorForm audioStartTimeForm;
  private final TimeTranslatorForm audioEndTimeForm;
  private final TimeTranslatorForm analysisStartTimeForm;
  private final TimeTranslatorForm analysisEndTimeForm;

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
  
  public void updateHeaderOptions(String[] headerOptions) {
    audioStartTimeForm.updateHeaderOptions(headerOptions);
    audioEndTimeForm.updateHeaderOptions(headerOptions);
    analysisStartTimeForm.updateHeaderOptions(headerOptions);
    analysisEndTimeForm.updateHeaderOptions(headerOptions);
  }

  public TimeTranslator getAudioStartTimeTranslator() {
    return audioStartTimeForm.toTranslator();
  }

  public TimeTranslator getAudioEndTimeTranslator() {
    return audioEndTimeForm.toTranslator();
  }

  public TimeTranslator getStartTime() { return analysisStartTimeForm.toTranslator(); }

  public TimeTranslator getEndTime() { return analysisStartTimeForm.toTranslator(); }
}
