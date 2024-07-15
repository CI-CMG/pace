package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public abstract class DataPanel<O extends ObjectWithUniqueField> extends JPanel {
  
  protected final DefaultTableModel tableModel;
  protected final CRUDRepository<O> repository;
  protected final Function<O, Object[]> objectConversion;
  protected final String[] headers;
  private final String name;
  
  protected MouseAdapter getTableMouseAdapter() {
    return new MouseAdapter() {};
  }

  public DataPanel(
      String name,
      CRUDRepository<O> repository,
      String[] headers,
      Function<O, Object[]> objectConversion
  ) {
    this.name = name;
    this.headers = headers;
    this.objectConversion = objectConversion;
    this.repository = repository;
    this.tableModel = createTableModel(headers);
  }
  
  public void init() {
    setName(name);
    setLayout(new BorderLayout());

    add(createContentPanel());
    loadData();
  }
  
  protected DefaultTableModel createTableModel(String[] headers) {
    return new TableModel(
        null,
        headers
    );
  }
  
  private JPanel createContentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(createToolBar(), BorderLayout.NORTH);
    panel.add(new JScrollPane(createTable()), BorderLayout.CENTER);
    panel.add(createControlPanel(), BorderLayout.SOUTH);
    return panel;
  }
  
  private JToolBar createToolBar() {
    JToolBar toolBar = new JToolBar();
    JTextField textField = new JTextField();
    textField.setName("searchField");
    textField.setForeground(Color.gray);
    textField.setText(String.format(
        "Search by %s", getHumanReadableUniqueFieldName()
    ));
    textField.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (textField.getText().contains(String.format(
            "Search by %s", getHumanReadableUniqueFieldName()
        ))) {
          textField.setText("");
          textField.setForeground(Color.black);
        }
      }
      @Override
      public void focusLost(FocusEvent e) {
        if (textField.getText().isEmpty()) {
          textField.setForeground(Color.gray);
          textField.setText(String.format(
              "Search by %s", getHumanReadableUniqueFieldName()
          ));
        }
      }
    });
    toolBar.add(textField);
    toolBar.add(createSearchButton(textField));
    toolBar.addSeparator();
    toolBar.add(createClearButton(textField));
    return toolBar;
  }

  protected String getHumanReadableUniqueFieldName() {
    return repository.getUniqueFieldName();
  }

  private JButton createClearButton(JTextField textField) {
    JButton button = new JButton("Clear");
    
    button.addActionListener(e -> {
      textField.setText("");
      loadData();
    });
    
    return button;
  }
  
  private JButton createSearchButton(JTextField textField) {
    JButton button = new JButton("Search");
    
    button.addActionListener(e -> searchData(
        getSearchParameters(
            Collections.singletonList(textField.getText())
        )
    ));
    
    return button;
  }
  
  protected abstract JPanel createControlPanel();
  protected abstract SearchParameters<O> getSearchParameters(List<String> uniqueFieldSearchTerms);
  
  protected void loadData() {
    cleanUpTable();
    
    try {
      repository.findAll().forEach(
          o -> tableModel.addRow(objectConversion.apply(o))
      );
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    }
  }
  
  private void searchData(SearchParameters<O> searchParameters) {
    cleanUpTable();

    try {
      repository.search(searchParameters).forEach(
          o -> tableModel.addRow(objectConversion.apply(o))
      );
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    }
  }
  
  private void cleanUpTable() {
    while (tableModel.getRowCount() > 0) {
      tableModel.removeRow(0);
    }
  }
  
  protected List<String> getHiddenColumns() {
    return Collections.singletonList("UUID");
  }
  
  protected JTable createTable() {
    JTable table = new JTable();
    table.setName("dataTable");
    table.setModel(tableModel);
    table.setAutoCreateRowSorter(true);

    table.addMouseListener(getTableMouseAdapter());
    table.setCellSelectionEnabled(false);
    table.setRowSelectionAllowed(false);

    TableColumnModel columnModel = table.getColumnModel();
    getHiddenColumns().forEach(
        c -> columnModel.removeColumn(
            columnModel.getColumn(
                columnModel.getColumnIndex(c)
            )
        )
    );
    
    return table;
  }

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
