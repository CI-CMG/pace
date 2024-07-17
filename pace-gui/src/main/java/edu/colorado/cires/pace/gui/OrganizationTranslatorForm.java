package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.OrganizationTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class OrganizationTranslatorForm  extends BaseTranslatorForm<OrganizationTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();
  private final JComboBox<String> streetField = new JComboBox<>();
  private final JComboBox<String> cityField = new JComboBox<>();
  private final JComboBox<String> stateField = new JComboBox<>();
  private final JComboBox<String> zipField = new JComboBox<>();
  private final JComboBox<String> countryField = new JComboBox<>();
  private final JComboBox<String> emailField = new JComboBox<>();
  private final JComboBox<String> phoneField = new JComboBox<>();

  public OrganizationTranslatorForm(OrganizationTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }

  @Override
  protected void addUniqueFields() {
    setName("organizationTranslatorForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    streetField.setName("street");
    cityField.setName("city");
    stateField.setName("state");
    zipField.setName("zip");
    countryField.setName("country");
    emailField.setName("email");
    phoneField.setName("phone");
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JLabel("Street"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    formPanel.add(streetField, configureLayout(c -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    formPanel.add(new JLabel("City"), configureLayout(c -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    formPanel.add(cityField, configureLayout(c -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
    formPanel.add(new JLabel("State"), configureLayout(c -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
    formPanel.add(stateField, configureLayout(c -> { c.gridx = 0; c.gridy = 9; c.weightx = 1; }));
    formPanel.add(new JLabel("Zip"), configureLayout(c -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
    formPanel.add(zipField, configureLayout(c -> { c.gridx = 0; c.gridy = 11; c.weightx = 1; }));
    formPanel.add(new JLabel("Country"), configureLayout(c -> { c.gridx = 0; c.gridy = 12; c.weightx = 1; }));
    formPanel.add(countryField, configureLayout(c -> { c.gridx = 0; c.gridy = 13; c.weightx = 1; }));
    formPanel.add(new JLabel("Email"), configureLayout(c -> { c.gridx = 0; c.gridy = 14; c.weightx = 1; }));
    formPanel.add(emailField, configureLayout(c -> { c.gridx = 0; c.gridy = 15; c.weightx = 1; }));
    formPanel.add(new JLabel("Phone"), configureLayout(c -> { c.gridx = 0; c.gridy = 16; c.weightx = 1; }));
    formPanel.add(phoneField, configureLayout(c -> { c.gridx = 0; c.gridy = 17; c.weightx = 1; }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 18; c.weighty = 1; }));
    tabbedPane.add("1. Organization Info", new JScrollPane(formPanel));
    
    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(OrganizationTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
        initialTranslator.getOrganizationUUID(),
        initialTranslator.getOrganizationName(),
        initialTranslator.getStreet(),
        initialTranslator.getCity(),
        initialTranslator.getState(),
        initialTranslator.getZip(),
        initialTranslator.getCountry(),
        initialTranslator.getEmail(),
        initialTranslator.getPhone()  
      };
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getOrganizationUUID());
      nameField.setSelectedItem(initialTranslator.getOrganizationName());
      streetField.setSelectedItem(initialTranslator.getStreet());
      cityField.setSelectedItem(initialTranslator.getCity());
      stateField.setSelectedItem(initialTranslator.getState());
      zipField.setSelectedItem(initialTranslator.getZip());
      countryField.setSelectedItem(initialTranslator.getCountry());
      emailField.setSelectedItem(initialTranslator.getEmail());
      phoneField.setSelectedItem(initialTranslator.getPhone());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected OrganizationTranslator toTranslator(JTextField uuidField, JTextField nameField) {
    return OrganizationTranslator.builder()
        .uuid(StringUtils.isBlank(uuidField.getText()) ? null : UUID.fromString(uuidField.getText()))
        .name(nameField.getText())
        .organizationUUID((String) this.uuidField.getSelectedItem())
        .organizationName((String) this.nameField.getSelectedItem())
        .street((String) streetField.getSelectedItem())
        .city((String) cityField.getSelectedItem())
        .state((String) stateField.getSelectedItem())
        .zip((String) zipField.getSelectedItem())
        .country((String) countryField.getSelectedItem())
        .email((String) emailField.getSelectedItem())
        .phone((String) phoneField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
    updateComboBoxModel(streetField, options);
    updateComboBoxModel(cityField, options);
    updateComboBoxModel(stateField, options);
    updateComboBoxModel(zipField, options);
    updateComboBoxModel(countryField, options);
    updateComboBoxModel(emailField, options);
    updateComboBoxModel(phoneField, options);
  }
}
