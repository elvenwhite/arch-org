package graph;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Set;

import samples.preview_new_graphdraw.StaticLayout;
import samples.preview_new_graphdraw.VisVertex;
import samples.preview_new_graphdraw.iterablelayouts.KKLayout;
import samples.preview_new_graphdraw.staticlayouts.CircleLayout;
import samples.preview_new_graphdraw.staticlayouts.IterableToStaticLayout;
import util.Constants;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.utils.UserData;
import framework.Analysis;
import framework.Pipe;

public class ApplyLayoutAnalysis implements Analysis {

    private EdgeWeightLabeller edgeWeightLabeller;


    public void analyze(Pipe pipe) {
        Graph newGraph = (Graph) pipe.getData(Constants.KEY_JUNG_GRAPH);

        edgeWeightLabeller = EdgeWeightLabeller.getLabeller(newGraph);
        StaticLayout end = new IterableToStaticLayout(new CircleLayout(),
                new MyKKLayout());
        end.initializeLocations(new Dimension(200, 200), newGraph);

        Set vertices = newGraph.getVertices();
        for (Object o : vertices) {
            if (o == null)
                continue;
            Vertex v = (Vertex) o;
            VisVertex vv = end.getVisVertex(v);
            v.setUserDatum(Constants.KEY_LOCATION, new Point2D.Double(vv.x,
                    vv.y), UserData.REMOVE);
        }
    }
    
    private final class MyKKLayout extends KKLayout {
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
}
