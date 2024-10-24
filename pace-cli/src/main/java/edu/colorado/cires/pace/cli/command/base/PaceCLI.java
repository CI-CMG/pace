package edu.colorado.cires.pace.cli.command.base;

import edu.colorado.cires.pace.cli.command.dataset.PackageCommand;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeCommand;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeCommand;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommand;
import edu.colorado.cires.pace.cli.command.migrate.MigrateCommand;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.person.PersonCommand;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand;
import edu.colorado.cires.pace.cli.command.translator.TranslatorCommand;
import picocli.CommandLine.Command;

@Command(
    name = "pace-cli",
    mixinStandardHelpOptions = true,
    description = "Passive Acoustic Collection Engine",
    versionProvider = VersionProvider.class,
    subcommands = {
        PackageCommand.class,
        DetectionTypeCommand.class,
        FileTypeCommand.class,
        InstrumentCommand.class,
        OrganizationCommand.class,
        PersonCommand.class,
        PlatformCommand.class,
        ProjectCommand.class,
        SeaCommand.class,
        ShipCommand.class,
        SensorCommand.class,
        TranslatorCommand.class,
        MigrateCommand.class
    }
)
public class PaceCLI {}
