package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.DetectionType;
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

public class DetectionTypeForm extends Form<DetectionType> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField sourceField = new JTextField();
  private final JTextField scienceNameField = new JTextField();

  public DetectionTypeForm(DetectionType initialDetectionType) {
    setName("detectionTypeForm");
    uuidField.setName("uuid");
    sourceField.setName("source");
    scienceNameField.setName("scienceName");
    
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.add(new JLabel("UUID"), configureFormLayout(0, 0));
    contentPanel.add(uuidField, configureFormLayout(0, 1));
    contentPanel.add(new JLabel("Source"), configureFormLayout(0, 2));
    contentPanel.add(sourceField, configureFormLayout(0, 3));
    contentPanel.add(new JLabel("Science Name"), configureFormLayout(0, 4));
    contentPanel.add(scienceNameField, configureFormLayout(0, 5));
    contentPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 6; c.weighty = 1; }));
    add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    
    uuidField.setEnabled(false);
    
    initializeFields(initialDetectionType);
  }

  @Override
  protected void initializeFields(DetectionType object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      sourceField.setText(object.getSource());
      scienceNameField.setText(object.getScienceName());
    }
  }

  @Override
  protected void save(CRUDRepository<DetectionType> repository)
      throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    
    String uuidText = uuidField.getText();
    boolean update = !StringUtils.isBlank(uuidText);
    
    DetectionType detectionType = DetectionType.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .source(sourceField.getText())
        .scienceName(scienceNameField.getText())
        .build();
    
    if (update) {
      repository.update(
          detectionType.getUuid(),
          detectionType
      );
    } else {
      repository.create(detectionType);
    }
  }

  @Override
  protected void delete(CRUDRepository<DetectionType> repository) throws NotFoundException, DatastoreException, BadArgumentException {
    String uuidText = uuidField.getText();
    repository.delete(UUID.fromString(uuidText));
  }
}
