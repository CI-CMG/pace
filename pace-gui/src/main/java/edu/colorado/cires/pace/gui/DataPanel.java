package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.lang3.StringUtils;

public abstract class DataPanel<O extends AbstractObject> extends JPanel {
  
  protected final DefaultTableModel tableModel;
  protected final CRUDRepository<O> repository;
  protected final Function<O, Object[]> objectConversion;
  protected final String[] headers;
  private final String name;
  
  private JTextField uniqueFieldSearchField;
  private JCheckBox visibilitySearchField;
  
  private final Map<String, TableColumn> hiddenColumns = new HashMap<>(0);
  
  private JTable table;
  
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

    this.table = createTable();
    
    add(createContentPanel());
    searchData();
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
    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    panel.add(createControlPanel(), BorderLayout.SOUTH);
    return panel;
  }
  
  protected JComponent createToolBar() {
    JToolBar toolBar = new JToolBar();
    uniqueFieldSearchField = new JTextField();
    uniqueFieldSearchField.setName("searchField");
    uniqueFieldSearchField.setForeground(Color.gray);
    uniqueFieldSearchField.setText(String.format(
        "Search by %s", getHumanReadableUniqueFieldName()
    ));
    uniqueFieldSearchField.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (uniqueFieldSearchField.getText().contains(String.format(
            "Search by %s", getHumanReadableUniqueFieldName()
        ))) {
          uniqueFieldSearchField.setText("");
          uniqueFieldSearchField.setForeground(Color.black);
        }
      }
      @Override
      public void focusLost(FocusEvent e) {
        if (uniqueFieldSearchField.getText().isEmpty()) {
          uniqueFieldSearchField.setForeground(Color.gray);
          uniqueFieldSearchField.setText(String.format(
              "Search by %s", getHumanReadableUniqueFieldName()
          ));
        }
      }
    });
    visibilitySearchField = new JCheckBox("Visible", true);
    toolBar.add(uniqueFieldSearchField);
    toolBar.add(visibilitySearchField);
    toolBar.addSeparator();
    toolBar.add(createSearchButton());
    toolBar.addSeparator();
    toolBar.add(createClearButton());
    return toolBar;
  }

  protected String getHumanReadableUniqueFieldName() {
    return repository.getUniqueFieldName();
  }

  private JButton createClearButton() {
    JButton button = new JButton("Clear");
    
    button.addActionListener(e -> {
      uniqueFieldSearchField.setText("");
      visibilitySearchField.setSelected(true);
      searchData();
    });
    
    return button;
  }
  
  private JButton createSearchButton() {
    JButton button = new JButton("Search");
    
    button.addActionListener(e -> searchData());
    
    return button;
  }
  
  private SearchParameters<O> searchParametersFromFields() {
    String searchText = uniqueFieldSearchField.getText();
    List<String> searchTextTerms;
    if (StringUtils.isBlank(searchText) || searchText.equals(String.format(
        "Search by %s", getHumanReadableUniqueFieldName()
    ))) {
      searchTextTerms = Collections.emptyList();
    } else {
      searchTextTerms = Collections.singletonList(searchText);
    }
    
    return getSearchParameters(
        searchTextTerms,
        Collections.singletonList(visibilitySearchField.isSelected())
    );
  }
  
  protected abstract JPanel createControlPanel();
  
  private SearchParameters<O> getSearchParameters(List<String> uniqueFieldSearchTerms, List<Boolean> visibilityStateSearchTerms) {
    return SearchParameters.<O>builder()
        .uniqueFields(uniqueFieldSearchTerms)
        .visibilityStates(visibilityStateSearchTerms)
        .build();
  }

  protected void searchData() {
    cleanUpTable();

    try {
      repository.search(searchParametersFromFields()).forEach(
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
    return List.of("UUID", "Visible");
  }
  
  protected TableColumn getHiddenColumnByHeaderValue(String headerValue) {
    return hiddenColumns.get(headerValue);
  }
  
  protected TableColumnModel getTableColumnModel() {
    return table.getColumnModel();
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
        c -> {
          TableColumn columnToHide = columnModel.getColumn(
              columnModel.getColumnIndex(c)
          );
          hiddenColumns.put((String) columnToHide.getHeaderValue(), columnToHide);
          
          columnModel.removeColumn(columnToHide);
        }
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
