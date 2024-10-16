package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * PlatformTranslatorForm extends BaseTranslatorForm and provides structure
 * relevant to platforms
 */
public class PlatformTranslatorForm extends BaseTranslatorForm<PlatformTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();

  /**
   * Creates a platform translator form
   * @param initialTranslator base translator to build upon
   * @param headerOptions headers to select from during mapping
   */
  public PlatformTranslatorForm(PlatformTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    setName("platformTranslatorForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }

  @Override
  protected void addUniqueFields() {
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weighty = 1; }));
    
    tabbedPane.add("Platform", new JScrollPane(formPanel));
    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(PlatformTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
          initialTranslator.getPlatformUUID(),
          initialTranslator.getPlatformName()
      };
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getPlatformUUID());
      nameField.setSelectedItem(initialTranslator.getPlatformName());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected PlatformTranslator toTranslator(UUID uuid, String name) {
    return PlatformTranslator.builder()
        .uuid(uuid)
        .name(name)
        .platformUUID((String) this.uuidField.getSelectedItem())
        .platformName((String) this.nameField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
  }
}
