package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * SensorTypeSpecificTranslatorForm extends JPanel and provides necessary
 * function outline
 * @param <T>
 */
public abstract class SensorTypeSpecificTranslatorForm<T extends SensorTranslator> extends JPanel {

  /**
   * Declares sensor type specific translator form object
   */
  public SensorTypeSpecificTranslatorForm() {
    
  }
  
  protected abstract void addFields();
  
  protected abstract void initializeFields(T initialTranslator, String[] headerOptions);
  
  protected abstract void updateHeaderOptions(String[] headerOptions);
  
  protected abstract T toTranslator(
      UUID translatorUUID,
      String translatorName,
      JComboBox<String> uuidField,
      JComboBox<String> nameField,
      JComboBox<String> descriptionField,
      JComboBox<String> sensorIdField);
}
