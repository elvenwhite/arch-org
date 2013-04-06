package cvx;

import java.io.IOException;

import util.Graph;
import util.properties.PropertiesManager;
import vis.GraphVisualization;
import cvx.cvsdata.CVSFile;

public class CdtFileWriteVisualization extends GraphVisualization {

    private static final String PK_OUTPUT_FILE = "cdt.output.file";

    private String filePath;

    @Override
    public void visualize(Graph graph) {
        assert (graph == null);
        if (graph == null)
            return;

        readProperties();

        CVSDataFileWriter writer = new CVSDataFileWriter(filePath, CVSFile
                .instances());
        try {
            writer.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readProperties() {
        this.filePath = PropertiesManager.getValue(PropertiesManager
                .getDefaultGroup(), PK_OUTPUT_FILE);
    }

}
