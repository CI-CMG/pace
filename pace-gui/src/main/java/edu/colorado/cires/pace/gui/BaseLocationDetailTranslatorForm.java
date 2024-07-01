package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.translator.LocationDetailTranslator;
import javax.swing.JPanel;

public abstract class BaseLocationDetailTranslatorForm<T extends LocationDetailTranslator> extends JPanel {
  
  private String[] headerOptions;

  protected BaseLocationDetailTranslatorForm(String[] headerOptions) {
    this.headerOptions = headerOptions;
  }

  public String[] getHeaderOptions() {
    return headerOptions;
  }

  public void setHeaderOptions(String[] headerOptions) {
    this.headerOptions = headerOptions;
    updateHeaderOptions(headerOptions);
  }

  protected abstract T toTranslator();

  protected abstract void updateHeaderOptions(String[] options);
}
