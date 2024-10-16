package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MarineInstrumentLocationTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * MarineInstrumentLocationForm extends JPanel and holds the fields relevant to marine
 * instrument locations
 */
public class MarineInstrumentLocationForm extends JPanel {
  
  private final JComboBox<String> latitudeField = new JComboBox<>();
  private final JComboBox<String> longitudeField = new JComboBox<>();
  private final JComboBox<String> seaFloorDepthField = new JComboBox<>();
  private final JComboBox<String> instrumentDepthField = new JComboBox<>();
  
  private final Consumer<MarineInstrumentLocationForm> removeAction;

  /**
   * Initializes a marine instrument location form with no remove action
   * @param headerOptions selectable headers for building translator
   * @param initialTranslator base translator to build upon
   */
  public MarineInstrumentLocationForm(String[] headerOptions, MarineInstrumentLocationTranslator initialTranslator) {
    this.removeAction = null;
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }

  /**
   * Initializes a marine instrument location form with a remove action
   * @param headerOptions selectable headers for building translator
   * @param initialTranslator base translator to build upon
   * @param removeAction removes the current form
   */
  public MarineInstrumentLocationForm(String[] headerOptions, MarineInstrumentLocationTranslator initialTranslator, Consumer<MarineInstrumentLocationForm> removeAction) {
    this.removeAction = removeAction;
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Latitude"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(latitudeField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Longitude"), configureLayout(c -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    add(longitudeField, configureLayout(c -> { c.gridx = 1; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Sea Floor Depth"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(seaFloorDepthField, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Instrument Depth"), configureLayout(c -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(instrumentDepthField, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    
    if (removeAction != null) {
      add(getRemoveButton(), configureLayout(c -> { c.gridx = 0; c.gridy = 8; c.weightx = 0; }));
    }
  }
  
  private JButton getRemoveButton() {
    JButton button = new JButton("Remove");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private void initializeFields(String[] headerOptions, MarineInstrumentLocationTranslator initialTranslator) {
    updateComboBoxModel(latitudeField, headerOptions);
    updateComboBoxModel(longitudeField, headerOptions);
    updateComboBoxModel(seaFloorDepthField, headerOptions);
    updateComboBoxModel(instrumentDepthField, headerOptions);
    
    if (initialTranslator != null) {
      latitudeField.setSelectedItem(initialTranslator.getLatitude());
      longitudeField.setSelectedItem(initialTranslator.getLongitude());
      seaFloorDepthField.setSelectedItem(initialTranslator.getSeaFloorDepth());
      instrumentDepthField.setSelectedItem(initialTranslator.getInstrumentDepth());
    }
  }

  /**
   * Returns a translator based on the information supplied in the form
   * @return MarineInstrumentLocationTranslator holding form info
   */
  public MarineInstrumentLocationTranslator toTranslator() {
    return MarineInstrumentLocationTranslator.builder()
        .longitude((String) longitudeField.getSelectedItem())
        .latitude((String) latitudeField.getSelectedItem())
        .instrumentDepth((String) instrumentDepthField.getSelectedItem())
        .seaFloorDepth((String) seaFloorDepthField.getSelectedItem())
        .build();
  }

  /**
   * Changes the selectable headers to be the provided options
   * @param options headers to select from now
   */
  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(latitudeField, options);
    updateComboBoxModel(longitudeField, options);
    updateComboBoxModel(seaFloorDepthField, options);
    updateComboBoxModel(instrumentDepthField, options);
  }
}
