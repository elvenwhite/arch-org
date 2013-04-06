package graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JPanel;

import org.apache.commons.collections.Predicate;

import util.Constants;
import util.png.PngSource;
import util.png.PngWriter;
import vis.viewer.DotGraphEdgeStringer;
import vis.viewer.DotGraphVertexShapeFunction;
import vis.viewer.DotGraphVertexStringer;
import vis.viewer.EdgeStyle;
import vis.viewer.GraphSelectionListener;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.AbstractVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.ConstantEdgeStringer;
import edu.uci.ics.jung.graph.decorators.ConstantVertexStringer;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeShapeFunction;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.GradientEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.PickableEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.AbstractLayout;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.HasGraphLayout;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.MultiPickedState;
import edu.uci.ics.jung.visualization.PickedInfo;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.transform.LayoutTransformer;

public class GraphPane extends JPanel implements PngSource {

    private static final Dimension DEFAULT_SIZE = new Dimension(600, 400);

    private static final long serialVersionUID = 6197422629444854492L;

    private PluggableRenderer mainRenderer;

    private EdgeStringer nullEdgeStringer, edgeStringer;

    private VertexStringer nullVertexStringer, vertexStringer;

    private VertexShapeFunction nullVertexShapeFunction, vertexShapeFunction;

    private ColoredEdgePaintFunction edgePaint;

    private ColoredNodePaintFunction nodeColorFunction;

    private DirectionDisplayPredicate directionDisplayPredicate;

    private VisualizationViewer mainGraphViewer;

    private ScalingControl scaler;

    private ArrayList<GraphSelectionListener> selectionListeners;

    private JPanel dummyPane = null, graphZoomScrollPane = null;

    public GraphPane() {
        selectionListeners = new ArrayList<GraphSelectionListener>();
        setLayout(new BorderLayout());
    }

    public void addGraphSelectionListener(GraphSelectionListener listener) {
        selectionListeners.add(listener);
    }

