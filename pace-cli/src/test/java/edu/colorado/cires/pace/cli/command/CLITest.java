package edu.colorado.cires.pace.cli.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.base.PaceCLI;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler.CLIError;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import picocli.CommandLine;

public abstract class CLITest {

  private final PrintStream out = System.out;

  protected final Path testPath = Paths.get("target").resolve("test-dir");
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  private final CommandLine CLI = new CommandLine(new PaceCLI())
      .setExecutionExceptionHandler(new ExecutionErrorHandler());

  protected void execute(String... arguments) {
    CLI.execute(arguments);
  }

  private final ByteArrayOutputStream commandOut = new ByteArrayOutputStream();
  
  @BeforeEach
  public void beforeEach() throws IOException {
    System.setProperty("pace.metadata-directory", testPath.resolve("test-metadata").toAbsolutePath().toString());
    FileUtils.deleteQuietly(testPath.toFile());
    FileUtils.forceMkdir(testPath.toFile());
    FileUtils.forceMkdir(testPath.resolve("test-metadata").toFile());
    System.setOut(new PrintStream(commandOut));
  }
  
  @AfterEach
  public void afterEach() {
    FileUtils.deleteQuietly(testPath.toFile());
    System.setOut(out);
    commandOut.reset();
  }
  
  protected CLIError getCLIException() throws JsonProcessingException {
    return objectMapper.readValue(getCommandOutput(), CLIError.class);
  }

  protected String getCommandOutput() {
    return commandOut.toString(StandardCharsets.UTF_8);
  }

  protected void clearOut() {
    commandOut.reset();
  }

}
