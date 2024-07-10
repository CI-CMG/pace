package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class OrganizationForm extends Form<Organization> {

  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();
  private final JTextField streetField = new JTextField();
  private final JTextField cityField = new JTextField();
  private final JTextField stateField = new JTextField();
  private final JTextField zipField = new JTextField();
  private final JTextField countryField = new JTextField();
  private final JTextField emailField = new JTextField();
  private final JTextField phoneField = new JTextField();
  private final Organization initialOrganization;

  public OrganizationForm(Organization initialOrganization) {
    setName("organizationForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    streetField.setName("street");
    cityField.setName("city");
    stateField.setName("state");
    zipField.setName("zip");
    countryField.setName("country");
    emailField.setName("email");
    phoneField.setName("phone");
    
    this.initialOrganization = initialOrganization;
    
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.add(new JLabel("UUID"), configureFormLayout(0, 0));
    contentPanel.add(uuidField, configureFormLayout(0, 1));
    contentPanel.add(new JLabel("Name"), configureFormLayout(0, 2));
    contentPanel.add(nameField, configureFormLayout(0, 3));
    contentPanel.add(new JLabel("Street"), configureFormLayout(0, 4));
    contentPanel.add(streetField, configureFormLayout(0, 5));
    contentPanel.add(new JLabel("City"), configureFormLayout(0, 6));
    contentPanel.add(cityField, configureFormLayout(0, 7));
    contentPanel.add(new JLabel("State"), configureFormLayout(0, 8));
    contentPanel.add(stateField, configureFormLayout(0, 9));
    contentPanel.add(new JLabel("Zip"), configureFormLayout(0, 10));
    contentPanel.add(zipField, configureFormLayout(0, 11));
    contentPanel.add(new JLabel("Country"), configureFormLayout(0, 12));
    contentPanel.add(countryField, configureFormLayout(0, 13));
    contentPanel.add(new JLabel("Email"), configureFormLayout(0, 14));
    contentPanel.add(emailField, configureFormLayout(0, 15));
    contentPanel.add(new JLabel("Phone"), configureFormLayout(0, 16));
    contentPanel.add(phoneField, configureFormLayout(0, 17));
    contentPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 18; c.weighty = 1; }));
    add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    
    initializeFields(initialOrganization);
  }

  @Override
  protected void initializeFields(Organization object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
      streetField.setText(object.getStreet());
      cityField.setText(object.getCity());
      stateField.setText(object.getState());
      zipField.setText(object.getZip());
      countryField.setText(object.getCountry());
      emailField.setText(object.getEmail());
      phoneField.setText(object.getPhone());
    }
  }

  @Override
  protected void save(CRUDRepository<Organization> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    Organization organization = Organization.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .street(streetField.getText())
        .city(cityField.getText())
        .state(stateField.getText())
        .zip(zipField.getText())
        .country(countryField.getText())
        .email(emailField.getText())
        .phone(phoneField.getText())
        .build();

    if (update) {
      repository.update(
          initialOrganization.getUuid(),
          organization
      );
    } else {
      repository.create(organization);
    }
  }

  @Override
  protected void delete(CRUDRepository<Organization> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    repository.delete(UUID.fromString(uuidText));
  }
}
