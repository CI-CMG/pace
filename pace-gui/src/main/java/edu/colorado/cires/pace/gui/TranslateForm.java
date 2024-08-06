package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.utilities.TranslationType.csv;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.CSVReader;
import edu.colorado.cires.pace.translator.ExcelReader;
import edu.colorado.cires.pace.translator.MapWithRowNumber;
import edu.colorado.cires.pace.translator.ObjectWithRowError;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.utilities.TranslationType;
import jakarta.validation.ConstraintViolationException;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.lang3.StringUtils;

public class TranslateForm<O extends AbstractObject, T extends Translator> extends JPanel {
  
  private final DefaultComboBoxModel<String> translatorComboBoxModel = new DefaultComboBoxModel<>();
  private final JTextField selectedFileField = new JTextField();
  private final JCheckBox updateCheckBox = new JCheckBox();
  
  private final TranslatorRepository translatorRepository;
  private final Converter<T, O> converter;
  
  private final Class<T> translatorClazz;
  private final CRUDRepository<O> repository;
  private final Class<O> clazz;
  private final Runnable successAction;

  public TranslateForm(Runnable successAction, CRUDRepository<O> repository, Class<O> clazz, TranslatorRepository translatorRepository,
      Converter<T, O> converter, Class<T> translatorClazz) {
    this.translatorRepository = translatorRepository;
    this.converter = converter;
    this.translatorClazz = translatorClazz;
    this.repository = repository;
    this.clazz = clazz;
    this.successAction = successAction;
  }
  
