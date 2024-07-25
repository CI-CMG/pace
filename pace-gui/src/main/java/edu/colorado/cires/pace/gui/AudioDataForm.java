package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.AudioDataPackageTranslator;
import edu.colorado.cires.pace.data.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.translator.TimeTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AudioDataForm<T extends AudioDataPackageTranslator> extends JPanel {
  
  private final JComboBox<String> instrumentIdField = new JComboBox<>();
  private final JComboBox<String> hydrophoneSensitivityField = new JComboBox<>();
  private final JComboBox<String> frequencyRangeField = new JComboBox<>();
  private final JComboBox<String> gainField = new JComboBox<>();
  private final JComboBox<String> commentsField = new JComboBox<>();
  
  private final JPanel sensorTranslatorsPanel = new JPanel(new GridBagLayout());
  private final JButton addSensorButton = new JButton("Add Sensor");
  
  private final TimeTranslatorForm deploymentTimeForm;
  private final TimeTranslatorForm recoveryTimeForm;

  public AudioDataForm(String[] headerOptions, T initialTranslator) {
    this.deploymentTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getDeploymentTime());
    this.recoveryTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getRecoveryTime());
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
    deploymentTimeForm.setBorder(createEtchedBorder("Deployment Time"));
    add(deploymentTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 10; c.weightx = 1;
    }));
    recoveryTimeForm.setBorder(createEtchedBorder("Recovery Time"));
    add(recoveryTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 10; c.weightx = 1;
    }));
    
    JPanel sensorsPanel = new JPanel(new GridBagLayout());
    sensorsPanel.add(sensorTranslatorsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    sensorsPanel.add(addSensorButton, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    sensorsPanel.setBorder(createEtchedBorder("Sensors"));
    add(sensorsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 11; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 12; c.weighty = 1;
    }));
  }
  
  public void initializeFields(String[] headerOptions, T initialTranslator) {
    updateComboBoxModel(instrumentIdField, headerOptions);
    updateComboBoxModel(hydrophoneSensitivityField, headerOptions);
    updateComboBoxModel(frequencyRangeField, headerOptions);
    updateComboBoxModel(gainField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);
    
    if (initialTranslator != null) {
      instrumentIdField.setSelectedItem(initialTranslator.getInstrumentId());
      hydrophoneSensitivityField.setSelectedItem(initialTranslator.getHydrophoneSensitivity());
      frequencyRangeField.setSelectedItem(initialTranslator.getFrequencyRange());
      gainField.setSelectedItem(initialTranslator.getGain());
      commentsField.setSelectedItem(initialTranslator.getComments());
      initialTranslator.getSensors().forEach(
          t -> addSensor(headerOptions, t)
      );
    }
  }

  private void addSensor(String[] headerOptions, PackageSensorTranslator translator) {
    PackageSensorTranslatorForm form = new PackageSensorTranslatorForm(headerOptions, translator, f -> {
      sensorTranslatorsPanel.remove(f.getParent());
      revalidate();
    });
    
    CollapsiblePanel<PackageSensorTranslatorForm> collapsiblePanel = new CollapsiblePanel<>(
        String.format(
            "Sensor %s", sensorTranslatorsPanel.getComponentCount() + 1
        ),
        form
    );
    collapsiblePanel.getContentPanel().setVisible(false);

    sensorTranslatorsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = sensorTranslatorsPanel.getComponentCount(); c.weightx = 1;
    }));
    revalidate();
  }

  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(instrumentIdField, headerOptions);
    updateComboBoxModel(hydrophoneSensitivityField, headerOptions);
    updateComboBoxModel(frequencyRangeField, headerOptions);
    updateComboBoxModel(gainField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);

    Arrays.stream(sensorTranslatorsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (PackageSensorTranslatorForm) p.getContentPanel())
        .forEach(p -> p.updateHeaderOptions(headerOptions));
    Arrays.stream(addSensorButton.getActionListeners())
            .forEach(addSensorButton::removeActionListener);
    addSensorButton.addActionListener(l -> addSensor(headerOptions, null));

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

  public List<PackageSensorTranslator> getSensorsValue() {
    return Arrays.stream(sensorTranslatorsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (PackageSensorTranslatorForm) p.getContentPanel())
        .map(PackageSensorTranslatorForm::toTranslator)
        .toList();
  }

  public TimeTranslator getDeploymentTimeTranslator() {
    return deploymentTimeForm.toTranslator();
  }

  public TimeTranslator getRecoveryTimeTranslator() {
    return recoveryTimeForm.toTranslator();
  }
}
