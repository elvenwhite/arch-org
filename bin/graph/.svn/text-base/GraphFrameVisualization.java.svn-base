package graph;

import util.Constants;
import vis.Visualization;
import edu.uci.ics.jung.graph.Graph;
import framework.ApplicationInvokeException;
import framework.Pipe;

public class GraphFrameVisualization implements Visualization {

    public void visualize(Pipe pipe) throws ApplicationInvokeException {
        Graph graph = (Graph) pipe.getData(Constants.KEY_JUNG_GRAPH);
        GraphFileFrame frame = new GraphFileFrame();
        frame.setVisible(true);
        frame.setGraph(graph, true);
    }

}
