package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioDataPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * AudioDataForm extends JPanel
 * @param <T> type of translator
 */
public class AudioDataForm<T extends AudioDataPackageTranslator> extends JPanel implements AuxiliaryTranslatorForm<AudioDataPackageTranslator> {
  
  private final JComboBox<String> instrumentIdField = new JComboBox<>();
  private final JComboBox<String> commentsField = new JComboBox<>();
  
  private final JPanel sensorTranslatorsPanel = new JPanel(new GridBagLayout());
  private final JButton addSensorButton = new JButton("Add Sensor");
  
  private final TimeTranslatorForm deploymentTimeForm;
  private final TimeTranslatorForm recoveryTimeForm;
  private final TimeTranslatorForm audioStartTimeForm;
  private final TimeTranslatorForm audioEndTimeForm;

  /**
   * Initializes the audio data form
   *
   * @param headerOptions possible headers to select
   * @param initialTranslator base translator
   */
  public AudioDataForm(String[] headerOptions, T initialTranslator) {
    this.deploymentTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getDeploymentTime());
    this.recoveryTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getRecoveryTime());
    this.audioStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioStartTime());
    this.audioEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getAudioEndTime());
    instrumentIdField.setName("instrumentId");
    commentsField.setName("comments");
    deploymentTimeForm.setName("deploymentTime");
    recoveryTimeForm.setName("recoveryTime");
    audioStartTimeForm.setName("audioStartTime");
    audioEndTimeForm.setName("audioEndTime");
    setName("audioDataForm");
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
    add(new JLabel("Comments"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    add(commentsField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    deploymentTimeForm.setBorder(createEtchedBorder("Deployment Time"));
    add(deploymentTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1;
    }));
    recoveryTimeForm.setBorder(createEtchedBorder("Recovery Time"));
    add(recoveryTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 4; c.weightx = 1;
    }));
    audioStartTimeForm.setBorder(createEtchedBorder("Audio Start Time"));
    add(audioStartTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1;
    }));
    audioEndTimeForm.setBorder(createEtchedBorder("Audio End Time"));
    add(audioEndTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 5; c.weightx = 1;
    }));
    
    JPanel sensorsPanel = new JPanel(new GridBagLayout());
    sensorsPanel.setName("sensors");
    sensorsPanel.add(sensorTranslatorsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    sensorsPanel.add(addSensorButton, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    sensorsPanel.setBorder(createEtchedBorder("Sensors"));
    add(sensorsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 6; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 7; c.weighty = 1;
    }));
  }

  /**
   * Initializes the header options into fields
   *
   * @param headerOptions headers to select from
   * @param initialTranslator base translator
   */
  public void initializeFields(String[] headerOptions, T initialTranslator) {
    updateComboBoxModel(instrumentIdField, headerOptions);
    updateComboBoxModel(commentsField, headerOptions);
    addSensorButton.addActionListener(l -> addSensor(headerOptions, null));
    if (initialTranslator != null) {
      instrumentIdField.setSelectedItem(initialTranslator.getInstrumentId());
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

  /**
   * Builds an audio data package translator with current value
   * @return AudioDataPackageTranslator given current held values
   */
  @Override
  public AudioDataPackageTranslator toTranslator() {
    return AudioPackageTranslator.builder()
        .instrumentId((String) instrumentIdField.getSelectedItem())
        .comments((String) commentsField.getSelectedItem())
        .sensors(Arrays.stream(sensorTranslatorsPanel.getComponents())
            .filter(p -> p instanceof CollapsiblePanel<?>)
            .map(p -> (CollapsiblePanel<?>) p)
            .map(p -> (PackageSensorTranslatorForm) p.getContentPanel())
            .map(PackageSensorTranslatorForm::toTranslator)
            .toList())
        .deploymentTime(deploymentTimeForm.toTranslator())
        .recoveryTime(recoveryTimeForm.toTranslator())
        .audioStartTime(audioStartTimeForm.toTranslator())
        .audioEndTime(audioEndTimeForm.toTranslator())
        .build();
  }

  /**
   * Changes current possible header options to provided options
   * @param headerOptions to be changed to
   */
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(instrumentIdField, headerOptions);
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
    audioStartTimeForm.updateHeaderOptions(headerOptions);
    audioEndTimeForm.updateHeaderOptions(headerOptions);
  }
}
