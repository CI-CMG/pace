package edu.colorado.cires.pace.gui;

import java.io.IOException;
import javax.swing.JTabbedPane;

public class ApplicationTabs extends JTabbedPane {

  public ApplicationTabs() throws IOException {
    setTabPlacement(JTabbedPane.LEFT);
    add("Packages", DataPanelFactory.createPackagesPanel());
    add("Metadata", new MetadataTabbedPane());
    add("Translators", new TranslatorsTabbedPane());
  }
}
