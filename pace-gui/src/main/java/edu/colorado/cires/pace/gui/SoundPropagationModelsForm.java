package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * SoundPropagationModelsForm extends JPanel and provides structure relevant to
 * sound propagation model forms
 */
public class SoundPropagationModelsForm extends JPanel implements AuxiliaryTranslatorForm<SoundPropagationModelsPackageTranslator> {
  
  private final JComboBox<String> modeledFrequencyField = new JComboBox<>();
  private final TimeTranslatorForm audioStartTimeForm;
  private final TimeTranslatorForm audioEndTimeForm;
  private final TimeTranslatorForm modelStartTimeForm;
  private final TimeTranslatorForm modelEndTimeForm;

  /**
   * Creates a sound propagation model form
   * @param headerOptions headers to select from during mapping
   * @param initialTranslator translator to build upon
   */
  public SoundPropagationModelsForm(String[] headerOptions, SoundPropagationModelsPackageTranslator initialTranslator) {
    this.modelStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.modelEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());
    this.audioStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioStartTimeTranslator());
    this.audioEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioEndTimeTranslator());
    modeledFrequencyField.setName("modeledFrequency");
    audioStartTimeForm.setName("audioStartTime");
    audioEndTimeForm.setName("audioEndTime");
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Modeled Frequency"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(modeledFrequencyField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    audioStartTimeForm.setBorder(createEtchedBorder("Audio Start Time"));
    add(audioStartTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    audioEndTimeForm.setBorder(createEtchedBorder("Audio End Time"));
    add(audioEndTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 2; c.weightx = 1;
    }));
    modelStartTimeForm.setBorder(createEtchedBorder("Model Start Time"));
    add(modelStartTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1;
    }));
    modelEndTimeForm.setBorder(createEtchedBorder("Model End Time"));
    add(modelEndTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 3; c.weightx = 1;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, SoundPropagationModelsPackageTranslator initialTranslator) {
    updateComboBoxModel(modeledFrequencyField, headerOptions);
    
    if (initialTranslator != null) {
      modeledFrequencyField.setSelectedItem(initialTranslator.getModeledFrequency());
    }
  }

  /**
   * Builds a sound propagation models package translator using the selected items
   * in the form
   * @return SoundPropagationModelsPackageTranslator mapped as according to form
   */
  @Override
  public SoundPropagationModelsPackageTranslator toTranslator() {
    return SoundPropagationModelsPackageTranslator.builder()
        .modeledFrequency((String) modeledFrequencyField.getSelectedItem())
        .audioStartTimeTranslator(audioStartTimeForm.toTranslator())
        .audioEndTimeTranslator(audioEndTimeForm.toTranslator())
        .build();
  }

  /**
   * Changes the selectable headers to the provided options
   * @param headerOptions new headers to select from
   */
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(modeledFrequencyField, headerOptions);
    
    audioStartTimeForm.updateHeaderOptions(headerOptions);
    audioEndTimeForm.updateHeaderOptions(headerOptions);
    modelStartTimeForm.updateHeaderOptions(headerOptions);
    modelEndTimeForm.updateHeaderOptions(headerOptions);
  }

  /**
   * Returns the start time
   * @return TimeTranslator modelStartTime
   */
  public TimeTranslator getStartTime() { return modelStartTimeForm.toTranslator(); }

  /**
   * Returns the end time
   * @return TimeTranslator modelEndTime
   */
  public TimeTranslator getEndTime() { return modelEndTimeForm.toTranslator(); }
}
