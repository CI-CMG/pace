package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.BorderLayout;
import java.util.UUID;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class ProjectForm extends Form<Project> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();

  public ProjectForm(Project initialProject) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel uuidPanel = new JPanel();
    uuidPanel.setLayout(new BorderLayout());
    JPanel uuidFieldPanel = new JPanel();
    uuidFieldPanel.setLayout(new BorderLayout());
    uuidFieldPanel.add(new JLabel("UUID"), BorderLayout.NORTH);
    uuidFieldPanel.add(uuidField, BorderLayout.SOUTH);
    uuidPanel.add(uuidFieldPanel, BorderLayout.NORTH);
    
    uuidField.setEnabled(false);
    
    JPanel namePanel = new JPanel();
    namePanel.setLayout(new BorderLayout());
    JPanel nameFieldPanel = new JPanel();
    nameFieldPanel.setLayout(new BorderLayout());
    nameFieldPanel.add(new JLabel("Name"), BorderLayout.NORTH);
    nameFieldPanel.add(nameField, BorderLayout.SOUTH);
    namePanel.add(nameFieldPanel, BorderLayout.NORTH);
    
    add(uuidPanel);
    add(namePanel);
    
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
