package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Sea;
import java.util.function.Consumer;

public class SeaService extends CRUDService<Sea, String> {

  protected SeaService(CRUDRepository<Sea, String> seaRepository, Consumer<Sea> onSuccess,
      Consumer<Exception> onFailure) {
    super(seaRepository, onSuccess, onFailure);
  }
}
