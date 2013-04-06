package vis.viewer;

import java.text.DecimalFormat;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;

public class DotGraphEdgeStringer implements EdgeStringer {

    private Graph graph;

    public DotGraphEdgeStringer(Graph g) {
        this.graph = g;
    }

    public String getLabel(ArchetypeEdge e) {
        EdgeWeightLabeller labeller = EdgeWeightLabeller.getLabeller(graph);
        Number n = labeller.getNumber(e);
        if (n == null)
            return "";

        double length = n.doubleValue();
        DecimalFormat format = new DecimalFormat("##.00");
        return format.format(length);
    }
}
