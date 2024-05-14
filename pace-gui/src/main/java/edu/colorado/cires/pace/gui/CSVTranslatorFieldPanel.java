package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CSVTranslatorFieldPanel extends TranslatorFieldPanel<CSVTranslatorField> {

  private final JTextField propertyNameField = new JTextField();
  private final JTextField columnNumberField = new JTextField();

  public CSVTranslatorFieldPanel(CSVTranslatorField initialField, Consumer<TranslatorFieldPanel<CSVTranslatorField>> panelConsumer) {
    setLayout(new GridBagLayout());

    JPanel propertyNamePanel = new JPanel(new GridBagLayout());
    propertyNamePanel.add(new JLabel("Property Name"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    propertyNamePanel.add(propertyNameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(propertyNamePanel, configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));

    JPanel columnNumberPanel = new JPanel(new GridBagLayout());
    columnNumberPanel.add(new JLabel("Column Number"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 0; }));
    columnNumberPanel.add(columnNumberField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 0; }));
    add(columnNumberPanel, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 0; }));

    JPanel removePanel = new JPanel(new GridBagLayout());
    removePanel.add(new JLabel(""), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 0; }));
    JButton removeButton = new JButton("Remove");
    removePanel.add(removeButton, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 0; }));
    add(removePanel, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));

    removeButton.addActionListener((e) -> panelConsumer.accept(this));

    initializeFields(initialField);
  }

  private void initializeFields(CSVTranslatorField initialField) {
    if (initialField != null) {
      propertyNameField.setText(initialField.getPropertyName());
      columnNumberField.setText(initialField.getColumnNumber().toString());
    }
  }

  @Override
  protected CSVTranslatorField toField() {
    return CSVTranslatorField.builder()
        .propertyName(propertyNameField.getText())
        .columnNumber(Integer.valueOf(columnNumberField.getText()))
        .build();
  }
}
