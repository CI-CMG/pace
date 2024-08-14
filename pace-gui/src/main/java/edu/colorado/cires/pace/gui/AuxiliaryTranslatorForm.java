package edu.colorado.cires.pace.gui;

import java.awt.Component;

public interface AuxiliaryTranslatorForm<T> {
  
  T toTranslator();
  Component[] getComponents();
  void updateHeaderOptions(String[] headerOptions);

}
