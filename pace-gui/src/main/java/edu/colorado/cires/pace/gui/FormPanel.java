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
  private final boolean isTranslator;
  
  public FormPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer, Runnable searchAction, boolean isEdit, boolean isTranslator) {
    this.form = form;
    this.repository = repository;
    this.updatedObjectsConsumer = updatedObjectsConsumer;
    this.searchAction = searchAction;
    this.isEdit = isEdit;
    this.isTranslator = isTranslator;
  }
  
  public void init() {
    setName("formPanel");
    setLayout(new BorderLayout());
    add(form, BorderLayout.CENTER);
    add(createControlPanel(form, repository, updatedObjectsConsumer, isEdit, isTranslator), BorderLayout.SOUTH);
  }
  
  private JPanel createControlPanel(Form<O> form, CRUDRepository<O> repository, Consumer<Stream<O>> updatedObjectsConsumer, boolean isEdit, boolean isTranslator) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JPanel subPanel = new JPanel();

    JButton copyButton = new JButton("Copy");

    JButton deleteButton = new JButton("Delete");
    subPanel.add(deleteButton);
    if (isTranslator) {
      subPanel.add(copyButton);
    }
    panel.add(subPanel, BorderLayout.WEST);

    JButton saveButton = new JButton("Save");
    panel.add(saveButton, BorderLayout.EAST);
    
    deleteButton.setVisible(isEdit);
    if (isTranslator) {
      copyButton.setVisible(isEdit);
    }

    copyButton.addActionListener((e) -> {
      try {
        form.saveCopy(repository);
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
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to proceed? This action cannot be undone.", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
          form.delete(repository);
          
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
