package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.SoftwareDependentPackageTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SoftwareForm<T extends SoftwareDependentPackageTranslator> extends JPanel {

  private final JComboBox<String> softwareNamesField = new JComboBox<>();
  private final JComboBox<String> softwareVersionsField = new JComboBox<>();
  private final JComboBox<String> softwareProtocolCitationField = new JComboBox<>();
  private final JComboBox<String> softwareDescriptionField = new JComboBox<>();
  private final JComboBox<String> softwareProcessingDescriptionField = new JComboBox<>();

  public SoftwareForm(String[] headerOptions, T initialTranslator) {
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Software Names"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(softwareNamesField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1;
    }));
    add(new JLabel("Software Versions"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    add(softwareVersionsField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1;
    }));
    add(new JLabel("Software Protocol Citation"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1;
    }));
    add(softwareProtocolCitationField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1;
    }));
    add(new JLabel("Software Description"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 6; c.weightx = 1;
    }));
    add(softwareDescriptionField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 7; c.weightx = 1;
    }));
    add(new JLabel("Software Processing Description"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 8; c.weightx = 1;
    }));
    add(softwareProcessingDescriptionField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 9; c.weightx = 1;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 11; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, T initialTranslator) {
    updateComboBoxModel(softwareNamesField, headerOptions);
    updateComboBoxModel(softwareVersionsField, headerOptions);
    updateComboBoxModel(softwareProtocolCitationField, headerOptions);
    updateComboBoxModel(softwareDescriptionField, headerOptions);
    updateComboBoxModel(softwareProcessingDescriptionField, headerOptions);
    
    if (initialTranslator != null) {
      softwareNamesField.setSelectedItem(initialTranslator.getSoftwareNames());
      softwareVersionsField.setSelectedItem(initialTranslator.getSoftwareVersions());
      softwareProtocolCitationField.setSelectedItem(initialTranslator.getSoftwareProtocolCitation());
      softwareDescriptionField.setSelectedItem(initialTranslator.getSoftwareDescription());
      softwareProcessingDescriptionField.setSelectedItem(initialTranslator.getSoftwareProcessingDescription());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(softwareNamesField, headerOptions);
    updateComboBoxModel(softwareVersionsField, headerOptions);
    updateComboBoxModel(softwareProtocolCitationField, headerOptions);
    updateComboBoxModel(softwareDescriptionField, headerOptions);
    updateComboBoxModel(softwareProcessingDescriptionField, headerOptions);
  }

  public String getSoftwareNamesValue() {
    return (String) softwareNamesField.getSelectedItem();
  }

  public String getSoftwareVersionsValue() {
    return (String) softwareVersionsField.getSelectedItem();
  }

  public String getSoftwareProtocolCitationValue() {
    return (String) softwareProtocolCitationField.getSelectedItem();
  }

  public String getSoftwareDescriptionValue() {
    return (String) softwareDescriptionField.getSelectedItem();
  }

  public String getSoftwareProcessingDescriptionValue() {
    return (String) softwareProcessingDescriptionField.getSelectedItem();
  }
}
