package edu.colorado.cires.pace.packaging;

import java.nio.file.Path;

record PackageInstruction(Path source, Path target) {}
