package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.ProjectTranslator;
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

public class ProjectTranslatorForm extends BaseTranslatorForm<ProjectTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();

  public ProjectTranslatorForm(ProjectTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    setName("projectTranslatorForm");
    uuidField.setName("uuid");
    nameField.setName("name");
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
    
    tabbedPane.add("1. Project Info", new JScrollPane(formPanel));
    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(ProjectTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
          initialTranslator.getProjectUUID(),
          initialTranslator.getProjectName()
      };
      
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getProjectUUID());
      nameField.setSelectedItem(initialTranslator.getProjectName());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected ProjectTranslator toTranslator(JTextField uuidField, JTextField nameField) {
    return ProjectTranslator.builder()
        .uuid(StringUtils.isBlank(uuidField.getText()) ? null : UUID.fromString(uuidField.getText()))
        .name(nameField.getText())
        .projectUUID((String) this.uuidField.getSelectedItem())
        .projectName((String) this.nameField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
  }
}
