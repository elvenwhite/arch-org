package graph;

import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

import util.Constants;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class GraphFileExporter {
	public void export(Graph graph, String filePath) throws IOException {
		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutputStream os = new ObjectOutputStream(fos);

		Set vertices = graph.getVertices();
		os.writeInt(vertices.size());
		for (Object child : vertices) {
			Vertex vertex = (Vertex) child;
			if (vertex == null)
				continue;
			writeVertex(vertex, (Point2D) vertex
					.getUserDatum(Constants.KEY_LOCATION), os);
		}

		Set edges = graph.getEdges();
		os.writeInt(edges.size());
		for (Object child : edges) {
			Edge edge = (Edge) child;
			if (edge == null)
				continue;

			writeEdge(edge, graph, os);
		}
		os.flush();
		os.close();
	}

	public void export(VisualizationViewer viewer, String filePath)
			throws IOException {
		VisualizationModel model = viewer.getModel();
		Layout layout = model.getGraphLayout();
		Graph graph = layout.getGraph();

		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutputStream os = new ObjectOutputStream(fos);

		Set vertices = graph.getVertices();
		os.writeInt(vertices.size());
		for (Object child : vertices) {
			Vertex vertex = (Vertex) child;
			if (vertex == null)
				continue;

			Point2D point = layout.getLocation(vertex);
			writeVertex(vertex, point, os);
		}

		Set edges = graph.getEdges();
		os.writeInt(edges.size());
		for (Object child : edges) {
			Edge edge = (Edge) child;
			if (edge == null)
				continue;

			writeEdge(edge, graph, os);
		}
		os.flush();
		os.close();
	}

	private void writeVertex(Vertex vertex, Point2D point, ObjectOutputStream os)
			throws IOException {
		String id = (String) vertex.getUserDatum(Constants.KEY_ID);
		String name = (String) vertex.getUserDatum(Constants.KEY_NAME);
		Double scaleFactor = (Double) vertex.getUserDatum(Constants.KEY_SIZE);

		os.writeObject(id);
		os.writeObject(name);
		os.writeDouble(scaleFactor);
		os.writeDouble(point.getX());
		os.writeDouble(point.getY());
	}

	private void writeEdge(Edge edge, Graph graph, ObjectOutputStream os)
			throws IOException {
		EdgeWeightLabeller edgeWeightLabeller = EdgeWeightLabeller
				.getLabeller(graph);

		Number length = edgeWeightLabeller.getNumber(edge);
		os.writeObject(length);

		Set vertices = edge.getIncidentVertices();
		for (Object child : vertices) {
			Vertex vertex = (Vertex) child;
			String id = (String) vertex.getUserDatum(Constants.KEY_ID);
			os.writeObject(id);
		}

	}
}
