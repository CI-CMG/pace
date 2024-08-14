package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CalibrationTranslatorForm extends JPanel implements AuxiliaryTranslatorForm<PackageTranslator> {
  
  private final JComboBox<String> calibrationDocumentsPathField = new JComboBox<>();
  private final JComboBox<String> calibrationDescriptionField = new JComboBox<>();
  private final DateTranslatorForm preDeploymentCalibrationDateForm;
  private final DateTranslatorForm postDeploymentCalibrationDateForm;

  public CalibrationTranslatorForm(String[] headerOptions, PackageTranslator initialTranslator) {
    this.preDeploymentCalibrationDateForm = new DateTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getPreDeploymentCalibrationDate());
    this.postDeploymentCalibrationDateForm = new DateTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getPostDeploymentCalibrationDate());
    calibrationDocumentsPathField.setName("calibrationDocumentsPath");
    calibrationDescriptionField.setName("calibrationDescription");
    preDeploymentCalibrationDateForm.setName("preDeploymentCalibrationDate");
    postDeploymentCalibrationDateForm.setName("postDeploymentCalibrationDate");
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Calibration Documents Path"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(calibrationDocumentsPathField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Calibration Description"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(calibrationDescriptionField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    preDeploymentCalibrationDateForm.setBorder(createEtchedBorder("Pre-Deployment Calibration Date"));
    add(preDeploymentCalibrationDateForm, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    postDeploymentCalibrationDateForm.setBorder(createEtchedBorder("Post-Deployment Calibration Date"));
    add(postDeploymentCalibrationDateForm, configureLayout((c) -> { c.gridx = 1; c.gridy = 4; c.weightx = 1; }));

    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weighty = 1; }));
  }

  private void initializeFields(String[] headerOptions, PackageTranslator initialTranslator) {
    updateComboBoxModel(calibrationDocumentsPathField, headerOptions);
    updateComboBoxModel(calibrationDescriptionField, headerOptions);
    
    if (initialTranslator != null) {
      calibrationDocumentsPathField.setSelectedItem(initialTranslator.getCalibrationDocumentsPath());
      calibrationDescriptionField.setSelectedItem(initialTranslator.getCalibrationDescription());
    }
  }

  @Override
  public PackageTranslator toTranslator() {
    return PackageTranslator.builder()
        .calibrationDocumentsPath((String) calibrationDocumentsPathField.getSelectedItem())
        .calibrationDescription((String) calibrationDescriptionField.getSelectedItem())
        .preDeploymentCalibrationDate(preDeploymentCalibrationDateForm.toTranslator())
        .postDeploymentCalibrationDate(postDeploymentCalibrationDateForm.toTranslator())
        .build();
  }

  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(calibrationDocumentsPathField, options);
    updateComboBoxModel(calibrationDescriptionField, options);
    preDeploymentCalibrationDateForm.updateHeaderOptions(options);
    postDeploymentCalibrationDateForm.updateHeaderOptions(options);
  }
}
