package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * PersonForm extends ContactForm and creates entry fields
 * relevant to person forms specifically
 */
public class PersonForm extends ContactForm<Person> {
  
  private JTextField orcid;
  private JTextField position;
  private JTextField organization;

  /**
   * Creates a person form
   * @param initialObject object to build upon
   */
  public PersonForm(Person initialObject) {
    super(initialObject);
  }

  @Override
  protected Person contactFromFormFields(UUID uuid, String name, boolean visible, String street, String city, String state, String zip,
      String country, String email, String phone) {
    return Person.builder()
        .uuid(uuid)
        .name(name)
        .visible(visible)
        .street(street)
        .city(city)
        .state(state)
        .zip(zip)
        .country(country)
        .email(email)
        .phone(phone)
        .position(position.getText())
        .organization(organization.getText())
        .orcid(orcid.getText())
        .build();
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    orcid = new JTextField();
    orcid.setName("orcid");
    position = new JTextField();
    position.setName("position");
    organization = new JTextField();
    organization.setName("organization");
    
    contentPanel.add(new JLabel("Orcid"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(orcid, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("Position"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(position, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("Organization"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(organization, configureFormLayout(0, contentPanel.getComponentCount()));
    
    super.addAdditionalFields(contentPanel);
  }

  @Override
  protected void initializeAdditionalFields(Person object, CRUDRepository<?>... dependencyRepositories) {
    super.initializeAdditionalFields(object);
    orcid.setText(object.getOrcid());
    position.setText(object.getPosition());
    organization.setText(object.getOrganization());
  }
}
