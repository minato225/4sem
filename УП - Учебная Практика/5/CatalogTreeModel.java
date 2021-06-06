
import javax.swing.tree.DefaultTreeModel;

class CatalogTreeModel extends DefaultTreeModel {
    private static final long serialVersionUID = 1L;

    private MovieTreeNode root;

    CatalogTreeModel(MovieTreeNode root) {
        super(root);
        this.root = root;
    }

    @Override
    public MovieTreeNode getRoot() {
        return root;
    }

    void insertNodeInto(MovieTreeNode child, MovieTreeNode parent, int i, boolean flag) {
        insertNodeInto(child, parent, i);
        parent.addNode(child);
    }
}
