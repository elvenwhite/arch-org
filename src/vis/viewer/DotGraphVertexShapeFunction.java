package vis.viewer;

import java.awt.Shape;
import java.util.HashMap;

import util.Constants;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.AbstractVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexAspectRatioFunction;
import edu.uci.ics.jung.graph.decorators.VertexSizeFunction;

public class DotGraphVertexShapeFunction extends AbstractVertexShapeFunction {

	public DotGraphVertexShapeFunction() {
		super(new DotGraphVertexSizeFunction(),
				new ConstantVertexAspectRatioFunction(DEFAULT_ASPECT_RATIO));
	}

	public Shape getShape(Vertex v) {
		return factory.getEllipse(v);
	}
}

class DotGraphVertexSizeFunction implements VertexSizeFunction {

	private HashMap<Vertex, Integer> sizeMap;

	private static final int DEFAULT_SIZE = 50;

	public DotGraphVertexSizeFunction() {
		sizeMap = new HashMap<Vertex, Integer>();
	}

	public int getSize(Vertex v) {
		if (!sizeMap.containsKey(v)) {
			Double scaleFactor = (Double) v
					.getUserDatum(Constants.KEY_SIZE);
			int size = (int) (DEFAULT_SIZE * scaleFactor);
			sizeMap.put(v, size);
		}
		return sizeMap.get(v);
	}
}