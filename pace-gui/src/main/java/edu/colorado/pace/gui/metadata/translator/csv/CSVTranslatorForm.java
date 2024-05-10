package edu.colorado.pace.gui.metadata.translator.csv;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorFactory;
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

public class CSVTranslatorForm extends ObjectForm<CSVTranslator> {

  private JTextField uuidField;
  private JTextField nameField;
  private JPanel formPanel;
  private JPanel fieldsContainerPanel;
  private JButton createFieldButton;
  private JPanel fieldsPanel;
  private JLabel uuidLabel;
  private JButton chooseTemplateButton;

  private final List<CSVTranslatorField> fields = new ArrayList<>(0);

  public CSVTranslatorForm(CSVTranslator initialTranslator) {
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

      CSVTranslator translator = switch (choice) {
        case "Ship" -> CSVTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Ship.class));
        case "Project" -> CSVTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Project.class));
        case "Organization" -> CSVTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Organization.class));
        case "Person" -> CSVTranslatorFactory.createTranslator(null, () -> FieldNameFactory.getDefaultDeclaredFields(Person.class)); 
        default -> null;
      };
      
      if (translator != null) {
        translator.getFields().forEach(this::createField);
      }
    });
  }
  
  private void createField(edu.colorado.cires.pace.data.object.CSVTranslatorField initialTranslatorField) {
    CSVTranslatorField field = new CSVTranslatorField(initialTranslatorField, (f) -> {
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
  protected CSVTranslator fieldsToObject() {
    String uuidText = uuidField.getText();
    return CSVTranslator.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .fields(fields.stream()
            .map(CSVTranslatorField::toField)
            .toList())
        .build();
  }
}
