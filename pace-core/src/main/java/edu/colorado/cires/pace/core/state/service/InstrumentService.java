package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentService extends CRUDService<Instrument> {

  public InstrumentService(CRUDRepository<Instrument> instrumentRepository) {
    super(instrumentRepository);
  }
}
