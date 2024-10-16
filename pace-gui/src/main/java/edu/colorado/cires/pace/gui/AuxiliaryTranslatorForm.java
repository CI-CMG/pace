package edu.colorado.cires.pace.gui;

import java.awt.Component;

/**
 * AuxiliaryTranslatorForm structures the necessary parts of a translator form
 * @param <T> the type of translator
 */
public interface AuxiliaryTranslatorForm<T> {
  
  T toTranslator();
  Component[] getComponents();
  void updateHeaderOptions(String[] headerOptions);

}
