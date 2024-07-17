package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class PackageRepository extends CRUDRepository<Package> {

  public PackageRepository(Datastore<Package> datastore) {
    super(datastore);
  }

  @Override
  protected Package setUUID(Package object, UUID uuid) throws BadArgumentException {
    if (object instanceof AudioPackage) {
      object = ((AudioPackage) object).toBuilder()
        .uuid(uuid)
        .build();
    } else if (object instanceof CPODPackage) {
      object = ((CPODPackage) object).toBuilder()
        .uuid(uuid)
        .build();
    } else if (object instanceof DetectionsPackage) {
      object = ((DetectionsPackage) object).toBuilder()
        .uuid(uuid)
        .build();
    } else if (object instanceof SoundClipsPackage) {
      object = ((SoundClipsPackage) object).toBuilder()
        .uuid(uuid)
        .build();
    } else if (object instanceof SoundLevelMetricsPackage) {
      object = ((SoundLevelMetricsPackage) object).toBuilder()
        .uuid(uuid)
        .build();
    } else if (object instanceof SoundPropagationModelsPackage) {
      object = ((SoundPropagationModelsPackage) object).toBuilder()
        .uuid(uuid)
        .build();
    } else {
      throw new BadArgumentException(String.format(
          "Unsupported package type: %s", object.getClass().getSimpleName()
      ));
    }
    
    return object;
  }
}
