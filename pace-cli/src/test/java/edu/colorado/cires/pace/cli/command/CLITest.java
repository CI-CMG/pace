package edu.colorado.cires.pace.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.base.PaceCLI;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import picocli.CommandLine;

public abstract class CLITest {

  private final InputStream in = System.in;
  private final PrintStream out = System.out;
  private final PrintStream err = System.err;

  protected final Path testPath = Paths.get("target").resolve("test-dir");
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  private final CommandLine CLI = new CommandLine(new PaceCLI())
      .setExecutionExceptionHandler(new ExecutionErrorHandler());

  protected void execute(String... arguments) {
    CLI.execute(arguments);
  }

  private final ByteArrayOutputStream commandOut = new ByteArrayOutputStream();
  private final ByteArrayOutputStream commandErr = new ByteArrayOutputStream();
  
  @BeforeEach
  public void beforeEach() throws IOException {
    FileUtils.deleteQuietly(testPath.toFile());
    FileUtils.forceMkdir(testPath.toFile());
    System.setOut(new PrintStream(commandOut));
    System.setErr(new PrintStream(commandErr));
  }
  
  @AfterEach
  public void afterEach() {
    FileUtils.deleteQuietly(testPath.toFile());
    System.setIn(in);
    System.setOut(out);
    System.setErr(err);
    commandOut.reset();
    commandErr.reset();
  }

  protected String getCommandOutput() {
    return getStreamContent(commandOut);
  }

  protected String getCommandErr() {
    return getStreamContent(commandErr);
  }

  private String getStreamContent(ByteArrayOutputStream outputStream) {
    return outputStream.toString(StandardCharsets.UTF_8);
  }

  protected void clearOut() {
    commandOut.reset();
  }

}
