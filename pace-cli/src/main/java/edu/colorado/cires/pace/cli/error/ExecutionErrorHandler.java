package edu.colorado.cires.pace.cli.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class ExecutionErrorHandler implements IExecutionExceptionHandler {
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  @Override
  public int handleExecutionException(Exception e, CommandLine commandLine, ParseResult parseResult) throws Exception {
    Throwable reportedException = e.getCause() == null ? e : e.getCause();
    
    commandLine.getErr().println(commandLine.getColorScheme().errorText(String.format(
        "%s: %s", reportedException.getClass().getSimpleName(), reportedException.getMessage()
    )));
    String errorDetail = getErrorDetail(reportedException);
    if (errorDetail != null) {
      commandLine.getErr().println(commandLine.getColorScheme().errorText(errorDetail));
    }

    return commandLine.getExitCodeExceptionMapper() != null
        ? commandLine.getExitCodeExceptionMapper().getExitCode(e)
        : commandLine.getCommandSpec().exitCodeOnExecutionException();
  }
  
  private String getErrorDetail(Throwable e) throws JsonProcessingException {
    if (e instanceof ConstraintViolationException) {
      return objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(toViolations(((ConstraintViolationException) e).getConstraintViolations()));
    }
    
    return null;
  }
  
  private record Violation(String property, String message) {}
  
  private Set<Violation> toViolations(Set<ConstraintViolation<?>> violations) {
    return violations.stream()
        .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
        .collect(Collectors.toSet());
  }
}
