package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateOnlyTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.awt.GridBagLayout;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimeTranslatorForm extends JPanel implements AuxiliaryTranslatorForm<TimeTranslator> {
  
  private final JComboBox<String> timeZoneField = new JComboBox<>();
  private final JComboBox<String> dateField = new JComboBox<>();
  private final JComboBox<String> timeField = new JComboBox<>();
  private final JLabel dateLabel = new JLabel("Date");
  private final JLabel timeLabel = new JLabel("Time");

  public TimeTranslatorForm(String[] headerOptions, TimeTranslator initialTranslator) {
    timeZoneField.setName("timeZone");
    dateField.setName("date");
    timeField.setName("time");
    addFields(initialTranslator);
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields(TimeTranslator initialTranslator) {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Time Format"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(getTimeFormatComboBox(initialTranslator), configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    if (initialTranslator == null || initialTranslator instanceof DefaultTimeTranslator || initialTranslator instanceof DateTimeSeparatedTimeTranslator) {
      add(timeLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
      add(timeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    }
    if (initialTranslator instanceof DateTimeSeparatedTimeTranslator || initialTranslator instanceof DateOnlyTimeTranslator) {
      add(dateLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
      add(dateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    }
    add(new JLabel("Time Zone"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(timeZoneField, configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
  }
  
  private JComboBox<String> getTimeFormatComboBox(TimeTranslator initialTranslator) {
    JComboBox<String> timeFormatField = new JComboBox<>(new DefaultComboBoxModel<>(new String[] {
        "Default", "Date-Time Separated","Date Only"
    }));
    timeFormatField.setName("timeFormat");
    if (initialTranslator != null) {
      if (initialTranslator instanceof DateTimeSeparatedTimeTranslator) {
        timeFormatField.setSelectedItem("Date-Time Separated");
      } else if (initialTranslator instanceof DateOnlyTimeTranslator) {
        timeFormatField.setSelectedItem("Date Only");
      } else {
        timeFormatField.setSelectedItem("Default");
      }
    } else {
      timeFormatField.setSelectedItem("Default");
    }

    //Default = 0, Date-Time Separated = 1, Date Only = 2
    AtomicInteger previousFormat = new AtomicInteger(0);
    if (initialTranslator instanceof DateTimeSeparatedTimeTranslator) {
      previousFormat.set(1);
    }
    if (initialTranslator instanceof DateOnlyTimeTranslator) {
      previousFormat.set(2);
    }

    timeFormatField.addItemListener(e -> {
      String choice = (String) e.getItem();

      if (choice.equals("Date-Time Separated") && previousFormat.get() == 0) {
        add(dateLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
        add(dateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
        revalidate();
        previousFormat.set(1);
      } else if (choice.equals("Date-Time Separated") && previousFormat.get() == 2) {
        add(timeLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
        add(timeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
        revalidate();
        previousFormat.set(1);
      } else if (choice.equals("Date Only") && previousFormat.get() == 0) {
        timeField.setSelectedItem(null);
        remove(timeField);
        remove(timeLabel);
        add(dateLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
        add(dateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
        revalidate();
        previousFormat.set(2);
      } else if (choice.equals("Date Only") && previousFormat.get() == 1) {
        timeField.setSelectedItem(null);
        remove(timeField);
        remove(timeLabel);
        revalidate();
        previousFormat.set(2);
      } else if (choice.equals("Default") && previousFormat.get() == 1) {
        dateField.setSelectedItem(null);
        remove(dateField);
        remove(dateLabel);
        revalidate();
        previousFormat.set(0);
      } else if (choice.equals("Default") && previousFormat.get() == 2) {
        dateField.setSelectedItem(null);
        remove(dateField);
        remove(dateLabel);
        add(timeLabel, configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
        add(timeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
        revalidate();
        previousFormat.set(0);
      }
    });
    
    return timeFormatField;
  }
  
  private void initializeFields(String[] headerOptions, TimeTranslator initialTranslator) {
    updateComboBoxModel(timeZoneField, headerOptions);
    updateComboBoxModel(timeField, headerOptions);
    updateComboBoxModel(dateField, headerOptions);
    
    if (initialTranslator != null) {
      if (initialTranslator instanceof DateTimeSeparatedTimeTranslator dateTimeSeparatedTimeTranslator) {
        timeZoneField.setSelectedItem(dateTimeSeparatedTimeTranslator.getTimeZone());
        timeField.setSelectedItem(dateTimeSeparatedTimeTranslator.getTime());
        dateField.setSelectedItem(dateTimeSeparatedTimeTranslator.getDate());
      } else if(initialTranslator instanceof DateOnlyTimeTranslator dateOnlyTimeTranslator) {
        timeZoneField.setSelectedItem(dateOnlyTimeTranslator.getTimeZone());
        dateField.setSelectedItem(dateOnlyTimeTranslator.getDate());
      } else {
        timeZoneField.setSelectedItem(initialTranslator.getTimeZone());
        timeField.setSelectedItem(initialTranslator.getTime());
      }
    }
  }
  
  public TimeTranslator toTranslator() {
    if (dateField.getSelectedItem() != null && timeField.getSelectedItem() != null) {
      return DateTimeSeparatedTimeTranslator.builder()
          .timeZone((String) timeZoneField.getSelectedItem())
          .date((String) dateField.getSelectedItem())
          .time((String) timeField.getSelectedItem())
          .build();
    } else if (dateField.getSelectedItem() != null) {
      return DateOnlyTimeTranslator.builder()
          .timeZone((String) timeZoneField.getSelectedItem())
          .date((String) dateField.getSelectedItem())
          .build();
    } else{
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
