package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * DetectionsForm extends JPanel and provides format for detection form
 */
public class DetectionsForm extends JPanel {
  
  private final JComboBox<String> soundSource = new JComboBox<>();
  private final TimeTranslatorForm detectionStartTimeForm;
  private final TimeTranslatorForm detectionEndTimeForm;

  public DetectionsForm(String[] headerOptions, DetectionsPackageTranslator initialTranslator) {
    this.detectionStartTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.detectionEndTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Sound Source"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(soundSource, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1;
    }));
    detectionStartTimeForm.setBorder(createEtchedBorder("Analysis Start Time"));
    add(detectionStartTimeForm, configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    detectionEndTimeForm.setBorder(createEtchedBorder("Analysis End Time"));
    add(detectionEndTimeForm, configureLayout((c) -> { c.gridx = 1; c.gridy = 2; c.weightx = 1; }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, DetectionsPackageTranslator initialTranslator) {
    updateComboBoxModel(soundSource, headerOptions);
    
    if (initialTranslator != null) {
      soundSource.setSelectedItem(initialTranslator.getSoundSource());
    }
  }

  /**
   * Changes the selectable header options
   * @param headerOptions possible selectable headers
   */
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(soundSource, headerOptions);
    detectionStartTimeForm.updateHeaderOptions(headerOptions);
    detectionEndTimeForm.updateHeaderOptions(headerOptions);
  }

  /**
   * Returns the sound source value
   * @return String sounds source value
   */
  public String getSoundSourceValue() {
    return (String) soundSource.getSelectedItem();
  }

  /**
   * Returns the start time
   * @return TimeTranslator start time
   */
  public TimeTranslator getStartTime() { return detectionStartTimeForm.toTranslator(); }

  /**
   * Returns the end time
   * @return TimeTranslator end time
   */
  public TimeTranslator getEndTime() { return detectionEndTimeForm.toTranslator(); }
}
