package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class PackageDependencyRepositoryTest<O extends ObjectWithUniqueField> extends UpstreamDependencyRepositoryTest<O, Package> {
  
  protected final Map<UUID, Package> packages = new HashMap<>(0);

  @BeforeEach
  void setUp() {
    packages.clear();
  }

  @AfterEach
  void tearDown() {
    packages.clear();
  }

   @Override
   protected Class<Package> getDependentObjectClass() {
     return Package.class;
   }

   @Override
   protected String getDependentObjectUniqueFieldName() {
     return "packageId";
   }

  protected abstract Package createAndSaveIndependentDependentObject();

  @Test
  void testIndependentPackageObject() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O object = repository.create(createNewObject(1));
    Package dependentObject = createAndSaveIndependentDependentObject();

    String newUniqueField = "new-value";
    O toUpdate = copyWithUpdatedUniqueField(object, newUniqueField);
    O updated = repository.update(object.getUuid(), toUpdate);
    assertNotEquals(object.getUniqueField(), updated.getUniqueField());
    assertFalse(objectInDependentObject(updated, dependentObject.getUuid()));
  }
 }
