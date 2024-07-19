package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
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
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class InstrumentForm extends Form<Instrument> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();
  private final JPanel fileTypesPanel = new JPanel(new GridBagLayout());
  private final FileTypeRepository fileTypeRepository;
  private final JPanel fluff = new JPanel();
  
  private final Instrument initialInstrument;

  public InstrumentForm(Instrument initialInstrument, FileTypeRepository fileTypeRepository) {
    this.initialInstrument = initialInstrument;
    this.fileTypeRepository = fileTypeRepository;
  }
  
  public void init() {
    setName("instrumentForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    fileTypesPanel.setName("fileTypeListingsPanel");

    setLayout(new GridBagLayout());

    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));

    JPanel controlPanel = new JPanel(new GridBagLayout());
    controlPanel.setName("controlPanel");
    controlPanel.add(new JLabel("File Types"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 0; }));
    controlPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JButton addFileTypeButton = new JButton("Add File Type");
    controlPanel.add(addFileTypeButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    add(controlPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(new JScrollPane(fileTypesPanel), configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH; }));

    uuidField.setEnabled(false);

    addFileTypeButton.addActionListener((e) -> addFileType(null));

    initializeFields(initialInstrument);
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
  protected void initializeFields(Instrument object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
      object.getFileTypes().forEach(this::addFileType);
    }
  }

  @Override
  protected void save(CRUDRepository<Instrument> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    Instrument instrument = Instrument.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .fileTypes(Arrays.stream(fileTypesPanel.getComponents())
            .filter(c -> c instanceof InstrumentFileTypePanel)
            .map(c -> (InstrumentFileTypePanel) c)
            .map(InstrumentFileTypePanel::toFileType)
            .map(FileType::getType)
            .filter(Objects::nonNull)
            .toList())
        .build();
    if (update) {
      repository.update(
          instrument.getUuid(),
          instrument
      );
    } else {
      repository.create(instrument);
    }
  }

  @Override
  protected void delete(CRUDRepository<Instrument> repository) throws NotFoundException, DatastoreException, BadArgumentException {
    String uuidText = uuidField.getText();

    repository.delete(UUID.fromString(uuidText));
  }
}
