package vis.viewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;

public class WeightDistance implements Distance {

	private Graph graph;

	private Number defaultDistance;

	private Map<ArchetypeVertex, HashMap<ArchetypeVertex, Number>> distanceMap;

	public WeightDistance(Graph graph) {
		this.graph = graph;

		distanceMap = new HashMap<ArchetypeVertex, HashMap<ArchetypeVertex, Number>>();

		defaultDistance = new Double(0.0);
		calculateDefaultDistance();
	}

	public Number getDistance(ArchetypeVertex source, ArchetypeVertex target) {
		Map<ArchetypeVertex, Number> map = getDistanceMap(source);
		// XXX 아마도 이 조건문에는 걸리는 경우가 없을 것이라고 생각됩니다만...
		if (!map.containsKey(target)) {
			return defaultDistance;
		}
		return map.get(target);
	}

	public Map<ArchetypeVertex, Number> getDistanceMap(ArchetypeVertex source) {
		if (!distanceMap.containsKey(source)) {
			calculateDistances(source);
		}

		return distanceMap.get(source);
	}

	private void calculateDefaultDistance() {
		Set edges = graph.getEdges();
		EdgeWeightLabeller labeller = EdgeWeightLabeller.getLabeller(graph);
		double max = 0.0;
		for (Object edgeObj : edges) {
			ArchetypeEdge edge = (ArchetypeEdge) edgeObj;
			try {
				double value = labeller.getNumber(edge).doubleValue();
				if (max < value) {
					max = value;
				}
			} catch (Throwable t) {
			}
		}
		defaultDistance = new Double(max * 1.5);
	}

	private void calculateDistances(ArchetypeVertex source) {
		EdgeWeightLabeller labeller = EdgeWeightLabeller.getLabeller(graph);
		HashMap<ArchetypeVertex, Number> map = new HashMap<ArchetypeVertex, Number>();
		Set vertices = graph.getVertices();
		for (Object vertexObj : vertices) {
			ArchetypeVertex target = (ArchetypeVertex) vertexObj;
			if (target == source)
				continue;

			ArchetypeEdge edge = source.findEdge(target);
			if (edge == null) {
				map.put(target, defaultDistance);
				continue;
			}
			try {
				map.put(target, labeller.getNumber(edge));
			} catch (Throwable t) {
				map.put(target, defaultDistance);
			}
		}
		distanceMap.put(source, map);
	}
}
