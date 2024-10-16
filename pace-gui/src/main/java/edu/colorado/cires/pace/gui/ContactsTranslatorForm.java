package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ContactsTranslatorForm extends JPanel and generates the
 * fields for a contact translator form
 */
public class ContactsTranslatorForm extends JPanel {
  
  private final JComboBox<String> scientistsField = new JComboBox<>();
  private final JComboBox<String> sponsorsField = new JComboBox<>();
  private final JComboBox<String> fundersField = new JComboBox<>();
  private final JComboBox<String> datasetPackagerField = new JComboBox<>();

  /**
   * Initializes a contacts translator form
   *
   * @param headerOptions Selectable headers within translator
   * @param initialTranslator base translator to build onto
   */
  public ContactsTranslatorForm(String[] headerOptions, PackageTranslator initialTranslator) {
    scientistsField.setName("scientists");
    sponsorsField.setName("sponsors");
    fundersField.setName("funders");
    datasetPackagerField.setName("datasetPackager");
    addComponents();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addComponents() {
    setLayout(new GridBagLayout());

    add(new JLabel("Scientists"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(scientistsField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Sources"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(sponsorsField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    add(new JLabel("Funders"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(fundersField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    add(new JLabel("Dataset Packager"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(datasetPackagerField, configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
    
    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weighty = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, PackageTranslator initialTranslator) {
    updateComboBoxModel(scientistsField, headerOptions);
    updateComboBoxModel(sponsorsField, headerOptions);
    updateComboBoxModel(fundersField, headerOptions);
    updateComboBoxModel(datasetPackagerField, headerOptions);
    
    if (initialTranslator != null) {
      scientistsField.setSelectedItem(initialTranslator.getScientists());
      sponsorsField.setSelectedItem(initialTranslator.getSponsors());
      fundersField.setSelectedItem(initialTranslator.getFunders());
      datasetPackagerField.setSelectedItem(initialTranslator.getDatasetPackager());
    }
  }

  /**
   * Returns scientists value
   * @return String scientists value
   */
  public String getScientistsValue() {
    return (String) scientistsField.getSelectedItem();
  }

  /**
   * Returns sponsors value
   * @return String sponsors value
   */
  public String getSponsorsValue() {
    return (String) sponsorsField.getSelectedItem();
  }

  /**
   * Returns funders value
   * @return String funders value
   */
  public String getFundersValue() {
    return (String) fundersField.getSelectedItem();
  }

  /**
   * Returns dataset packager value
   * @return String dataset packager value
   */
  public String getDatasetPackagerValue() {
    return (String) datasetPackagerField.getSelectedItem();
  }

  /**
   * Updates the header options to those provided
   * @param options to allow selection of
   */
  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(scientistsField, options);
    updateComboBoxModel(sponsorsField, options);
    updateComboBoxModel(fundersField, options);
    updateComboBoxModel(datasetPackagerField, options);
  }
}
