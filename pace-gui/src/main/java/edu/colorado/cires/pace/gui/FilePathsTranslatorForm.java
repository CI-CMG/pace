package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * FilePathsTranslatorForm extends JPanel and provides structure for file path
 * translator form
 */
public class FilePathsTranslatorForm extends JPanel {
  
  private final JComboBox<String> temperaturePathField = new JComboBox<>();
  private final JComboBox<String> biologicalPathField = new JComboBox<>();
  private final JComboBox<String> otherPathField = new JComboBox<>();
  private final JComboBox<String> documentsPathField = new JComboBox<>();
  private final JComboBox<String> sourcePathField = new JComboBox<>();

  /**
   * Initializes a file paths translator form
   * @param headerOptions possible headers to select from
   * @param initialTranslator base translator to build upon
   */
  public FilePathsTranslatorForm(String[] headerOptions, PackageTranslator initialTranslator) {
    temperaturePathField.setName("temperaturePath");
    biologicalPathField.setName("biologicalPath");
    otherPathField.setName("otherPath");
    documentsPathField.setName("documentsPath");
    sourcePathField.setName("sourcePath");
    addComponents();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addComponents() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Temperature Path"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(temperaturePathField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Biological Path"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(biologicalPathField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    add(new JLabel("Other Path"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(otherPathField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    add(new JLabel("Documents Path"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(documentsPathField, configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
    add(new JLabel("Source Path"), configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
    add(sourcePathField, configureLayout((c) -> { c.gridx = 0; c.gridy = 11; c.weightx = 1; }));

    add(new JLabel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 12; c.weighty = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, PackageTranslator initialTranslator) {
    updateComboBoxModel(temperaturePathField, headerOptions);
    updateComboBoxModel(biologicalPathField, headerOptions);
    updateComboBoxModel(otherPathField, headerOptions);
    updateComboBoxModel(documentsPathField, headerOptions);
    updateComboBoxModel(sourcePathField, headerOptions);
    
    if (initialTranslator != null) {
      temperaturePathField.setSelectedItem(initialTranslator.getTemperaturePath());
      biologicalPathField.setSelectedItem(initialTranslator.getBiologicalPath());
      otherPathField.setSelectedItem(initialTranslator.getOtherPath());
      documentsPathField.setSelectedItem(initialTranslator.getDocumentsPath());
      sourcePathField.setSelectedItem(initialTranslator.getSourcePath());
    }
  }

  /**
   * Returns the temperature path value
   * @return String temperature path
   */
  public String getTemperaturePathValue() {
    return (String) temperaturePathField.getSelectedItem();
  }

  /**
   * Returns the biological path value
   * @return String biological path
   */
  public String getBiologicalPathValue() {
    return (String) biologicalPathField.getSelectedItem();
  }

  /**
   * Returns the other path value
   * @return String other path
   */
  public String getOtherPathValue() {
    return (String) otherPathField.getSelectedItem();
  }

  /**
   * Returns the documents path value
   * @return String documents path
   */
  public String getDocumentsPathValue() {
    return (String) documentsPathField.getSelectedItem();
  }

  /**
   * Returns the source path value
   * @return String source path
   */
  public String getSourcePathValue() {
    return (String) sourcePathField.getSelectedItem();
  }

  /**
   * Changes the possible selectable headers
   * @param options new headers to change possible selections to
   */
  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(temperaturePathField, options);
    updateComboBoxModel(biologicalPathField, options);
    updateComboBoxModel(otherPathField, options);
    updateComboBoxModel(documentsPathField, options);
    updateComboBoxModel(sourcePathField, options);
  }
}
