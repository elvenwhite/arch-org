package vis;

import java.awt.Dimension;

import samples.preview_new_graphdraw.StaticLayout;
import samples.preview_new_graphdraw.VisVertex;
import samples.preview_new_graphdraw.iterablelayouts.KKLayout;
import samples.preview_new_graphdraw.staticlayouts.CircleLayout;
import samples.preview_new_graphdraw.staticlayouts.IterableToStaticLayout;
import util.Graph;
import util.GraphConverter;
import vis.viewer.OldDotGraphViewerFrame;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.AbstractLayout;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.Layout;

public class DOTGraphViewerVisualization extends GraphVisualization {

    protected Graph graph;

    private EdgeWeightLabeller edgeWeightLabeller;

    public void visualize(Graph graph) {
        this.graph = graph;
        edu.uci.ics.jung.graph.Graph newGraph = GraphConverter.convert(graph);

        edgeWeightLabeller = EdgeWeightLabeller.getLabeller(newGraph);
        StaticLayout end = new IterableToStaticLayout(new CircleLayout(),
                new MyKKLayout());
        end.initializeLocations(new Dimension(200, 200), newGraph);

        Layout layout = new MyLayout(newGraph, end);

        OldDotGraphViewerFrame frame = new OldDotGraphViewerFrame(newGraph,
                graph, layout);
        frame.setVisible(true);
    }

    class MyKKLayout extends KKLayout {
        protected double getDistance(Vertex v1, Vertex v2) {
            Edge edge = v1.findEdge(v2);
            Number n = edgeWeightLabeller.getNumber(edge);
            if (n == null)
                return (diameter + 1);
            else {
                double value = n.doubleValue();
                return value;
            }
        }
    }

    class MyLayout extends AbstractLayout {

        StaticLayout end;

        public MyLayout(edu.uci.ics.jung.graph.Graph g, StaticLayout end) {
            super(g);
            this.end = end;
        }

        @Override
        public void advancePositions() {
        }

        @Override
        protected void initialize_local_vertex(Vertex v) {
            VisVertex vv = end.getVisVertex(v);
            Coordinates coord = new Coordinates(vv.x, vv.y);
            v.setUserDatum(getBaseKey(), coord, UserData.REMOVE);
        }

        public boolean incrementsAreDone() {
            return true;
        }

        public boolean isIncremental() {
            return false;
        }
    }
}
