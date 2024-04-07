package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentService extends CRUDService<Instrument, String> {

  public InstrumentService(CRUDRepository<Instrument, String> instrumentRepository) {
    super(instrumentRepository);
  }
}
