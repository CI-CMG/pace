package edu.colorado.pace.gui;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.pace.gui.metadata.common.MetadataPanel;
import edu.colorado.pace.gui.metadata.util.MetadataPanelFactory;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainUI {

  private JPanel rootPanel;
  private JTabbedPane applicationTabPane;
  private JTabbedPane metadataTabbedPane;
  private MetadataPanel<Ship> shipPanel;
  private MetadataPanel<Project> projectPanel;
  private JTabbedPane translatorsTabbedPane;
  private MetadataPanel<CSVTranslator> csvTranslatorPanel;
  private MetadataPanel<ExcelTranslator> excelTranslatorPanel;

  public JPanel getRootPanel() {
    return rootPanel;
  }

  private void createUIComponents() throws DatastoreException, IOException {
    MetadataPanelFactory metadataPanelFactory = new MetadataPanelFactory();
    
    shipPanel = metadataPanelFactory.createShipPanel();
    projectPanel = metadataPanelFactory.createProjectPanel();
    csvTranslatorPanel = metadataPanelFactory.createCSVTranslatorPanel();
    excelTranslatorPanel = metadataPanelFactory.createExcelTranslatorPanel();
  }
}
