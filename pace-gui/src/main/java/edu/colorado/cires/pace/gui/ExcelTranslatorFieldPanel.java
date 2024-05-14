package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExcelTranslatorFieldPanel extends TranslatorFieldPanel<ExcelTranslatorField> {
  
  private final JTextField propertyNameField = new JTextField();
  private final JTextField sheetNumberField = new JTextField();
  private final JTextField columnNumberField = new JTextField();

  public ExcelTranslatorFieldPanel(ExcelTranslatorField initialField, Consumer<TranslatorFieldPanel<ExcelTranslatorField>> panelConsumer) {
    setLayout(new GridBagLayout());
    
    JPanel propertyNamePanel = new JPanel(new GridBagLayout());
    propertyNamePanel.add(new JLabel("Property Name"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    propertyNamePanel.add(propertyNameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(propertyNamePanel, configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    
    JPanel sheetNumberPanel = new JPanel(new GridBagLayout());
    sheetNumberPanel.add(new JLabel("Sheet Number"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 0; }));
    sheetNumberPanel.add(sheetNumberField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 0; }));
    add(sheetNumberPanel, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 0; }));

    JPanel columnNumberPanel = new JPanel(new GridBagLayout());
    columnNumberPanel.add(new JLabel("Column Number"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 0; }));
    columnNumberPanel.add(columnNumberField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 0; }));
    add(columnNumberPanel, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));

    JPanel removePanel = new JPanel(new GridBagLayout());
    removePanel.add(new JLabel(""), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 0; }));
    JButton removeButton = new JButton("Remove");
    removePanel.add(removeButton, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 0; }));
    add(removePanel, configureLayout((c) -> { c.gridx = 3; c.gridy = 0; c.weightx = 0; }));
    
    removeButton.addActionListener((e) -> panelConsumer.accept(this));
    
    initializeFields(initialField);
  }
  
  private void initializeFields(ExcelTranslatorField initialField) {
    if (initialField != null) {
      propertyNameField.setText(initialField.getPropertyName());
      sheetNumberField.setText(initialField.getSheetNumber().toString());
      columnNumberField.setText(initialField.getColumnNumber().toString());
    }
  }
  
  @Override
  public ExcelTranslatorField toField() {
    return ExcelTranslatorField.builder()
        .propertyName(propertyNameField.getText())
        .sheetNumber(Integer.valueOf(sheetNumberField.getText()))
        .columnNumber(Integer.valueOf(columnNumberField.getText()))
        .build();
  }
}
