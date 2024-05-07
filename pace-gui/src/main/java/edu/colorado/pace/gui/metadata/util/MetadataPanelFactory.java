package edu.colorado.pace.gui.metadata.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.datastore.json.CSVTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ExcelTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.pace.gui.metadata.common.MetadataPanel;
import edu.colorado.pace.gui.metadata.common.ObjectForm;
import edu.colorado.pace.gui.metadata.project.ProjectForm;
import edu.colorado.pace.gui.metadata.ship.ShipForm;
import edu.colorado.pace.gui.metadata.translator.csv.CSVTranslatorForm;
import edu.colorado.pace.gui.metadata.translator.excel.ExcelTranslatorForm;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public final class MetadataPanelFactory {
  
  private static final String[] OBJECT_WITH_NAME_HEADERS = { "UUID", "Name" };
  
  private final Path workDir;
  private final ObjectMapper objectMapper;

  public MetadataPanelFactory() {
    this.workDir = new ApplicationPropertyResolver().getWorkDir();
    this.objectMapper = SerializationUtils.createObjectMapper();
  }
  
  public MetadataPanel<Ship> createShipPanel() throws IOException {
    ShipRepository shipRepository = new ShipRepository(
        new ShipJsonDatastore(
            workDir, objectMapper
        )
    );
    return new MetadataPanel<>(shipRepository, true, Ship.class) {
      @Override
      protected String[] getHeaders() {
        return OBJECT_WITH_NAME_HEADERS;
      }

      @Override
      protected Object[] objectToItemArray(Ship object) {
        return new Object[]{object.getUuid(), object.getName()};
      }

      @Override
      protected Ship objectFromItemsArray(Object[] itemsArray) {
        return Ship.builder()
            .uuid(UUID.fromString(itemsArray[0].toString()))
            .name(itemsArray[1].toString())
            .build();
      }

      @Override
      protected ObjectForm<Ship> getEditForm(Ship object) {
        return new ShipForm(object);
      }
    };
  }

  public MetadataPanel<Project> createProjectPanel() throws IOException {
    ProjectRepository repository = new ProjectRepository(
        new ProjectJsonDatastore(
            workDir, objectMapper
        )
    );
    
    return new MetadataPanel<>(repository, true, Project.class) {
      @Override
      protected String[] getHeaders() {
        return OBJECT_WITH_NAME_HEADERS;
      }

      @Override
      protected Object[] objectToItemArray(Project object) {
        return new Object[] { object.getUuid(), object.getName() };
      }

      @Override
      protected Project objectFromItemsArray(Object[] itemsArray) {
        return Project.builder()
            .uuid(UUID.fromString(itemsArray[0].toString()))
            .name(itemsArray[1].toString())
            .build();
      }

      @Override
      protected ObjectForm<Project> getEditForm(Project object) {
        return new ProjectForm(object);
      }
    };
  }
  
  public MetadataPanel<CSVTranslator> createCSVTranslatorPanel() throws IOException {
    CSVTranslatorRepository repository = new CSVTranslatorRepository(
        new CSVTranslatorJsonDatastore(
            workDir, objectMapper
        )
    );
    
    return new MetadataPanel<>(repository, false, null) {
      @Override
      protected String[] getHeaders() {
        return new String[] { "UUID", "Name", "Fields" };
      }

      @Override
      protected List<String> getHiddenColumns() {
        return List.of("UUID", "Fields");
      }

      @Override
      protected Object[] objectToItemArray(CSVTranslator object) {
        return new Object[] { object.getUuid(), object.getName(), object.getFields() };
      }

      @Override
      protected CSVTranslator objectFromItemsArray(Object[] itemsArray) {
        return CSVTranslator.builder()
            .uuid(UUID.fromString(itemsArray[0].toString()))
            .name(itemsArray[1].toString())
            .fields((List<CSVTranslatorField>) itemsArray[2])
            .build();
      }

      @Override
      protected ObjectForm<CSVTranslator> getEditForm(CSVTranslator object) {
        return new CSVTranslatorForm(object);
      }
    };
  }

  public MetadataPanel<ExcelTranslator> createExcelTranslatorPanel() throws IOException {
    ExcelTranslatorRepository repository = new ExcelTranslatorRepository(
        new ExcelTranslatorJsonDatastore(
            workDir, objectMapper
        )
    );
    
    return new MetadataPanel<>(repository, false, null) {
      @Override
      protected String[] getHeaders() {
        return new String[] { "UUID", "Name", "Fields" };
      }

      @Override
      protected List<String> getHiddenColumns() {
        return List.of("UUID", "Fields");
      }

      @Override
      protected Object[] objectToItemArray(ExcelTranslator object) {
        return new Object[] { object.getUuid(), object.getName(), object.getFields() };
      }

      @Override
      protected ExcelTranslator objectFromItemsArray(Object[] itemsArray) {
        return ExcelTranslator.builder()
            .uuid(UUID.fromString(itemsArray[0].toString()))
            .name(itemsArray[1].toString())
            .fields((List<ExcelTranslatorField>) itemsArray[2])
            .build();
      }

      @Override
      protected ObjectForm<ExcelTranslator> getEditForm(ExcelTranslator object) {
        return new ExcelTranslatorForm(object);
      }
    };
  }
}
