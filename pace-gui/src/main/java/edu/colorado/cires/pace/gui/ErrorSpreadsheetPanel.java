package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.getImageIcon;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

/**
 * ErrorSpreadsheetPanel extends JPanel
 * @param <O> Type of error objects
 */
public class ErrorSpreadsheetPanel<O extends AbstractObject> extends JPanel {
  
  private final boolean autoResizeColumns;
  private final File file;
  private final List<ObjectWithRowError<O>> exceptions;
  private final CRUDRepository<O> repository;
  private final Runnable refreshTable;

  /**
   * Structures an error spreadsheet panel
   * @param file which exceptions are found in
   * @param exceptions exceptions to show in panel
   * @param autoResizeColumns declares whether the columns should automatically resize
   * @param repository relevant repository of data
   * @param refreshTable runnable which refreshes the table after changes
   */
  public ErrorSpreadsheetPanel(File file, List<ObjectWithRowError<O>> exceptions, boolean autoResizeColumns, CRUDRepository<O> repository, Runnable refreshTable) {
    this.file = file;
    this.exceptions = exceptions;
    this.autoResizeColumns = autoResizeColumns;
    this.repository = repository;
    this.refreshTable = refreshTable;
  }

  /**
   * Initializes the error spreadsheet panel
   */
  public void init() {
    TableData tableData = readSpreadsheet(file, exceptions);
    setLayout(new BorderLayout());
    JTable table = createTable(tableData, exceptions);
    add(new JScrollPane(table), BorderLayout.CENTER);
    JPanel overwritePanel = new JPanel(new BorderLayout());
    JButton overwriteButton = new JButton("Overwrite All Conflicts");

    overwriteButton.addActionListener((e) -> {
      exceptions.stream().forEach(o -> {
        java.lang.Throwable t = o.throwable();
        try {
          if (t instanceof ConflictException conflictException && !conflictException.checkIdentical()) {
            O object = o.object();
            try {
              O existing = repository.getByUniqueField(object.getUniqueField());
              object = (O) object.setUuid(existing.getUuid());
              repository.update(object.getUuid(), object);
              refreshTable.run();
            } catch (RuntimeException ex) {
              throw ex;
            } catch (ConflictException | NotFoundException | DatastoreException | BadArgumentException ex) {
              ;
            }
          }
        } catch (JsonProcessingException ex) {
          throw new RuntimeException(ex);
        }
      });
      for (int i = 0; i < table.getRowCount(); i++) {
        if (table.getValueAt(i, 0).toString().equals("Collision error")) {
          System.out.println("changing icon");
          table.setValueAt(getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()), i,0);
        }
      }
    });

