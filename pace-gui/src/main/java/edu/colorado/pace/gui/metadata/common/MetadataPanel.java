package edu.colorado.pace.gui.metadata.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class MetadataPanel<O extends ObjectWithUniqueField> {

  private JPanel metadataPanel;
  private JTable itemTable;
  private JButton createButton;
  private JButton translateButton;

  private final CRUDRepository<O> repository;

  public MetadataPanel(CRUDRepository<O> repository, boolean translatableResource, Class<O> clazz) {
    this.repository = repository;
    loadTable();
    createButton.addActionListener(e -> {
      MetadataForm<O> metadataForm = createForm(null);
      
      metadataForm.pack();
      metadataForm.setVisible(true);
    });
    
    itemTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        JTable table =(JTable) e.getSource();
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);
        if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
          List<Object> values = new ArrayList<>(0);
          for (int i = 0; i < getHeaders().length; i++) {
            values.add(
                table.getModel().getValueAt(row, i)
            );
          }
          MetadataForm<O> metadataForm = createUpdateForm(values.toArray(Object[]::new));
          metadataForm.pack();
          metadataForm.setVisible(true);
        }
      }
    });
    
    translateButton.setVisible(translatableResource);
    translateButton.addActionListener(e -> {
      try {
        TranslateForm<O> translateForm = new TranslateForm<>(clazz, repository, this::loadTable);
        translateForm.pack();
        translateForm.setVisible(true);
      } catch (IOException | DatastoreException ex) {
        throw new RuntimeException(ex);
      }
    });
  }
  
  private MetadataForm<O> createUpdateForm(Object[] values) {
    return createForm(objectFromItemsArray(values));
  }
  
  private MetadataForm<O> createForm(O object) {
    return new MetadataForm<>(object, this::loadTable, repository) {
      @Override
      protected ObjectForm<O> getFormPanel(O object) {
        return getEditForm(object);
      }
    };
  }
  
  private void loadTable() {
    try {
      itemTable.setModel(new TableModel(
          repository.findAll()
              .map(this::objectToItemArray)
              .toArray(Object[][]::new),
          getHeaders()
      ));
      getHiddenColumns().forEach(c -> itemTable.removeColumn(itemTable.getColumn(c)));
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    }
  }
  
  protected List<String> getHiddenColumns() {
    return List.of("UUID");
  }
  
  protected abstract String[] getHeaders();
  protected abstract Object[] objectToItemArray(O object);
  protected abstract O objectFromItemsArray(Object[] itemsArray);
  protected abstract ObjectForm<O> getEditForm(O object);
  
  private static class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }
}
