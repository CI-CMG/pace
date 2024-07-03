package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.AudioDataPackageTranslator;
import edu.colorado.cires.pace.data.translator.TimeTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class AudioDataForm<T extends AudioDataPackageTranslator> extends JPanel {
  
  private final JComboBox<String> instrumentIdField = new JComboBox<>();
  private final JComboBox<String> hydrophoneSensitivityField = new JComboBox<>();
  private final JComboBox<String> frequencyRangeField = new JComboBox<>();
  private final JComboBox<String> gainField = new JComboBox<>();
  private final JComboBox<String> commentsField = new JComboBox<>();
  private final JComboBox<String> sensorsField = new JComboBox<>();
  
  private final TimeTranslatorForm deploymentTimeForm;
  private final TimeTranslatorForm recoveryTimeForm;

  public AudioDataForm(String[] headerOptions, T initialTranslator) {
    this.deploymentTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getDeploymentTimeTranslator());
    this.recoveryTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getRecoveryTimeTranslator());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Instrument ID"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(instrumentIdField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Hydrophone Sensitivity"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    add(hydrophoneSensitivityField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Frequency Range"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1;
    }));
    add(frequencyRangeField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Gain"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 6; c.weightx = 1;
    }));
    add(gainField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Comments"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 8; c.weightx = 1;
    }));
    add(commentsField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 9; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Sensors"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 10; c.weightx = 1;
    }));
    add(sensorsField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 11; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    deploymentTimeForm.setBorder(createEtchedBorder("Deployment Time"));
    add(deploymentTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 12; c.weightx = 1;
    }));
    recoveryTimeForm.setBorder(createEtchedBorder("Recovery Time"));
    add(recoveryTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 12; c.weightx = 1;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 13; c.weighty = 1;
    }));
  }
  
  public void initializeFields(String[] headerOptions, T initialTranslator) {
    updateComboBoxModel(instrumentIdField, headerOptions);
    updateComboBoxModel(hydrophoneSensitivityField, headerOptions);
    updateComboBoxModel(frequencyRangeField, headerOptions);
    updateComboBoxModel(gainField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);
    updateComboBoxModel(sensorsField, headerOptions);
    
    if (initialTranslator != null) {
      instrumentIdField.setSelectedItem(initialTranslator.getInstrumentId());
      hydrophoneSensitivityField.setSelectedItem(initialTranslator.getHydrophoneSensitivity());
      frequencyRangeField.setSelectedItem(initialTranslator.getFrequencyRange());
      gainField.setSelectedItem(initialTranslator.getGain());
      commentsField.setSelectedItem(initialTranslator.getComments());
      sensorsField.setSelectedItem(initialTranslator.getSensors());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(instrumentIdField, headerOptions);
    updateComboBoxModel(hydrophoneSensitivityField, headerOptions);
    updateComboBoxModel(frequencyRangeField, headerOptions);
    updateComboBoxModel(gainField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);
    updateComboBoxModel(sensorsField, headerOptions);

    deploymentTimeForm.updateHeaderOptions(headerOptions);
    recoveryTimeForm.updateHeaderOptions(headerOptions);
  }

  public String getInstrumentIdValue() {
    return (String) instrumentIdField.getSelectedItem();
  }

  public String getHydrophoneSensitivityValue() {
    return (String) hydrophoneSensitivityField.getSelectedItem();
  }

  public String getFrequencyRangeValue() {
    return (String) frequencyRangeField.getSelectedItem();
  }

  public String getGainValue() {
    return (String) gainField.getSelectedItem();
  }

  public String getCommentsValue() {
    return (String) commentsField.getSelectedItem();
  }

  public String getSensorsValue() {
    return (String) sensorsField.getSelectedItem();
  }

  public TimeTranslator getDeploymentTimeTranslator() {
    return deploymentTimeForm.toTranslator();
  }

  public TimeTranslator getRecoveryTimeTranslator() {
    return recoveryTimeForm.toTranslator();
  }
}
