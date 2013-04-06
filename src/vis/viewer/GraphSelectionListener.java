package vis.viewer;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;

public interface GraphSelectionListener {
	public void graphElementsSelected(Vertex[] vertices, Edge[] edges);

	public void graphElementsDeselected();

}
