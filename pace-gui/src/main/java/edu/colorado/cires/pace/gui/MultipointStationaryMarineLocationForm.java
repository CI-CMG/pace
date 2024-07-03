package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.MarineInstrumentLocationTranslator;
import edu.colorado.cires.pace.data.translator.MultipointStationaryMarineLocationTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class MultipointStationaryMarineLocationForm extends BaseLocationDetailTranslatorForm<MultipointStationaryMarineLocationTranslator> {
  
  private final JComboBox<String> seaAreaField = new JComboBox<>();
  private final JPanel locationsPanel = new JPanel(new GridBagLayout());

  public MultipointStationaryMarineLocationForm(String[] headerOptions, MultipointStationaryMarineLocationTranslator initialTranslator) {
    super(headerOptions);
    addFields(headerOptions);
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields(String[] headerOptions) {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Sea Area"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(seaAreaField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    locationsPanel.setBorder(createEtchedBorder("Locations"));
    add(locationsPanel, configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(getAddLocationButton(headerOptions), configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
  }
  
  private JButton getAddLocationButton(String[] headerOptions) {
    JButton button = new JButton("Add");
    button.addActionListener(e -> addLocation(headerOptions, null));
    return button;
  }
  
  private void initializeFields(String[] headerOptions, MultipointStationaryMarineLocationTranslator initialTranslator) {
    updateComboBoxModel(seaAreaField, headerOptions);
    
    if (initialTranslator != null) {
      seaAreaField.setSelectedItem(initialTranslator.getSeaArea());
      initialTranslator.getLocationTranslators().forEach(
          t -> addLocation(headerOptions, t)
      );
    }
  }
  
  private void addLocation(String[] headerOptions, MarineInstrumentLocationTranslator initialTranslator) {
    MarineInstrumentLocationForm marineInstrumentLocationForm = new MarineInstrumentLocationForm(headerOptions, initialTranslator, this::removeLocation);
    locationsPanel.add(
        marineInstrumentLocationForm,
        configureLayout(c -> { c.gridx = 0; c.gridy = locationsPanel.getComponentCount(); c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER; })
    );
    revalidate();
  }
  
  private void removeLocation(MarineInstrumentLocationForm marineInstrumentLocationForm) {
    locationsPanel.remove(marineInstrumentLocationForm);
    revalidate();
  }

  @Override
  protected MultipointStationaryMarineLocationTranslator toTranslator() {
    return MultipointStationaryMarineLocationTranslator.builder()
        .seaArea((String) seaAreaField.getSelectedItem())
        .locationTranslators(Arrays.stream(locationsPanel.getComponents())
            .filter(p -> p instanceof MarineInstrumentLocationForm)
            .map(p -> (MarineInstrumentLocationForm) p)
            .map(MarineInstrumentLocationForm::toTranslator)
            .toList())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(seaAreaField, options);
    Arrays.stream(locationsPanel.getComponents())
        .filter(p -> p instanceof MarineInstrumentLocationForm)
        .map(p -> (MarineInstrumentLocationForm) p)
        .forEach(marineInstrumentLocationForm -> marineInstrumentLocationForm.updateHeaderOptions(options));
  }
}
