package edu.colorado.cires.pace.migrator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Migrator {
  
  public static <O extends ObjectWithUniqueField> void migrate(MigrationRepositoryPair<O> migrationRepositoryPair) throws MigrationException {
    RuntimeException runtimeException;
    try {
      runtimeException = migrationRepositoryPair.getInputRepository().findAll().map(
              (objectWithUniqueField -> {
                try {
                  migrationRepositoryPair.getOutputRepository().create(objectWithUniqueField);
                  return null;
                } catch (DatastoreException | ConflictException | NotFoundException | BadArgumentException e) {
                  return new RuntimeException(e);
                } catch (ConstraintViolationException e) {
                  Throwable cause = new RuntimeException(String.format(
                      "%s (%s)", e.getMessage(), e.getConstraintViolations().stream()
                              .map(v -> String.format("%s: %s", v.getPropertyPath().toString(), v.getMessage()))
                          .collect(Collectors.joining(", "))
                  ));
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
  }

}
