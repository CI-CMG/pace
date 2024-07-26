package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileTypeForm extends MetadataForm<FileType> {
  
  private JTextField comment;

  protected FileTypeForm(FileType initialObject) {
    super(initialObject, "Type");
  }

  @Override
  protected FileType objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return FileType.builder()
        .uuid(uuid)
        .type(uniqueField)
        .comment(comment.getText())
        .visible(visible)
        .build();
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    comment = new JTextField();
    comment.setName("comment");
    contentPanel.add(new JLabel("Comment"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(comment, configureFormLayout(0, contentPanel.getComponentCount()));
  }

  @Override
  protected void initializeAdditionalFields(FileType object, CRUDRepository<?>... dependencyRepositories) {
    comment.setText(object.getComment());
  }
}
