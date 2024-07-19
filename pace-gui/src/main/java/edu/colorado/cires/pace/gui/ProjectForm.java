package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.Project;
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

public class ProjectForm extends Form<Project> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();

  public ProjectForm(Project initialProject) {
    setName("projectForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.add(new JLabel("UUID"), configureFormLayout(0, 0));
    contentPanel.add(uuidField, configureFormLayout(0, 1));
    contentPanel.add(new JLabel("Name"), configureFormLayout(0, 2));
    contentPanel.add(nameField, configureFormLayout(0, 3));
    contentPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weighty = 1; }));
    add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    
    uuidField.setEnabled(false);
    
    initializeFields(initialProject);
  }


  @Override
  protected void initializeFields(Project object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
    }
  }

  @Override
  protected void save(CRUDRepository<Project> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();
    
    boolean update = !StringUtils.isBlank(uuidText);
    Project project = Project.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .build();
    if (update) {
      repository.update(
          project.getUuid(),
          project
      );
    } else {
      repository.create(project);
    }
  }

  @Override
  protected void delete(CRUDRepository<Project> repository) throws NotFoundException, DatastoreException, BadArgumentException {
    String uuidText = uuidField.getText();
    
    repository.delete(UUID.fromString(uuidText));
  }
}
