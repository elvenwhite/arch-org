package graph;

import java.io.IOException;

import util.Constants;
import util.properties.PropertiesManager;
import edu.uci.ics.jung.graph.Graph;
import framework.Analysis;
import framework.Pipe;

public class GraphFileReadAnalysis implements Analysis {

    private static final String PK_INPUT_FILE = "graph.input.file";

    private String filePath;

    public void analyze(Pipe pipe) {
        readProperties();

        GraphFileImporter importer = new GraphFileImporter();
        try {
            Graph graph = importer.importGraph(filePath);
            pipe.addData(Constants.KEY_JUNG_GRAPH, graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readProperties() {
        this.filePath = PropertiesManager.getValue(PropertiesManager
                .getDefaultGroup(), PK_INPUT_FILE);
    }

}
