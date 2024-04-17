package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class SensorRepository extends CRUDRepository<Sensor> {

  public SensorRepository(Datastore<Sensor> datastore) {
    super(datastore);
  }

  @Override
  protected Sensor setUUID(Sensor object, UUID uuid) {
    if (object instanceof DepthSensor) {
      return ((DepthSensor) object).toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof AudioSensor) {
      return ((AudioSensor) object).toBuilder()
          .uuid(uuid)
          .build();
    } else if (object instanceof OtherSensor) {
      return ((OtherSensor) object).toBuilder()
          .uuid(uuid)
          .build();
    } else {
      throw new IllegalArgumentException(String.format(
          "Unsupported sensor type: %s", object.getClass().getSimpleName()
      ));
    }
  }

}
