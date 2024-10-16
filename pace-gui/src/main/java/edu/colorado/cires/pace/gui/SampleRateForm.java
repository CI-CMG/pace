package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * SampleRateForm extends JPanel and provides structure
 * relevant to sample rate forms
 */
public class SampleRateForm extends JPanel implements AuxiliaryTranslatorForm<SampleRateTranslator> {
  
  private final JComboBox<String> sampleRateField = new JComboBox<>();
  private final JComboBox<String> sampleBitsField = new JComboBox<>();
  private final TimeTranslatorForm startTimeForm;
  private final TimeTranslatorForm endTimeForm;
  
  private final Consumer<SampleRateForm> removeAction;

  /**
   * Creates sample rate form
   * @param headerOptions headers to select from in mapping
   * @param initialTranslator translator to build upon
   * @param removeAction deletes form
   */
  public SampleRateForm(String[] headerOptions, SampleRateTranslator initialTranslator, Consumer<SampleRateForm> removeAction) {
    this.removeAction = removeAction;
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());
    sampleRateField.setName("sampleRate");
    sampleBitsField.setName("sampleBits");
    startTimeForm.setName("startTime");
    endTimeForm.setName("endTime");
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Sample Rate"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(sampleRateField, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JLabel("Sample Bits"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(sampleBitsField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER; 
    }));
    startTimeForm.setBorder(createEtchedBorder("Start Time"));
    add(startTimeForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1;
    }));
    endTimeForm.setBorder(createEtchedBorder("End Time"));
    add(endTimeForm, configureLayout(c -> { 
      c.gridx = 1; c.gridy = 4; c.weightx = 1;
    }));
    add(getRemoveButton(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private JButton getRemoveButton() {
    JButton button = new JButton("Remove Sample Rate");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private void initializeFields(String[] headerOptions, SampleRateTranslator initialTranslator) {
    updateComboBoxModel(sampleRateField, headerOptions);
    updateComboBoxModel(sampleBitsField, headerOptions);
    
    if (initialTranslator != null) {
      sampleRateField.setSelectedItem(initialTranslator.getSampleRate());
      sampleBitsField.setSelectedItem(initialTranslator.getSampleBits());
    }
  }

  /**
   * Changes headers to select from
   * @param headerOptions new headers to select from
   */
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(sampleRateField, headerOptions);
    updateComboBoxModel(sampleBitsField, headerOptions);
    
    startTimeForm.updateHeaderOptions(headerOptions);
    endTimeForm.updateHeaderOptions(headerOptions);
  }

  /**
   * Builds a sample rate translator based on selected items in form
   * @return SampleRateTranslator mapped according to selected items
   */
  public SampleRateTranslator toTranslator() {
    return SampleRateTranslator.builder()
        .startTime(startTimeForm.toTranslator())
        .endTime(endTimeForm.toTranslator())
        .sampleRate((String) sampleRateField.getSelectedItem())
        .sampleBits((String) sampleBitsField.getSelectedItem())
        .build();
  }
}
