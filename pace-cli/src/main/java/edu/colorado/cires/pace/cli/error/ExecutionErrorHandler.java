package edu.colorado.cires.pace.cli.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.colorado.cires.pace.cli.command.common.BatchWriteException;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.migrator.MigrationException;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.RowConversionException;
import edu.colorado.cires.pace.translator.TranslationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    
    commandLine.getErr().println(commandLine.getColorScheme().errorText(getExceptionText(reportedException)));
    String errorDetail = getErrorDetail(reportedException);
    if (errorDetail != null) {
      commandLine.getErr().println(commandLine.getColorScheme().errorText(errorDetail));
    }

    return commandLine.getExitCodeExceptionMapper() != null
        ? commandLine.getExitCodeExceptionMapper().getExitCode(e)
        : commandLine.getCommandSpec().exitCodeOnExecutionException();
  }
  
  private String getErrorDetail(Throwable e) throws JsonProcessingException {
    ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    
    if (e instanceof ConstraintViolationException) {
      return objectWriter
          .writeValueAsString(toViolations(((ConstraintViolationException) e).getConstraintViolations()));
    } else if (e instanceof TranslationException) {
      Map<Integer, List<RowConversionException>> exceptions = Arrays.stream(e.getSuppressed())
          .map(ex -> (RowConversionException) ex)
          .collect(Collectors.groupingBy(
              RowConversionException::getRow
          ));
      
      return objectWriter.writeValueAsString(
          exceptions.entrySet().stream()
              .map(entry ->
                  new TranslateRowException(
                      entry.getKey(),
                      entry.getValue().stream()
                          .map(re -> Arrays.stream(re.getSuppressed()).toList())
                          .flatMap(List::stream)
                          .map(fe -> (FieldException) fe)
                          .map(fe -> new Violation(fe.getProperty(), fe.getMessage()))
                          .toList()
                  )
              ).toList()
      );
    } else if (e instanceof BatchWriteException || e instanceof MigrationException) {
      return objectWriter.writeValueAsString(
          Arrays.stream(e.getSuppressed())
              .map(ExecutionErrorHandler::getExceptionText)
              .toList()
      );
    }

    return null;
  }
  
  private record Violation(String field, String message) {}
  
  private record TranslateRowException(int row, List<Violation> violations) {}
  
  private Set<Violation> toViolations(Set<ConstraintViolation<?>> violations) {
    return violations.stream()
        .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
        .collect(Collectors.toSet());
  }
  
  private static String getExceptionText(Throwable throwable) {
    return throwable.getMessage();
  }
}
