package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.DutyCycleTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class DutyCycleForm extends JPanel {
  
  private final JComboBox<String> durationField = new JComboBox<>();
  private final JComboBox<String> intervalField = new JComboBox<>();
  private final TimeTranslatorForm startTimeForm;
  private final TimeTranslatorForm endTimeForm;
  
  private final Consumer<DutyCycleForm> removeAction;

  public DutyCycleForm(String[] headerOptions, DutyCycleTranslator initialTranslator, Consumer<DutyCycleForm> removeAction) {
    this.removeAction = removeAction;
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTimeTranslator());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTimeTranslator());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Duration"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(durationField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Interval"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    add(intervalField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    startTimeForm.setBorder(new TitledBorder("Start Time"));
    add(startTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1;
    }));
    endTimeForm.setBorder(new TitledBorder("End Time"));
    add(endTimeForm, configureLayout(c -> {
      c.gridx = 1; c.gridy = 4; c.weightx = 1;
    }));
    add(getRemoveButton(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private JButton getRemoveButton() {
    JButton button = new JButton("Remove");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private void initializeFields(String[] headerOptions, DutyCycleTranslator initialTranslator) {
    updateComboBoxModel(durationField, headerOptions);
    updateComboBoxModel(intervalField, headerOptions);
    
    if (initialTranslator != null) {
      durationField.setSelectedItem(initialTranslator.getDuration());
      intervalField.setSelectedItem(initialTranslator.getInterval());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(durationField, headerOptions);
    updateComboBoxModel(intervalField, headerOptions);
    startTimeForm.updateHeaderOptions(headerOptions);
    endTimeForm.updateHeaderOptions(headerOptions);
  }
  
  public DutyCycleTranslator toTranslator() {
    return DutyCycleTranslator.builder()
        .startTimeTranslator(startTimeForm.toTranslator())
        .endTimeTranslator(endTimeForm.toTranslator())
        .duration((String) durationField.getSelectedItem())
        .interval((String) intervalField.getSelectedItem())
        .build();
  }
}
