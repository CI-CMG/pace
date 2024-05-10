package edu.colorado.pace.gui.metadata.organization;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.pace.gui.metadata.common.ObjectForm;
import java.util.UUID;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class OrganizationForm extends ObjectForm<Organization> {

  private JPanel formPanel;
  private JTextField uuidField;
  private JTextField nameField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField countryField;
  private JTextField emailField;
  private JTextField phoneField;
  
  public OrganizationForm(Organization initialOrganization) {
    if (initialOrganization != null) {
      uuidField.setText(initialOrganization.getUuid().toString());
      nameField.setText(initialOrganization.getName());
      streetField.setText(initialOrganization.getStreet());
      cityField.setText(initialOrganization.getCity());
      stateField.setText(initialOrganization.getState());
      zipField.setText(initialOrganization.getZip());
      countryField.setText(initialOrganization.getCountry());
      emailField.setText(initialOrganization.getEmail());
      phoneField.setText(initialOrganization.getPhone());
    }
  }

  @Override
  protected JPanel getFormPanel() {
    return formPanel;
  }

  @Override
  protected Organization fieldsToObject() {
    String uuidText = uuidField.getText();
    
    return Organization.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .street(streetField.getText())
        .city(cityField.getText())
        .state(stateField.getText())
        .zip(zipField.getText())
        .country(countryField.getText())
        .email(emailField.getText())
        .phone(phoneField.getText())
        .build();
  }
}
