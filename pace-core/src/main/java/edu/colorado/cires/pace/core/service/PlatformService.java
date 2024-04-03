package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Platform;
import java.util.function.Consumer;

public class PlatformService extends CRUDService<Platform, String> {

  protected PlatformService(CRUDRepository<Platform, String> platformRepository,
      Consumer<Platform> onSuccess, Consumer<Exception> onFailure) {
    super(platformRepository, onSuccess, onFailure);
  }
}
