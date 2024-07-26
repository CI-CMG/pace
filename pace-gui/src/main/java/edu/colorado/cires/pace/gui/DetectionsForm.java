package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DetectionsForm extends JPanel {
  
  private final JComboBox<String> soundSource = new JComboBox<>();

  public DetectionsForm(String[] headerOptions, DetectionsPackageTranslator initialTranslator) {
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
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, DetectionsPackageTranslator initialTranslator) {
    updateComboBoxModel(soundSource, headerOptions);
    
    if (initialTranslator != null) {
      soundSource.setSelectedItem(initialTranslator.getSoundSource());
    }
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(soundSource, headerOptions);
  }

  public String getSoundSourceValue() {
    return (String) soundSource.getSelectedItem();
  }
}
