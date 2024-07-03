package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.GainTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GainForm extends JPanel {
  
  private final JComboBox<String> gainField = new JComboBox<>();
  private final TimeTranslatorForm startTimeForm;
  private final TimeTranslatorForm endTimeForm;
  
  private final Consumer<GainForm> removeAction;

  public GainForm(String[] headerOptions, GainTranslator initialTranslator, Consumer<GainForm> removeAction) {
    this.removeAction = removeAction;
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTimeTranslator());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTimeTranslator());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Gain"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(gainField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    startTimeForm.setBorder(createEtchedBorder("Start Time"));
    add(startTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1;
    }));
    endTimeForm.setBorder(createEtchedBorder("End Time"));
    add(endTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1;
    }));
    add(getRemoveButton(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private JButton getRemoveButton() {
    JButton button = new JButton("Remove");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private void initializeFields(String[] headerOptions, GainTranslator initialTranslator) {
    updateComboBoxModel(gainField, headerOptions);
    
    if (initialTranslator != null) {
      gainField.setSelectedItem(initialTranslator.getGain());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(gainField, headerOptions);
    
    startTimeForm.updateHeaderOptions(headerOptions);
    endTimeForm.updateHeaderOptions(headerOptions);
  }
  
  public GainTranslator toTranslator() {
    return GainTranslator.builder()
        .startTimeTranslator(startTimeForm.toTranslator())
        .endTimeTranslator(endTimeForm.toTranslator())
        .gain((String) gainField.getSelectedItem())
        .build();
  }
}
