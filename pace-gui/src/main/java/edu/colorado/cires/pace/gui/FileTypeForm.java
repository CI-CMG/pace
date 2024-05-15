package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class FileTypeForm extends Form<FileType> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField typeField = new JTextField();
  private final JTextField commentField = new JTextField();

  public FileTypeForm(FileType initialFileType) {
    setLayout(new GridBagLayout());
    
    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Type"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(typeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    add(new JLabel("Comment"), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(commentField, configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weighty = 1; }));
    
    uuidField.setEnabled(false);
    
    initializeFields(initialFileType);
  }

  @Override
  protected void initializeFields(FileType object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      typeField.setText(object.getType());
      commentField.setText(object.getComment());
    }
  }

  @Override
  protected void save(CRUDRepository<FileType> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    FileType fileType = FileType.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .type(typeField.getText())
        .comment(commentField.getText())
        .build();
    if (update) {
      repository.update(
          fileType.getUuid(),
          fileType
      );
    } else {
      repository.create(fileType);
    }
  }

  @Override
  protected void delete(CRUDRepository<FileType> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    if (!StringUtils.isBlank(uuidText)) {
      repository.delete(UUID.fromString(uuidText));
    }
  }
}
