package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.DateTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class CalibrationTranslatorForm extends JPanel {
  
  private final JComboBox<String> calibrationDocumentsPathField = new JComboBox<>();
  private final JComboBox<String> calibrationDescriptionField = new JComboBox<>();
  private final DateTranslatorForm preDeploymentCalibrationDateForm;
  private final DateTranslatorForm postDeploymentCalibrationDateForm;

  public CalibrationTranslatorForm(String[] headerOptions, PackageTranslator initialTranslator) {
    this.preDeploymentCalibrationDateForm = new DateTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getPreDeploymentCalibrationDate());
    this.postDeploymentCalibrationDateForm = new DateTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getPostDeploymentCalibrationDate());
    
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new Label("Calibration Documents Path"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(calibrationDocumentsPathField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new Label("Calibration Description"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(calibrationDescriptionField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    preDeploymentCalibrationDateForm.setBorder(new TitledBorder("Pre-Deployment Calibration Date"));
    add(preDeploymentCalibrationDateForm, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    postDeploymentCalibrationDateForm.setBorder(new TitledBorder("Post-Deployment Calibration Date"));
    add(postDeploymentCalibrationDateForm, configureLayout((c) -> { c.gridx = 1; c.gridy = 4; c.weightx = 1; }));

    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weighty = 1; }));
  }

  public String getCalibrationDocumentsPathValue() {
    return (String) calibrationDocumentsPathField.getSelectedItem();
  }

  public String getCalibrationDescriptionValue() {
    return (String) calibrationDescriptionField.getSelectedItem();
  }

  public DateTranslator getPreDeploymentCalibrationDateTranslator() {
    return preDeploymentCalibrationDateForm.toTranslator();
  }

  public DateTranslator getPostDeploymentCalibrationDateTranslator() {
    return postDeploymentCalibrationDateForm.toTranslator();
  }

  private void initializeFields(String[] headerOptions, PackageTranslator initialTranslator) {
    updateComboBoxModel(calibrationDocumentsPathField, headerOptions);
    updateComboBoxModel(calibrationDescriptionField, headerOptions);
    
    if (initialTranslator != null) {
      calibrationDocumentsPathField.setSelectedItem(initialTranslator.getCalibrationDocumentsPath());
      calibrationDescriptionField.setSelectedItem(initialTranslator.getCalibrationDescription());
    }
  }

  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(calibrationDocumentsPathField, options);
    updateComboBoxModel(calibrationDescriptionField, options);
    preDeploymentCalibrationDateForm.updateHeaderOptions(options);
    postDeploymentCalibrationDateForm.updateHeaderOptions(options);
  }
}
