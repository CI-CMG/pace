package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundClips.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public abstract class MetadataForm<O extends ObjectWithUniqueField> extends Form<O> {
  
  private final JTextField uuid = new JTextField();
  private final JTextField uniqueField = new JTextField();
  private final JCheckBox visible = new JCheckBox("Visible", true);

  protected abstract O objectFromFormFields(UUID uuid, String uniqueField, boolean visible);
  protected abstract void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories);
  protected abstract void initializeAdditionalFields(O object, CRUDRepository<?>... dependencyRepositories);
  
  protected MetadataForm(O initialObject, String humanReadableUniqueFieldName, CRUDRepository<?>... dependencyRepositories) {
    createForm(initialObject, humanReadableUniqueFieldName, true, true, dependencyRepositories);
  }

  protected MetadataForm(O initialObject, String humanReadableUniqueFieldName, boolean addSpaceToFormBottom, boolean scrollableContentPanel, CRUDRepository<?>... dependencyRepositories) {
    createForm(initialObject, humanReadableUniqueFieldName, addSpaceToFormBottom, scrollableContentPanel, dependencyRepositories);
  }
  
  private void createForm(O initialObject, String humanReadableUniqueFieldName, boolean addSpaceToFormBottom, boolean scrollableContentPanel, CRUDRepository<?>... dependencyRepositories) {
    uuid.setName("uuid");
    uniqueField.setName("uniqueField");
    visible.setName("visible");
    
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.setName("contentPanel");
    contentPanel.add(new JLabel("UUID"), configureFormLayout(0, 0));
    contentPanel.add(uuid, configureFormLayout(0, 1));
    contentPanel.add(new JLabel(humanReadableUniqueFieldName), configureFormLayout(0, 2));
    contentPanel.add(uniqueField, configureFormLayout(0, 3));
    addAdditionalFields(contentPanel, dependencyRepositories);
    contentPanel.add(visible, configureFormLayout(0, 100));
    if (addSpaceToFormBottom) {
      contentPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weighty = 1; }));
    }

    JComponent component = !scrollableContentPanel ? contentPanel : new JScrollPane(contentPanel); 
    add(component, BorderLayout.CENTER);

    uuid.setEnabled(false);

    initializeFields(initialObject, dependencyRepositories);
  }

  @Override
  protected void initializeFields(O object, CRUDRepository<?>... dependencyRepositories) {
    if (object != null) {
      uuid.setText(object.getUuid().toString());
      uniqueField.setText(object.getUniqueField());
      visible.setSelected(object.isVisible());
      
      initializeAdditionalFields(object, dependencyRepositories);
    }
  }

  @Override
  protected void save(CRUDRepository<O> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuid.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    UUID uuidValue = !update ? null : UUID.fromString(uuidText);
    O object = objectFromFormFields(uuidValue, uniqueField.getText(), visible.isSelected());
    if (update) {
      repository.update(
          object.getUuid(),
          object
      );
    } else if (object instanceof AudioPackageTranslator || object instanceof CPODPackageTranslator || object instanceof DetectionsPackageTranslator || object instanceof SoundClipsPackageTranslator
        || object instanceof SoundLevelMetricsPackageTranslator || object instanceof SoundPropagationModelsPackageTranslator) {
      repository.create(object);
    } else if (object instanceof PackageTranslator) {
      throw new BadArgumentException("Please choose a package type");
    } else {
      repository.create(object);
    }
  }

  @Override
  protected void delete(CRUDRepository<O> repository) throws NotFoundException, DatastoreException, BadArgumentException {
    repository.delete(UUID.fromString(uuid.getText()));
  }
}
