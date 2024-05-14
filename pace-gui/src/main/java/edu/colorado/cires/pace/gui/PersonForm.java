package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class PersonForm extends Form<Person> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();
  private final JTextField organizationField = new JTextField();
  private final JTextField positionField = new JTextField();
  private final JTextField streetField = new JTextField();
  private final JTextField cityField = new JTextField();
  private final JTextField stateField = new JTextField();
  private final JTextField zipField = new JTextField();
  private final JTextField countryField = new JTextField();
  private final JTextField emailField = new JTextField();
  private final JTextField phoneField = new JTextField();
  private final JTextField orcidField = new JTextField();

  public PersonForm(Person initialPerson) {
   setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridLayout(24, 1));
    contentPanel.add(new JLabel("UUID"));
    contentPanel.add(uuidField);
    contentPanel.add(new JLabel("Name"));
    contentPanel.add(nameField);
    contentPanel.add(new JLabel("Organization"));
    contentPanel.add(organizationField);
    contentPanel.add(new JLabel("Position"));
    contentPanel.add(positionField);
    contentPanel.add(new JLabel("Street"));
    contentPanel.add(streetField);
    contentPanel.add(new JLabel("City"));
    contentPanel.add(cityField);
    contentPanel.add(new JLabel("State"));
    contentPanel.add(stateField);
    contentPanel.add(new JLabel("Zip"));
    contentPanel.add(zipField);
    contentPanel.add(new JLabel("Country"));
    contentPanel.add(countryField);
    contentPanel.add(new JLabel("Email"));
    contentPanel.add(emailField);
    contentPanel.add(new JLabel("Phone"));
    contentPanel.add(phoneField);
    contentPanel.add(new JLabel("Orcid"));
    contentPanel.add(orcidField);
    add(contentPanel, BorderLayout.NORTH);
    
    uuidField.setEnabled(false);
    
    initializeFields(initialPerson);
  }

  @Override
  protected void initializeFields(Person object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
      organizationField.setText(object.getOrganization());
      positionField.setText(object.getPosition());
      streetField.setText(object.getStreet());
      cityField.setText(object.getCity());
      stateField.setText(object.getState());
      zipField.setText(object.getZip());
      countryField.setText(object.getCountry());
      emailField.setText(object.getEmail());
      phoneField.setText(object.getPhone());
      orcidField.setText(object.getOrcid());
    }
  }

  @Override
  protected void save(CRUDRepository<Person> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();
    
    boolean update = !StringUtils.isBlank(uuidText);
    Person person = Person.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .organization(organizationField.getText())
        .position(positionField.getText())
        .street(streetField.getText())
        .city(cityField.getText())
        .state(stateField.getText())
        .zip(zipField.getText())
        .country(countryField.getText())
        .email(emailField.getText())
        .phone(phoneField.getText())
        .orcid(orcidField.getText())
        .build();
    
    if (update) {
      repository.update(
          person.getUuid(),
          person
      );
    } else {
      repository.create(person);
    }
  }

  @Override
  protected void delete(CRUDRepository<Person> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();
    
    if (!StringUtils.isBlank(uuidText)) {
      repository.delete(UUID.fromString(uuidText));
    }
  }
}
