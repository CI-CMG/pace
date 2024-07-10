package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.Person;
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
  
  private final Person initialPerson;

  public PersonForm(Person initialPerson) {
    this.initialPerson = initialPerson;
   setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridBagLayout());
    contentPanel.add(new JLabel("UUID"), configureFormLayout(0, 0));
    contentPanel.add(uuidField, configureFormLayout(0, 1));
    contentPanel.add(new JLabel("Name"), configureFormLayout(0, 2));
    contentPanel.add(nameField, configureFormLayout(0, 3));
    contentPanel.add(new JLabel("Organization"), configureFormLayout(0, 4));
    contentPanel.add(organizationField, configureFormLayout(0, 5));
    contentPanel.add(new JLabel("Position"), configureFormLayout(0, 6));
    contentPanel.add(positionField, configureFormLayout(0, 7));
    contentPanel.add(new JLabel("Street"), configureFormLayout(0, 8));
    contentPanel.add(streetField, configureFormLayout(0, 9));
    contentPanel.add(new JLabel("City"), configureFormLayout(0, 10));
    contentPanel.add(cityField, configureFormLayout(0, 11));
    contentPanel.add(new JLabel("State"), configureFormLayout(0, 12));
    contentPanel.add(stateField, configureFormLayout(0, 13));
    contentPanel.add(new JLabel("Zip"), configureFormLayout(0, 14));
    contentPanel.add(zipField, configureFormLayout(0, 15));
    contentPanel.add(new JLabel("Country"), configureFormLayout(0, 16));
    contentPanel.add(countryField, configureFormLayout(0, 17));
    contentPanel.add(new JLabel("Email"), configureFormLayout(0, 18));
    contentPanel.add(emailField, configureFormLayout(0, 19));
    contentPanel.add(new JLabel("Phone"), configureFormLayout(0, 20));
    contentPanel.add(phoneField, configureFormLayout(0, 21));
    contentPanel.add(new JLabel("Orcid"), configureFormLayout(0, 22));
    contentPanel.add(orcidField, configureFormLayout(0, 23));
    contentPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 24; c.weighty = 1; }));
    add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    
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
          initialPerson.getUuid(),
          person
      );
    } else {
      repository.create(person);
    }
  }

  @Override
  protected void delete(CRUDRepository<Person> repository) throws NotFoundException, DatastoreException {
    repository.delete(initialPerson.getUuid());
  }
}
