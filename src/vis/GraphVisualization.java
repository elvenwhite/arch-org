package vis;

import util.Constants;
import util.Graph;
import framework.ApplicationInvokeException;
import framework.Pipe;

public abstract class GraphVisualization implements Visualization {

    public void visualize(Pipe pipe) throws ApplicationInvokeException {
        try {
            Graph graph = (Graph) pipe.getData(Constants.KEY_GRAPH);
            assert (graph != null);
            visualize(graph);
        } catch (Throwable t) {
            throw new ApplicationInvokeException(t);
        }
    }

    public abstract void visualize(Graph graph);
}
