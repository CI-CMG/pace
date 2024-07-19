package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.AudioDataPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.List;

public class SensorRepository extends PackageDependencyRepository<Sensor> {

  public SensorRepository(Datastore<Sensor> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Sensor object) {
    if (dependency instanceof AudioDataPackage audioDataPackage) {
      return audioDataPackage.getSensors().contains(object.getName()) ||
          audioDataPackage.getChannels().stream()
              .map(Channel::getSensor)
              .anyMatch(s -> s.equals(object.getName()));
    }
    
    return false;
  }

  @Override
  protected Package applyObjectToDependentObjects(Sensor original, Sensor updated, Package dependency) {
    AudioDataPackage audioDataPackage = (AudioDataPackage) dependency;
    List<String> sensors = replaceStringInList(audioDataPackage.getSensors(), original.getName(), updated.getName());
    List<Channel> channels = audioDataPackage.getChannels().stream()
        .map(c -> c.toBuilder()
            .sensor(replaceString(c.getSensor(), original.getName(), updated.getName()))
            .build())
        .toList();

    return audioDataPackage.updateChannels(channels)
        .updateSensors(sensors);
  }
}
