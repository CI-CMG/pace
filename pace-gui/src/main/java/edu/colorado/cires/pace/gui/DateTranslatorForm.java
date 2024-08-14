package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DateTranslatorForm extends JPanel {
  
  private final JComboBox<String> dateField = new JComboBox<>();
  private final JComboBox<String> timeZoneField = new JComboBox<>();

  public DateTranslatorForm(String[] headerOptions, DateTranslator initialTranslator) {
    dateField.setName("date");
    timeZoneField.setName("timeZone");
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Date"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(dateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Time Zone"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(timeZoneField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, DateTranslator initialTranslator) {
    updateComboBoxModel(dateField, headerOptions);
    updateComboBoxModel(timeZoneField, headerOptions);
    
    if (initialTranslator != null) {
      dateField.setSelectedItem(initialTranslator.getDate());
      timeZoneField.setSelectedItem(initialTranslator.getTimeZone());
    }
  }
  
  public DateTranslator toTranslator() {
    return DateTranslator.builder()
        .date((String) dateField.getSelectedItem())
        .timeZone((String) timeZoneField.getSelectedItem())
        .build();
  }

  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(dateField, options);
    updateComboBoxModel(timeZoneField, options);
  }
}
