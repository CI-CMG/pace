package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.LocationDetailTranslator;
import javax.swing.JPanel;

/**
 * BaseLocationDetailTranslatorForm extends JPanel and creates base location detail translator functionality
 * @param <T> Type of translator
 */
public abstract class BaseLocationDetailTranslatorForm<T extends LocationDetailTranslator> extends JPanel {
  
  private String[] headerOptions;

  protected BaseLocationDetailTranslatorForm(String[] headerOptions) {
    this.headerOptions = headerOptions;
  }

  /**
   * Returns the possible header options
   * @return String list headerOptions
   */
  public String[] getHeaderOptions() {
    return headerOptions;
  }

  /**
   * Sets the possible header options as provided
   * @param headerOptions possible header options
   */
  public void setHeaderOptions(String[] headerOptions) {
    this.headerOptions = headerOptions;
    updateHeaderOptions(headerOptions);
  }

  protected abstract T toTranslator();

  protected abstract void updateHeaderOptions(String[] options);
}
