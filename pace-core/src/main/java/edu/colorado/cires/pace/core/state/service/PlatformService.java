package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Platform;

public class PlatformService extends CRUDService<Platform> {

  public PlatformService(CRUDRepository<Platform> platformRepository) {
    super(platformRepository);
  }
}
