package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.sensor.other.translator.OtherSensorTranslator;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * OtherSensorTranslatorForm extends SensorTypeSpecificTranslatorForm and adds fields
 * relevant to "other" sensors
 */
public class OtherSensorTranslatorForm extends SensorTypeSpecificTranslatorForm<OtherSensorTranslator> {
  
  private final JComboBox<String> sensorTypeField = new JComboBox<>();
  private final JComboBox<String> propertiesField = new JComboBox<>();

  /**
   * Creates an other sensor translator form
   * @param initialTranslator translator to build upon
   * @param headerOptions selectable headers during mapping
   */
  public OtherSensorTranslatorForm(OtherSensorTranslator initialTranslator, String[] headerOptions) {
    super();
    sensorTypeField.setName("sensorType");
    propertiesField.setName("properties");
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
  protected OtherSensorTranslator toTranslator(UUID translatorUUID, String translatorName, JComboBox<String> uuidField, JComboBox<String> nameField, JComboBox<String> descriptionField,
      JComboBox<String> sensorIdField) {
    return OtherSensorTranslator.builder()
        .uuid(translatorUUID)
        .name(translatorName)
        .sensorUUID((String) uuidField.getSelectedItem())
        .sensorName((String) nameField.getSelectedItem())
        .description((String) descriptionField.getSelectedItem())
        .sensorType((String) sensorTypeField.getSelectedItem())
        .properties((String) propertiesField.getSelectedItem())
        .id((String) sensorIdField.getSelectedItem())
        .build();
  }
}
