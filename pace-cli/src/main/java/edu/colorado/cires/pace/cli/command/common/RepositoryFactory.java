package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.CRUDRepository;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface RepositoryFactory<O extends ObjectWithUniqueField> {
  CRUDRepository<O> createRepository(Path datastorePath, ObjectMapper objectMapper) throws IOException;
}
