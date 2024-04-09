package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.CRUDController;
import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface ControllerFactory<O extends ObjectWithUniqueField> {
  CRUDController<O> createController(Path datastorePath, ObjectMapper objectMapper) throws IOException;
}
