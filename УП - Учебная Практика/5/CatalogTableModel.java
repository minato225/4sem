import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;

class CatalogTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<>();
    private List<MovieNode> infoNodes = new ArrayList<>();

    private static final String[] columnNames = new String[]{"Genre", "Name", "Year", "Price"};
    private static final Class<?>[] columnTypes = new Class[]{String.class, String.class, Integer.class, Float.class};

    CatalogTableModel() {}

    CatalogTableModel(List<MovieNode> al) {
        setInfoNodes(al);
    }

    private void setInfoNodes(List<MovieNode> movieNodes) {
        infoNodes = movieNodes;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return infoNodes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MovieNode busNode = infoNodes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return busNode.genre;
            case 1:
                return busNode.name;
            case 2:
                return busNode.year;
            case 3:
                return busNode.price;
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {  }
}
