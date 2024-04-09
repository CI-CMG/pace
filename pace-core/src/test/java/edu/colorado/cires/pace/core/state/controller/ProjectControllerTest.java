package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Project;
import java.util.UUID;
import java.util.function.Supplier;

class ProjectControllerTest extends CRUDControllerTest<Project> {

  @Override
  protected CRUDController<Project> createController(Datastore<Project> datastore) {
    return new ProjectController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Project createNewObject(boolean withUUID) {
    return new Project(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected Project setUniqueField(Project object, String uniqueField) {
    return new Project(
        object.uuid(),
        uniqueField
    );
  }
}