  public void init() throws DatastoreException {
    setName("translateForm");
    this.translatorComboBoxModel.removeAllElements();
    this.translatorComboBoxModel.addAll(translatorRepository.findAll()
        .filter(t -> translatorClazz.isAssignableFrom(t.getClass()))
        .map(Translator::getName)
        .toList());

    setLayout(new GridBagLayout());

    add(createFormPanel(), configureLayout((c) -> { c.gridx = c.gridy = 0; c.anchor = GridBagConstraints.NORTH; c.weightx = 1; }));
    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weighty = 1; }));
    add(createControlPanel(successAction, repository, clazz), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
  }

  private JPanel createFormPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setName("translateFormPanel");
    panel.add(new JLabel("Translator"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    JComboBox<String> translatorComboBox = new JComboBox<>(translatorComboBoxModel);
    translatorComboBox.setSelectedItem(null);
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
    
    chooseFileButton.addActionListener((e) -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.setFileFilter(new FileNameExtensionFilter("csv, xlsx", "csv", "xlsx"));
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
        selectedFileField.setText(fileChooser.getSelectedFile().toString());
      }
    });
    
    return panel;
  }
  
  private JPanel createControlPanel(Runnable successAction, CRUDRepository<O> repository, Class<O> clazz) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JButton translateButton = new JButton("Translate");
    panel.add(translateButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    
    translateButton.addActionListener((e) -> translateSpreadsheet(successAction, repository, clazz));
    
    return panel;
  }
  
  private void translateSpreadsheet(Runnable successAction, CRUDRepository<O> repository, Class<O> clazz) {
    String translatorName = (String) translatorComboBoxModel.getSelectedItem();
    if (translatorName == null) {
      JOptionPane.showMessageDialog(this, "Please choose a translator", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    T translator;
    try {
      translator = (T) translatorRepository.getByUniqueField(translatorName);
    } catch (DatastoreException | NotFoundException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    String selectedFile = selectedFileField.getText();
    if (StringUtils.isBlank(selectedFile)) {
      JOptionPane.showMessageDialog(this, "Please select a file", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    Function<ObjectWithRowError<O>, ObjectWithRowError<O>> saveAction = (o) -> {
      O object = o.object();
      try {
        if (updateCheckBox.isSelected()) {
          return new ObjectWithRowError<>(
              repository.update(object.getUuid(), object),
              o.row(),
              null
          );
        } else {
          return new ObjectWithRowError<>(
              repository.create(object),
              o.row(),
              null
          );
        }
      } catch (ConstraintViolationException | BadArgumentException | ConflictException | NotFoundException | DatastoreException e) {
        return new ObjectWithRowError<>(
            object,
            o.row(),
            e
        );
      }
    };

    TranslationType translationType;
    if (selectedFile.endsWith("xlsx")) {
      translationType = TranslationType.excel;
    } else if (selectedFile.endsWith("csv")) {
      translationType = csv;
    } else {
      JOptionPane.showMessageDialog(this, "Invalid spreadsheet format", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    List<ObjectWithRowError<O>> exceptions = switch (translationType) {
      case excel -> {
        try (InputStream inputStream = new FileInputStream(selectedFile)) {
          yield postProcessStream(
              ExcelReader.read(inputStream, 0)
                  .filter(this::isProcessableRow)
                  .map(mapWithRowNumber -> {
                    try {
                      RuntimeException runtimeException = new RuntimeException();
                      O object = converter.convert(translator, mapWithRowNumber.map(), mapWithRowNumber.row(), runtimeException);
                      if (runtimeException.getSuppressed().length == 0) {
                        return new ObjectWithRowError<>(object, mapWithRowNumber.row(), null);
                      }
                      return new ObjectWithRowError<>(object, mapWithRowNumber.row(), runtimeException);
                    } catch (TranslationException e) {
                      return new ObjectWithRowError<>(null, mapWithRowNumber.row(), e);
                    }
                  }),
              saveAction,
              successAction
          );
        } catch (IOException e) {
          JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          yield Collections.emptyList();
        }
      }
      case csv -> {
        try (InputStream inputStream = new FileInputStream(selectedFile); Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
          yield postProcessStream(
              CSVReader.read(reader)
                  .filter(this::isProcessableRow)
                  .map(mapWithRowNumber -> {
                    try {
                      RuntimeException runtimeException = new RuntimeException();
                      O object = converter.convert(translator, mapWithRowNumber.map(), mapWithRowNumber.row(), runtimeException);
                      if (runtimeException.getSuppressed().length == 0) {
                        return new ObjectWithRowError<>(object, mapWithRowNumber.row(), null);
                      }
                      return new ObjectWithRowError<>(object, mapWithRowNumber.row(), runtimeException);
                    } catch (TranslationException e) {
                      return new ObjectWithRowError<>(null, mapWithRowNumber.row(), e);
                    }
                  }),
              saveAction,
              successAction
          );
        } catch (IOException e) {
          JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          yield Collections.emptyList();
        }
      }
    };

    if (!exceptions.isEmpty()) {
      JDialog errorDialog = new JDialog();
      errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      errorDialog.setLocationRelativeTo(this);
      ErrorSpreadsheetPanel<O> errorSpreadsheetPanel = new ErrorSpreadsheetPanel<>(new File(selectedFile), exceptions, !clazz.getSimpleName().equals(Package.class.getSimpleName()));
      errorSpreadsheetPanel.init();
      Dimension size = UIUtils.getPercentageOfWindowDimension(0.5, 0.4);
      errorDialog.setSize(size);
      errorDialog.setPreferredSize(size);
      errorDialog.setTitle("Spreadsheet Errors");
      errorDialog.add(errorSpreadsheetPanel);
      errorDialog.pack();
      errorDialog.setVisible(true);
    }

  }
  
  private boolean isProcessableRow(MapWithRowNumber rowNumber) {
    return !rowNumber.map().values().stream().allMatch(
        v -> v.value().isEmpty()
    );
  }
  
  private List<ObjectWithRowError<O>> postProcessStream(Stream<ObjectWithRowError<O>> stream, Function<ObjectWithRowError<O>, ObjectWithRowError<O>> saveAction, Runnable successAction) {
    List<ObjectWithRowError<O>> exceptions = new ArrayList<>(0);
    stream.peek(o -> {
      Throwable exception = o.throwable();
      if (exception != null) {
        if (exception.getSuppressed().length == 0) {
          exceptions.add(new ObjectWithRowError<>(o.object(), o.row(), exception));
        } else {
          exceptions.addAll(
              Arrays.stream(exception.getSuppressed())
                  .map(throwable -> new ObjectWithRowError<>(o.object(), o.row(), throwable))
                  .toList()
          );
        }
      }
    }).filter(o -> Objects.nonNull(o.object()) && Objects.isNull(o.throwable()))
    .map(saveAction)
    .filter(objectWithRowConversionException -> Objects.nonNull(objectWithRowConversionException.throwable()))
    .forEach(exceptions::add);
    
    successAction.run();
    
    return exceptions;
  }
}
