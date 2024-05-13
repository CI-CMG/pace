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
  
  public FormPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer) {
    setLayout(new BorderLayout());
    add(form, BorderLayout.CENTER);
    add(createControlPanel(form, repository, updatedObjectsConsumer), BorderLayout.SOUTH);
  }
  
  private JPanel createControlPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JButton deleteButton = new JButton("Delete");
    panel.add(deleteButton, BorderLayout.WEST);
    JButton saveButton = new JButton("Save");
    panel.add(saveButton, BorderLayout.EAST);
    
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
      } catch (NotFoundException | DatastoreException | ConstraintViolationException ex ) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
    
    return panel;
  }
}
