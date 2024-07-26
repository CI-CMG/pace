package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.detectionType.translator.DetectionTypeTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class DetectionTypeTranslatorForm extends BaseTranslatorForm<DetectionTypeTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> sourceField = new JComboBox<>();
  private final JComboBox<String> scienceNameField = new JComboBox<>();

  public DetectionTypeTranslatorForm(DetectionTypeTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }

  @Override
  protected void addUniqueFields() {
    setName("detectionTypeTranslatorForm");
    uuidField.setName("uuid");
    sourceField.setName("source");
    scienceNameField.setName("scienceName");
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Source"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(sourceField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JLabel("Science Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    formPanel.add(scienceNameField, configureLayout(c -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    formPanel.add(new JLabel(), configureLayout(c -> { c.gridx = 0; c.gridy = 6; c.weighty = 1; }));
    
    tabbedPane.add("1. Detection Type Info", new JScrollPane(formPanel));
    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(DetectionTypeTranslator initialTranslator) {
    if (initialTranslator != null) {
      headerOptions = new String[] {
          initialTranslator.getDetectionTypeUUID(),
          initialTranslator.getSource(),
          initialTranslator.getScienceName()
      };
      
      setHeaderOptions(headerOptions);
      
      uuidField.setSelectedItem(initialTranslator.getDetectionTypeUUID());
      sourceField.setSelectedItem(initialTranslator.getSource());
      scienceNameField.setSelectedItem(initialTranslator.getScienceName());
    } else {
      setHeaderOptions(getHeaderOptions());
    }
  }

  @Override
  protected DetectionTypeTranslator toTranslator(UUID uuid, String name) {
    return DetectionTypeTranslator.builder()
        .uuid(uuid)
        .name(name)
        .detectionTypeUUID((String) this.uuidField.getSelectedItem())
        .source((String) sourceField.getSelectedItem())
        .scienceName((String) scienceNameField.getSelectedItem())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(sourceField, options);
    updateComboBoxModel(scienceNameField, options);
  }
}
