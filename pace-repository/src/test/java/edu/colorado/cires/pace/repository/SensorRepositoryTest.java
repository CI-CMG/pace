package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.AudioDataPackage;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.repository.search.SensorSearchParameters;
import java.util.Collections;
import java.util.List;
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
  protected SearchParameters<Sensor> createSearchParameters(List<Sensor> objects) {
    return SensorSearchParameters.builder()
        .names(objects.stream().map(Sensor::getName).toList())
        .build();
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
        .sensors(Collections.singletonList(object.getName()))
        .channels(p.getChannels().stream()
            .map(c -> c.toBuilder()
                .sensor(object.getName())
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
      return audioDataPackage.getSensors().contains(updated.getName()) &&
          audioDataPackage.getChannels().stream()
              .map(Channel::getSensor)
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
