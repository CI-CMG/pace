package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.fileType.translator.FileTypeTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class FileTypeTranslatorForm extends BaseTranslatorForm<FileTypeTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> typeField = new JComboBox<>();
  private final JComboBox<String> commentField = new JComboBox<>();

  public FileTypeTranslatorForm(FileTypeTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    setName("fileTypeTranslatorForm");
    uuidField.setName("uuid");
    typeField.setName("type");
    commentField.setName("comment");
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
    formPanel.add(new JLabel("Type"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(typeField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JLabel("Comment"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    formPanel.add(commentField, configureLayout(c -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 6; c.weighty = 1; }));
    
    tabbedPane.add("1. File Type Info", new JScrollPane(formPanel));
    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(FileTypeTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
          initialTranslator.getFileTypeUUID(),
          initialTranslator.getType(),
          initialTranslator.getComment()
      };
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getFileTypeUUID());
      typeField.setSelectedItem(initialTranslator.getType());
      commentField.setSelectedItem(initialTranslator.getComment());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected FileTypeTranslator toTranslator(UUID uuid, String name) {
    return FileTypeTranslator.builder()
        .uuid(uuid)
        .name(name)
        .fileTypeUUID((String) this.uuidField.getSelectedItem())
        .type((String) typeField.getSelectedItem())
        .comment((String) commentField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(typeField, options);
    updateComboBoxModel(commentField, options);
  }
}
