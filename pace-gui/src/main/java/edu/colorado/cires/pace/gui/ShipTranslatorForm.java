package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.ShipTranslator;
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

public class ShipTranslatorForm extends BaseTranslatorForm<ShipTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();

  public ShipTranslatorForm(ShipTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }

  @Override
  protected void addUniqueFields() {
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weighty = 1; }));
    
    tabbedPane.add("1. Ship Info", new JScrollPane(formPanel));
    
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
  protected ShipTranslator toTranslator(JTextField uuidField, JTextField nameField) {
    return ShipTranslator.builder()
        .uuid(StringUtils.isBlank(uuidField.getText()) ? null : UUID.fromString(uuidField.getText()))
        .name(nameField.getText())
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
