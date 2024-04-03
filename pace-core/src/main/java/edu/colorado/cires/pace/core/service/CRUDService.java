package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.core.repository.CRUDRepository;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CRUDService<O, U> {
  
  private final CRUDRepository<O, U> crudRepository;

  protected CRUDService(CRUDRepository<O, U> crudRepository) {
    this.crudRepository = crudRepository;
  }


  public O create(O object) throws IllegalArgumentException, ConflictException {
    return crudRepository.create(object);
  }
  
  public O getByUniqueField(U uniqueField) throws NotFoundException {
    return crudRepository.getByUniqueField(uniqueField);
  }
  
  public O getByUUID(UUID uuid) throws NotFoundException {
    return crudRepository.getByUUID(uuid);
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
  
  public O update(UUID uuid, O object) throws NotFoundException, IllegalArgumentException, ConflictException {
    return crudRepository.update(uuid, object);
  }
  
  public void delete(UUID uuid) throws NotFoundException {
    crudRepository.delete(uuid);
  }
}
