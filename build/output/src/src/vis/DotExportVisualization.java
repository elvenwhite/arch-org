package vis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import util.Graph;

public abstract class DotExportVisualization extends GraphVisualization {

	public static final int UNDIRECTED = 1;

	public static final int DIRECTED = 2;

	public static final String DOT = "dot";

	public static final String NEATO = "neato";

	public static final String FDP = "fdp";

	public static final String ALL_ALGORITHMS = "dot neato fdp";

	private static final String TAB = "\t";

	private static final String NEW_LINE = "\n";

	private static final String FONT = "BabelSans";

	private int graphType;

	private boolean isEdgeVisible;

	private StringBuffer buffer;

	private String inputFile;

	/**
	 * CONSTRUCTOR
	 */
	protected DotExportVisualization(String inputFile) {
		graphType = UNDIRECTED;
		isEdgeVisible = true;
		buffer = new StringBuffer();
        this.inputFile = inputFile;
	}

	/**
	 * @return true if edges are visible, false o.w.
	 */
	protected boolean isEdgeVisible() {
		return isEdgeVisible;
	}

	/**
	 * @return true if this is a directed graph, false o.w.
	 */
	protected boolean isDirectedGraph() {
		return graphType == DIRECTED;
	}

	/**
	 * @return true if this is a undirected graph, false o.w.
	 */
	protected boolean isUndirectedGraph() {
		return graphType == UNDIRECTED;
	}

	/**
	 * @param graphType
	 */
	public void setGraphType(int graphType) {
		if (graphType == DIRECTED)
			this.graphType = DIRECTED;
		else
			this.graphType = UNDIRECTED;
	}

	/**
	 * @param [b]
	 *            is true if edges are visible, false o.w.
	 */
	public void setEdgeVisible(boolean b) {
		isEdgeVisible = b;
	}
    
	public void visualize(Graph graph) {
		if (buffer.length() > 0)
			buffer = new StringBuffer();

		if (isDirectedGraph())
			buffer.append("digraph ");
		else if (isUndirectedGraph())
			buffer.append("graph ");
		else { /* impossible */
		}
		buffer.append("G {" + NEW_LINE);
		buffer.append(TAB + "graph [bgcolor=" + quote("lightgrey")
				+ "]" + NEW_LINE);
		buffer.append(TAB + "node [" + NEW_LINE);
		buffer.append(TAB + TAB + "fontname=" + quote(FONT) + NEW_LINE);
		buffer.append(TAB + TAB + "label=" + quote("\\N") + NEW_LINE);
		buffer.append(TAB + TAB + "shape=" + quote("circle") + NEW_LINE);
		buffer.append(TAB + TAB + "fixedsize=" + quote("true") + NEW_LINE);
		buffer.append(TAB + TAB + "style=" + quote("filled") + NEW_LINE);
		buffer.append(TAB + TAB + "fillcolor=" + quote("#aaccffbf") + NEW_LINE);
		buffer.append(TAB + TAB + "color=" + quote("steelblue2") + NEW_LINE);
		buffer.append(TAB + "]" + NEW_LINE);
		if (!isEdgeVisible())
			buffer.append(TAB + "edge [ style=" + quote("invis") + " ]"
					+ NEW_LINE);
		buffer.append(NEW_LINE);

		visualizeNodes(graph);
		buffer.append(NEW_LINE);

		visualizeEdges(graph);
		buffer.append("}" + NEW_LINE);

		try {
			System.out.println("write to inputFile");
			BufferedWriter w = new BufferedWriter(new FileWriter(inputFile));
			w.write(buffer.toString());
			w.newLine();
			w.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	protected abstract void visualizeNodes(Graph graph);

	protected abstract void visualizeEdges(Graph graph);

	/**
	 * @param node
	 * @param propertiesVars
	 * @param propertiesVals
	 */
	protected void addNode(String node, String[] propertiesVars,
			String[] propertiesVals) {
		String s = TAB;
		s += node;
		s += " [ ";
		for (int i = 0; i < propertiesVars.length; i++) {
			s += propertiesVars[i] + "=\"" + propertiesVals[i] + "\" ";
		}
		s += "]" + NEW_LINE;
		buffer.append(s);
	}

	/**
	 * @param node1
	 * @param node2
	 * @param propertiesVars
	 * @param propertiesVals
	 */
	protected void addEdge(String node1, String node2, String[] propertiesVars,
			String[] propertiesVals) {
		String s = TAB;
		s += node1;
		if (isDirectedGraph())
			s += " -> ";
		else if (isUndirectedGraph())
			s += " -- ";
		else { /* impossible */
		}
		s += node2;
		s += " [ ";
		for (int i = 0; i < propertiesVars.length; i++) {
			s += propertiesVars[i] + "=\"" + propertiesVals[i] + "\" ";
		}
		s += "]" + NEW_LINE;
		buffer.append(s);
	}

	private String quote(String s) {
		return "\"" + s + "\"";
	}
}
