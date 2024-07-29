package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.getImageIcon;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.ObjectWithRowError;
import edu.colorado.cires.pace.utilities.TranslationType;
import jakarta.validation.ConstraintViolationException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public class ErrorSpreadsheetPanel<O extends AbstractObject> extends JPanel {
  
  private final boolean autoResizeColumns;
  private final File file;
  private final List<ObjectWithRowError<O>> exceptions;

  public ErrorSpreadsheetPanel(File file, List<ObjectWithRowError<O>> exceptions, boolean autoResizeColumns) {
    this.file = file;
    this.exceptions = exceptions;
    this.autoResizeColumns = autoResizeColumns;
  }
  
  public void init() {
    TableData tableData = readSpreadsheet(file, exceptions);
    setLayout(new BorderLayout());
    add(new JScrollPane(createTable(tableData, exceptions)), BorderLayout.CENTER);
  }
  
  private JTable createTable(TableData tableData, List<ObjectWithRowError<O>> exceptions) {
    DefaultTableModel tableModel = new TableModel(
        tableData.data().stream()
            .map(List::toArray)
            .toArray(Object[][]::new),
        tableData.headers().toArray()
    );
    JTable table = new JTable(tableModel) {
      public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent)c;

        exceptions.forEach(o -> {
          java.lang.Throwable t = o.throwable();
          if (t instanceof FieldException fieldException) {
            if (fieldException.getColumn() == (column - 1) && fieldException.getRow() == (row + 2)) {
              jc.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
            }
          } else {
            c.setBackground(getBackground());
          }
        });

        return c;
      }
    };
    table.setCellSelectionEnabled(false);
    table.setRowSelectionAllowed(false);
    if (!autoResizeColumns) {
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    table.setGridColor(Color.RED);
    
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);
        int col = table.columnAtPoint(point);
        if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
          Set<String> messages = exceptions.stream().filter(o -> {
            java.lang.Throwable t = o.throwable();
                if (t instanceof FieldException fieldException) {
                  return fieldException.getColumn() == (col - 1) && fieldException.getRow() == (row + 2);
                } else if (t instanceof ConstraintViolationException || t instanceof NotFoundException || t instanceof ConflictException || t instanceof DatastoreException || t instanceof BadArgumentException) {
                  return (row + 2) == o.row() && col == 0;
                } else {
                  return false;
                }
              }).map(ObjectWithRowError::throwable).map((t) -> {
                if (t instanceof ConstraintViolationException constraintViolationException) {
                  return constraintViolationException.getConstraintViolations().stream()
                      .map(constraintViolation -> String.format("%s - %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
                      .collect(Collectors.joining("\n"));
                }
                
                return t.getMessage();
              })
              .collect(Collectors.toSet());

          if (!messages.isEmpty()) {
            JOptionPane.showMessageDialog(ErrorSpreadsheetPanel.this, messages.iterator().next(), "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });
    
    return table;
  }
  
  private TableData readSpreadsheet(File file, List<ObjectWithRowError<O>> exceptions) {
    TranslationType fileType = file.getName().endsWith("csv") ? TranslationType.csv : TranslationType.excel;
    
    return switch (fileType) {
      case excel -> {
        try (InputStream inputStream = new FileInputStream(file); ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
          List<Row> rows = workbook.getFirstSheet().read();
          if (rows.isEmpty()) {
            yield new TableData(
                Collections.emptyList(),
                Collections.emptyList()
            );
          }
          Row row = rows.get(0);
          List<String> headerNames = new ArrayList<>(0);
          headerNames.add(0, "Status");
          for (int i = 0; i < row.getCellCount(); i++) {
            headerNames.add(row.getCell(i).getText());
          }
          
          List<List<Object>> data = new ArrayList<>(0);
          for (int i = 1; i < rows.size(); i++) {
            Row currentRow = rows.get(i);
            List<Object> rowValues = new ArrayList<>(0);
            Throwable t = exceptions.stream()
                .filter(oObjectWithRowError -> currentRow.getRowNum() == oObjectWithRowError.row() - 1)
                .findFirst().map(ObjectWithRowError::throwable).orElse(null);
            if (t == null) {
              rowValues.add(0, getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()));
            } else if (t instanceof NotFoundException || t instanceof ConflictException || t instanceof DatastoreException
                || t instanceof BadArgumentException || t instanceof ConstraintViolationException) {
              rowValues.add(0, getImageIcon("close_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()));
            } else if (t instanceof FieldException) {
              rowValues.add(0, getImageIcon("exclamation_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()));
            } else {
              rowValues.add(0, null);
            }
            for (int j = 0; j < currentRow.getCellCount(); j++) {
              rowValues.add(currentRow.getCell(j).getText());
            }
            data.add(rowValues);
          }
          
          yield new TableData(
              headerNames,
              data
          );
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      case csv -> {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader()
            .build();
        
        try (InputStream inputStream = new FileInputStream(file); Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8) ) {
          CSVParser parser = format.parse(reader);
          
          List<String> headers = new ArrayList<>(parser.getHeaderNames());
          headers.add(0, "Status");
          List<List<Object>> data = parser.stream()
              .map(record -> {
                List<Object> values  = new ArrayList<>(record.toList());
                Throwable t = exceptions.stream()
                    .filter(oObjectWithRowError -> record.getRecordNumber() == oObjectWithRowError.row() - 1)
                    .findFirst().map(ObjectWithRowError::throwable).orElse(null);
                if (t == null) {
                  values.add(0, getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()));
                } else if (t instanceof NotFoundException || t instanceof ConflictException || t instanceof DatastoreException 
                    || t instanceof BadArgumentException || t instanceof ConstraintViolationException) {
                  values.add(0, getImageIcon("close_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()));
                } else if (t instanceof FieldException) {
                  values.add(0, getImageIcon("exclamation_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()));
                } else {
                  values.add(0, null);
                }
                return values;
              }).toList();
          
          yield new TableData(headers, data);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
  
  private record TableData(List<String> headers, List<List<Object>> data) {}

  protected static class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      if (columnIndex == 0) {
        return ImageIcon.class;
      }
      return super.getColumnClass(columnIndex);
    }
  }
}
