package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.repository.search.SensorSearchParameters;
import java.util.List;

abstract class SensorRepositoryTest extends CrudRepositoryTest<Sensor> {

  @Override
  protected SearchParameters<Sensor> createSearchParameters(List<Sensor> objects) {
    return SensorSearchParameters.builder()
        .names(objects.stream().map(Sensor::getName).toList())
        .build();
  }

  @Override
  protected CRUDRepository<Sensor> createRepository() {
    return new SensorRepository(createDatastore());
  }
}
