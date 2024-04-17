package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.SoundSource;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class SoundSourceRepository extends CRUDRepository<SoundSource> {

  public SoundSourceRepository(Datastore<SoundSource> datastore) {
    super(datastore);
  }

  @Override
  protected SoundSource setUUID(SoundSource object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }
}
