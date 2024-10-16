package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * PersonTranslatorForm extends BaseTranslatorForm and creates structure
 * relevant to person translator forms
 */
public class PersonTranslatorForm extends BaseTranslatorForm<PersonTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();
  private final JComboBox<String> positionField = new JComboBox<>();
  private final JComboBox<String> organizationField = new JComboBox<>();
  private final JComboBox<String> streetField = new JComboBox<>();
  private final JComboBox<String> cityField = new JComboBox<>();
  private final JComboBox<String> stateField = new JComboBox<>();
  private final JComboBox<String> zipField = new JComboBox<>();
  private final JComboBox<String> countryField = new JComboBox<>();
  private final JComboBox<String> emailField = new JComboBox<>();
  private final JComboBox<String> phoneField = new JComboBox<>();
  private final JComboBox<String> orcidField = new JComboBox<>();

  /**
   * Creates a person translator form
   * @param initialTranslator translator to build upon
   * @param headerOptions headers to select from during mapping
   */
  public PersonTranslatorForm(PersonTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    setName("personTranslatorForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    positionField.setName("position");
    organizationField.setName("organization");
    streetField.setName("street");
    cityField.setName("city");
    stateField.setName("state");
    zipField.setName("zip");
    countryField.setName("country");
    emailField.setName("email");
    phoneField.setName("phone");
    orcidField.setName("orcid");
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }

  @Override
  protected void addUniqueFields() {
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JLabel("Position"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    formPanel.add(positionField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    formPanel.add(new JLabel("Organization"), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    formPanel.add(organizationField, configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
    formPanel.add(new JLabel("Street"), configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
    formPanel.add(streetField, configureLayout((c) -> { c.gridx = 0; c.gridy = 9; c.weightx = 1; }));
    formPanel.add(new JLabel("City"), configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
    formPanel.add(cityField, configureLayout((c) -> { c.gridx = 0;  c.gridy = 11; c.weightx = 1; }));
    formPanel.add(new JLabel("State"), configureLayout((c) -> { c.gridx = 0; c.gridy = 12; c.weightx = 1; }));
    formPanel.add(stateField, configureLayout((c) -> { c.gridx = 0; c.gridy = 13; c.weightx = 1; }));
    formPanel.add(new JLabel("Zip"), configureLayout((c) -> { c.gridx = 0; c.gridy = 14; c.weightx = 1; }));
    formPanel.add(zipField, configureLayout((c) -> { c.gridx = 0; c.gridy = 15; c.weightx = 1; }));
    formPanel.add(new JLabel("Country"), configureLayout((c) -> { c.gridx = 0; c.gridy = 16; c.weightx = 1; }));
    formPanel.add(countryField, configureLayout((c) -> { c.gridx = 0; c.gridy = 17; c.weightx = 1; }));
    formPanel.add(new JLabel("Email"), configureLayout((c) -> { c.gridx = 0; c.gridy = 18; c.weightx = 1; }));
    formPanel.add(emailField, configureLayout((c) -> { c.gridx = 0; c.gridy = 19; c.weightx = 1; }));
    formPanel.add(new JLabel("Phone"), configureLayout((c) -> { c.gridx = 0; c.gridy = 20; c.weightx = 1; }));
    formPanel.add(phoneField, configureLayout((c) -> { c.gridx = 0; c.gridy = 21; c.weightx = 1; }));
    formPanel.add(new JLabel("Orcid"), configureLayout((c) -> { c.gridx = 0; c.gridy = 22; c.weightx = 1; }));
    formPanel.add(orcidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 23; c.weightx = 1; }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 24; c.weighty = 1; }));
    tabbedPane.add("Person", new JScrollPane(formPanel));

    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(PersonTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
          initialTranslator.getPersonUUID(),
          initialTranslator.getPersonName(),
          initialTranslator.getOrganization(),
          initialTranslator.getPosition(),
          initialTranslator.getStreet(),
          initialTranslator.getCity(),
          initialTranslator.getState(),
          initialTranslator.getZip(),
          initialTranslator.getCountry(),
          initialTranslator.getEmail(),
          initialTranslator.getPhone(),
          initialTranslator.getOrcid(),
      };
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getPersonUUID());
      nameField.setSelectedItem(initialTranslator.getPersonName());
      organizationField.setSelectedItem(initialTranslator.getOrganization());
      positionField.setSelectedItem(initialTranslator.getPosition());
      streetField.setSelectedItem(initialTranslator.getStreet());
      cityField.setSelectedItem(initialTranslator.getCity());
      stateField.setSelectedItem(initialTranslator.getState());
      zipField.setSelectedItem(initialTranslator.getZip());
      countryField.setSelectedItem(initialTranslator.getCountry());
      emailField.setSelectedItem(initialTranslator.getEmail());
      phoneField.setSelectedItem(initialTranslator.getPhone());
      orcidField.setSelectedItem(initialTranslator.getOrcid());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected PersonTranslator toTranslator(UUID uuid, String name) {
    return PersonTranslator.builder()
        .uuid(uuid)
        .name(name)
        .personUUID((String) this.uuidField.getSelectedItem())
        .personName((String) this.nameField.getSelectedItem())
        .organization((String) organizationField.getSelectedItem())
        .position((String) positionField.getSelectedItem())
        .street((String) streetField.getSelectedItem())
        .city((String) cityField.getSelectedItem())
        .state((String) stateField.getSelectedItem())
        .zip((String) zipField.getSelectedItem())
        .country((String) countryField.getSelectedItem())
        .email((String) emailField.getSelectedItem())
        .phone((String) phoneField.getSelectedItem())
        .orcid((String) orcidField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
    updateComboBoxModel(positionField, options);
    updateComboBoxModel(organizationField, options);
    updateComboBoxModel(streetField, options);
    updateComboBoxModel(cityField, options);
    updateComboBoxModel(stateField, options);
    updateComboBoxModel(zipField, options);
    updateComboBoxModel(countryField, options);
    updateComboBoxModel(emailField, options);
    updateComboBoxModel(phoneField, options);
    updateComboBoxModel(orcidField, options);
  }
}
