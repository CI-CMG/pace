package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PackageSensorTranslatorForm extends JPanel {
  
  private final PositionTranslatorForm positionTranslatorForm;
  private final JComboBox<String> nameField = new JComboBox<>();
  private final Consumer<PackageSensorTranslatorForm> removeAction;

  public PackageSensorTranslatorForm(String[] headerOptions, PackageSensorTranslator initialTranslator) {
    this.positionTranslatorForm = new PositionTranslatorForm(initialTranslator == null ? null : initialTranslator.getPosition(), headerOptions);
    this.removeAction = null;
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  public PackageSensorTranslatorForm(String[] headerOptions, PackageSensorTranslator initialTranslator, Consumer<PackageSensorTranslatorForm> removeAction) {
    this.positionTranslatorForm = new PositionTranslatorForm(initialTranslator == null ? null : initialTranslator.getPosition(), headerOptions);
    this.removeAction = removeAction;
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }

  private void initializeFields(String[] headerOptions, PackageSensorTranslator initialTranslator) {
    updateComboBoxModel(nameField, headerOptions);
    
    if (initialTranslator != null) {
      nameField.setSelectedItem(initialTranslator.getName());
    }
  }

  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Name"), configureFormLayout(0, 0));
    add(nameField, configureFormLayout(0, 1));
    
    positionTranslatorForm.setBorder(createEtchedBorder("Position"));
    add(positionTranslatorForm, configureFormLayout(0, 2));
    
    if (removeAction != null) {
      add(createRemoveButton(), configureLayout(c -> {
        c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
      }));
    }
  }
  
  private JButton createRemoveButton() {
    JButton button = new JButton("Remove Sensor");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(nameField, headerOptions);
    positionTranslatorForm.updateHeaderOptions(headerOptions);
  }
  
  public PackageSensorTranslator toTranslator() {
    return PackageSensorTranslator.builder()
        .name((String) nameField.getSelectedItem())
        .position(positionTranslatorForm.toTranslator())
        .build();
  }
}