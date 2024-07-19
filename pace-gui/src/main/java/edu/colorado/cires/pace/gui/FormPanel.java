package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FormPanel<O extends ObjectWithUniqueField> extends JPanel {
  
  private final Form<O> form;
  private final CRUDRepository<O> repository;
  private final Consumer<Stream<O>> updatedObjectsConsumer;
  private final boolean isEdit;
  
  public FormPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer, boolean isEdit) {
    this.form = form;
    this.repository = repository;
    this.updatedObjectsConsumer = updatedObjectsConsumer;
    this.isEdit = isEdit;
  }
  
  public void init() {
    setName("formPanel");
    setLayout(new BorderLayout());
    add(form, BorderLayout.CENTER);
    add(createControlPanel(form, repository, updatedObjectsConsumer, isEdit), BorderLayout.SOUTH);
  }
  
  private JPanel createControlPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer, boolean isEdit) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JButton deleteButton = new JButton("Delete");
    panel.add(deleteButton, BorderLayout.WEST);
    JButton saveButton = new JButton("Save");
    panel.add(saveButton, BorderLayout.EAST);
    
    deleteButton.setVisible(isEdit);
    
    saveButton.addActionListener((e) -> {
      try {
        form.save(repository);
        updatedObjectsConsumer.accept(
            repository.findAll()
        );
      } catch (BadArgumentException | ConflictException | NotFoundException | DatastoreException | ConstraintViolationException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
    
    deleteButton.addActionListener((e) -> {
      try {
        form.delete(repository);
        updatedObjectsConsumer.accept(
            repository.findAll()
        );
      } catch (NotFoundException | DatastoreException | ConstraintViolationException | BadArgumentException ex ) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
    
    return panel;
  }
}
