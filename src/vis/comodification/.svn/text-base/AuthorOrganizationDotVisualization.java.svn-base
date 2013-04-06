package vis.comodification;

import java.util.Iterator;

import util.Edge;
import util.Graph;
import util.Node;
import vis.DotVisualization;

public class AuthorOrganizationDotVisualization extends DotVisualization {

	protected void visualizeNodes(Graph graph) {
		Iterator<Node> iterator = graph.nodes();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			double accessFactor = (Double) node.getData();
			String[] propertiesVars = new String[] { "width", "height" };
			String[] propertiesVals = new String[] { "" + accessFactor,
					"" + accessFactor };
			addNode(node.getName(), propertiesVars, propertiesVals);
		}
	}

	protected void visualizeEdges(Graph graph) {
		Iterator<Edge> iterator = graph.edges();
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			Node src = edge.getSrc();
			Node dstn = edge.getDstn();
			double coModificationFactor = (Double) edge.getData();
			String[] propertiesVars = new String[] { "len" };
			String[] propertiesVals = new String[] { "" + coModificationFactor };
            System.out.println("com: " + coModificationFactor);
			addEdge(src.getName(), dstn.getName(), propertiesVars,
					propertiesVals);
		}
	}
}
