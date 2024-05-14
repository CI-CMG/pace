package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class ProjectForm extends Form<Project> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();

  public ProjectForm(Project initialProject) {
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridLayout(4, 1));
    contentPanel.add(new JLabel("UUID"));
    contentPanel.add(uuidField);
    contentPanel.add(new JLabel("Name"));
    contentPanel.add(nameField);
    add(contentPanel, BorderLayout.NORTH);
    
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
  protected void delete(CRUDRepository<Project> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();
    
    if (!StringUtils.isBlank(uuidText)) {
      repository.delete(UUID.fromString(uuidText));
    }
  }
}
