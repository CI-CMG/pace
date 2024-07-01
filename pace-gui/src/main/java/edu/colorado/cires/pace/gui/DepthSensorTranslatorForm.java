package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class DepthSensorTranslatorForm extends SensorTypeSpecificTranslatorForm<DepthSensorTranslator> {

  public DepthSensorTranslatorForm(DepthSensorTranslator initialTranslator, String[] headerOptions) {
    super(initialTranslator, headerOptions);
    addFields();
    initializeFields(initialTranslator, headerOptions);
  }

  @Override
  protected void addFields() {
    
  }

  @Override
  protected void initializeFields(DepthSensorTranslator initialTranslator, String[] headerOptions) {

  }

  @Override
  protected void updateHeaderOptions(String[] headerOptions) {

  }

  @Override
  protected DepthSensorTranslator toTranslator(JTextField translatorUUIDField, JTextField translatorNameField, JComboBox<String> uuidField, JComboBox<String> nameField, JComboBox<String> descriptionField,
      PositionTranslatorForm positionTranslatorForm) {
    String uuidText = translatorUUIDField.getText();
    return DepthSensorTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(translatorNameField.getText())
        .sensorUUID((String) uuidField.getSelectedItem())
        .sensorName((String) nameField.getSelectedItem())
        .description((String) descriptionField.getSelectedItem())
        .positionTranslator(positionTranslatorForm.toTranslator())
        .build();
  }
}
