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

  public InstrumentForm(Instrument initialInstrument, FileTypeRepository fileTypeRepository) {
    this.fileTypeRepository = fileTypeRepository;
    
    setLayout(new GridBagLayout());
    
    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    
    JPanel fileTypesControlPanel = new JPanel(new GridBagLayout());
    fileTypesControlPanel.add(new JLabel("File Types"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 0; }));
    fileTypesControlPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JButton addFileTypeButton = new JButton("Add File Type");
    fileTypesControlPanel.add(addFileTypeButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    fileTypesControlPanel.add(new JScrollPane(fileTypesPanel), configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1; c.anchor = GridBagConstraints.NORTH; c.gridwidth = GridBagConstraints.REMAINDER; }));
    add(fileTypesControlPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; c.weighty = 1; c.anchor = GridBagConstraints.NORTH; }));
    
    uuidField.setEnabled(false);
    
    addFileTypeButton.addActionListener((e) -> addFileType(null));
    
    initializeFields(initialInstrument);
  }
  
  private void addFileType(FileType initialFileType) {
    try {
      fileTypesPanel.add(new InstrumentFileTypePanel(
          initialFileType, fileTypeRepository, (p) -> {
            fileTypesPanel.remove(p);
            revalidate();
          }
      ), configureLayout((c) -> { c.gridx = 0; c.gridy = fileTypesPanel.getComponentCount(); c.weightx = 1; }));
      revalidate();
    } catch (DatastoreException e) {
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
            .map(c -> (InstrumentFileTypePanel) c)
            .map(InstrumentFileTypePanel::toFileType)
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
  protected void delete(CRUDRepository<Instrument> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    if (!StringUtils.isBlank(uuidText)) {
      repository.delete(UUID.fromString(uuidText));
    }
  }
}
