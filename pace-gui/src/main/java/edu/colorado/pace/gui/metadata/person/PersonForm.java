package edu.colorado.pace.gui.metadata.person;

import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.pace.gui.metadata.common.ObjectForm;
import java.util.UUID;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class PersonForm extends ObjectForm<Person> {

  private JPanel formPanel;
  private JTextField uuidField;
  private JTextField nameField;
  private JTextField positionField;
  private JTextField organizationField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField countryField;
  private JTextField emailField;
  private JTextField phoneField;
  private JTextField orcidField;
  
  public PersonForm(Person initialPerson) {
    if (initialPerson != null) {
      uuidField.setText(initialPerson.getUuid().toString());
      nameField.setText(initialPerson.getName());
      positionField.setText(initialPerson.getPosition());
      organizationField.setText(initialPerson.getOrganization());
      streetField.setText(initialPerson.getStreet());
      cityField.setText(initialPerson.getCity());
      stateField.setText(initialPerson.getState());
      zipField.setText(initialPerson.getZip());
      countryField.setText(initialPerson.getCountry());
      emailField.setText(initialPerson.getEmail());
      phoneField.setText(initialPerson.getPhone());
      orcidField.setText(initialPerson.getOrcid());
    }
  }

  @Override
  protected JPanel getFormPanel() {
    return formPanel;
  }

  @Override
  protected Person fieldsToObject() {
    String uuidText = uuidField.getText();
    
    return Person.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .position(positionField.getText())
        .organization(organizationField.getText())
        .street(streetField.getText())
        .city(cityField.getText())
        .state(stateField.getText())
        .zip(zipField.getText())
        .country(countryField.getText())
        .email(emailField.getText())
        .phone(phoneField.getText())
        .orcid(orcidField.getText())
        .build();
  }
}
