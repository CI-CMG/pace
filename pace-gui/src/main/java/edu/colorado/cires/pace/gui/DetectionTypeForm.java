package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;

import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DetectionTypeForm extends MetadataForm<DetectionType> {
  
  private JTextField scienceName;

  protected DetectionTypeForm(DetectionType initialObject) {
    super(initialObject, "Source");
  }

  @Override
  protected DetectionType objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return DetectionType.builder()
        .uuid(uuid)
        .source(uniqueField)
        .scienceName(scienceName.getText())
        .visible(visible)
        .build();
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    scienceName = new JTextField();
    scienceName.setName("scienceName");
    
    contentPanel.add(new JLabel("Science Name"), configureFormLayout(0, contentPanel.getComponentCount()));
    contentPanel.add(scienceName, configureFormLayout(0, contentPanel.getComponentCount()));
  }

  @Override
  protected void initializeAdditionalFields(DetectionType object, CRUDRepository<?>... dependencyRepositories) {
    scienceName.setText(object.getScienceName());
  }
}
