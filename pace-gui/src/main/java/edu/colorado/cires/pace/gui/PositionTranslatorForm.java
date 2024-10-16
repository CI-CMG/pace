package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PositionTranslator;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * PositionTranslatorForm extends JPanel and provides structure
 * relevant to position translators
 */
public class PositionTranslatorForm extends JPanel {
  
  private final JComboBox<String> xField = new JComboBox<>();
  private final JComboBox<String> yField = new JComboBox<>();
  private final JComboBox<String> zField = new JComboBox<>();

  /**
   * Creates a position translator form
   * @param initialTranslator translator to build upon
   * @param headerOptions headers to select from during mapping
   */
  public PositionTranslatorForm(PositionTranslator initialTranslator, String[] headerOptions) {
    xField.setName("x");
    yField.setName("y");
    zField.setName("z");
    addFields();
    initializeFields(initialTranslator, headerOptions);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    add(new JLabel("X"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(xField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Y"), configureLayout(c -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    add(yField, configureLayout(c -> { c.gridx = 1; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Z"), configureLayout(c -> { c.gridx = 2; c.gridy = 0; c.weightx = 1; }));
    add(zField, configureLayout(c -> { c.gridx = 2; c.gridy = 1; c.weightx = 1; }));
  }
  
  private void initializeFields(PositionTranslator initialTranslator, String[] headerOptions) {
    updateHeaderOptions(headerOptions);
    if (initialTranslator != null) {
      xField.setSelectedItem(initialTranslator.getX());
      yField.setSelectedItem(initialTranslator.getY());
      zField.setSelectedItem(initialTranslator.getZ());
    }
  }

  /**
   * Builds a position translator given selected items in form
   * @return PositionTranslator with selected items in form
   */
  public PositionTranslator toTranslator() {
    return PositionTranslator.builder()
        .x((String) xField.getSelectedItem())
        .y((String) yField.getSelectedItem())
        .z((String) zField.getSelectedItem())
        .build();
  }

  /**
   * Changes the selectable headers in form
   * @param headerOptions headers to select from
   */
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(xField, headerOptions);
    updateComboBoxModel(yField, headerOptions);
    updateComboBoxModel(zField, headerOptions);
  }
}
