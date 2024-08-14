package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.LocationDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MobileMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MultipointStationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import java.awt.GridBagLayout;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LocationDetailForm extends JPanel {
  
  private final JComboBox<String> locationTypeComboBox;
  
  private BaseLocationDetailTranslatorForm<?> locationDetailTranslatorForm = new BaseLocationDetailTranslatorForm<>(new String[0]) {
    @Override
    protected LocationDetailTranslator toTranslator() {
      return null;
    }

    @Override
    protected void updateHeaderOptions(String[] options) {
      
    }
  };

  public LocationDetailForm(String[] headerOptions, LocationDetailTranslator initialTranslator) {
    this.locationTypeComboBox = getLocationTypeField(headerOptions, initialTranslator);
    locationTypeComboBox.setName("locationType");
    addFields(headerOptions, initialTranslator);
  }
  
  private void addFields(String[] headerOptions, LocationDetailTranslator initialTranslator) {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Location Type"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(locationTypeComboBox, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    
    if (initialTranslator != null) {
      if (initialTranslator instanceof MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator) {
        locationDetailTranslatorForm = new MultipointStationaryMarineLocationForm(headerOptions, multipointStationaryMarineLocationTranslator);
      } else if (initialTranslator instanceof StationaryMarineLocationTranslator stationaryMarineLocationTranslator) {
        locationDetailTranslatorForm = new StationaryMarineLocationForm(headerOptions, stationaryMarineLocationTranslator);
      } else if (initialTranslator instanceof StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator) {
        locationDetailTranslatorForm = new StationaryTerrestrialLocationForm(headerOptions, stationaryTerrestrialLocationTranslator);
      } else if (initialTranslator instanceof MobileMarineLocationTranslator mobileMarineLocationTranslator) {
        locationDetailTranslatorForm = new MobileMarineLocationTranslatorForm(headerOptions, mobileMarineLocationTranslator);
      }
    }
    
    add(locationDetailTranslatorForm, configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weighty = 1; }));
  }
  
  private JComboBox<String> getLocationTypeField(String[] headerOptions, LocationDetailTranslator initialTranslator) {
    JComboBox<String> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(new String[] {
        "Stationary Marine", "Multipoint Stationary Marine", "Mobile Marine", "Stationary Terrestrial"
    }));
    
    if (initialTranslator != null) {
      if (initialTranslator instanceof StationaryMarineLocationTranslator) {
        comboBox.setSelectedItem("Stationary Marine");
      } else if (initialTranslator instanceof MultipointStationaryMarineLocationTranslator) {
        comboBox.setSelectedItem("Multipoint Stationary Marine");
      } else if (initialTranslator instanceof MobileMarineLocationTranslator) {
        comboBox.setSelectedItem("Mobile Marine");
      } else if (initialTranslator instanceof StationaryTerrestrialLocationTranslator) {
        comboBox.setSelectedItem("Stationary Terrestrial");
      } else {
        comboBox.setSelectedItem(null);
      }
    } else {
      comboBox.setSelectedItem(null);
    }
    
    comboBox.addItemListener(e -> {
      String choice = (String) e.getItem();
      
      if (choice == null) {
        return;
      }
      
      BaseLocationDetailTranslatorForm<?> newLocationTranslatorForm = null;
      
      if (choice.equals("Stationary Marine")) {
        newLocationTranslatorForm = new StationaryMarineLocationForm(headerOptions, null);
      } else if (choice.equals("Multipoint Stationary Marine")) {
        newLocationTranslatorForm = new MultipointStationaryMarineLocationForm(headerOptions, null);
      } else if (choice.equals("Mobile Marine")) {
        newLocationTranslatorForm = new MobileMarineLocationTranslatorForm(headerOptions, null);
      } else if (choice.equals("Stationary Terrestrial")) {
        newLocationTranslatorForm = new StationaryTerrestrialLocationForm(headerOptions, null);
      }
      
      if (newLocationTranslatorForm != null) {
        remove(locationDetailTranslatorForm);
        locationDetailTranslatorForm = newLocationTranslatorForm;
        add(locationDetailTranslatorForm, configureLayout((c) -> {
          c.gridx = 0; c.gridy = 2; c.weightx = 1;
        }));
        revalidate();
      }
    });
    
    return comboBox;
  }
  
  public LocationDetailTranslator toTranslator() {
    return locationDetailTranslatorForm.toTranslator();
  }

  public void updateHeaderOptions(String[] options) {
    locationDetailTranslatorForm.setHeaderOptions(options);

    Arrays.stream(locationTypeComboBox.getItemListeners())
        .forEach(locationTypeComboBox::removeItemListener);

    locationTypeComboBox.addItemListener(e -> {
      String choice = (String) e.getItem();

      if (choice == null) {
        return;
      }

      BaseLocationDetailTranslatorForm<?> newLocationTranslatorForm = null;

      if (choice.equals("Stationary Marine")) {
        newLocationTranslatorForm = new StationaryMarineLocationForm(options, null);
      } else if (choice.equals("Multipoint Stationary Marine")) {
        newLocationTranslatorForm = new MultipointStationaryMarineLocationForm(options, null);
      } else if (choice.equals("Mobile Marine")) {
        newLocationTranslatorForm = new MobileMarineLocationTranslatorForm(options, null);
      } else if (choice.equals("Stationary Terrestrial")) {
        newLocationTranslatorForm = new StationaryTerrestrialLocationForm(options, null);
      }

      if (newLocationTranslatorForm != null) {
        remove(locationDetailTranslatorForm);
        locationDetailTranslatorForm = newLocationTranslatorForm;
        add(locationDetailTranslatorForm, configureLayout((c) -> {
          c.gridx = 0; c.gridy = 2; c.weightx = 1;
        }));
        revalidate();
      }
    });
  }
}
