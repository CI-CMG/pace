package edu.colorado.pace.gui.metadata.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;

@FunctionalInterface
public interface CreateObject<O extends ObjectWithUniqueField> {
  O create(O object) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException;
}