    public void removeGraphSelectionListener(GraphSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    public void showGraph(Graph graph) {
        if (graph == null)
            return;

        if (dummyPane != null) {
            remove(dummyPane);
            dummyPane = null;
        }
        scaler = new CrossoverScalingControl();
        Layout layout = new DefaultLayout(graph);
        VisualizationModel model = new DefaultVisualizationModel(layout,
                DEFAULT_SIZE);
        mainGraphViewer = createMainViewer(graph, model);
        graphZoomScrollPane = new GraphZoomScrollPane(mainGraphViewer);
        add(graphZoomScrollPane, BorderLayout.CENTER);
        updateUI();
    }

    public void removeGraph() {
        if (mainGraphViewer != null) {
            remove(graphZoomScrollPane);
            graphZoomScrollPane = null;
            mainGraphViewer = null;
        }

        if (dummyPane == null) {
            dummyPane = new JPanel();
        }
        add(dummyPane, BorderLayout.CENTER);

        updateUI();
    }

    private VisualizationViewer createMainViewer(Graph graph,
            VisualizationModel model) {
        nullVertexStringer = new ConstantVertexStringer(null);
        vertexStringer = new DotGraphVertexStringer();
        nullVertexShapeFunction = new AbstractVertexShapeFunction() {
            public Shape getShape(Vertex v) {
                return factory.getEllipse(v);
            }
        };
        vertexShapeFunction = new DotGraphVertexShapeFunction();
        nullEdgeStringer = new ConstantEdgeStringer(null);
        edgeStringer = new DotGraphEdgeStringer(graph);
        directionDisplayPredicate = new DirectionDisplayPredicate(false);

        mainRenderer = new PluggableRenderer();
        mainRenderer.setVertexShapeFunction(vertexShapeFunction);
        mainRenderer.setVertexStringer(vertexStringer);
        mainRenderer.setEdgeStringer(nullEdgeStringer);
        mainRenderer.setEdgeIncludePredicate(directionDisplayPredicate);

        VisualizationViewer viewer = new VisualizationViewer(model,
                mainRenderer, DEFAULT_SIZE);
        viewer.setBackground(Color.white);
        viewer.setPickSupport(new ShapePickSupport());

        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        viewer.setGraphMouse(graphMouse);

        PickedState pickedState = viewer.getPickedState();

        edgePaint = new ColoredEdgePaintFunction(pickedState,
                new PickableEdgePaintFunction(viewer.getPickedState(), null,
                        null), viewer, viewer);
        mainRenderer.setEdgePaintFunction(edgePaint);

        nodeColorFunction = new ColoredNodePaintFunction(pickedState);
        mainRenderer.setVertexPaintFunction(nodeColorFunction);

        pickedState.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Object source = e.getSource();
                if (source instanceof MultiPickedState) {
                    MultiPickedState mps = (MultiPickedState) source;
                    switch (e.getStateChange()) {
                    case 1:
                        objectSelected(mps);
                        break;
                    case 2:
                        objectDeselected(mps);
                        break;
                    }
                }
            }
        });
        return viewer;
    }

    @SuppressWarnings("unchecked")
    private void objectSelected(MultiPickedState mps) {
        Set edgeSet = mps.getPickedEdges();
        int edgeSize = edgeSet.size();
        Edge[] edgeArray = null;
        if (edgeSize > 0) {
            edgeArray = new Edge[edgeSize];
            edgeSet.toArray(edgeArray);
        } else {
            edgeArray = new Edge[0];
        }

        Set verticesSet = mps.getPickedVertices();
        int verticesSize = verticesSet.size();
        Vertex[] vertexArray = null;
        if (verticesSize > 0) {
            vertexArray = new Vertex[verticesSize];
            verticesSet.toArray(vertexArray);
        } else {
            vertexArray = new Vertex[0];
        }

        for (GraphSelectionListener listener : selectionListeners) {
            listener.graphElementsSelected(vertexArray, edgeArray);
        }
    }

    private void objectDeselected(MultiPickedState mps) {
        for (GraphSelectionListener listener : selectionListeners) {
            listener.graphElementsDeselected();
        }
    }

    public void zoomIn() {
        Rectangle rect = getBounds();
        double centerX = rect.getCenterX();
        double centerY = rect.getCenterY();
        scaler.scale(mainGraphViewer, 1.1f,
                new Point2D.Double(centerX, centerY));
    }

    public void zoomOut() {
        Rectangle rect = getBounds();
        double centerX = rect.getCenterX();
        double centerY = rect.getCenterY();
        scaler.scale(mainGraphViewer, 1 / 1.1f, new Point2D.Double(centerX,
                centerY));
    }

    public void showNodeLabel(boolean visible) {
        if (visible == true) {
            mainRenderer.setVertexStringer(vertexStringer);
        } else {
            mainRenderer.setVertexStringer(nullVertexStringer);
        }
        mainGraphViewer.repaint();
    }

    public void showNodeSize(boolean visible) {
        if (visible == true) {
            mainRenderer.setVertexShapeFunction(vertexShapeFunction);
        } else {
            mainRenderer.setVertexShapeFunction(nullVertexShapeFunction);
        }
        mainGraphViewer.repaint();
    }

    public void setNodeAlpha(int value) {
        nodeColorFunction.setAlpha(value);
        mainGraphViewer.repaint();
    }

    public Color getNodeColor() {
        if (nodeColorFunction == null) {
            return Color.RED;
        }
        return nodeColorFunction.color;
    }

    public void setNodeColor(Color color) {
        if (nodeColorFunction == null)
            return;
        nodeColorFunction.setColor(color);
        mainGraphViewer.repaint();
    }

    public void showEdge(boolean visible) {
        if (directionDisplayPredicate == null)
            return;
        directionDisplayPredicate.showDirected(visible);
        mainGraphViewer.repaint();
    }

    public void showEdgeLabel(boolean visible) {
        if (visible == true) {
            mainRenderer.setEdgeStringer(edgeStringer);
        } else {
            mainRenderer.setEdgeStringer(nullEdgeStringer);
        }
        mainGraphViewer.repaint();
    }

    public void setEdgeStyle(EdgeStyle style) {
        if (mainRenderer == null)
            return;

        EdgeShapeFunction edgeShapeFunction = null;
        switch (style) {
        case ES_LINE:
            edgeShapeFunction = new EdgeShape.Line();
            break;
        case ES_WEDGE:
            edgeShapeFunction = new EdgeShape.Wedge(10);
            break;
        case ES_QUAD:
            edgeShapeFunction = new EdgeShape.QuadCurve();
            break;
        case ES_CUBIC:
            edgeShapeFunction = new EdgeShape.CubicCurve();
            break;
        default:
            return;
        }

        mainRenderer.setEdgeShapeFunction(edgeShapeFunction);
        mainGraphViewer.repaint();
    }

    public void setEdgeAlpha(int alpha) {
        if (edgePaint == null)
            return;

        edgePaint.setAlpha(alpha);
        mainGraphViewer.repaint();
    }

    public Color getEdgeColor1() {
        if (edgePaint == null) {
            return Color.BLACK;
        }
        return edgePaint.color1;
    }

    public Color getEdgeColor2() {
        if (edgePaint == null) {
            return Color.BLACK;
        }
        return edgePaint.color2;
    }

    public void setEdgeColor1(Color color) {
        if (edgePaint == null) {
            return;
        }
        edgePaint.setColor1(color);
        mainGraphViewer.repaint();
    }

    public void setEdgeColor2(Color color) {
        if (edgePaint == null) {
            return;
        }
        edgePaint.setColor2(color);
        mainGraphViewer.repaint();
    }

    public VisualizationViewer getVisualizationViewer() {
        return mainGraphViewer;
    }

    private static final class DefaultLayout extends AbstractLayout {

        public DefaultLayout(Graph g) {
            super(g);
        }

        @Override
        public void advancePositions() {
        }

        @Override
        protected void initialize_local_vertex(Vertex v) {
            Point2D point = (Point2D) v.getUserDatum(Constants.KEY_LOCATION);

            Coordinates coord = new Coordinates(point.getX(), point.getY());
            v.setUserDatum(getBaseKey(), coord, UserData.REMOVE);
        }

        public boolean incrementsAreDone() {
            return true;
        }

        public boolean isIncremental() {
            return false;
        }

    }

    private final static class DirectionDisplayPredicate implements Predicate {
        protected boolean show;

        public DirectionDisplayPredicate(boolean show) {
            this.show = show;
        }

        public void showDirected(boolean b) {
            show = b;
        }

        public boolean evaluate(Object arg0) {
            if (show && arg0 instanceof Edge)
                return true;
            return false;
        }
    }

    private static final class ColoredEdgePaintFunction extends
            GradientEdgePaintFunction {

        protected boolean fillEdge = false;

        private Color color1, color2;

        protected PickedInfo pi;

        public ColoredEdgePaintFunction(PickedInfo pi,
                EdgePaintFunction defaultEdgePaintFunction, HasGraphLayout vv,
                LayoutTransformer transformer) {
            super(Color.BLACK, Color.WHITE, vv, transformer);
            this.pi = pi;
            this.color1 = Color.BLACK;
            this.color2 = Color.BLACK;
        }

        protected void setAlpha(int alpha) {
            color1 = new Color(color1.getRed(), color1.getGreen(), color1
                    .getBlue(), alpha);
            color2 = new Color(color2.getRed(), color2.getGreen(), color2
                    .getBlue(), alpha);
        }

        protected void setColor1(Color color) {
            color1 = new Color(color.getRed(), color.getGreen(), color
                    .getBlue(), color1.getAlpha());
        }

        protected void setColor2(Color color) {
            color2 = new Color(color.getRed(), color.getGreen(), color
                    .getBlue(), color2.getAlpha());
        }

        protected Color getColor1(Edge e) {
            return color1;
        }

        protected Color getColor2(Edge e) {
            if (pi != null && pi.isPicked(e)) {
                return new Color(0, 255, 0, color2.getAlpha());
            }
            return color2;
        }

        public void useFill(boolean b) {
            fillEdge = b;
        }
    }

    private static final class ColoredNodePaintFunction implements
            VertexPaintFunction {

        protected Color color;

        protected PickedInfo pi;

        public ColoredNodePaintFunction(PickedInfo pi) {
            this.pi = pi;
            color = new Color(1F, 0F, 0F, 1F);
        }

        public void setColor(Color c) {
            this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), color
                    .getAlpha());
        }

        protected void setAlpha(int alpha) {
            color = new Color(color.getRed(), color.getGreen(),
                    color.getBlue(), alpha);
        }

        public Paint getDrawPaint(Vertex v) {
            return Color.BLACK;
        }

        public Paint getFillPaint(Vertex v) {
            if (pi != null && pi.isPicked(v)) {
                return new Color(0, 255, 0, color.getAlpha());
            }
            return color;
        }
    }

    public void setNodeLabelType(int type) {
        if (vertexStringer == null
                || !(vertexStringer instanceof DotGraphVertexStringer)) {
            return;
        }
        ((DotGraphVertexStringer) vertexStringer).setType(type);
        updateUI();
    }

    public void export(OutputStream os) throws IOException {
        Rectangle rect = mainGraphViewer.getBounds();
        Dimension size = new Dimension(rect.width, rect.height);
        PngWriter.write(this, size, os);
    }

    public void drawPng(Graphics g) {
        mainGraphViewer.paint(g);
    }
}
