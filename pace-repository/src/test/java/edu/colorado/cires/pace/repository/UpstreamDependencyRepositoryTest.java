package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

abstract class UpstreamDependencyRepositoryTest<O extends AbstractObject, D extends AbstractObject> extends CrudRepositoryTest<O> {
  protected abstract Class<D> getDependentObjectClass();

  protected String getDependentObjectUniqueFieldName() {
    return "name";
  }

  protected abstract boolean objectInDependentObject(O updated, UUID dependentObjectUUID);

  protected abstract D createAndSaveDependentObject(O object);

  @Test
  void testDeleteAttachedToDependencies() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O object = repository.create(createNewObject(1));
    D dependentObject = createAndSaveDependentObject(object);
    
    Exception exception = assertThrows(BadArgumentException.class, () -> repository.delete(object.getUuid()));
    assertEquals(String.format(
        "%s with %s = %s depends on %s with %s = %s. %s cannot be deleted",
        getDependentObjectClass().getSimpleName(),
        getDependentObjectUniqueFieldName(),
        dependentObject.getUniqueField(),
        repository.getClassName(),
        repository.getUniqueFieldName(),
        object.getUniqueField(),
        repository.getClassName()
    ), exception.getMessage());
  }

  @Test
  void testUpdateCascadeDependencies() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O object = repository.create(createNewObject(1));
    D dependentObject = createAndSaveDependentObject(object);

    String newUniqueField = "new-value";
    O toUpdate = copyWithUpdatedUniqueField(object, newUniqueField);
    O updated = repository.update(object.getUuid(), toUpdate);
    assertNotEquals(object.getUniqueField(), updated.getUniqueField());
    assertTrue(objectInDependentObject(updated, dependentObject.getUuid()));
  }
  
  @Test
  void testUpdateIndependentObjects() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O object1 = repository.create(createNewObject(1));
    O object2 = repository.create(createNewObject(2));
    D dependentObject = createAndSaveDependentObject(object2);

    String newUniqueField = "new-value";
    O toUpdate = copyWithUpdatedUniqueField(object1, newUniqueField);
    O updated = repository.update(object1.getUuid(), toUpdate);
    assertNotEquals(object1.getUniqueField(), updated.getUniqueField());
    assertFalse(objectInDependentObject(updated, dependentObject.getUuid()));
  }
}
