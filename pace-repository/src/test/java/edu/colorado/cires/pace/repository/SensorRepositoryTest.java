package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioDataPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import java.util.Collections;
import java.util.UUID;

abstract class SensorRepositoryTest extends PackageDependencyRepositoryTest<Sensor> {

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Sensor> getObjectClass() {
    return Sensor.class;
  }

  @Override
  protected CRUDRepository<Sensor> createRepository() {
    return new SensorRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected Package createAndSaveDependentObject(Sensor object) {
    AudioPackage p = ((AudioPackage) PackageRepositoryTest.createAudioPackingJob(1));
    p = p.toBuilder()
        .uuid(UUID.randomUUID())
        .sensors(Collections.singletonList(PackageSensor.builder()
                .name(object.getName())
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
            .build()))
        .channels(p.getChannels().stream()
            .map(c -> c.toBuilder()
                .sensor(c.getSensor().toBuilder()
                    .name(object.getName())
                    .build())
                .build())
            .toList())
        .build();
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected boolean objectInDependentObject(Sensor updated, UUID dependentObjectUUID) {
    Package p = packages.get(dependentObjectUUID);
    
    if (p instanceof AudioDataPackage audioDataPackage) {
      return audioDataPackage.getSensors().stream().map(PackageSensor::getName).toList().contains(updated.getName()) &&
          audioDataPackage.getChannels().stream()
              .map(Channel::getSensor)
              .map(PackageSensor::getName)
              .toList().contains(updated.getName());
    }
    
    return false;
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((SoundLevelMetricsPackage) PackageRepositoryTest.createSoundLevelMetricsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}
