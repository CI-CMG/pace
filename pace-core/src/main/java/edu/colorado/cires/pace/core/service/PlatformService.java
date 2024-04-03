package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Platform;

public class PlatformService extends CRUDService<Platform, String> {

  protected PlatformService(CRUDRepository<Platform, String> platformRepository) {
    super(platformRepository);
  }
}
