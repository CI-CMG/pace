package edu.colorado.cires.pace.cli.command.packaging;

import static edu.colorado.cires.pace.cli.util.SerializationUtils.createObjectMapper;
import static edu.colorado.cires.pace.cli.util.SerializationUtils.deserializeBlob;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.data.object.PackingJob;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import java.io.File;
import java.io.IOException;
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

      new PackageProcessor(new ObjectMapper()).process(
          packingJob,
          new ApplicationPropertyResolver().getWorkDir().resolve("output"),
          sourceContainsAudioData
      );
    } catch (IOException | PackagingException e) {
      throw new IllegalStateException("Packaging failed", e);
    }
  }

  
}
