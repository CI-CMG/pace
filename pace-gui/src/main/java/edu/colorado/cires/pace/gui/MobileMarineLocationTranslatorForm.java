package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MobileMarineLocationTranslator;
import java.awt.GridBagLayout;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * MobileMarineLocationTranslatorForm extends BaseLocationDetailTranslatorForm and provides
 * fields specific to mobile marine location
 */
public class MobileMarineLocationTranslatorForm extends BaseLocationDetailTranslatorForm<MobileMarineLocationTranslator> {
  
  private final JComboBox<String> seaAreaField = new JComboBox<>();
  private final JComboBox<String> vesselField = new JComboBox<>();
  private final JComboBox<String> locationDerivationDescriptionField = new JComboBox<>();
  private final JComboBox<String> singleStringFiles = new JComboBox<>();

  /**
   * Creates a mobile marine location translator form
   * @param headerOptions possible headers to select from in mapping
   * @param initialTranslator base translator to build upon
   */
  public MobileMarineLocationTranslatorForm(String[] headerOptions, MobileMarineLocationTranslator initialTranslator) {
    super(headerOptions);
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Sea Area"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(seaAreaField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Vessel"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(vesselField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    add(new JLabel("Location Derivation Description"), configureLayout((c) -> { c.gridx = 0; c.gridy  = 4; c.weightx = 1; }));
    add(locationDerivationDescriptionField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    add(new JLabel("File(s)"), configureLayout((c) -> { c.gridx = 0; c.gridy  = 6; c.weightx = 1; }));
    add(singleStringFiles, configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, MobileMarineLocationTranslator initialTranslator) {
    updateComboBoxModel(seaAreaField, headerOptions);
    updateComboBoxModel(vesselField, headerOptions);
    updateComboBoxModel(locationDerivationDescriptionField, headerOptions);
    updateComboBoxModel(singleStringFiles, headerOptions);
    
    if (initialTranslator != null) {
      seaAreaField.setSelectedItem(initialTranslator.getSeaArea());
      vesselField.setSelectedItem(initialTranslator.getVessel());
      locationDerivationDescriptionField.setSelectedItem(initialTranslator.getLocationDerivationDescription());
      singleStringFiles.setSelectedItem(initialTranslator.getSingleStringFiles());
    }
  }

  @Override
  protected MobileMarineLocationTranslator toTranslator() {
    return MobileMarineLocationTranslator.builder()
        .seaArea((String) seaAreaField.getSelectedItem())
        .vessel((String) vesselField.getSelectedItem())
        .locationDerivationDescription((String) locationDerivationDescriptionField.getSelectedItem())
        .singleStringFiles((String) singleStringFiles.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(seaAreaField, options);
    updateComboBoxModel(vesselField, options);
    updateComboBoxModel(locationDerivationDescriptionField, options);
    updateComboBoxModel(singleStringFiles, options);
  }
}
