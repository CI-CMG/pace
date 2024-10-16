package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import javax.swing.JPanel;

/**
 * Form extends JPanel and provides the structure for forms in pace
 * @param <O>
 */
public abstract class Form<O extends ObjectWithUniqueField> extends JPanel {
  
  protected abstract void initializeFields(O object, CRUDRepository<?>... dependencyRepositories);
  
  protected abstract void save(CRUDRepository<O> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException;
  
  protected abstract void delete(CRUDRepository<O> repository) throws NotFoundException, DatastoreException, BadArgumentException;

  protected abstract void saveCopy(CRUDRepository<O> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException;
}
