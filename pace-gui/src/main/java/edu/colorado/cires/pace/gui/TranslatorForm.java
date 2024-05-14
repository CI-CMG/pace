package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public abstract class TranslatorForm<F extends TabularTranslationField, T extends TabularTranslator<F>> extends Form<T> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();
  private final JPanel fieldsPanel = new JPanel(new GridBagLayout());
  
  public TranslatorForm(T initialTranslator) {
    setLayout(new GridBagLayout());
    
    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));

    JPanel controlPanel = new JPanel(new GridBagLayout());
    controlPanel.add(new JLabel("Fields"), configureLayout((c) -> c.gridx = c.gridy = 0));
    controlPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JButton addFieldButton = new JButton("Add Field");
    controlPanel.add(addFieldButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; }));
    add(controlPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; }));
    add(new JScrollPane(fieldsPanel), configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weighty = 1; c.weightx = 1; c.anchor = GridBagConstraints.NORTH; }));
    
    uuidField.setEnabled(false);
    
    addFieldButton.addActionListener((e) -> addField(null));
    
    initializeFields(initialTranslator);
  }
  
  protected abstract TranslatorFieldPanel<F> createTranslatorFieldPanel(F initialField, Consumer<TranslatorFieldPanel<F>> fieldPanelConsumer);
  
  private void addField(F initialField) {
    TranslatorFieldPanel<F> panel = createTranslatorFieldPanel(initialField, (p) -> {
      fieldsPanel.remove(p);
      revalidate();
    });
    fieldsPanel.add(panel, configureLayout((c) -> { c.gridx = 0; c.gridy = fieldsPanel.getComponentCount(); c.weightx = 1; c.weighty = 0; }));

    revalidate();
  }

  @Override
  protected void initializeFields(T object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
      object.getFields().forEach(this::addField);
    }
  }
  
  protected abstract T toTranslator(String uuidText, String name, List<F> fields);

  @Override
  protected void save(CRUDRepository<T> repository)
      throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    
    T translator = toTranslator(
        uuidText,
        nameField.getText(),
        Arrays.stream(fieldsPanel.getComponents())
            .map(c -> (TranslatorFieldPanel<F>) c)
            .map(TranslatorFieldPanel::toField)
            .toList()
    );
    
    if (update) {
      repository.update(
          translator.getUuid(),
          translator
      );
    } else {
      repository.create(translator);
    }
  }

  @Override
  protected void delete(CRUDRepository<T> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    if (!StringUtils.isBlank(uuidText)) {
      repository.delete(UUID.fromString(uuidText));
    }
  }
}
