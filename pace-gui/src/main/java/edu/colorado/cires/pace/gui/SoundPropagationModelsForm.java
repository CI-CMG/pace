package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SoundPropagationModelsForm extends JPanel implements AuxiliaryTranslatorForm<SoundPropagationModelsPackageTranslator> {
  
  private final JComboBox<String> modeledFrequencyField = new JComboBox<>();
  private final TimeTranslatorForm audioStartTimeForm;
  private final TimeTranslatorForm audioEndTimeForm;

  public SoundPropagationModelsForm(String[] headerOptions, SoundPropagationModelsPackageTranslator initialTranslator) {
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
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, SoundPropagationModelsPackageTranslator initialTranslator) {
    updateComboBoxModel(modeledFrequencyField, headerOptions);
    
    if (initialTranslator != null) {
      modeledFrequencyField.setSelectedItem(initialTranslator.getModeledFrequency());
    }
  }

  @Override
  public SoundPropagationModelsPackageTranslator toTranslator() {
    return SoundPropagationModelsPackageTranslator.builder()
        .modeledFrequency((String) modeledFrequencyField.getSelectedItem())
        .audioStartTimeTranslator(audioStartTimeForm.toTranslator())
        .audioEndTimeTranslator(audioEndTimeForm.toTranslator())
        .build();
  }

  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(modeledFrequencyField, headerOptions);
    
    audioStartTimeForm.updateHeaderOptions(headerOptions);
    audioEndTimeForm.updateHeaderOptions(headerOptions);
  }
}
