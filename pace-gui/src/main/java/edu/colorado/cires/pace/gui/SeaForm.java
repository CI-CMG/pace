package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.Sea;
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

public class SeaForm extends Form<Sea> {

  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();

  public SeaForm(Sea initialSea) {
    setName("seaForm");
    uuidField.setName("uuid");
    nameField.setName("name");
    
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    contentPanel.add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    contentPanel.add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    contentPanel.add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    contentPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weighty = 1; }));

    uuidField.setEnabled(false);
    
    add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    
    initializeFields(initialSea);
  }

  @Override
  protected void initializeFields(Sea object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
    }
  }

  @Override
  protected void save(CRUDRepository<Sea> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    Sea sea = Sea.builder()
        .uuid(!update ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .build();
    if (update) {
      repository.update(
          sea.getUuid(),
          sea
      );
    } else {
      repository.create(sea);
    }
  }

  @Override
  protected void delete(CRUDRepository<Sea> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    repository.delete(UUID.fromString(uuidText));
  }
}
