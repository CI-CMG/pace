package edu.colorado.pace.gui.metadata.translator.excel;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorFactory;
import edu.colorado.pace.gui.metadata.common.ObjectForm;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class ExcelTranslatorForm extends ObjectForm<ExcelTranslator> {

  private JTextField uuidField;
  private JPanel formPanel;
  private JTextField nameField;
  private JButton createFieldButton;
  private JPanel fieldsPanel;
  private JLabel uuidLabel;
  private JButton chooseTemplateButton;

  private final List<ExcelTranslatorField> fields = new ArrayList<>(0);
  
  public ExcelTranslatorForm(ExcelTranslator initialTranslator) {
    uuidLabel.setVisible(false);
    uuidField.setVisible(false);
    
    if (initialTranslator != null) {
      uuidField.setText(initialTranslator.getUuid().toString());
      nameField.setText(initialTranslator.getName());
      initialTranslator.getFields().forEach(this::createField);
    }
    
    createFieldButton.addActionListener(e -> createField(null));
    
    chooseTemplateButton.setVisible(initialTranslator == null);
    chooseTemplateButton.addActionListener(e -> {
      String choice = (String) JOptionPane.showInputDialog(formPanel, null, "Choose translator template", JOptionPane.PLAIN_MESSAGE, null, new Object[] {
          "Ship", "Project", "Person", "Organization"
      }, null);

      ExcelTranslator translator = switch (choice) {
        case "Ship" -> ExcelTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Ship.class));
        case "Project" -> ExcelTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Project.class));
        case "Organization" -> ExcelTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Organization.class));
        case "Person" -> ExcelTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Person.class));
        default -> null;
      };

      if (translator != null) {
        translator.getFields().forEach(this::createField);
      }
    });
  }
  
  private void createField(edu.colorado.cires.pace.data.object.ExcelTranslatorField initialTranslatorField) {
    ExcelTranslatorField field = new ExcelTranslatorField(initialTranslatorField, (f) -> {
      fieldsPanel.remove(f.getFieldPanel());
      fields.remove(f);
      fieldsPanel.revalidate();
    });

    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = fieldsPanel.getComponentCount();
    gridBagConstraints.weightx = 100;
    gridBagConstraints.fill = GridBagConstraints.BOTH;

    fieldsPanel.add(field.getFieldPanel(), gridBagConstraints);
    fields.add(field);
    fieldsPanel.revalidate();
  }

  @Override
  protected JPanel getFormPanel() {
    return formPanel;
  }

  @Override
  protected ExcelTranslator fieldsToObject() {
    String uuidText = uuidField.getText();
    return ExcelTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .fields(fields.stream()
            .map(ExcelTranslatorField::toField)
            .toList())
        .build();
  }
}
