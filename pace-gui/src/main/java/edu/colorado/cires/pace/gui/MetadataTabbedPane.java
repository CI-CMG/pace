package edu.colorado.cires.pace.gui;

import javax.swing.JTabbedPane;

public class MetadataTabbedPane extends JTabbedPane {

  public MetadataTabbedPane() {
    add("Projects", DataPanelFactory.createProjectsPanel());
    add("People", DataPanelFactory.createPeoplePanel());
    add("Organizations", DataPanelFactory.createOrganizationsPanel());
    add("Platforms", DataPanelFactory.createPlatformPanel());
  }
}
