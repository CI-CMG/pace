package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioDataPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.List;

public class SensorRepository extends PackageDependencyRepository<Sensor> {

  public SensorRepository(Datastore<Sensor> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Sensor object) {
    if (dependency instanceof AudioDataPackage audioDataPackage) {
      return audioDataPackage.getSensors().stream().map(PackageSensor::getName).toList().contains(object.getName()) ||
          audioDataPackage.getChannels().stream()
              .map(Channel::getSensor)
              .anyMatch(s -> s.getName().equals(object.getName()));
    }
    
    return false;
  }

  @Override
  protected Package applyObjectToDependentObjects(Sensor original, Sensor updated, Package dependency) {
    AudioDataPackage audioDataPackage = (AudioDataPackage) dependency;
    List<PackageSensor> sensors = audioDataPackage.getSensors().stream()
        .map(s -> s.toBuilder()
            .name(replaceString(s.getName(), original.getName(), updated.getName()))
            .build())
        .toList();
    List<Channel> channels = audioDataPackage.getChannels().stream()
        .map(c -> c.toBuilder()
            .sensor(c.getSensor().toBuilder()
                .name(replaceString(c.getSensor().getName(), original.getName(), updated.getName()))
                .build())
            .build())
        .toList();

    return audioDataPackage.updateChannels(channels)
        .updateSensors(sensors);
  }
}
