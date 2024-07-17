package edu.colorado.cires.pace.gui;

import javax.swing.JTabbedPane;

public class MetadataTabbedPane extends JTabbedPane {

  public MetadataTabbedPane(DataPanelFactory dataPanelFactory) {
    setName("metadataTabs");
    
    add("Projects", dataPanelFactory.createProjectsPanel());
    add("People", dataPanelFactory.createPeoplePanel());
    add("Organizations", dataPanelFactory.createOrganizationsPanel());
    add("Platforms", dataPanelFactory.createPlatformPanel());
    add("File Types", dataPanelFactory.createFileTypesPanel());
    add("Instruments", dataPanelFactory.createInstrumentsPanel());
    add("Sensors", dataPanelFactory.createSensorsPanel());
    add("Sea Areas", dataPanelFactory.createSeaAreasPanel());
    add("Detection Types", dataPanelFactory.createDetectionTypesPanel());
    add("Ships", dataPanelFactory.createShipsPanel());
  }
}
