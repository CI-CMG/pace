package edu.colorado.cires.pace.data;

import java.nio.file.Path;

public record PackingJob(
    Path temperaturePath,
    Path biologicalPath,
    Path otherPath,
    Path documentsPath,
    Path calibrationDocumentsPath,
    Path navigationPath,
    Path sourcePath
) {}
