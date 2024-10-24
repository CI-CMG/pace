package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.sensor.depth.translator.DepthSensorTranslator;
import java.util.UUID;
import javax.swing.JComboBox;

/**
 * DepthSensorTranslatorForm extends SensorTypeSpecificTranslatorForm and adds
 * fields relevant to depth sensors
 */
public class DepthSensorTranslatorForm extends SensorTypeSpecificTranslatorForm<DepthSensorTranslator> {

  /**
   * Initializes a DepthSensorTranslatorForm object
   * @param initialTranslator translator to build upon
   * @param headerOptions headers to select from
   */
  public DepthSensorTranslatorForm(DepthSensorTranslator initialTranslator, String[] headerOptions) {
    super();
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
  protected DepthSensorTranslator toTranslator(UUID translatorUUID, String translatorName, JComboBox<String> uuidField, JComboBox<String> nameField, JComboBox<String> descriptionField,
      JComboBox<String> sensorIdField) {
    return DepthSensorTranslator.builder()
        .uuid(translatorUUID)
        .name(translatorName)
        .sensorUUID((String) uuidField.getSelectedItem())
        .sensorName((String) nameField.getSelectedItem())
        .description((String) descriptionField.getSelectedItem())
        .id((String) sensorIdField.getSelectedItem())
        .build();
  }
}
