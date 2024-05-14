package edu.colorado.cires.pace.gui;

import java.io.IOException;
import javax.swing.JTabbedPane;

public class MetadataTabbedPane extends JTabbedPane {

  public MetadataTabbedPane() throws IOException {
    add("Projects", DataPanelFactory.createProjectsPanel());
    add("People", DataPanelFactory.createPeoplePanel());
  }
}
