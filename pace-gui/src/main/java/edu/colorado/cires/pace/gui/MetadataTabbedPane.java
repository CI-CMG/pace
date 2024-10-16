package edu.colorado.cires.pace.gui;

import javax.swing.JTabbedPane;

/**
 * MetadataTabbedPane extends JTabbedPane and provides an initializer
 * with relevant metadata sections on pane
 */
public class MetadataTabbedPane extends JTabbedPane {

  /**
   * Given a data panel factory for creating relevant panels,
   * initializes the metadata tabbed pane
   *
   * @param dataPanelFactory creates relevant panels
   */
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
