package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.ProjectConverter;
import java.util.UUID;

public class ProjectsPanel extends MetadataPanel<Project, ProjectTranslator> {

  public ProjectsPanel(CRUDRepository<Project> repository,
      TranslatorRepository translatorRepository) {
    super(
        "projectsPanel",
        repository,
        new String[]{ "UUID", "Name", "Visible"},
        (project -> new Object[] {project.getUuid(), project.getName(), project.isVisible()}),
        Project.class,
        (o) -> Project.builder()
          .uuid((UUID) o[0])
          .name((String) o[1])
          .visible((Boolean) o[2])
          .build(),
        ProjectForm::new,
        translatorRepository,
        new ProjectConverter(),
        ProjectTranslator.class
    );
  }

  @Override
  protected String getUniqueField(Project object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Project";
  }
}
