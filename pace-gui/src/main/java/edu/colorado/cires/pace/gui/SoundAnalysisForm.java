package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.SoundAnalysisPackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SoundAnalysisForm<T extends SoundAnalysisPackageTranslator> extends JPanel {
  
  private final JComboBox<String> analysisTimeZoneField = new JComboBox<>();
  private final JComboBox<String> analysisEffortField = new JComboBox<>();
  private final JComboBox<String> sampleRateField = new JComboBox<>();
  private final JComboBox<String> minFrequencyField = new JComboBox<>();
  private final JComboBox<String> maxFrequencyField = new JComboBox<>();

  public SoundAnalysisForm(String[] headerOptions, T initialTranslator) {
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Analysis Time Zone"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(analysisTimeZoneField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Analysis Effort"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    add(analysisEffortField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Sample Rate"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1;
    }));
    add(sampleRateField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Min Frequency"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 6; c.weightx = 1;
    }));
    add(minFrequencyField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 7; c.weightx = 1;
    }));
    add(new JLabel("Max Frequency"), configureLayout(c -> {
      c.gridx = 1; c.gridy = 6; c.weightx = 1;
    }));
    add(maxFrequencyField, configureLayout(c -> {
      c.gridx = 1; c.gridy = 7; c.weightx = 1;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 8; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, T initialTranslator) {
    updateComboBoxModel(analysisTimeZoneField, headerOptions);
    updateComboBoxModel(analysisEffortField, headerOptions);
    updateComboBoxModel(sampleRateField, headerOptions);
    updateComboBoxModel(minFrequencyField, headerOptions);
    updateComboBoxModel(maxFrequencyField, headerOptions);
    
    if (initialTranslator != null) {
      analysisTimeZoneField.setSelectedItem(initialTranslator.getAnalysisTimeZone());
      analysisEffortField.setSelectedItem(initialTranslator.getAnalysisEffort());
      sampleRateField.setSelectedItem(initialTranslator.getSampleRate());
      minFrequencyField.setSelectedItem(initialTranslator.getMinFrequency());
      maxFrequencyField.setSelectedItem(initialTranslator.getMaxFrequency());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(analysisTimeZoneField, headerOptions);
    updateComboBoxModel(analysisEffortField, headerOptions);
    updateComboBoxModel(sampleRateField, headerOptions);
    updateComboBoxModel(minFrequencyField, headerOptions);
    updateComboBoxModel(maxFrequencyField, headerOptions);
  }

  public String getAnalysisTimeZoneValue() {
    return (String) analysisTimeZoneField.getSelectedItem();
  }

  public String getAnalysisEffortValue() {
    return (String) analysisEffortField.getSelectedItem();
  }

  public String getSampleRateValue() {
    return (String) sampleRateField.getSelectedItem();
  }

  public String getMinFrequencyValue() {
    return (String) minFrequencyField.getSelectedItem();
  }

  public String getMaxFrequencyValue() {
    return (String) maxFrequencyField.getSelectedItem();
  }
}
