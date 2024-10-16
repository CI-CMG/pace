package edu.colorado.cires.pace.migrator;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Migrator provides the migrate function which maps from one repository to
 * another, usually from a sqlite repository to a json repository
 */
public final class Migrator {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Migrator.class);

  /**
   * Maps from one repository to another
   * @param migrationRepositoryPair pair of repositories to map between
   * @param runtimeException exception to throw in case of error
   * @param <O> type of object
   */
  public static <O extends ObjectWithUniqueField> void migrate(MigrationRepositoryPair<O> migrationRepositoryPair, RuntimeException runtimeException) {
    RuntimeException rte;
    try {
      LOGGER.info("Migrating {} objects", migrationRepositoryPair.getInputRepository().getClassName());
      rte = migrationRepositoryPair.getInputRepository().findAll().map(
              (objectWithUniqueField -> {
                try {
                  migrationRepositoryPair.getOutputRepository().create(objectWithUniqueField);
                  return null;
                } catch (DatastoreException | ConflictException | NotFoundException | BadArgumentException e) {
                  LOGGER.error("Error encountered during {} objects migration: {}", migrationRepositoryPair.getInputRepository().getClassName(), e.getMessage());
                  return new RuntimeException(e);
                } catch (ConstraintViolationException e) {
                  Throwable cause = new RuntimeException(String.format(
                      "%s (%s)", e.getMessage(), e.getConstraintViolations().stream()
                              .map(v -> String.format("%s: %s", v.getPropertyPath().toString(), v.getMessage()))
                          .collect(Collectors.joining(", "))
                  ));
                  LOGGER.error("Error encountered during {} objects migration: {}", migrationRepositoryPair.getInputRepository().getClassName(), e.getMessage());
                  return new RuntimeException(cause);
                }
              })
          ).filter(Objects::nonNull)
          .reduce(new RuntimeException(String.format(
              "Migration failed for %s", migrationRepositoryPair.getOutputRepository().getClassName()
          )), (e1, e2) -> {
            e1.addSuppressed(e2.getCause());
            return e1;
          });
    } catch (DatastoreException e) {
      rte = new RuntimeException(String.format(
          "Migration failed for %s", migrationRepositoryPair.getOutputRepository().getClassName()
      ));
      for (Throwable throwable : e.getCause().getSuppressed()) {
        rte.addSuppressed(throwable);
      }
    }
    
    LOGGER.info("Migrated {} objects", migrationRepositoryPair.getInputRepository().getClassName());
    
    if (rte.getSuppressed().length > 0) {
      LOGGER.error("Migrating {} object produced {} errors", migrationRepositoryPair.getInputRepository().getClassName(), rte.getSuppressed().length);
      for (Throwable throwable : rte.getSuppressed()) {
        runtimeException.addSuppressed(throwable);
      }
    }
  }

}
