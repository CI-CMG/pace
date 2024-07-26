package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class StationaryTerrestrialLocationForm extends BaseLocationDetailTranslatorForm<StationaryTerrestrialLocationTranslator> {
  
  private final JComboBox<String> latitudeField = new JComboBox<>();
  private final JComboBox<String> longitudeField = new JComboBox<>();
  private final JComboBox<String> surfaceElevationField = new JComboBox<>();
  private final JComboBox<String> instrumentElevationField = new JComboBox<>();

  public StationaryTerrestrialLocationForm(String[] headerOptions, StationaryTerrestrialLocationTranslator initialTranslator) {
    super(headerOptions);
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Latitude"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(latitudeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Longitude"), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    add(longitudeField, configureLayout((c) -> { c.gridx = 1; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Surface Elevation"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(surfaceElevationField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Instrument Elevation"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(instrumentElevationField, configureLayout((c) -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private void initializeFields(String[] headerOptions, StationaryTerrestrialLocationTranslator initialTranslator) {
    updateComboBoxModel(latitudeField, headerOptions);
    updateComboBoxModel(longitudeField, headerOptions);
    updateComboBoxModel(surfaceElevationField, headerOptions);
    updateComboBoxModel(instrumentElevationField, headerOptions);
    
    if (initialTranslator != null) {
      latitudeField.setSelectedItem(initialTranslator.getLatitude());
      longitudeField.setSelectedItem(initialTranslator.getLongitude());
      surfaceElevationField.setSelectedItem(initialTranslator.getSurfaceElevation());
      instrumentElevationField.setSelectedItem(initialTranslator.getInstrumentElevation());
    }
  }

  @Override
  protected StationaryTerrestrialLocationTranslator toTranslator() {
    return StationaryTerrestrialLocationTranslator.builder()
        .longitude((String) longitudeField.getSelectedItem())
        .latitude((String) latitudeField.getSelectedItem())
        .instrumentElevation((String) instrumentElevationField.getSelectedItem())
        .surfaceElevation((String) surfaceElevationField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(latitudeField, options);
    updateComboBoxModel(longitudeField, options);
    updateComboBoxModel(surfaceElevationField, options);
    updateComboBoxModel(instrumentElevationField, options);
  }
}
