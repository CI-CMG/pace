package edu.colorado.cires.pace.migrator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.repository.CRUDRepository;

public class MigrationRepositoryPair<O extends ObjectWithUniqueField> {

  private final CRUDRepository<O> inputRepository;
  private final CRUDRepository<O> outputRepository;

  public MigrationRepositoryPair(CRUDRepository<O> inputRepository, CRUDRepository<O> outputRepository) {
    this.inputRepository = inputRepository;
    this.outputRepository = outputRepository;
  }

  public CRUDRepository<O> getInputRepository() {
    return inputRepository;
  }

  public CRUDRepository<O> getOutputRepository() {
    return outputRepository;
  }
}
