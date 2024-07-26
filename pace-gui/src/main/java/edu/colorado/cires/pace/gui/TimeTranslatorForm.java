package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.awt.GridBagLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimeTranslatorForm extends JPanel {
  
  private final JComboBox<String> timeZoneField = new JComboBox<>();
  private final JComboBox<String> dateField = new JComboBox<>();
  private final JComboBox<String> timeField = new JComboBox<>();
  private final JLabel dateLabel = new JLabel("Date");

  public TimeTranslatorForm(String[] headerOptions, TimeTranslator initialTranslator) {
    addFields(initialTranslator);
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields(TimeTranslator initialTranslator) {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Time Format"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(getTimeFormatComboBox(initialTranslator), configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Time"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(timeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    if (initialTranslator instanceof DateTimeSeparatedTimeTranslator) {
      add(dateLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
      add(dateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    }
    add(new JLabel("Time Zone"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(timeZoneField, configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
  }
  
  private JComboBox<String> getTimeFormatComboBox(TimeTranslator initialTranslator) {
    JComboBox<String> timeFormatField = new JComboBox<>(new DefaultComboBoxModel<>(new String[] {
        "Default", "Date-Time Separated"
    }));
    if (initialTranslator != null) {
      if (initialTranslator instanceof DefaultTimeTranslator) {
        timeFormatField.setSelectedItem("Default");
      } else if (initialTranslator instanceof DateTimeSeparatedTimeTranslator) {
        timeFormatField.setSelectedItem("Date-Time Separated");
      } else {
        timeFormatField.setSelectedItem(null);
      }
    } else {
      timeFormatField.setSelectedItem("Default");
    }
    
    timeFormatField.addItemListener(e -> {
      String choice = (String) e.getItem();
      
      if (choice == null) {
        return;
      }
      
      if (choice.equals("Default")) {
        dateField.setSelectedItem(null);
        remove(dateField);
        remove(dateLabel);
        revalidate();
      } else if (choice.equals("Date-Time Separated")) {
        add(dateLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
        add(dateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
        revalidate();
      }
    });
    
    return timeFormatField;
  }
  
  private void initializeFields(String[] headerOptions, TimeTranslator initialTranslator) {
    updateComboBoxModel(timeZoneField, headerOptions);
    updateComboBoxModel(timeField, headerOptions);
    updateComboBoxModel(dateField, headerOptions);
    
    if (initialTranslator != null) {
      if (initialTranslator instanceof DefaultTimeTranslator defaultTimeTranslator) {
        timeZoneField.setSelectedItem(defaultTimeTranslator.getTimeZone());
        timeField.setSelectedItem(defaultTimeTranslator.getTime());
      } else if (initialTranslator instanceof DateTimeSeparatedTimeTranslator dateTimeSeparatedTimeTranslator) {
        timeZoneField.setSelectedItem(dateTimeSeparatedTimeTranslator.getTimeZone());
        timeField.setSelectedItem(dateTimeSeparatedTimeTranslator.getTime());
        dateField.setSelectedItem(dateTimeSeparatedTimeTranslator.getDate());
      }
    }
  }
  
  public TimeTranslator toTranslator() {
    if (dateField.getSelectedItem() != null) {
      return DateTimeSeparatedTimeTranslator.builder()
          .timeZone((String) timeZoneField.getSelectedItem())
          .date((String) dateField.getSelectedItem())
          .time(String.valueOf(timeField.getSelectedItem()))
          .build();
    } else {
      return DefaultTimeTranslator.builder()
          .timeZone((String) timeZoneField.getSelectedItem())
          .time((String) timeField.getSelectedItem())
          .build();
    }
  }

  public void updateHeaderOptions(String[] options) {
    updateComboBoxModel(timeZoneField, options);
    updateComboBoxModel(dateField, options);
    updateComboBoxModel(timeField, options);
  }
}
