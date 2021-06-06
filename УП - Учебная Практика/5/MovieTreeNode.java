import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

class MovieTreeNode extends DefaultMutableTreeNode {
    private final String nodeName;
    private MovieNode nodeValue = null;
    private final List<MovieTreeNode> nodes = new ArrayList<>();
    private boolean isLeafNode = false;

    MovieTreeNode(String name) {
        nodeName = name;
    }

    MovieTreeNode(String name, MovieNode value) {
        nodeName = name;
        nodeValue = value;
        isLeafNode = true;
    }

    void addNode(MovieTreeNode node) {
        nodes.add(node);
    }

    void deleteNode(MovieTreeNode node) {
        boolean isExist = false;
        for (int i = 0; i < nodes.size(); ++i)
            if (nodes.get(i).toString().compareToIgnoreCase(node.toString()) == 0) {
                nodes.remove(i);
                isExist = true;
            }
    }

    List<MovieNode> getAllLeaves() {
        List<MovieNode> leaves = new ArrayList<>();
        Deque<MovieTreeNode> deque = new ArrayDeque<>();

        deque.push(this);
        MovieTreeNode temp;
        while (!deque.isEmpty()) {
            temp = deque.removeFirst();
            if (temp.isLeafNode)
                leaves.add(temp.getNodeValue());
             else
                temp.nodes.forEach(deque::push);
        }

        return leaves;
    }

    public MovieNode getNodeValue() {
        return nodeValue;
    }

    @Override
    public String toString() {
        return nodeName;
    }
}
