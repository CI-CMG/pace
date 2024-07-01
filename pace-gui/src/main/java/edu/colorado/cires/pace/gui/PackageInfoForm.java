package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.DateTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.data.translator.TimeTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PackageInfoForm extends JPanel {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> siteOrCruiseNameField = new JComboBox<>();
  private final JComboBox<String> deploymentIdField = new JComboBox<>();
  private final JComboBox<String> projectsField = new JComboBox<>();
  private final JComboBox<String> platformField = new JComboBox<>();
  private final JComboBox<String> instrumentField = new JComboBox<>();
  private final JComboBox<String> deploymentTitleField = new JComboBox<>();
  private final JComboBox<String> deploymentPurposeField = new JComboBox<>();
  private final JComboBox<String> deploymentDescriptionField = new JComboBox<>();
  private final JComboBox<String> alternateSiteNameField = new JComboBox<>();
  private final JComboBox<String> alternateDeploymentNameField = new JComboBox<>();
  private final TimeTranslatorForm startTimeForm;
  private final TimeTranslatorForm endTimeForm;
  private final DateTranslatorForm publicReleaseDateForm;

  public PackageInfoForm(String[] headerOptions, PackageTranslator initialTranslator) {
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTimeTranslator());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTimeTranslator());
    this.publicReleaseDateForm = new DateTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getPublicReleaseDate());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Site Or Cruise Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(siteOrCruiseNameField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment ID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(deploymentIdField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Projects"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(projectsField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Platform"), configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
    add(platformField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 9; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Instrument"), configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
    add(instrumentField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 11; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment Title"), configureLayout((c) -> { c.gridx = 0; c.gridy = 12; c.weightx = 1; }));
    add(deploymentTitleField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 13; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment Purpose"), configureLayout((c) -> { c.gridx = 0; c.gridy = 14; c.weightx = 1; }));
    add(deploymentPurposeField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 15; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment Description"), configureLayout((c) -> { c.gridx = 0; c.gridy = 16; c.weightx = 1; }));
    add(deploymentDescriptionField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 17; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Alternate Site Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 18; c.weightx = 1; }));
    add(alternateSiteNameField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 19; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Alternate Deployment Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 20; c.weightx = 1; }));
    add(alternateDeploymentNameField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 21; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    
    startTimeForm.setBorder(new TitledBorder("Start Time"));
    add(startTimeForm, configureLayout((c) -> { c.gridx = 0; c.gridy = 22; c.weightx = 1; }));
    endTimeForm.setBorder(new TitledBorder("End Time"));
    add(endTimeForm, configureLayout((c) -> { c.gridx = 1; c.gridy = 22; c.weightx = 1; }));
    publicReleaseDateForm.setBorder(new TitledBorder("Public Release Date"));
    add(publicReleaseDateForm, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 24; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    
    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 25; c.weighty = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, PackageTranslator initialTranslator) {
    updateComboBoxModel(uuidField, headerOptions);
    updateComboBoxModel(siteOrCruiseNameField, headerOptions);
    updateComboBoxModel(deploymentIdField, headerOptions);
    updateComboBoxModel(projectsField, headerOptions);
    updateComboBoxModel(platformField, headerOptions);
    updateComboBoxModel(instrumentField, headerOptions);
    updateComboBoxModel(deploymentTitleField, headerOptions);
    updateComboBoxModel(deploymentPurposeField, headerOptions);
    updateComboBoxModel(deploymentDescriptionField, headerOptions);
    updateComboBoxModel(alternateSiteNameField, headerOptions);
    updateComboBoxModel(alternateDeploymentNameField, headerOptions);
    
    if (initialTranslator != null) {
      uuidField.setSelectedItem(initialTranslator.getPackageUUID());
      siteOrCruiseNameField.setSelectedItem(initialTranslator.getSiteOrCruiseName());
      deploymentIdField.setSelectedItem(initialTranslator.getDeploymentId());
      projectsField.setSelectedItem(initialTranslator.getProjects());
      platformField.setSelectedItem(initialTranslator.getPlatform());
      instrumentField.setSelectedItem(initialTranslator.getInstrument());
      deploymentTitleField.setSelectedItem(initialTranslator.getDeploymentTitle());
      deploymentPurposeField.setSelectedItem(initialTranslator.getDeploymentPurpose());
      deploymentDescriptionField.setSelectedItem(initialTranslator.getDeploymentDescription());
      alternateSiteNameField.setSelectedItem(initialTranslator.getAlternateSiteName());
      alternateDeploymentNameField.setSelectedItem(initialTranslator.getAlternateDeploymentName());
    }
  }

  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(siteOrCruiseNameField, options);
    updateComboBoxModel(deploymentIdField, options);
    updateComboBoxModel(projectsField, options);
    updateComboBoxModel(platformField, options);
    updateComboBoxModel(instrumentField, options);
    updateComboBoxModel(deploymentTitleField, options);
    updateComboBoxModel(deploymentPurposeField, options);
    updateComboBoxModel(deploymentDescriptionField, options);
    updateComboBoxModel(alternateSiteNameField, options);
    updateComboBoxModel(alternateDeploymentNameField, options);
    startTimeForm.updateHeaderOptions(options);
    endTimeForm.updateHeaderOptions(options);
    publicReleaseDateForm.updateHeaderOptions(options);
  }

  public String getUuidValue() {
    return (String) uuidField.getSelectedItem();
  }

  public String getSiteOrCruiseNameValue() {
    return (String) siteOrCruiseNameField.getSelectedItem();
  }

  public String getDeploymentIdValue() {
    return (String) deploymentIdField.getSelectedItem();
  }

  public String getProjectsValue() {
    return (String) projectsField.getSelectedItem();
  }

  public String getPlatformValue() {
    return (String) platformField.getSelectedItem();
  }

  public String getInstrumentValue() {
    return (String) instrumentField.getSelectedItem();
  }

  public String getDeploymentTitleValue() {
    return (String) deploymentTitleField.getSelectedItem();
  }

  public String getDeploymentPurposeValue() {
    return (String) deploymentPurposeField.getSelectedItem();
  }

  public String getDeploymentDescriptionValue() {
    return (String) deploymentDescriptionField.getSelectedItem();
  }

  public String getAlternateSiteNameValue() {
    return (String) alternateSiteNameField.getSelectedItem();
  }

  public String getAlternateDeploymentNameValue() {
    return (String) alternateDeploymentNameField.getSelectedItem();
  }

  public TimeTranslator getStartTimeTranslator() {
    return startTimeForm.toTranslator();
  }

  public TimeTranslator getEndTimeTranslator() {
    return endTimeForm.toTranslator();
  }

  public DateTranslator getPublicReleaseDateTranslator() {
    return publicReleaseDateForm.toTranslator();
  }
}
