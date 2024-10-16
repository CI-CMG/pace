package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * PackageInfoForm extends JPanel and provides package info specific structuring
 */
public class PackageInfoForm extends JPanel implements AuxiliaryTranslatorForm<PackageTranslator> {
  
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
  private final DateTranslatorForm publicReleaseDateForm;
  private final JComboBox<String> dataCollectionNameField = new JComboBox<>();

  /**
   * Creates a package info form
   * @param headerOptions selectable headers during mapping
   * @param initialTranslator translator to build upon
   */
  public PackageInfoForm(String[] headerOptions, PackageTranslator initialTranslator) {
    this.publicReleaseDateForm = new DateTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getPublicReleaseDate());

    uuidField.setName("uuid");
    siteOrCruiseNameField.setName("siteOrCruiseName");
    deploymentIdField.setName("deploymentId");
    projectsField.setName("projects");
    platformField.setName("platform");
    instrumentField.setName("instrument");
    deploymentTitleField.setName("deploymentTitle");
    deploymentPurposeField.setName("deploymentPurpose");
    deploymentDescriptionField.setName("deploymentDescription");
    alternateSiteNameField.setName("alternateSiteName");
    alternateDeploymentNameField.setName("alternateDeploymentName");
    publicReleaseDateForm.setName("publicReleaseDate");
    dataCollectionNameField.setName("dataCollectionName");

    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Data Collection Name"), configureFormLayout(0, 2));
    add(dataCollectionNameField, configureLayout((c) -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Site Or Cruise Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(siteOrCruiseNameField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment ID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(deploymentIdField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Projects"), configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
    add(projectsField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 9; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Platform"), configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
    add(platformField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 11; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Instrument"), configureLayout((c) -> { c.gridx = 0; c.gridy = 12; c.weightx = 1; }));
    add(instrumentField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 13; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment Title"), configureLayout((c) -> { c.gridx = 0; c.gridy = 14; c.weightx = 1; }));
    add(deploymentTitleField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 15; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment Purpose"), configureLayout((c) -> { c.gridx = 0; c.gridy = 16; c.weightx = 1; }));
    add(deploymentPurposeField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 17; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Deployment Description"), configureLayout((c) -> { c.gridx = 0; c.gridy = 18; c.weightx = 1; }));
    add(deploymentDescriptionField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 19; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Alternate Site Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 20; c.weightx = 1; }));
    add(alternateSiteNameField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 21; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Alternate Deployment Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 22; c.weightx = 1; }));
    add(alternateDeploymentNameField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 23; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));

    publicReleaseDateForm.setBorder(createEtchedBorder("Public Release Date"));
    add(publicReleaseDateForm, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 26; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    
    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 27; c.weighty = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, PackageTranslator initialTranslator) {
    updateComboBoxModel(uuidField, headerOptions);
    updateComboBoxModel(dataCollectionNameField, headerOptions);
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
      dataCollectionNameField.setSelectedItem(initialTranslator.getDataCollectionName());
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

  /**
   * Builds a package translator based on currently selected headers within the form
   * @return PackageTranslator holding current selections
   */
  @Override
  public PackageTranslator toTranslator() {
    return PackageTranslator.builder()
        .packageUUID((String) uuidField.getSelectedItem())
        .siteOrCruiseName((String) siteOrCruiseNameField.getSelectedItem())
        .deploymentId((String) deploymentIdField.getSelectedItem())
        .projects((String) projectsField.getSelectedItem())
        .platform((String) platformField.getSelectedItem())
        .instrument((String) instrumentField.getSelectedItem())
        .deploymentTitle((String) deploymentTitleField.getSelectedItem())
        .deploymentPurpose((String) deploymentPurposeField.getSelectedItem())
        .deploymentDescription((String) deploymentDescriptionField.getSelectedItem())
        .alternateSiteName((String) alternateSiteNameField.getSelectedItem())
        .alternateDeploymentName((String) alternateDeploymentNameField.getSelectedItem())
        .publicReleaseDate(publicReleaseDateForm.toTranslator())
        .dataCollectionName((String) dataCollectionNameField.getSelectedItem())
        .build();
  }

  /**
   * Changes header options to be the provided options
   * @param options headers to now be selectable
   */
  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(dataCollectionNameField, options);
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
    publicReleaseDateForm.updateHeaderOptions(options);
  }
}
