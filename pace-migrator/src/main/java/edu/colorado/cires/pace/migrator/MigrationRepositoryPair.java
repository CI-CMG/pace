package edu.colorado.cires.pace.migrator;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.repository.CRUDRepository;

/**
 * MigrationRepositoryPair provides the input and output repository
 * structure for migration between repositories
 * @param <O> Object type
 */
public class MigrationRepositoryPair<O extends ObjectWithUniqueField> {

  private final CRUDRepository<O> inputRepository;
  private final CRUDRepository<O> outputRepository;

  /**
   * Creates a migration repository pair
   * @param inputRepository input repository
   * @param outputRepository output repository
   */
  public MigrationRepositoryPair(CRUDRepository<O> inputRepository, CRUDRepository<O> outputRepository) {
    this.inputRepository = inputRepository;
    this.outputRepository = outputRepository;
  }

  /**
   * Returns the input repository
   * @return CRUDRepository inputRepository
   */
  public CRUDRepository<O> getInputRepository() {
    return inputRepository;
  }

  /**
   * Returns the output repository
   * @return CRUDRepository outputRepository
   */
  public CRUDRepository<O> getOutputRepository() {
    return outputRepository;
  }
}
