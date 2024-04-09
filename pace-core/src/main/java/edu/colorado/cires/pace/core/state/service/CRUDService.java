package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CRUDService<O extends ObjectWithUniqueField> {
  
  private final CRUDRepository<O> crudRepository;

  protected CRUDService(CRUDRepository<O> crudRepository) {
    this.crudRepository = crudRepository;
  }


  public O create(O object) throws Exception {
    return crudRepository.create(object);
  }
  
  public O getByUniqueField(String uniqueField) throws Exception {
    return crudRepository.getByUniqueField(uniqueField);
  }
  
  public O getByUUID(UUID uuid) throws Exception {
    return crudRepository.getByUUID(uuid);
  }
  
  public Stream<O> readAll(List<Function<O, Boolean>> filters) throws Exception {
    return crudRepository.findAll()
        .filter(v -> {
          boolean filterValue = true;
          for (Function<O, Boolean> filter : filters) {
            filterValue = filter.apply(v);
          }
          return filterValue;
        });
  }
  
  public O update(UUID uuid, O object) throws Exception {
    return crudRepository.update(uuid, object);
  }
  
  public void delete(UUID uuid) throws Exception {
    crudRepository.delete(uuid);
  }
}
