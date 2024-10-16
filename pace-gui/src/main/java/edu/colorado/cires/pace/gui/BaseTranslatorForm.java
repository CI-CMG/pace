package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * BaseTranslatorForm extends JPanel and provides base translator form functionality
 * @param <T> Type of translator
 */
public abstract class BaseTranslatorForm<T extends Translator> extends JPanel {
  
  protected String[] headerOptions;

  /**
   * Initialized the base translator form
   * @param headerOptions header options to select from in PACE
   */
  public BaseTranslatorForm(String[] headerOptions) {
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
    this.headerOptions = Arrays.stream(headerOptions)
        .filter(StringUtils::isNotEmpty)
        .collect(Collectors.toSet()).stream().sorted().toArray(String[]::new);
    this.headerOptions = ArrayUtils.addFirst(this.headerOptions, null);
    updateHeaderOptions(this.headerOptions);
  }

  protected abstract void addUniqueFields();
  
  protected abstract void initializeUniqueFields(T initialTranslator);

  protected abstract T toTranslator(UUID uuid, String name);
  
  protected abstract void updateHeaderOptions(String[] options);

}
