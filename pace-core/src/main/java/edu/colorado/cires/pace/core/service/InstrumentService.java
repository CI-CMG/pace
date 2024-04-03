package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentService extends CRUDService<Instrument, String> {

  protected InstrumentService(CRUDRepository<Instrument, String> instrumentRepository) {
    super(instrumentRepository);
  }
}
