package edu.colorado.pace.gui.metadata.translator.csv;

import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class CSVTranslatorField {
  private JPanel fieldPanel;
  private JTextField propertyNameField;
  private JTextField columnNumberField;
  private JButton removeButton;

  public CSVTranslatorField(edu.colorado.cires.pace.data.object.CSVTranslatorField initialTranslatorField, Consumer<CSVTranslatorField> removeAction) {
    if (initialTranslatorField != null) {
      propertyNameField.setText(initialTranslatorField.getPropertyName());
      columnNumberField.setText(initialTranslatorField.getColumnNumber().toString());
    }
    removeButton.addActionListener(e -> removeAction.accept(this));
  }

  public JPanel getFieldPanel() {
    return fieldPanel;
  }

  public edu.colorado.cires.pace.data.object.CSVTranslatorField toField() {
    String columnNumberText = columnNumberField.getText();
    return edu.colorado.cires.pace.data.object.CSVTranslatorField.builder()
        .propertyName(propertyNameField.getText())
        .columnNumber(StringUtils.isBlank(columnNumberText) ? null : Integer.parseInt(columnNumberText))
        .build();
  }
}
