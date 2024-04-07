package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.core.state.controller.CRUDController;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class CRUDCommand<O, U> implements Runnable {
  protected final ObjectMapper objectMapper = new ObjectMapper();
  protected abstract ControllerFactory<O, U> getControllerFactory(); 
  
  protected CRUDController<O, U> createController() throws IOException {
    return getControllerFactory().createController(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  protected abstract Object runCommand() throws Exception;

  private Path getDatastoreDirectory() {
    return Paths.get(
        new ApplicationPropertyResolver().getPropertyValue("pace-cli.work-dir")
    ).toAbsolutePath();
  }
  
  @Override
  public void run() {
    try {
      System.out.println(
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            runCommand()
          )
      );
    } catch (Exception e) {
      throw new IllegalStateException("Command failed", e);
    }
  }
}
