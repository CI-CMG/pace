package edu.colorado.cires.pace.cli.command.packaging;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.JsonBlobCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.core.packaging.PackageController;
import edu.colorado.cires.pace.data.PackingJob;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "package", description = "Package files and deployment metadata", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class PackageCommand extends JsonBlobCommand<PackingJob, Void> {
  
  @Parameters(description = "file containing package job (- for stdin)")
  private File packageJob;
  
  @Option(names = {"--audio-data", "-ad"}, description = "Specifies that data files are audio files (defaults to ${DEFAULT-VALUE})", required = true, defaultValue = "false")
  private Boolean sourceContainsAudioData;

  @Override
  protected ControllerFactory<PackingJob, Void> getControllerFactory() {
    return null;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> packageJob;
  }

  @Override
  protected Class<PackingJob> getJsonClass() {
    return PackingJob.class;
  }

  @Override
  protected PackingJob runCommandWithDeserializedObject(PackingJob object) throws Exception {
    String workDir = new ApplicationPropertyResolver().getPropertyValue("pace-cli.work-dir");
    
    new PackageController().process(
        object,
        Path.of(workDir).resolve("output"),
        sourceContainsAudioData
    );
    
    return object;
  }
}
