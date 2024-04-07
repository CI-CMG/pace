package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Sea;

public class SeaService extends CRUDService<Sea, String> {

  public SeaService(CRUDRepository<Sea, String> seaRepository) {
    super(seaRepository);
  }
}
