package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * ShipTranslatorForm extends BaseTranslatorForm and provides structure
 * relevant to ship translator forms
 */
public class ShipTranslatorForm extends BaseTranslatorForm<ShipTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();

  /**
   * Creates ship translator form
   * @param initialTranslator translator to build upon
   * @param headerOptions headers to select from during mapping
   */
  public ShipTranslatorForm(ShipTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }

  @Override
  protected void addUniqueFields() {
    setName("shipTranslatorForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weighty = 1; }));
    
    tabbedPane.add("Ship", new JScrollPane(formPanel));
    
    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(ShipTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
          initialTranslator.getShipUUID(),
          initialTranslator.getShipName()
      };
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getShipUUID());
      nameField.setSelectedItem(initialTranslator.getShipName());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected ShipTranslator toTranslator(UUID uuid, String name) {
    return ShipTranslator.builder()
        .uuid(uuid)
        .name(name)
        .shipUUID((String) this.uuidField.getSelectedItem())
        .shipName((String) this.nameField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
  }
}
