package edu.colorado.cires.pace.migrator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Migrator {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Migrator.class);
  
  public static <O extends ObjectWithUniqueField> void migrate(MigrationRepositoryPair<O> migrationRepositoryPair) throws MigrationException {
    RuntimeException runtimeException;
    try {
      LOGGER.info("Migrating {} objects", migrationRepositoryPair.getInputRepository().getClassName());
      runtimeException = migrationRepositoryPair.getInputRepository().findAll().map(
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
      runtimeException = new RuntimeException(String.format(
          "Migration failed for %s", migrationRepositoryPair.getOutputRepository().getClassName()
      ));
      for (Throwable throwable : e.getCause().getSuppressed()) {
        runtimeException.addSuppressed(throwable);
      }
    }

    if (runtimeException.getSuppressed().length > 0) {
      MigrationException migrationException = new MigrationException(runtimeException.getMessage());

      for (Throwable throwable : runtimeException.getSuppressed()) {
        migrationException.addSuppressed(throwable);
      }
      
      throw migrationException;
    }
    LOGGER.info("Successfully migrated {} objects", migrationRepositoryPair.getInputRepository().getClassName());
  }

}
