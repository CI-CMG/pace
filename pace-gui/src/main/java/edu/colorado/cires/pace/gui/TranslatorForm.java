package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.detectionType.translator.DetectionTypeTranslator;
import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;
import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.ArrayUtils;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;

public class TranslatorForm extends ObjectWithNameForm<Translator> {
  private String[] headerOptions = new String[] {};
  
  private JPanel contentPanel;
  
  private BaseTranslatorForm<? extends Translator> baseTranslatorForm;
  
  private static Translator initialTranslator;
  
  private static final BaseTranslatorForm<? extends Translator> DEFAULT_BASE_TRANSLATOR_FORM = new BaseTranslatorForm<>(new String[] {}) {
    @Override
    protected void addUniqueFields() {

    }

    @Override
    protected void initializeUniqueFields(Translator initialTranslator) {

    }

    @Override
    protected Translator toTranslator(UUID uuid, String name) {
      return null;
    }

    @Override
    protected void updateHeaderOptions(String[] options) {
      
    }
  };

  private TranslatorForm(Translator initialTranslator) {
    super(initialTranslator, false, false);
  }
  
  public static TranslatorForm create(Translator translator) {
    initialTranslator = translator;
    return new TranslatorForm(translator);
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
  protected Translator objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return (Translator) baseTranslatorForm.toTranslator(uuid, uniqueField).setVisible(visible);
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    baseTranslatorForm = DEFAULT_BASE_TRANSLATOR_FORM;
    this.contentPanel = contentPanel;
    if (initialTranslator == null) {
      contentPanel.add(baseTranslatorForm, configureLayout((c) -> {
        c.gridx = 0; c.gridy = 7; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
      }));

      JComboBox<String> translatorTypeComboBox = getTranslatorTypeComboBox();

      contentPanel.add(new JLabel("Translator Type"), configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
      contentPanel.add(translatorTypeComboBox, configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    }
    
    JButton addFieldsButton = new JButton("Import Spreadsheet Headers");
    addFieldsButton.addActionListener(e -> importSpreadsheetHeaders());
    contentPanel.add(addFieldsButton, configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 0; }));
  }

  @Override
  protected void initializeAdditionalFields(Translator object, CRUDRepository<?>... dependencyRepositories) {
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
    contentPanel.add(baseTranslatorForm, configureLayout((c) -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
    }));

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
        contentPanel.remove(baseTranslatorForm);
        revalidate();
        baseTranslatorForm = newTranslatorForm;
        contentPanel.add(baseTranslatorForm, configureLayout((c) -> {
          c.gridx = 0; c.gridy = 7; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        }));
        revalidate();
      }
    });
    return translatorTypeComboBox;
  }
}
