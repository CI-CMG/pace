package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InstrumentForm extends ObjectWithNameForm<Instrument> {
  
  private JPanel fileTypesPanel;
  private FileTypeRepository fileTypeRepository;
  private JPanel fluff;

  private InstrumentForm(Instrument initialInstrument, FileTypeRepository fileTypeRepository) {
    super(initialInstrument, false, false, fileTypeRepository);
  }
  
  public static InstrumentForm create(Instrument initialInstrument, FileTypeRepository repository) {
    return new InstrumentForm(initialInstrument, repository);
  }
  
  private void addFileType(String initialFileTypeName) {
    try {
      FileType initialFileType;
      if (initialFileTypeName == null) {
        initialFileType = null;
      } else {
        initialFileType = fileTypeRepository.getByUniqueField(initialFileTypeName);
      }
      fileTypesPanel.remove(fluff);
      InstrumentFileTypePanel instrumentFileTypePanel = new InstrumentFileTypePanel(
          initialFileType, fileTypeRepository, (p) -> {
            fileTypesPanel.remove(p);
            revalidate();
            repaint();
          }
      );
      instrumentFileTypePanel.init();
      if (initialFileType != null) {
        instrumentFileTypePanel.setName(initialFileType.getType());
      }
      fileTypesPanel.add(instrumentFileTypePanel, configureLayout((c) -> { c.gridx = 0; c.gridy = fileTypesPanel.getComponentCount(); c.weightx = 1; }));
      fileTypesPanel.add(fluff, configureLayout((c) -> { c.gridx = 0; c.gridy = fileTypesPanel.getComponentCount(); c.weightx = 1; c.weighty = 1; }));
      revalidate();
    } catch (DatastoreException | NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    fileTypeRepository = (FileTypeRepository) dependencyRepositories[0];
    
    fileTypesPanel = new JPanel(new GridBagLayout());
    fluff = new JPanel();
    
    JPanel controlPanel = new JPanel(new GridBagLayout());
    controlPanel.setName("controlPanel");
    controlPanel.add(new JLabel("File Types"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 0; }));
    controlPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JButton addFileTypeButton = new JButton("Add File Type");
    controlPanel.add(addFileTypeButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    contentPanel.add(controlPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weightx = 1; }));
    contentPanel.add(new JScrollPane(fileTypesPanel), configureLayout((c) -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; }));
    addFileTypeButton.addActionListener((e) -> addFileType(null));
  }

  @Override
  protected void initializeAdditionalFields(Instrument object, CRUDRepository<?>... dependencyRepositories) {
    object.getFileTypes().forEach(this::addFileType);
  }

  @Override
  protected Instrument objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return Instrument.builder()
        .uuid(uuid)
        .name(uniqueField)
        .visible(visible)
        .fileTypes(Arrays.stream(fileTypesPanel.getComponents())
            .filter(c -> c instanceof InstrumentFileTypePanel)
            .map(c -> (InstrumentFileTypePanel) c)
            .map(InstrumentFileTypePanel::toFileType)
            .filter(Objects::nonNull)
            .map(FileType::getType)
            .filter(Objects::nonNull)
            .toList())
        .build();
  }
}
