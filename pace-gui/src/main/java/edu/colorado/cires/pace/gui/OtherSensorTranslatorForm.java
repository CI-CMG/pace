package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class OtherSensorTranslatorForm extends SensorTypeSpecificTranslatorForm<OtherSensorTranslator> {
  
  private final JComboBox<String> sensorTypeField = new JComboBox<>();
  private final JComboBox<String> propertiesField = new JComboBox<>();

  public OtherSensorTranslatorForm(OtherSensorTranslator initialTranslator, String[] headerOptions) {
    super(initialTranslator, headerOptions);
    addFields();
    initializeFields(initialTranslator, headerOptions);
  }

  @Override
  protected void addFields() {
    setLayout(new GridBagLayout());
    add(new JLabel("Sensor Type"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(sensorTypeField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Properties"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(propertiesField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
  }

  @Override
  protected void initializeFields(OtherSensorTranslator initialTranslator, String[] headerOptions) {
    updateHeaderOptions(headerOptions);
    
    if (initialTranslator != null) {
      sensorTypeField.setSelectedItem(initialTranslator.getSensorType());
      propertiesField.setSelectedItem(initialTranslator.getProperties());
    }
  }

  @Override
  protected void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(sensorTypeField, headerOptions);
    updateComboBoxModel(propertiesField, headerOptions);
  }

  @Override
  protected OtherSensorTranslator toTranslator(JTextField translatorUUIDField, JTextField translatorNameField, JComboBox<String> uuidField, JComboBox<String> nameField, JComboBox<String> descriptionField,
      PositionTranslatorForm positionTranslatorForm) {
    String uuidText = translatorUUIDField.getText();
    return OtherSensorTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(translatorNameField.getText())
        .sensorUUID((String) uuidField.getSelectedItem())
        .sensorName((String) nameField.getSelectedItem())
        .description((String) descriptionField.getSelectedItem())
        .positionTranslator(positionTranslatorForm.toTranslator())
        .sensorType((String) sensorTypeField.getSelectedItem())
        .properties((String) propertiesField.getSelectedItem())
        .build();
  }
}