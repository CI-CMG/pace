package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.data.translator.FileTypeTranslator;
import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import edu.colorado.cires.pace.data.translator.OrganizationTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.data.translator.PersonTranslator;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import edu.colorado.cires.pace.data.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import edu.colorado.cires.pace.data.translator.ShipTranslator;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.csv.CSVFormat;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;

public class TranslatorForm extends Form<Translator> {

  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();
  
  private String[] headerOptions = new String[] {};
  
  private BaseTranslatorForm<? extends Translator> baseTranslatorForm = new BaseTranslatorForm<>(new String[] {}) {
    @Override
    protected void addUniqueFields() {

    }

    @Override
    protected void initializeUniqueFields(Translator initialTranslator) {

    }

    @Override
    protected Translator toTranslator(JTextField uuidField, JTextField nameField) {
      return null;
    }

    @Override
    protected void updateHeaderOptions(String[] options) {
      
    }
  };
  
  private final Translator initialTranslator;

  public TranslatorForm(Translator initialTranslator) {
    setName("translatorForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    this.initialTranslator = initialTranslator;
  }
  
  public void init() {
    setLayout(new GridBagLayout());

    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    JButton addFieldsButton = new JButton("Import Spreadsheet Headers");
    addFieldsButton.addActionListener(e -> importSpreadsheetHeaders());
    add(addFieldsButton, configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 0; }));

    uuidField.setEnabled(false);
    initializeFields(initialTranslator);
  }
  
  private void importSpreadsheetHeaders() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("CSV, Excel", "csv", "xlsx"));
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      String fileName = file.getName();
      
      String[] newHeaderOptions;
      if (fileName.endsWith("csv")) {
        newHeaderOptions = readCSVHeaders(file);
      } else if (fileName.endsWith("xlsx")) {
        newHeaderOptions = readExcelHeaders(file);
      } else {
        newHeaderOptions = new String[] {};
      }
      
      baseTranslatorForm.setHeaderOptions(newHeaderOptions);
      headerOptions = newHeaderOptions;
    }
  }
  
  private String[] readExcelHeaders(File file) {
    try (InputStream inputStream = new FileInputStream(file); ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
      return workbook.getFirstSheet().openStream()
          .findFirst()
          .map(row -> row.getCells(0, row.getCellCount()).stream()
              .map(Cell::getRawValue)
              .toArray(String[]::new)
          )
          .orElse(new String[] {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private String[] readCSVHeaders(File file) {
    try (InputStream inputStream = new FileInputStream(file); InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
          .setSkipHeaderRecord(false)
          .build();
      
      return csvFormat.parse(reader).stream()
          .findFirst()
          .map(record -> record.stream().toArray(String[]::new))
          .orElse(new String[0]);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void initializeFields(Translator object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
      
      if (object instanceof PackageTranslator packageTranslator) {
        baseTranslatorForm = new PackageTranslatorForm(packageTranslator, headerOptions);
      } else if (object instanceof PersonTranslator personTranslator) {
        baseTranslatorForm = new PersonTranslatorForm(personTranslator, headerOptions);
      } else if (object instanceof OrganizationTranslator organizationTranslator) {
        baseTranslatorForm = new OrganizationTranslatorForm(organizationTranslator, headerOptions);
      } else if (object instanceof ShipTranslator shipTranslator) {
        baseTranslatorForm = new ShipTranslatorForm(shipTranslator, headerOptions);
      } else if (object instanceof DetectionTypeTranslator detectionTypeTranslator) {
        baseTranslatorForm = new DetectionTypeTranslatorForm(detectionTypeTranslator, headerOptions);
      } else if (object instanceof FileTypeTranslator fileTypeTranslator) {
        baseTranslatorForm = new FileTypeTranslatorForm(fileTypeTranslator, headerOptions);
      } else if (object instanceof InstrumentTranslator instrumentTranslator) {
        baseTranslatorForm = new InstrumentTranslatorForm(instrumentTranslator, headerOptions);
      } else if (object instanceof PlatformTranslator platformTranslator) {
        baseTranslatorForm = new PlatformTranslatorForm(platformTranslator, headerOptions);
      } else if (object instanceof ProjectTranslator projectTranslator) {
        baseTranslatorForm = new ProjectTranslatorForm(projectTranslator, headerOptions);
      } else if (object instanceof SeaTranslator seaTranslator) {
        baseTranslatorForm = new SeaTranslatorForm(seaTranslator, headerOptions);
      } else if (object instanceof SensorTranslator sensorTranslator) {
        baseTranslatorForm = new SensorTranslatorForm(sensorTranslator, headerOptions);
      }
      add(baseTranslatorForm, configureLayout((c) -> {
        c.gridx = 0; c.gridy = 5; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
      }));
    } else {
      add(baseTranslatorForm, configureLayout((c) -> {
        c.gridx = 0; c.gridy = 7; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
      }));

      JComboBox<String> translatorTypeComboBox = getTranslatorTypeComboBox();

      add(new JLabel("Translator Type"), configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
      add(translatorTypeComboBox, configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    }

    revalidate();
  }

  private JComboBox<String> getTranslatorTypeComboBox() {
    JComboBox<String> translatorTypeComboBox = new JComboBox<>(new DefaultComboBoxModel<>(
        new String[] { 
            "Package",
            "Person",
            "Organization",
            "Ship",
            "Detection Type",
            "File Type",
            "Instrument",
            "Platform",
            "Project",
            "Sea Area",
            "Sensor"
        }
    ));
    translatorTypeComboBox.setName("translatorType");
    translatorTypeComboBox.setSelectedItem(null);
    translatorTypeComboBox.addItemListener(e -> {
      String choice = (String) e.getItem();
      if (choice == null) {
        return;
      }
      
      BaseTranslatorForm<?> newTranslatorForm = switch (choice) {
        case "Package" -> new PackageTranslatorForm(null, headerOptions);
        case "Person" -> new PersonTranslatorForm(null, headerOptions);
        case "Organization" -> new OrganizationTranslatorForm(null, headerOptions);
        case "Ship" -> new ShipTranslatorForm(null, headerOptions);
        case "Detection Type" -> new DetectionTypeTranslatorForm(null, headerOptions);
        case "File Type" -> new FileTypeTranslatorForm(null, headerOptions);
        case "Instrument" -> new InstrumentTranslatorForm(null, headerOptions);
        case "Platform" -> new PlatformTranslatorForm(null, headerOptions);
        case "Project" -> new ProjectTranslatorForm(null, headerOptions);
        case "Sea Area" -> new SeaTranslatorForm(null, headerOptions);
        case "Sensor" -> new SensorTranslatorForm(null, headerOptions);
        default -> null;
      };

      if (newTranslatorForm != null) {
        remove(baseTranslatorForm);
        revalidate();
        baseTranslatorForm = newTranslatorForm;
        add(baseTranslatorForm, configureLayout((c) -> {
          c.gridx = 0; c.gridy = 7; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        }));
        revalidate();
      }
    });
    return translatorTypeComboBox;
  }

  @Override
  protected void save(CRUDRepository<Translator> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Translator translator = baseTranslatorForm.toTranslator(uuidField, nameField);
    boolean isUpdate = translator.getUuid() != null;
    
    if (isUpdate) {
      repository.update(translator.getUuid(), translator);
    } else {
      repository.create(translator);
    }
  }

  @Override
  protected void delete(CRUDRepository<Translator> repository) throws NotFoundException, DatastoreException {
    Translator translator = baseTranslatorForm.toTranslator(uuidField, nameField);
    repository.delete(translator.getUuid());
  }
}
