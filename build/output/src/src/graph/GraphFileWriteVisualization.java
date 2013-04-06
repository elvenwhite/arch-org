package graph;

import java.io.IOException;

import util.Constants;
import util.properties.PropertiesManager;
import vis.Visualization;
import edu.uci.ics.jung.graph.Graph;
import framework.ApplicationInvokeException;
import framework.Pipe;

public class GraphFileWriteVisualization implements Visualization {

    private static final String PK_OUTPUT_FILE = "graph.output.file";

    private String filePath;

    public void visualize(Pipe pipe) throws ApplicationInvokeException {
        Graph graph = (Graph) pipe.getData(Constants.KEY_JUNG_GRAPH);
        readProperties();
        GraphFileExporter exporter = new GraphFileExporter();
        try {
            exporter.export(graph, filePath);
        } catch (IOException e) {
            throw new ApplicationInvokeException(e);
        }
    }

    private void readProperties() {
        this.filePath = PropertiesManager.getValue(PropertiesManager
                .getDefaultGroup(), PK_OUTPUT_FILE);
    }
}
