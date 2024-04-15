package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Sensor;
import java.util.function.Function;

abstract class SensorRepositoryTest extends CrudRepositoryTest<Sensor> {

  @Override
  protected CRUDRepository<Sensor> createRepository() {
    return new SensorRepository(createDatastore());
  }

  @Override
  protected Function<Sensor, String> uniqueFieldGetter() {
    return Sensor::getName;
  }
}
