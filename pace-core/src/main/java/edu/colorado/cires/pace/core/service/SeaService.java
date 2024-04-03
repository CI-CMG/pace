package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Sea;

public class SeaService extends CRUDService<Sea, String> {

  protected SeaService(CRUDRepository<Sea, String> seaRepository) {
    super(seaRepository);
  }
}
