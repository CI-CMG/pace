package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface RepositoryFactory<O extends AbstractObject> {
  CRUDRepository<O> createRepository(Path datastorePath, ObjectMapper objectMapper) throws IOException;
}
