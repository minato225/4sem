import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DragAndDrop extends JComponent implements
        DragGestureListener, DragSourceListener,
        DropTargetListener, MouseListener, MouseMotionListener
{
    private static final int LINE_WIDTH = 2;
    private static final BasicStroke LINE_STYLE = new BasicStroke(LINE_WIDTH);
    private static final Border NORMAL_BORDER = new BevelBorder(BevelBorder.LOWERED);
    private static final Border DROP_BORDER = new BevelBorder(BevelBorder.RAISED);

    private final ArrayList<CustomShape> witches = new ArrayList<>();
    private CustomShape customShape;
    private boolean dragMode;

    public static void main(String[] args) {
        createPane(100);
        createPane(600);
    }

    private DragAndDrop() {
        setBorder(NORMAL_BORDER);

        addMouseListener(this);
        addMouseMotionListener(this);

        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE,this);

        DropTarget dropTarget = new DropTarget(this,this);
        this.setDropTarget(dropTarget);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new MyStroke(LINE_WIDTH));

        witches.forEach(g2D::draw);
        g2D.setStroke(LINE_STYLE);
    }

    private void setDragMode(boolean dragMode) {
        this.dragMode = dragMode;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (dragMode)  return;
        int param = 200;
        CustomShape currentScribble = new CustomShape(param, e.getX(), e.getY());
        witches.add(currentScribble);
        repaint();
    }
    @Override
    public void dragGestureRecognized(DragGestureEvent e) {
        if (!dragMode)  return;

        MouseEvent inputEvent = (MouseEvent) e.getTriggerEvent();
        int x = inputEvent.getX();
        int y = inputEvent.getY();

        Rectangle rectangle = new Rectangle(x - LINE_WIDTH, y - LINE_WIDTH, LINE_WIDTH * 2, LINE_WIDTH * 2);

        for (CustomShape witch : witches) {
            if (witch.intersects(rectangle)) {
                customShape = witch;
                CustomShape dragScribble = (CustomShape) witch.clone();
                dragScribble.translate(-x, -y);
                Cursor cursor;
                switch (e.getDragAction()) {
                    case DnDConstants.ACTION_COPY:
                        cursor = DragSource.DefaultCopyDrop;
                        break;
                    case DnDConstants.ACTION_MOVE:
                        cursor = DragSource.DefaultMoveDrop;
                        break;
                    default:
                        return;
                }
                e.startDrag(cursor, dragScribble, this);
                return;
            }
        }
    }
    @Override
    public void dragDropEnd(DragSourceDropEvent e) {
        if (!e.getDropSuccess())  return;
        int action = e.getDropAction();
        if (action == DnDConstants.ACTION_MOVE) {
            witches.remove(customShape);
            customShape = null;
            repaint();
        }
    }
    @Override
    public void dragEnter(DropTargetDragEvent e) {
        if (e.isDataFlavorSupported(CustomShape.decDataFlavor) || e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            this.setBorder(DROP_BORDER);
        }
    }
    @Override
    public void dragExit(DropTargetEvent e) {
        this.setBorder(NORMAL_BORDER);
    }
    @Override
    public void drop(DropTargetDropEvent e) {
        this.setBorder(NORMAL_BORDER);

        if (e.isDataFlavorSupported(CustomShape.decDataFlavor) || e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        } else {
            e.rejectDrop();
            return;
        }

        Transferable t = e.getTransferable();
        CustomShape droppedScribble;

        try {
            droppedScribble = (CustomShape) t.getTransferData(CustomShape.decDataFlavor);
        } catch (Exception ex) {
            try {
                String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                droppedScribble = CustomShape.getFromString(s);
            } catch (Exception ex2) {
                e.dropComplete(false);
                return;
            }
        }

        Point p = e.getLocation();
        droppedScribble.translate((int) p.getX(), (int) p.getY());
        witches.add(droppedScribble);
        repaint();
        e.dropComplete(true);
    }
    /*--------------------------------------------------------------------------------------*/
    @Override
    public void dragOver(DropTargetDragEvent e) {}
    @Override
    public void dropActionChanged(DropTargetDragEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void dragEnter(DragSourceDragEvent e) {}
    @Override
    public void dragExit(DragSourceEvent e) {}
    @Override
    public void dropActionChanged(DragSourceDragEvent e) {}
    @Override
    public void dragOver(DragSourceDragEvent e) {}

    private static void createPane(int x){
        JFrame frame = new JFrame("ScribbleDragAndDrop");
        frame.setBounds(x, 100,500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        DragAndDrop scribblePane = new DragAndDrop();
        frame.getContentPane().add(scribblePane, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar();
        ButtonGroup group = new ButtonGroup();
        JToggleButton draw = new JToggleButton("Draw");
        JToggleButton drag = new JToggleButton("Drag");

        draw.addActionListener(e -> scribblePane.setDragMode(false));
        drag.addActionListener(e -> scribblePane.setDragMode(true));

        group.add(draw);
        group.add(drag);
        toolbar.add(draw);
        toolbar.add(drag);

        frame.getContentPane().add(toolbar, BorderLayout.NORTH);

        draw.setSelected(true);
        scribblePane.setDragMode(false);
        frame.setVisible(true);
    }
}
