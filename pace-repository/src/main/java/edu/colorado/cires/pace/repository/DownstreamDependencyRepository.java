package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.DatastoreException;
import jakarta.validation.ConstraintViolationException;

/**
 * DownstreamDependencyRepository provides structure for dependency repositories
 * @param <O>
 */
interface DownstreamDependencyRepository<O extends AbstractObject> {
  
  void checkDownstreamDependencies(O object) throws DatastoreException, ConstraintViolationException;

}
