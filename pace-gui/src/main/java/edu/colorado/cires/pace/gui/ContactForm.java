package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;

import edu.colorado.cires.pace.data.object.contact.Contact;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * ContactForm extends ObjectWithNameForm and holds the fields which apply to a contact
 * @param <C> Contact type
 */
public abstract class ContactForm<C extends Contact> extends ObjectWithNameForm<C> {
  
  private JTextField street;
  private JTextField city;
  private JTextField state;
  private JTextField zip;
  private JTextField country;
  private JTextField email;
  private JTextField phone;

  protected abstract C contactFromFormFields(
      UUID uuid,
      String name,
      boolean visible,
      String street,
      String city,
      String state,
      String zip,
      String country,
      String email,
      String phone
  );

  /**
   * Initializes a contact form
   * @param initialObject to create object with name
   */
  public ContactForm(C initialObject) {
    super(initialObject);
  }

  @Override
  protected C objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return contactFromFormFields(
        uuid,
        uniqueField,
        visible,
        street.getText(),
        city.getText(),
        state.getText(),
        zip.getText(),
        country.getText(),
        email.getText(),
        phone.getText()
    );
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    street = new JTextField();
    street.setName("street");
    city = new JTextField();
    city.setName("city");
    state = new JTextField();
    state.setName("state");
    zip = new JTextField();
    zip.setName("zip");
    country = new JTextField();
    country.setName("country");
    email = new JTextField();
    email.setName("email");
    phone = new JTextField();
    phone.setName("phone");
    
    contentPanel.add(new JLabel("Street"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(street, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("City"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(city, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("State"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(state, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("Zip"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(zip, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("Country"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(country, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("Email"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(email, configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(new JLabel("Phone"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(phone, configureFormLayout(0, contentPanel.getComponentCount()));
  }

  @Override
  protected void initializeAdditionalFields(C object, CRUDRepository<?>... dependencyRepositories) {
    street.setText(object.getStreet());
    city.setText(object.getCity());
    state.setText(object.getState());
    zip.setText(object.getZip());
    country.setText(object.getCountry());
    email.setText(object.getEmail());
    phone.setText(object.getPhone());
  }
}
