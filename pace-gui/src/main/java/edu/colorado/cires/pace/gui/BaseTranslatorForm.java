package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.translator.Translator;
import java.util.UUID;
import javax.swing.JPanel;

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

  protected abstract T toTranslator(UUID uuid, String name);
  
  protected abstract void updateHeaderOptions(String[] options);

}
