package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.RowConversionException;
import edu.colorado.cires.pace.utilities.TranslationType;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

public class ErrorSpreadsheetPanel extends JPanel {
  
  private final boolean autoResizeColumns;

  public ErrorSpreadsheetPanel(File file, List<Throwable> exceptions, boolean autoResizeColumns) {
    this.autoResizeColumns = autoResizeColumns;
    TableData tableData = readSpreadsheet(file);
    setLayout(new BorderLayout());
    add(new JScrollPane(createTable(tableData, exceptions)), BorderLayout.CENTER);
  }
  
  private JTable createTable(TableData tableData, List<Throwable> exceptions) {
    DefaultTableModel tableModel = new TableModel(
        tableData.data().stream()
            .map(List::toArray)
            .toArray(Object[][]::new),
        tableData.headers().toArray()
    );
    JTable table = new JTable(tableModel) {
      public Class<?> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
      }

      public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent)c;

        if (exceptions.stream().noneMatch(t -> {
          if (t instanceof FieldException fieldException) {
            return fieldException.getColumn() == column && fieldException.getRow() == row;
          } else if (t instanceof RowConversionException rowConversionException) {
            return (rowConversionException.getRow() - 1) == row;
          } else {
            return false;
          }
        })) {
          c.setBackground(getBackground());
        } else {
          jc.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED) );
        }

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
          String message = exceptions.stream().filter(t -> {
                if (t instanceof FieldException fieldException) {
                  return fieldException.getColumn() == col && fieldException.getRow() == row;
                } else if (t instanceof RowConversionException rowConversionException) {
                  return (rowConversionException.getRow() - 1) == row;
                } else {
                  return false;
                }
              }).map(Throwable::getMessage)
              .collect(Collectors.joining());

          if (!StringUtils.isBlank(message)) {
            JOptionPane.showMessageDialog(ErrorSpreadsheetPanel.this, message, "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });
    
    return table;
  }
  
  private TableData readSpreadsheet(File file) {
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
          for (int i = 0; i < row.getCellCount(); i++) {
            headerNames.add(row.getCell(i).getText());
          }
          
          List<List<String>> data = new ArrayList<>(0);
          for (int i = 1; i < rows.size(); i++) {
            Row currentRow = rows.get(i);
            List<String> rowValues = new ArrayList<>(0);
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
        
        try (InputStream inputStream = new FileInputStream(file); Reader reader = new InputStreamReader(inputStream) ) {
          CSVParser parser = format.parse(reader);
          yield new TableData(
              parser.getHeaderNames(),
              parser.stream()
                  .map(CSVRecord::toList)
                  .toList()
          );
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
  
  private record TableData(List<String> headers, List<List<String>> data) {}

  protected static class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }
}
