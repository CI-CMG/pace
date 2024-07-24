package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.repository.CRUDRepository;
import javax.swing.JPanel;

public abstract class ObjectWithNameForm<O extends ObjectWithName> extends MetadataForm<O> {

  public ObjectWithNameForm(O initialObject, CRUDRepository<?>... dependencyRepositories) {
    super(initialObject, "Name", dependencyRepositories);
  }
  
  public ObjectWithNameForm(O initialObject, boolean addSpaceToFormBottom, CRUDRepository<?>... dependencyRepositories) {
    super(initialObject, "Name", addSpaceToFormBottom, dependencyRepositories);
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    
  }

  @Override
  protected void initializeAdditionalFields(O object, CRUDRepository<?>... dependencyRepositories) {

  }
}