package graph;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import util.Constants;
import vis.viewer.DotGraphVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.GraphUtils;
import edu.uci.ics.jung.utils.UserData;

public class GraphFileImporter {
	private HashMap<String, Vertex> vertexMap = null;

	public GraphFileImporter() {
		vertexMap = new HashMap<String, Vertex>();
	}

	public Graph importGraph(String filePath) throws IOException {
		UndirectedSparseGraph graph = new UndirectedSparseGraph();

		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream is = new ObjectInputStream(fis);

		int vertexSize = is.readInt();
		for (int i = 0; i < vertexSize; i++) {
			try {
				readVertex(is, graph);
			} catch (ClassNotFoundException e) {
				throw new IOException(e.getMessage());
			}
		}

		int edgeSize = is.readInt();
		for (int i = 0; i < edgeSize; i++) {
			try {
				readEdge(is, graph);
			} catch (ClassNotFoundException e) {
				throw new IOException(e.getMessage());
			}
		}

		return graph;
	}

	private void readVertex(ObjectInputStream is, Graph graph)
			throws IOException, ClassNotFoundException {
		String id = (String) is.readObject();
		String name = (String) is.readObject();
		double scaleFactor = is.readDouble();
		double pointX = is.readDouble();
		double pointY = is.readDouble();

		DotGraphVertex vertex = new DotGraphVertex(id);
		vertex.setUserDatum(Constants.KEY_ID, id, UserData.REMOVE);
		vertex.setUserDatum(Constants.KEY_NAME, name, UserData.REMOVE);
		vertex.setUserDatum(Constants.KEY_SIZE, new Double(scaleFactor),
				UserData.REMOVE);
		vertex.setUserDatum(Constants.KEY_LOCATION, new Point2D.Double(
				pointX, pointY), UserData.REMOVE);
		graph.addVertex(vertex);
		vertexMap.put(id, vertex);
	}

	private void readEdge(ObjectInputStream is, Graph graph)
			throws IOException, ClassNotFoundException {
		Number length = (Number) is.readObject();

		String vertexLabel1 = (String) is.readObject();
		String vertexLabel2 = (String) is.readObject();

		Vertex v1 = vertexMap.get(vertexLabel1);
		Vertex v2 = vertexMap.get(vertexLabel2);

		Edge edge = GraphUtils.addEdge(graph, v1, v2);

		EdgeWeightLabeller edgeWeightLabeller = EdgeWeightLabeller
				.getLabeller(graph);
		edgeWeightLabeller.setNumber(edge, length);
	}
}
