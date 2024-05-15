package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.ObjectWithRowConversionException;
import edu.colorado.cires.pace.translator.RowConversionException;
import edu.colorado.cires.pace.translator.TranslatorExecutor;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import edu.colorado.cires.pace.translator.csv.CSVGenerator;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorExecutor;
import edu.colorado.cires.pace.translator.excel.ExcelGenerator;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorExecutor;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.TranslationType;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.lang3.StringUtils;

public class TranslateForm<O extends ObjectWithUniqueField> extends JPanel {
  
  private final DefaultComboBoxModel<TranslationType> fileFormatComboBoxModel = new DefaultComboBoxModel<>();
  private final DefaultComboBoxModel<String> translatorComboBoxModel = new DefaultComboBoxModel<>();
  private final JTextField selectedFileField = new JTextField();
  private final JCheckBox updateCheckBox = new JCheckBox();
  private FileFilter fileNameFilter = null;
  
  private final Map<String, ExcelTranslator> excelTranslators = new HashMap<>(0);
  private final Map<String, CSVTranslator> csvTranslators = new HashMap<>(0);

  public TranslateForm(Runnable successAction, CRUDRepository<O> repository, ExcelTranslatorRepository excelTranslatorRepository, CSVTranslatorRepository csvTranslatorRepository, Class<O> clazz, CRUDRepository<?>... dependencyRepositories)
      throws DatastoreException {
    initializeModels();
    initializeTranslatorOptions(excelTranslatorRepository, excelTranslators);
    initializeTranslatorOptions(csvTranslatorRepository, csvTranslators);
    
    setLayout(new GridBagLayout());
    
    add(createFormPanel(), configureLayout((c) -> { c.gridx = c.gridy = 0; c.anchor = GridBagConstraints.NORTH; c.weightx = 1; }));
    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weighty = 1; }));
    add(createControlPanel(successAction, repository, excelTranslatorRepository, csvTranslatorRepository, clazz, dependencyRepositories), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
  }
  
  private void initializeModels() {
    fileFormatComboBoxModel.addAll(Arrays.stream(TranslationType.values()).toList());
  }
  
  private <T extends TabularTranslator<? extends TabularTranslationField>> void initializeTranslatorOptions(CRUDRepository<T> repository, Map<String, T> optionsMap)
      throws DatastoreException {
    optionsMap.putAll(
        repository.findAll().collect(Collectors.toMap(
            ObjectWithName::getName,
            t -> t
        ))
    );
  }
  
  private JPanel createFormPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(new JLabel("File Format"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    JComboBox<TranslationType> fileFormatComboBox = new JComboBox<>(fileFormatComboBoxModel);
    panel.add(fileFormatComboBox, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    panel.add(new JLabel("Translator"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    JComboBox<String> translatorComboBox = new JComboBox<>(translatorComboBoxModel); 
    panel.add(translatorComboBox, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    panel.add(new JLabel("Spreadsheet"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    
    JPanel chooseFilePanel = new JPanel(new GridBagLayout());
    selectedFileField.setEditable(false);
    chooseFilePanel.add(selectedFileField, configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    JButton chooseFileButton = new JButton("Choose File");
    chooseFilePanel.add(chooseFileButton, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 0; }));
    panel.add(chooseFilePanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    
    JPanel updatePanel = new JPanel(new GridBagLayout());
    updatePanel.add(new JLabel("Update:"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 0; }));
    updatePanel.add(updateCheckBox, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 0; }));
    updatePanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 1; }));
    panel.add(updatePanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 6; }));
    
    fileFormatComboBox.addItemListener((l) -> {
      TranslationType translationType = (TranslationType) l.getItem();
      translatorComboBoxModel.removeAllElements();
      translatorComboBoxModel.addAll(translationType.equals(TranslationType.csv) ? csvTranslators.keySet() : excelTranslators.keySet());
      fileNameFilter = new FileNameExtensionFilter(translationType.name(), translationType.equals(TranslationType.csv) ? "csv" : "xlsx");
    });
    chooseFileButton.addActionListener((e) -> {
      if (fileNameFilter == null) {
        JOptionPane.showMessageDialog(this, "Please choose a file format", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.setFileFilter(fileNameFilter);
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
        selectedFileField.setText(fileChooser.getSelectedFile().toString());
      }
    });
    
    return panel;
  }
  
  private JPanel createControlPanel(Runnable successAction, CRUDRepository<O> repository, ExcelTranslatorRepository excelTranslatorRepository, CSVTranslatorRepository csvTranslatorRepository, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) {
    JPanel panel = new JPanel(new GridBagLayout());
    JButton generateTemplateButton = new JButton("Generate Template");
    panel.add(generateTemplateButton, configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 0; }));
    panel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JButton translateButton = new JButton("Translate");
    panel.add(translateButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    
    translateButton.addActionListener((e) -> translateSpreadsheet(successAction, repository, excelTranslatorRepository, csvTranslatorRepository, clazz, dependencyRepositories));
    generateTemplateButton.addActionListener((e) -> generateTemplate());
    
    return panel;
  }
  
  private void generateTemplate() {
    TranslationType translationType = (TranslationType) fileFormatComboBoxModel.getSelectedItem();
    if (translationType == null) {
      JOptionPane.showMessageDialog(this, "Please choose a file format", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String translatorName = (String) translatorComboBoxModel.getSelectedItem();
    if (translatorName == null) {
      JOptionPane.showMessageDialog(this, "Please choose a translator", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    ApplicationPropertyResolver propertyResolver = new ApplicationPropertyResolver();

    Path outputDirectory = propertyResolver.getWorkDir().resolve("templates");
    Path outputPath = null;
    
    try {
      switch (translationType) {
        case csv -> {
          CSVTranslator translator = csvTranslators.get(translatorName);
          outputPath = outputDirectory.resolve(String.format(
              "%s.csv", translatorName
          ));
          new CSVGenerator().generateSpreadsheet(
              outputPath,
              translator
          );
        }
        case excel -> {
          ExcelTranslator translator = excelTranslators.get(translatorName);
          outputPath = outputDirectory.resolve(String.format(
              "%s.xlsx", translatorName
          ));
          new ExcelGenerator(propertyResolver.getVersion(false)).generateSpreadsheet(
              outputPath,
              translator
          );
        }
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().open(outputPath.toFile());
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, String.format(
            "Failed to open template. Template can be opened at: %s", outputPath
        ), "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, String.format(
          "Generated template. Template can be opened at: %s", outputPath
      ), null, JOptionPane.PLAIN_MESSAGE);
    }
    
  }
  
  private void translateSpreadsheet(Runnable successAction, CRUDRepository<O> repository, ExcelTranslatorRepository excelTranslatorRepository, CSVTranslatorRepository csvTranslatorRepository, Class<O> clazz, CRUDRepository<?>... dependencyRepositories) {
    TranslationType translationType = (TranslationType) fileFormatComboBoxModel.getSelectedItem();
    if (translationType == null) {
      JOptionPane.showMessageDialog(this, "Please choose a file format", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String translatorName = (String) translatorComboBoxModel.getSelectedItem();
    if (translatorName == null) {
      JOptionPane.showMessageDialog(this, "Please choose a translator", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    TabularTranslator<? extends TabularTranslationField> translator = switch (translationType) {
      case csv -> csvTranslators.get(translatorName);
      case excel -> excelTranslators.get(translatorName);
    };

    TranslatorExecutor<O, ? extends TabularTranslator<? extends TabularTranslationField>> executor = null;
    try {
      switch (translationType) {
        case csv -> executor = new CSVTranslatorExecutor<>((CSVTranslator) translator, clazz, dependencyRepositories);
        case excel -> executor = new ExcelTranslatorExecutor<>((ExcelTranslator) translator, clazz, dependencyRepositories);
      }
    } catch (TranslatorValidationException ex) {
      JOptionPane.showMessageDialog(this, "Invalid translator", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    String selectedFile = selectedFileField.getText();
    if (StringUtils.isBlank(selectedFile)) {
      JOptionPane.showMessageDialog(this, "Please select a file", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    Consumer<O> saveAction = (o) -> {
      try {
        if (updateCheckBox.isSelected()) {
          repository.update(o.getUuid(), o);
        } else {
          repository.create(o);
        }
      } catch (DatastoreException | ConflictException | NotFoundException | BadArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    };

    try {
      switch (translationType) {
        case excel -> {
          try (InputStream inputStream = new FileInputStream(selectedFile)) {
            postProcessStream(executor.translate(inputStream), saveAction, successAction);
          }
        }
        case csv -> {
          try (InputStream inputStream = new FileInputStream(selectedFile); Reader reader = new InputStreamReader(inputStream)) {
            postProcessStream(executor.translate(reader), saveAction, successAction);
          }
        }
      };
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

  }
  
  private void postProcessStream(Stream<ObjectWithRowConversionException<O>> stream, Consumer<O> saveAction, Runnable successAction) {
    stream.peek(o -> {
      RowConversionException exception = o.rowConversionException();
      if (exception != null) {
        JOptionPane.showMessageDialog(this, Arrays.stream(exception.getSuppressed())
                .map(Throwable::getMessage)
            .collect(Collectors.joining("\n")), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }).map(ObjectWithRowConversionException::object)
        .filter(Objects::nonNull)
        .forEach(saveAction);
    
    successAction.run();
  }
}
