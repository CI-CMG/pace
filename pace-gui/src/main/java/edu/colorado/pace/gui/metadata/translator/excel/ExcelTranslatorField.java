package edu.colorado.pace.gui.metadata.translator.excel;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class ExcelTranslatorField {

  private JPanel fieldPanel;
  private JTextField propertyNameField;
  private JTextField sheetNumberField;
  private JTextField columnNumberField;
  private JButton removeButton;
  
  public ExcelTranslatorField(edu.colorado.cires.pace.data.object.ExcelTranslatorField initialTranslatorField, Consumer<ExcelTranslatorField> removeAction) {
    if (initialTranslatorField != null) {
      propertyNameField.setText(initialTranslatorField.getPropertyName());
      sheetNumberField.setText(initialTranslatorField.getSheetNumber().toString());
      columnNumberField.setText(initialTranslatorField.getColumnNumber().toString());
    }
    removeButton.addActionListener(e -> removeAction.accept(this));
  }

  public JPanel getFieldPanel() {
    return fieldPanel;
  }
  
  public edu.colorado.cires.pace.data.object.ExcelTranslatorField toField() {
    String sheetNumberText = sheetNumberField.getText();
    String columnNumberText = columnNumberField.getText();
    return edu.colorado.cires.pace.data.object.ExcelTranslatorField.builder()
        .propertyName(propertyNameField.getText())
        .sheetNumber(StringUtils.isBlank(sheetNumberText) ? null : Integer.parseInt(sheetNumberText))
        .columnNumber(StringUtils.isBlank(columnNumberText) ? null : Integer.parseInt(columnNumberText))
        .build();
  }
}
