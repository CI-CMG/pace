package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.util.Arrays;
import java.util.UUID;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

abstract class MetadataFormTest<O extends ObjectWithUniqueField, F extends MetadataForm<O>> {
  
  protected abstract F createMetadataForm(O initialObject);
  protected abstract O createObject();
  protected abstract void populateAdditionalFormFields(O object, JPanel contentPanel);
  protected abstract void assertAdditionalFieldsEqual(O expected, O actual);
  
  private final CRUDRepository<O> repository = mock(CRUDRepository.class);

  @BeforeEach
  void setUp() {
    Boolean headless = ApplicationPropertyResolver.getPropertyValue("java.awt.headless", Boolean::parseBoolean);
    assertNotNull(headless);
    assertTrue(headless, "Test must run in headless mode");
    reset(repository); 
  }

  @Test
  void testCreate() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    F metadataForm = createMetadataForm(null);
    JPanel contentPanel = getContentPanel(metadataForm);
    O object = createObject();
    populateForm(object, contentPanel);

    ArgumentCaptor<O> argumentCaptor = (ArgumentCaptor<O>) ArgumentCaptor.forClass(object.getClass());
    metadataForm.save(repository);
    verify(repository).create(argumentCaptor.capture());
    assertEquals(1, argumentCaptor.getAllValues().size());
    assertObjectsEqual(object, argumentCaptor.getValue(), false);
  }
  
  @Test
  void testUpdate() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O object = createObject();
    F metadataForm = createMetadataForm(object);

    ArgumentCaptor<O> objectArgumentCaptor = (ArgumentCaptor<O>) ArgumentCaptor.forClass(object.getClass());
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    metadataForm.save(repository);
    verify(repository).update(uuidArgumentCaptor.capture(), objectArgumentCaptor.capture());
    assertEquals(1, uuidArgumentCaptor.getAllValues().size());
    assertEquals(object.getUuid(), uuidArgumentCaptor.getValue());
    assertEquals(1, objectArgumentCaptor.getAllValues().size());
    assertObjectsEqual(object, objectArgumentCaptor.getValue(), true);
  }
  
  @Test
  void testDelete() throws BadArgumentException, NotFoundException, DatastoreException {
    O object = createObject();
    F metadataForm = createMetadataForm(object);

    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
    metadataForm.delete(repository);
    verify(repository).delete(uuidArgumentCaptor.capture());
    assertEquals(1, uuidArgumentCaptor.getAllValues().size());
    assertEquals(object.getUuid(), uuidArgumentCaptor.getValue());
  }

  private void assertObjectsEqual(O expected, O actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNull(actual.getUuid());
    }
    assertEquals(expected.getUniqueField(), actual.getUniqueField());
    assertEquals(expected.isVisible(), actual.isVisible());
    
    assertAdditionalFieldsEqual(expected, actual);
  }

  private void populateForm(O object, JPanel contentPanel) {
    JTextField uuid = getNestedComponent(contentPanel, JTextField.class, "uuid");
    assertEquals(expectUUIDEnabled(), uuid.isEnabled());
    if (expectUUIDEnabled()) {
      uuid.setText(object.getUuid().toString());
    }
    
    updateTextField(contentPanel, "uniqueField", object.getUniqueField());
    updateCheckboxField(contentPanel, "visible", object.isVisible());
    
    populateAdditionalFormFields(object, contentPanel);
  }

  protected boolean expectUUIDEnabled() {
    return false;
  }

  private JPanel getContentPanel(F metadataForm) {
    JScrollPane scrollPane = getNestedComponent(metadataForm, JScrollPane.class);
    JViewport viewport = getNestedComponent(scrollPane, JViewport.class);
    return getNestedComponent(viewport, JPanel.class, "contentPanel");
  }
  
  protected void updateTextField(JComponent component, String componentName, String value) {
    getNestedComponent(component, JTextField.class, componentName)
        .setText(value);
  }
  
  private void updateCheckboxField(JComponent component, String componentName, boolean value) {
    getNestedComponent(component, JCheckBox.class, componentName)
        .setSelected(value);
  }
  
  protected <C extends JComponent> C getNestedComponent(JComponent component, Class<C> componentClass, String componentName) {
    return Arrays.stream(component.getComponents())
        .filter(c -> componentClass.isAssignableFrom(c.getClass()))
        .filter(c -> componentName.equals(c.getName()))
        .map(c -> (C) c)
        .findFirst().orElseThrow(
            () -> new IllegalStateException(String.format(
                "Component named %s not found", componentName
            ))
        );
  }
  
  protected <C extends JComponent> C getNestedComponent(JComponent component, Class<C> componentClass) {
    return Arrays.stream(component.getComponents())
        .filter(c -> componentClass.isAssignableFrom(c.getClass()))
        .map(c -> (C) c)
        .findFirst().orElseThrow(
            () -> new IllegalStateException(String.format(
                "Component of type %s not found", componentClass.getSimpleName()
            ))
        );
  }

}