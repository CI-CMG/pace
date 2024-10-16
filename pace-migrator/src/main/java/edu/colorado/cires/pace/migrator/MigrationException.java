package edu.colorado.cires.pace.migrator;

/**
 * MigrationException extends Exception and is thrown during
 * the migration between one repository to another in case of an error
 */
public class MigrationException extends Exception {

  /**
   * Migration Exception has standard error format with
   * new title
   * @param message message to attach to error
   */
  public MigrationException(String message) {
    super(message);
  }
}
