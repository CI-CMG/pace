package edu.colorado.cires.pace.gui;

import javax.swing.JTabbedPane;

public class TranslatorsTabbedPane extends JTabbedPane {

  public TranslatorsTabbedPane() {
    add("Excel", DataPanelFactory.createExcelTranslatorsPanel());
    add("CSV", DataPanelFactory.createCSVTranslatorsPanel());
  }
}
