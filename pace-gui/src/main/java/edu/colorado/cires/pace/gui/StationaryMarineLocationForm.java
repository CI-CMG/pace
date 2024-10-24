package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryMarineLocationTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * StationaryMarineLocationForm extends BaseLocationDetailTranslatorForm and provides structure
 * relevant to stationary marine locations
 */
public class StationaryMarineLocationForm extends BaseLocationDetailTranslatorForm<StationaryMarineLocationTranslator> {
  
  private final JComboBox<String> seaAreaField = new JComboBox<>();
  private final MarineInstrumentLocationForm deploymentLocationForm;
  private final MarineInstrumentLocationForm recoveryLocationForm;

  /**
   * Creates a stationary marine location form
   * @param headerOptions headers to select from during mapping
   * @param initialTranslator translator to build upon
   */
  public StationaryMarineLocationForm(String[] headerOptions, StationaryMarineLocationTranslator initialTranslator) {
    super(headerOptions);
    this.deploymentLocationForm = new MarineInstrumentLocationForm(headerOptions, initialTranslator == null ? null : initialTranslator.getDeploymentLocationTranslator());
    this.recoveryLocationForm = new MarineInstrumentLocationForm(headerOptions, initialTranslator == null ? null : initialTranslator.getRecoveryLocationTranslator());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Sea Area"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(seaAreaField, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    deploymentLocationForm.setBorder(createEtchedBorder("Deployment Location"));
    add(deploymentLocationForm, configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    recoveryLocationForm.setBorder(createEtchedBorder("Recovery Location"));
    add(recoveryLocationForm, configureLayout(c -> { c.gridx = 1; c.gridy = 2; c.weightx = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, StationaryMarineLocationTranslator initialTranslator) {
    updateComboBoxModel(seaAreaField, headerOptions);
    
    if (initialTranslator != null) {
      seaAreaField.setSelectedItem(initialTranslator.getSeaArea());
    }
  }

  @Override
  protected StationaryMarineLocationTranslator toTranslator() {
    return StationaryMarineLocationTranslator.builder()
        .seaArea((String) seaAreaField.getSelectedItem())
        .deploymentLocationTranslator(deploymentLocationForm.toTranslator())
        .recoveryLocationTranslator(recoveryLocationForm.toTranslator())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(seaAreaField, options);
    deploymentLocationForm.updateHeaderOptions(options);
    recoveryLocationForm.updateHeaderOptions(options);
  }
}
