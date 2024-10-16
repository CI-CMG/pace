package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import edu.colorado.cires.pace.repository.CRUDRepository;
import javax.swing.JPanel;

/**
 * ObjectWithNameForm extends MetadataForm
 * @param <O> Object type
 */
public abstract class ObjectWithNameForm<O extends ObjectWithName> extends MetadataForm<O> {

  /**
   * Creates an object with name form
   * @param initialObject object to build upon
   * @param dependencyRepositories holds repositories relevant to form type
   */
  public ObjectWithNameForm(O initialObject, CRUDRepository<?>... dependencyRepositories) {
    super(initialObject, "Name", dependencyRepositories);
  }

  /**
   * Creates an object with name form with formating booleans taken in
   * @param initialObject object to build upon
   * @param addSpaceToFormBottom indicates spacing at the bottom of the form
   * @param scrollableContentForm indicates the scroll-ability of the form
   * @param dependencyRepositories holds repositories relevant to form type
   */
  public ObjectWithNameForm(O initialObject, boolean addSpaceToFormBottom, boolean scrollableContentForm, CRUDRepository<?>... dependencyRepositories) {
    super(initialObject, "Name", addSpaceToFormBottom, scrollableContentForm, dependencyRepositories);
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    
  }

  @Override
  protected void initializeAdditionalFields(O object, CRUDRepository<?>... dependencyRepositories) {

  }
}
