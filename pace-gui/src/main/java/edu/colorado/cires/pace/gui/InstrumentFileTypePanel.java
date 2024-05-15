package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class InstrumentFileTypePanel extends JPanel {
  
  private final Map<String, FileType> fileTypeOptions = new HashMap<>(0);
  private final DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
  private final JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);

  public InstrumentFileTypePanel(FileType initialFileType, FileTypeRepository fileTypeRepository, Consumer<InstrumentFileTypePanel> fileTypePanelConsumer) throws DatastoreException {
    fileTypeOptions.putAll(fileTypeRepository.findAll().collect(
        Collectors.toMap(
            FileType::getType,
            (ft) -> ft
        )
    ));
    
    comboBoxModel.addAll(fileTypeOptions.keySet());
    
    setLayout(new GridBagLayout());
    
    add(comboBox, configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    JButton removeButton = new JButton("Remove");
    add(removeButton, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 0; }));
    
    removeButton.addActionListener((e) -> fileTypePanelConsumer.accept(this));
    
    initializeFields(initialFileType);
  }
  
  private void initializeFields(FileType initialFileType) {
    if (initialFileType != null) {
      comboBox.setSelectedItem(initialFileType.getType());
    }
  }
  
  public FileType toFileType() {
    Object selectedItem = comboBox.getSelectedItem();
    if (selectedItem != null) {
      return fileTypeOptions.get((String) selectedItem);
    }
    return null;
  }
}
