package edu.colorado.pace.gui.metadata.project;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.pace.gui.metadata.common.ObjectForm;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class ProjectForm extends ObjectForm<Project> {

  private JPanel formPanel;
  private JTextField uuidField;
  private JTextField nameField;
  private JLabel uuidLabel;

  public ProjectForm(Project initialProject) {
    uuidLabel.setVisible(false);
    uuidField.setVisible(false);
    
    if (initialProject != null) {
      uuidField.setText(initialProject.getUuid().toString());
      nameField.setText(initialProject.getName());
    }
  }

  @Override
  protected JPanel getFormPanel() {
    return formPanel;
  }

  @Override
  protected Project fieldsToObject() {
    String uuidText = uuidField.getText();
    return Project.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .build();
  }
}
