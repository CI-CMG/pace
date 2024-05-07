package edu.colorado.pace.gui.metadata.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.datastore.json.CSVTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ExcelTranslatorJsonDatastore;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import edu.colorado.cires.pace.translator.ObjectWithRowConversionException;
import edu.colorado.cires.pace.translator.RowConversionException;
import edu.colorado.cires.pace.translator.TranslatorExecutor;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorExecutor;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorExecutor;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.pace.utilities.TranslationType;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

public class TranslateForm<O extends ObjectWithUniqueField> extends JDialog {

  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JComboBox<TranslationType> formatOptions;
  private JComboBox<String> translatorOptions;
  private JCheckBox updateCheckbox;
  private JTextField fileField;
  private JButton chooseFileButton;
  private final Map<String, ExcelTranslator> excelTranslators;
  private final Map<String, CSVTranslator> csvTranslators;
  private final Class<O> clazz;
  private final CRUDRepository<O> repository;

  public TranslateForm(Class<O> clazz, CRUDRepository<O> repository, Runnable translationCompleteAction) throws IOException, DatastoreException {
    this.clazz = clazz;
    this.repository = repository;
    Path workDir = new ApplicationPropertyResolver().getWorkDir();
    ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
    excelTranslators = new ExcelTranslatorRepository(
        new ExcelTranslatorJsonDatastore(
            workDir, objectMapper
        )
    ).findAll().collect(
        Collectors.toMap(
            ExcelTranslator::getName,
            t -> t
        )
    );
    csvTranslators = new CSVTranslatorRepository(
        new CSVTranslatorJsonDatastore(
            workDir, objectMapper
        )
    ).findAll().collect(
        Collectors.toMap(
            CSVTranslator::getName,
            t -> t
        )
    );
    
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK(translationCompleteAction));

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    DefaultComboBoxModel<TranslationType> comboBoxModel = new DefaultComboBoxModel<>();
    comboBoxModel.addAll(Arrays.stream(TranslationType.values()).toList());
    formatOptions.setModel(comboBoxModel);
    
    DefaultComboBoxModel<String> translatorOptionsModel = new DefaultComboBoxModel<>();
    translatorOptions.setModel(translatorOptionsModel);
    
    formatOptions.addItemListener((i) -> {
      translatorOptionsModel.removeAllElements();
      
      TranslationType translationType = (TranslationType) i.getItem();
      if (translationType.equals(TranslationType.excel)) {
        translatorOptionsModel.addAll(excelTranslators.keySet());
      } else if (translationType.equals(TranslationType.csv)) {
        translatorOptionsModel.addAll(csvTranslators.keySet());
      }
    });
    chooseFileButton.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      
      String acceptedExtension = null;
      TranslationType translationType = (TranslationType) formatOptions.getSelectedItem();
      if (translationType != null) {
        if (translationType.equals(TranslationType.csv)) {
          acceptedExtension = "csv";
        } else if (translationType.equals(TranslationType.excel)) {
          acceptedExtension = "xlsx";
        }
      }

      String finalAcceptedExtension = acceptedExtension;
      fileChooser.setFileFilter(new FileFilter() {
        @Override
        public boolean accept(File f) {
          if (f.isDirectory()) {
            return true;
          }
          if (finalAcceptedExtension == null) {
            return false;
          }
          return f.getName().endsWith(finalAcceptedExtension);
        }

        @Override
        public String getDescription() {
          return finalAcceptedExtension;
        }
      });

      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        fileField.setText(fileChooser.getSelectedFile().toString());
      }
    });
  }

  private void onOK(Runnable translationCompleteAction) {
    TranslationType translationType = (TranslationType) formatOptions.getSelectedItem();
    if (translationType == null) {
      JOptionPane.showMessageDialog(this, "Select a file format", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    TranslatorExecutor<O, ?> executor = null;
    
    try {
      if (translationType.equals(TranslationType.csv)) {
        executor = new CSVTranslatorExecutor<>(
            csvTranslators.get((String) translatorOptions.getSelectedItem()),
            clazz
        );
      } else if (translationType.equals(TranslationType.excel)) {
        executor = new ExcelTranslatorExecutor<>(
            excelTranslators.get((String) translatorOptions.getSelectedItem()),
            clazz
        );
      }
    } catch (TranslatorValidationException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (executor == null) {
      JOptionPane.showMessageDialog(this, String.format(
          "Invalid file format: %s", translationType.name()
      ), "Error", JOptionPane.ERROR_MESSAGE);
      return;
    } else {
      String absoluteFilePath = fileField.getText();
      if (absoluteFilePath == null) {
        JOptionPane.showMessageDialog(this, "Select a file", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      Path path = Paths.get(absoluteFilePath);
      
      try {
        if (translationType.equals(TranslationType.csv)) {
          try (
              InputStream inputStream = new FileInputStream(path.toFile());
              Reader reader = new InputStreamReader(inputStream)
          ) {
            processStream(executor.translate(reader));
          }
        } else {
          try (InputStream inputStream = new FileInputStream(path.toFile())) {
            processStream(executor.translate(inputStream));
          }
        }
        
        translationCompleteAction.run();
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
    
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }
  
  private void processStream(Stream<ObjectWithRowConversionException<O>> stream) {
    stream
        .forEach(o -> {
          O object = o.object();
          RowConversionException exception = o.rowConversionException();
          if (exception != null) {
            JOptionPane.showMessageDialog(this, String.format(
                "Error translating row %s: %s", exception.getRow(), exception.getMessage()
            ), "Error", JOptionPane.ERROR_MESSAGE);
            return;
          }
          try {
            if (updateCheckbox.isSelected()) {
              repository.update(object.getUuid(), object);
            } else {
              repository.create(object);
            }
          } catch (Exception e) {
            JOptionPane.showMessageDialog(this, String.format(
                "Error saving row %s: %s", o.row(), e.getMessage()
            ), "Error", JOptionPane.ERROR_MESSAGE);
          }
        });
  }
}
