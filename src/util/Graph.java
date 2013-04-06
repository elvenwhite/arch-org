package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Graph {
	
	private Map<String, Node> nodes;

	private Set<Edge> edges;

	private SetMap<Node, Edge> outgoing, incoming;

	public Graph() {
		nodes = new HashMap<String, Node>();
		edges = new HashSet<Edge>();
		outgoing = new SetMap<Node, Edge>();
		incoming = new SetMap<Node, Edge>();
	}
	
	public Node getNode(String nodeName) {
		if( !nodes.containsKey(nodeName) )
			nodes.put(nodeName, new Node(nodeName));
		return nodes.get(nodeName);
	}
	
	public Edge addEdge(Node src, Node dstn) {
		Edge edge = new Edge(src, dstn);
		edges.add(edge);
		outgoing.put(edge.getSrc(), edge);
		incoming.put(edge.getDstn(), edge);
		return edge;
	}

	public void remove(Node node) {
		nodes.remove(node.getName());		
		outgoing.remove(node);
		incoming.remove(node);
	}
	
	public void remove(Edge edge) {
		edges.remove(edge);
		outgoing.removeValue(edge.getSrc(), edge);
		incoming.removeValue(edge.getDstn(), edge);
	}

	public Iterator<Edge> edges() {
		return edges.iterator();
	}

	public Iterator<Node> nodes() {
		return nodes.values().iterator();
	}
}

enum GraphType {
	
}
