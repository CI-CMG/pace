package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Sensor;

abstract class SensorRepositoryTest extends CrudRepositoryTest<Sensor> {

  @Override
  protected CRUDRepository<Sensor> createRepository() {
    return new SensorRepository(createDatastore());
  }
}
