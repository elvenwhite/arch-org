package util;

import java.util.HashMap;
import java.util.Iterator;

import vis.viewer.DotGraphVertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.StringLabeller.UniqueLabelException;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.GraphUtils;
import edu.uci.ics.jung.utils.UserData;

public class GraphConverter {
	public static edu.uci.ics.jung.graph.Graph convert(util.Graph graph) {

		UndirectedSparseGraph newGraph = new UndirectedSparseGraph();
		StringLabeller stringLabeller = StringLabeller.getLabeller(newGraph);
		EdgeWeightLabeller edgeWeightLabeller = EdgeWeightLabeller
				.getLabeller(newGraph);

		HashMap<Node, DotGraphVertex> nodesMap = new HashMap<Node, DotGraphVertex>();
		Iterator<Node> nodes = graph.nodes();
		while (nodes.hasNext()) {
			Node node = nodes.next();
            String id = (String) node.getValue(Constants.KEY_ID);
			String name = (String) node.getValue(Constants.KEY_NAME);
			double accessFactor = (Double) node.getData();

			DotGraphVertex newNode = new DotGraphVertex(name);
			newNode.setUserDatum(Constants.KEY_ID, id, UserData.REMOVE);
			newNode.setUserDatum(Constants.KEY_NAME, name, UserData.REMOVE);
			newNode.setUserDatum(Constants.KEY_SIZE, new Double(accessFactor),
					UserData.REMOVE);
			newGraph.addVertex(newNode);
			try {
				stringLabeller.setLabel(newNode, node.getName());
			} catch (UniqueLabelException e) {
			}
			nodesMap.put(node, newNode);
		}

		Iterator<Edge> edges = graph.edges();
		while (edges.hasNext()) {
			Edge edge = edges.next();
			Node src = edge.getSrc();
			Node dstn = edge.getDstn();
			DotGraphVertex newSrc = nodesMap.get(src);
			DotGraphVertex newDstn = nodesMap.get(dstn);
			try {
				edu.uci.ics.jung.graph.Edge newEdge = GraphUtils.addEdge(
						newGraph, newSrc, newDstn);
				Double length = (Double) edge.getData();
				edgeWeightLabeller.setNumber(newEdge, length);
			} catch (Throwable t) {
			}
		}
		return newGraph;
	}
}
