package edu.colorado.cires.pace.cli.command.packaging;

import static edu.colorado.cires.pace.cli.util.SerializationUtils.createObjectMapper;
import static edu.colorado.cires.pace.cli.util.SerializationUtils.deserializeBlob;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.core.exception.PackingException;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.core.packaging.PackageController;
import edu.colorado.cires.pace.data.object.PackingJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "package", description = "Package files and deployment metadata", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class PackageCommand implements Runnable {
  
  @Parameters(description = "file containing package job (- for stdin)")
  private File packageJob;
  
  @Option(names = {"--audio-data", "-ad"}, description = "Specifies that data files are audio files (defaults to ${DEFAULT-VALUE})", required = true, defaultValue = "false")
  private Boolean sourceContainsAudioData;

  @Override
  public void run() {
    try {
      ObjectMapper objectMapper = createObjectMapper();
      PackingJob packingJob = deserializeBlob(objectMapper, packageJob, PackingJob.class);
      String workDir = new ApplicationPropertyResolver().getPropertyValue("pace-cli.work-dir");

      new PackageController().process(
          packingJob,
          Path.of(workDir).resolve("output"),
          sourceContainsAudioData
      );
    } catch (IOException | ValidationException | PackingException e) {
      throw new IllegalStateException("Packaging failed", e);
    }
  }

  
}