    overwritePanel.add(overwriteButton, BorderLayout.WEST);
    add(overwritePanel, BorderLayout.SOUTH);
  }
  
  private JTable createTable(TableData tableData, List<ObjectWithRowError<O>> exceptions) {
    DefaultTableModel tableModel = new TableModel(
        tableData.data().stream()
            .map(List::toArray)
            .toArray(Object[][]::new),
        tableData.headers().toArray()
    );

    ArrayList<Point> possibleMatches = new ArrayList<Point>();
    ArrayList<Integer> possibleRows = new ArrayList<Integer>();
    ArrayList<Integer> nonFieldExceptions = new ArrayList<Integer>();

    exceptions.forEach(o -> {
      java.lang.Throwable t = o.throwable();
      if (t instanceof FieldException fieldException) {
        possibleMatches.add(new Point(fieldException.getColumn(), fieldException.getRow()));
        if (!possibleRows.contains(fieldException.getRow())) {
          possibleRows.add(fieldException.getRow());
        }
      } else if (t instanceof NotFoundException|| t instanceof DatastoreException
          || t instanceof BadArgumentException || t instanceof ConstraintViolationException) {
        if (!nonFieldExceptions.contains(o.row())) {
          nonFieldExceptions.add(o.row());
          possibleRows.add(o.row());
        }
      }
    });

    JTable table = new JTable(tableModel) {
      public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        JComponent jc = (JComponent)c;

        Collections.sort(possibleRows);
        int errorRow = 0;
        try{
          errorRow = possibleRows.get(row);
        } catch (IndexOutOfBoundsException e) {
          c.setBackground(getBackground());
          return c;
        }

        if (possibleMatches.contains(new Point(column-2, errorRow)) && !nonFieldExceptions.contains(errorRow)) {
          jc.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
        } else {
          c.setBackground(getBackground());
        }

        return c;
      }
    };
    table.setDefaultRenderer(ImageIcon.class, (table1, value, isSelected, hasFocus, row, column) -> {
      ImageIcon imageIcon = (ImageIcon) value;
      JLabel label = new JLabel(imageIcon);
      label.setToolTipText(imageIcon.getDescription());
      return label;
    });
    table.setCellSelectionEnabled(false);
    table.setRowSelectionAllowed(false);
    if (!autoResizeColumns) {
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    table.setGridColor(Color.RED);

    table.addMouseMotionListener(new MouseAdapter() {
      /**
       * Upon mouse movement, checks if the mouse now hovers over an icon and determines if
       * it should indicate that it is clickable by changing the icon
       *
       * @param e the event to be processed
       */
      @Override
      public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        int col = table.columnAtPoint(p);
        int row = table.rowAtPoint(p);
        boolean errorHover = false;

        String desc = (table.getValueAt(row,col) instanceof ImageIcon icon) ? icon.getDescription() : null;
        if (col == 0 && (Objects.equals(desc, "Package validation error") || Objects.equals(desc, "Collision error"))) {
          for (int i = 0; i < table.getRowCount(); i++) {
            String descInside = (table.getValueAt(i,0) instanceof ImageIcon icon) ? icon.getDescription() : null;
            if (i != row && Objects.equals(descInside, "Package validation error")) {
              ImageIcon imageIcon = getImageIcon("close_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
              imageIcon.setDescription("Package validation error");
              table.setValueAt(imageIcon, i, 0);
            }
            if (i != row && Objects.equals(descInside, "Collision error")) {
              ImageIcon imageIcon = getImageIcon("regular_qm.png", this.getClass());
              imageIcon.setDescription("Collision error");
              table.setValueAt(imageIcon, i, 0);
            }
          }
          if (Objects.equals(desc, "Package validation error")) {
            ImageIcon imageIcon = getImageIcon("close_hover.png", this.getClass());
            imageIcon.setDescription("Package validation error");
            table.setValueAt(imageIcon, row, 0);
          } else if (Objects.equals(desc, "Collision error")) {
            ImageIcon imageIcon = getImageIcon("hover_qm.png", this.getClass());
            imageIcon.setDescription("Collision error");
            table.setValueAt(imageIcon, row, 0);
          }
        }

        for (ObjectWithRowError exception : exceptions) {
          java.lang.Throwable t = exception.throwable();
          if (t instanceof FieldException fieldException) {
            if (fieldException.getColumn() == col-2) {
              errorHover = true;
              table.setRowSelectionAllowed(true);
              table.setColumnSelectionAllowed(true);
              table.setColumnSelectionInterval(col, col);
              table.setRowSelectionInterval(row, row);
            }
          }
        }
        if (!errorHover) {
          table.setRowSelectionAllowed(false);
          table.setColumnSelectionAllowed(false);
        }
      }
    });

    table.addMouseListener(new MouseAdapter() {
      /**
       * Upon a mouse click, checks if the cell clicked has information about
       * a specific error to display and then displays it if it does
       * @param e the event to be processed
       */
      @Override
      public void mousePressed(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        Point point = e.getPoint();
        int tableRow = table.rowAtPoint(point);
        int col = table.columnAtPoint(point);
        int row = 0;
        if (table.getValueAt(tableRow,1) instanceof AtomicInteger atom) {
          row = atom.get();
        } else {
          row = (int) table.getValueAt(tableRow, 1) - 2;
        }

        if (e.getClickCount() == 1 && table.getSelectedRow() != -1) {
          int finalRow = row;
          Optional<ObjectWithRowError<O>> error = exceptions.stream().filter(o -> {
            Throwable t = o.throwable();
            if (t instanceof FieldException fieldException) {
              return fieldException.getColumn() == (col - 2) && fieldException.getRow() == (finalRow + 2);
            } else if (t instanceof ConstraintViolationException || t instanceof NotFoundException || t instanceof ConflictException || t instanceof DatastoreException || t instanceof BadArgumentException) {
              return (finalRow + 2) == o.row() && col == 0;
            } else {
              return false;
            }
          }).findFirst();

          if (error.isPresent()) {
            ObjectWithRowError<O> objectWithRowError = error.get();
            Throwable t = objectWithRowError.throwable();
            String message;
            if (t instanceof ConstraintViolationException constraintViolationException) {
              message = constraintViolationException.getConstraintViolations().stream()
                  .map(constraintViolation -> String.format("%s - %s", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
                  .collect(Collectors.joining("\n"));
            } else if (t instanceof FieldException fieldException) {
              message = String.format(
                  "Failed to resolve %s from %s: %s",
                  fieldException.getTargetProperty(),
                  fieldException.getProperty(),
                  fieldException.getMessage()
              );
            } else {
              message = t.getMessage();
            }

            if (t instanceof ConflictException) {
              int result = JOptionPane.showConfirmDialog(ErrorSpreadsheetPanel.this, message+". Would you like to overwrite?", "Overwrite object?", JOptionPane.YES_NO_OPTION);
              if (result == JOptionPane.YES_OPTION) {
                O object = objectWithRowError.object();
                try {
                  O existing = repository.getByUniqueField(object.getUniqueField());
                  object = (O) object.setUuid(existing.getUuid());
                  repository.update(object.getUuid(), object);
                  refreshTable.run();
                } catch (RuntimeException ex) {
                  throw ex;
                } catch (ConflictException | NotFoundException | DatastoreException | BadArgumentException ex) {
                  ;
                }
                table.setValueAt(getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass()), objectWithRowError.row()-2,0);
              }
            } else {
              JOptionPane.showMessageDialog(ErrorSpreadsheetPanel.this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
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
          List<Row> rows = workbook.getFirstSheet().read().stream()
              .filter(row -> row.getFirstNonEmptyCell().isPresent())
              .toList();
          if (rows.isEmpty()) {
            yield new TableData(
                Collections.emptyList(),
                Collections.emptyList()
            );
          }
          Row row = rows.get(0);
          List<String> headerNames = new ArrayList<>(0);
          headerNames.add(0, "Status");
          headerNames.add(1, "Row");
          for (int i = 0; i < row.getCellCount(); i++) {
            headerNames.add(row.getCell(i).getText());
          }

          List<List<Object>> data = new ArrayList<>(0);
          for (int i = 1; i < rows.size(); i++) {
            boolean hopOver = false;
            Row currentRow = rows.get(i);
            List<Object> rowValues = new ArrayList<>(0);
            Throwable t = exceptions.stream()
                .filter(oObjectWithRowError -> currentRow.getRowNum() == oObjectWithRowError.row())
                .findFirst().map(ObjectWithRowError::throwable).orElse(null);
            if (t == null) {
              hopOver = true;
//              ImageIcon imageIcon = getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
//              imageIcon.setDescription("Package saved");
//              rowValues.add(0, imageIcon);
            } else if (t instanceof ConflictException conflictException){
              if (!conflictException.checkIdentical()){
                ImageIcon imageIcon = getImageIcon("regular_qm.png", this.getClass());
                imageIcon.setDescription("Collision error");
                rowValues.add(0, imageIcon);
                rowValues.add(1, i+1);
              } else {
                hopOver = true;
              }
            } else if (t instanceof NotFoundException|| t instanceof DatastoreException
                || t instanceof BadArgumentException || t instanceof ConstraintViolationException) {
              ImageIcon imageIcon = getImageIcon("close_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
              imageIcon.setDescription("Package validation error");
              rowValues.add(0, imageIcon);
              rowValues.add(1, i+1);
            } else if (t instanceof FieldException) {
              ImageIcon imageIcon = getImageIcon("exclamation_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
              imageIcon.setDescription("Spreadsheet validation error");
              rowValues.add(0, imageIcon);
              rowValues.add(1, i+1);
            } else {
              ImageIcon imageIcon = getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
              imageIcon.setDescription("Packaged");
              rowValues.add(0, imageIcon);
              rowValues.add(1, i+1);
            }
            if (!hopOver) {
              for (int j = 0; j < currentRow.getCellCount(); j++) {
                if (currentRow.getCell(j) != null) {
                  rowValues.add(currentRow.getCell(j).getText());
                } else {
                  rowValues.add("");
                }
              }
            data.add(rowValues);
            }
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
          headers.add(1, "Row");
          AtomicInteger row = new AtomicInteger(1);
          List<List<Object>> data = new ArrayList<>(parser.stream()
              .filter(record -> !record.stream().allMatch(StringUtils::isBlank))
              .map(record -> {
                List<Object> values = new ArrayList<>(record.toList());
                Throwable t = exceptions.stream()
                    .filter(oObjectWithRowError -> record.getRecordNumber() == oObjectWithRowError.row() - 1)
                    .findFirst().map(ObjectWithRowError::throwable).orElse(null);
                row.set((int) (record.getRecordNumber() + 1));
                AtomicBoolean hopOver = new AtomicBoolean(true);
                if (t == null) {
                  hopOver.set(true);
//                  ImageIcon imageIcon = getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
//                  imageIcon.setDescription("Package saved");
//                  values.add(0, imageIcon);
                } else if (t instanceof ConflictException conflictException) {
                  try {
                    if (!conflictException.checkIdentical()) {
                      ImageIcon imageIcon = getImageIcon("regular_qm.png", this.getClass());
                      imageIcon.setDescription("Collision error");
                      values.add(0, imageIcon);
                      values.add(1, row.get());
                    } else {
                      hopOver.set(true);
                    }
                  } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                  }
                } else if (t instanceof NotFoundException || t instanceof DatastoreException
                    || t instanceof BadArgumentException || t instanceof ConstraintViolationException) {
                  ImageIcon imageIcon = getImageIcon("close_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
                  imageIcon.setDescription("Package validation error");
                  values.add(0, imageIcon);
                  values.add(1, row.get());
                } else if (t instanceof FieldException) {
                  ImageIcon imageIcon = getImageIcon("exclamation_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
                  imageIcon.setDescription("Spreadsheet validation error");
                  values.add(0, imageIcon);
                  values.add(1, row.get());
                } else {
                  ImageIcon imageIcon = getImageIcon("check_20dp_FILL0_wght400_GRAD0_opsz20.png", this.getClass());
                  imageIcon.setDescription("Packaged");
                  values.add(0, imageIcon);
                  values.add(1, row.get());
                }
                if (!hopOver.get()) {
                  return values;
                }
                return null;
              }).toList());

          data.removeAll(Collections.singleton(null));
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
