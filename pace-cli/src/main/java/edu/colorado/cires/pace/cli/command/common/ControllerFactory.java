package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.CRUDController;
import java.io.IOException;
import java.nio.file.Path;

@FunctionalInterface
public interface ControllerFactory<O, U> {
  CRUDController<O, U> createController(Path datastorePath, ObjectMapper objectMapper) throws IOException;
}
