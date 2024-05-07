package edu.colorado.pace.gui.metadata.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public abstract class MetadataForm<O extends ObjectWithUniqueField> extends JDialog {

  private JPanel contentPane;
  private JButton saveButton;
  private JButton buttonCancel;
  private JPanel formPanel;
  private JButton deleteButton;
  private final ObjectForm<O> objectForm;
  private final Runnable successAction;
  
  private final CRUDRepository<O> repository;
  private final O initialObject;

  public MetadataForm(O initialObject, Runnable successAction, CRUDRepository<O> repository) {
    this.initialObject = initialObject;
    this.successAction = successAction;
    this.repository = repository;
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(saveButton);

    saveButton.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.PAGE_AXIS));
    objectForm = getFormPanel(initialObject);
    formPanel.add(objectForm.getFormPanel());
    
    deleteButton.setVisible(initialObject != null);
    deleteButton.addActionListener(e -> onDelete());
  }
  
  private void onDelete() {
    try {
      int choice = JOptionPane.showOptionDialog(this, "Delete object?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
      if (choice == JOptionPane.YES_OPTION) {
        repository.delete(initialObject.getUuid());
        successAction.run();
        dispose();
      }
    } catch (DatastoreException | NotFoundException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onOK() {
    try {
      if (initialObject == null) {
        repository.create(objectForm.fieldsToObject());
      } else {
        repository.update(initialObject.getUuid(), objectForm.fieldsToObject());
      }
      successAction.run();
      dispose();
    } catch (BadArgumentException | ConflictException | NotFoundException | DatastoreException | ConstraintViolationException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onCancel() {
    dispose();
  }
  
  protected abstract ObjectForm<O> getFormPanel(O object);
}
