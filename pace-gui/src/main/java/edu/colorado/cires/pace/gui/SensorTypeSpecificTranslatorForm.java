package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.translator.SensorTranslator;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class SensorTypeSpecificTranslatorForm<T extends SensorTranslator> extends JPanel {

  public SensorTypeSpecificTranslatorForm(T initialTranslator, String[] headerOptions) {
    
  }
  
  protected abstract void addFields();
  
  protected abstract void initializeFields(T initialTranslator, String[] headerOptions);
  
  protected abstract void updateHeaderOptions(String[] headerOptions);
  
  protected abstract T toTranslator(
      JTextField translatorUUIDField,
      JTextField translatorNameField,
      JComboBox<String> uuidField,
      JComboBox<String> nameField,
      JComboBox<String> descriptionField,
      PositionTranslatorForm positionTranslatorForm
  );
}
