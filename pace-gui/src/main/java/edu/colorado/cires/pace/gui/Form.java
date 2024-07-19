package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import javax.swing.JPanel;

public abstract class Form<O extends ObjectWithUniqueField> extends JPanel {
  
  protected abstract void initializeFields(O object);
  
  protected abstract void save(CRUDRepository<O> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException;
  
  protected abstract void delete(CRUDRepository<O> repository) throws NotFoundException, DatastoreException, BadArgumentException;

}
