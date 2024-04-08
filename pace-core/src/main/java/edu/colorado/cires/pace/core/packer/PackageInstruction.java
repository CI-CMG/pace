package edu.colorado.cires.pace.core.packer;

import java.nio.file.Path;

record PackageInstruction(Path source, Path target) {}
