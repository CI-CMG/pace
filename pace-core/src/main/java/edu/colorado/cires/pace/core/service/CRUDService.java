package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CRUDService<O, U> {
  
  private final CRUDRepository<O, U> crudRepository;
  
  private final Consumer<O> onSuccess;
  private final Consumer<Exception> onFailure;

  protected CRUDService(CRUDRepository<O, U> crudRepository, Consumer<O> onSuccess, Consumer<Exception> onFailure) {
    this.crudRepository = crudRepository;
    this.onSuccess = onSuccess;
    this.onFailure = onFailure;
  }


  public O create(O object) {
    try {
      O saved = crudRepository.create(object);
      onSuccess.accept(saved);
      return saved;
    } catch (Exception e) {
      onFailure.accept(e);
      throw new IllegalStateException(e);
    }
  }
  
  public O getByUniqueField(U uniqueField) {
    try {
      return crudRepository.getByUniqueField(uniqueField);
    } catch (Exception e) {
      onFailure.accept(e);
      throw new IllegalStateException(e);
    }
  }
  
  public O getByUUID(UUID uuid) {
    try {
      return crudRepository.getByUUID(uuid);
    } catch (Exception e) {
      onFailure.accept(e);
      throw new IllegalStateException(e);
    }
  }
  
  public Stream<O> readAll(List<Function<O, Boolean>> filters) {
    return crudRepository.findAll()
        .filter(v -> {
          boolean filterValue = true;
          for (Function<O, Boolean> filter : filters) {
            filterValue = filter.apply(v);
          }
          return filterValue;
        });
  }
  
  public O update(UUID uuid, O object) {
    try {
      O result = crudRepository.update(uuid, object);
      onSuccess.accept(result);
      return result;
    } catch (Exception e) {
      onFailure.accept(e);
      throw new IllegalStateException(e);
    }
  }
  
  public void delete(UUID uuid) {
    try {
      O deleted = crudRepository.delete(uuid);
      onSuccess.accept(deleted);
    } catch (Exception e) {
      handleException(e);
    }
  }
  
  private void handleException(Exception e) {
    onFailure.accept(e);
    throw new IllegalStateException(e);
  }
}
