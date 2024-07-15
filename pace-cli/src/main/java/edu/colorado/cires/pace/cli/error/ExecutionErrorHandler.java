package edu.colorado.cires.pace.cli.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.migrator.MigrationException;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class ExecutionErrorHandler implements IExecutionExceptionHandler {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionErrorHandler.class);
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  @Override
  public int handleExecutionException(Exception e, CommandLine commandLine, ParseResult parseResult) throws Exception {
    java.lang.Throwable reportedException = e.getCause() == null ? e : e.getCause();
    
    LOGGER.error("Command failed: {}", reportedException.getMessage());
    
    Object errorDetail = getErrorDetail(reportedException);
    
    System.out.println(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
          new CLIError(
              reportedException.getMessage(), errorDetail
          )
    ));

    return commandLine.getCommandSpec().exitCodeOnExecutionException();
  }
  
  private Object getErrorDetail(java.lang.Throwable e) {
    if (e instanceof ConstraintViolationException) {
      return toViolations(((ConstraintViolationException) e).getConstraintViolations());
    } else if (e instanceof TranslationException) {
      Map<Integer, List<FieldException>> exceptions = Arrays.stream(e.getSuppressed())
          .map(ex -> (FieldException) ex)
          .collect(Collectors.groupingBy(
              FieldException::getRow
          ));
      
      return exceptions.entrySet().stream()
          .map(entry ->
              new TranslateRowError(
                  entry.getKey(),
                  entry.getValue().stream()
                      .map(fe -> new RowViolation(fe.getColumn(), fe.getProperty(), fe.getMessage()))
                      .toList()
              )
          ).sorted(Comparator.comparing(TranslateRowError::row)).toList();
    } else if (e instanceof PackagingException) {
      return String.format(
          "Failed to read file or directory: %s", e.getCause().getMessage()
      );
    } else if (e instanceof MigrationException) {
      return Arrays.stream(e.getSuppressed())
              .map(Throwable::getMessage)
              .toList();
    }

    return null;
  }
  
  public record RowViolation(int column, String field, String message) {}
  
  private record Violation(String field, String message) {}
  
  public record TranslateRowError(int row, List<RowViolation> violations) {}
  
  public record CLIError(String message, Object detail) {}
  
  private Set<Violation> toViolations(Set<ConstraintViolation<?>> violations) {
    return violations.stream()
        .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
        .collect(Collectors.toSet());
  }
}
