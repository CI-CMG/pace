package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import jakarta.validation.ConstraintViolationException;

interface DownstreamDependencyRepository<O extends ObjectWithUniqueField> {
  
  void checkDownstreamDependencies(O object) throws DatastoreException, ConstraintViolationException;

}
