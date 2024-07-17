package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class AudioSensorTranslatorForm extends SensorTypeSpecificTranslatorForm<AudioSensorTranslator> {
  
  private final JComboBox<String> hydrophoneIdField = new JComboBox<>();
  private final JComboBox<String> preampIdField = new JComboBox<>();

  public AudioSensorTranslatorForm(AudioSensorTranslator initialTranslator, String[] headerOptions) {
    super(initialTranslator, headerOptions);
    hydrophoneIdField.setName("hydrophoneId");
    preampIdField.setName("preampId");
    addFields();
    initializeFields(initialTranslator, headerOptions);
  }

  @Override
  protected void addFields() {
    setLayout(new GridBagLayout());
    add(new JLabel("Hydrophone ID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(hydrophoneIdField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Preamp ID"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(preampIdField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
  }

  @Override
  protected void initializeFields(AudioSensorTranslator initialTranslator, String[] headerOptions) {
    updateHeaderOptions(headerOptions);
    
    if (initialTranslator != null) {
      hydrophoneIdField.setSelectedItem(initialTranslator.getHydrophoneId());
      preampIdField.setSelectedItem(initialTranslator.getPreampId());
    }
  }

  @Override
  protected void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(hydrophoneIdField, headerOptions);
    updateComboBoxModel(preampIdField, headerOptions);
  }

  @Override
  protected AudioSensorTranslator toTranslator(JTextField translatorUUIDField, JTextField translatorNameField, JComboBox<String> uuidField, JComboBox<String> nameField, JComboBox<String> descriptionField,
      PositionTranslatorForm positionTranslatorForm) {
    String uuidText = translatorUUIDField.getText();
    
    return AudioSensorTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(translatorNameField.getText())
        .sensorUUID((String) uuidField.getSelectedItem())
        .sensorName((String) nameField.getSelectedItem())
        .description((String) descriptionField.getSelectedItem())
        .positionTranslator(positionTranslatorForm.toTranslator())
        .hydrophoneId((String) hydrophoneIdField.getSelectedItem())
        .preampId((String) preampIdField.getSelectedItem())
        .build();
  }
}
