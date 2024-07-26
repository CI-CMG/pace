package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntryTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QualityEntryForm extends JPanel {
  
  private final TimeTranslatorForm startTimeForm;
  private final TimeTranslatorForm endTimeForm;
  private final JComboBox<String> minFrequencyField = new JComboBox<>();
  private final JComboBox<String> maxFrequencyField = new JComboBox<>();
  private final JComboBox<String> qualityLevelField = new JComboBox<>();
  private final JComboBox<String> commentsField = new JComboBox<>();
  
  private final Consumer<QualityEntryForm> removeAction;

  public QualityEntryForm(String[] headerOptions, DataQualityEntryTranslator initialTranslator, Consumer<QualityEntryForm> removeAction) {
    this.removeAction = removeAction;
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());
    
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Min Frequency"), configureLayout(c -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(minFrequencyField,  configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Max Frequency"), configureLayout(c -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    add(maxFrequencyField, configureLayout(c -> { c.gridx = 1; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Quality Level"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(qualityLevelField, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Comments"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(commentsField, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    startTimeForm.setBorder(createEtchedBorder("Start Time"));
    add(startTimeForm, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 6; c.weightx = 1;
    }));
    endTimeForm.setBorder(createEtchedBorder("End Time"));
    add(endTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 6; c.weightx = 1;
    }));
    add(getRemoveButton(), configureLayout(c -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private JButton getRemoveButton() {
    JButton button = new JButton("Remove Entry");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private void initializeFields(String[] headerOptions, DataQualityEntryTranslator initialTranslator) {
    updateComboBoxModel(minFrequencyField, headerOptions);
    updateComboBoxModel(maxFrequencyField, headerOptions);
    updateComboBoxModel(qualityLevelField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);
    
    if (initialTranslator != null) {
      minFrequencyField.setSelectedItem(initialTranslator.getMinFrequency());
      maxFrequencyField.setSelectedItem(initialTranslator.getMaxFrequency());
      qualityLevelField.setSelectedItem(initialTranslator.getQualityLevel());
      commentsField.setSelectedItem(initialTranslator.getComments());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(minFrequencyField, headerOptions);
    updateComboBoxModel(maxFrequencyField, headerOptions);
    updateComboBoxModel(qualityLevelField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);
    
    startTimeForm.updateHeaderOptions(headerOptions);
    endTimeForm.updateHeaderOptions(headerOptions);
  }
  
  public DataQualityEntryTranslator toTranslator() {
    return DataQualityEntryTranslator.builder()
        .startTime(startTimeForm.toTranslator())
        .endTime(endTimeForm.toTranslator())
        .minFrequency((String) minFrequencyField.getSelectedItem())
        .maxFrequency((String) maxFrequencyField.getSelectedItem())
        .qualityLevel((String) qualityLevelField.getSelectedItem())
        .comments((String) commentsField.getSelectedItem())
        .build();
  }
}
