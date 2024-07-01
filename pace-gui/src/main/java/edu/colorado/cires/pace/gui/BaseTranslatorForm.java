package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.translator.Translator;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class BaseTranslatorForm<T extends Translator> extends JPanel {
  
  protected String[] headerOptions;
  
  public BaseTranslatorForm(String[] headerOptions) {
    this.headerOptions = headerOptions;
  }

  public String[] getHeaderOptions() {
    return headerOptions;
  }
  
  public void setHeaderOptions(String[] headerOptions) {
    this.headerOptions = headerOptions;
    updateHeaderOptions(headerOptions);
  }

  protected abstract void addUniqueFields();
  
  protected abstract void initializeUniqueFields(T initialTranslator);

  protected abstract T toTranslator(JTextField uuidField, JTextField nameField);
  
  protected abstract void updateHeaderOptions(String[] options);

}
