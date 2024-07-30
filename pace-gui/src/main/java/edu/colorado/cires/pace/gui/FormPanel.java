package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FormPanel<O extends ObjectWithUniqueField> extends JPanel {
  
  private final Form<O> form;
  private final CRUDRepository<O> repository;
  private final Consumer<Stream<O>> updatedObjectsConsumer;
  private final Runnable searchAction;
  private final boolean isEdit;
  
  public FormPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer, Runnable searchAction, boolean isEdit) {
    this.form = form;
    this.repository = repository;
    this.updatedObjectsConsumer = updatedObjectsConsumer;
    this.searchAction = searchAction;
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
        searchAction.run();
      } catch (BadArgumentException | ConflictException | NotFoundException | DatastoreException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      } catch (ConstraintViolationException ex) {
        JOptionPane.showMessageDialog(
            this,
            ex.getConstraintViolations().stream()
                .map(constraintViolation -> String.format("%s - %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
                .collect(Collectors.joining("\n")),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
      }
    });
    
    deleteButton.addActionListener((e) -> {
      try {
        int choice = form.delete(repository);
        if (choice == JOptionPane.YES_OPTION) {
          updatedObjectsConsumer.accept(
              repository.findAll()
          );
          searchAction.run();
        }
      } catch (NotFoundException | DatastoreException | BadArgumentException ex ) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      } catch (ConstraintViolationException ex) {
        JOptionPane.showMessageDialog(
            this,
            ex.getConstraintViolations().stream()
                .map(constraintViolation -> String.format("%s - %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
            .collect(Collectors.joining("\n")),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
      }
    });
    
    return panel;
  }
}
