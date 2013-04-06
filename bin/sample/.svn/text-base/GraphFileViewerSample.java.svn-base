package sample;

import vis.Visualization;
import framework.ApplicationInvokeException;
import framework.DefaultPropertiesApplication;
import framework.TargetSoftware;
import graph.GraphFrameVisualization;

public class GraphFileViewerSample {
    public static void main(String[] args) throws ApplicationInvokeException {
        Visualization[] visualizer = new Visualization[] { new GraphFrameVisualization() };

        DefaultPropertiesApplication app = new DefaultPropertiesApplication(
                null, visualizer);
        app.execute(TargetSoftware.JUnit);
    }
}
