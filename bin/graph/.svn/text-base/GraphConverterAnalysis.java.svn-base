package graph;

import util.Constants;
import util.GraphConverter;
import framework.Analysis;
import framework.Pipe;

public class GraphConverterAnalysis implements Analysis {

    public void analyze(Pipe pipe) {
        util.Graph oldGraph = (util.Graph) pipe.getData(Constants.KEY_GRAPH);
        edu.uci.ics.jung.graph.Graph newGraph = GraphConverter
                .convert(oldGraph);
        pipe.addData(Constants.KEY_JUNG_GRAPH, newGraph);
    }

}
