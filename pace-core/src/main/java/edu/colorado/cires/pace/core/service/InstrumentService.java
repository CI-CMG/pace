package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Instrument;
import java.util.function.Consumer;

public class InstrumentService extends CRUDService<Instrument, String> {

  protected InstrumentService(CRUDRepository<Instrument, String> instrumentRepository,
      Consumer<Instrument> onSuccess, Consumer<Exception> onFailure) {
    super(instrumentRepository, onSuccess, onFailure);
  }
}
