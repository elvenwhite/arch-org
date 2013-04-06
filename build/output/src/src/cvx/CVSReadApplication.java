package cvx;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import util.Constants;
import util.Graph;
import vis.GraphVisualization;
import vis.comodification.AuthorOrganizationGraphViewerVisualization;
import cvx.cvsdata.CVSDatabase;
import cvx.cvsdata.CVSFile;
import framework.Application;
import framework.ApplicationInvokeException;
import framework.Pipe;
import framework.TargetSoftware;

public class CVSReadApplication extends Application {

    private static String file = "";

    public static void main(String[] args) throws ApplicationInvokeException {
        if (args.length < 1) {
            System.out.println("Usage: CVSApplicationRunner FILE");
            return;
        }
        file = args[0];

        CVSReadApplication app = new CVSReadApplication();
        app.execute(null);
    }

    public void setUp() {
    }

    public void prepare(TargetSoftware target, Pipe pipe) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            CVSFile file = null;
            try {
                do {
                    file = (CVSFile) ois.readObject();
                    CVSFile.regist(file);
                } while (file != null);
            } catch (EOFException ex) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        CVSDatabase db = CVSDatabase.instance();
        db.load();
        pipe.addData(Constants.KEY_CVS_DATABASE, db);
    }

    public void analyze(Pipe pipe) {
        CVSDatabase db = (CVSDatabase) pipe.getData(Constants.KEY_CVS_DATABASE);
        if (db == null)
            return;

        CVSAnalysis analysis = new AuthorOrganizationAnalysis();
        analysis.analyze(db, pipe);
    }

    Graph graph;

    public void visualize(Pipe pipe) {
        Graph graph = (Graph) pipe.getData(Constants.KEY_GRAPH);
        graph = (Graph) pipe.getData(Constants.KEY_GRAPH);

        // DotVisualization visu = new AuthorOrganizationDotVisualization();
        // visu.setGraphType(DotVisualization.UNDIRECTED);
        // visu.setAlgorithm(DotVisualization.NEATO);
        // visu.setEdgeVisible(false);

        GraphVisualization visu = new AuthorOrganizationGraphViewerVisualization();

        visu.visualize(graph);
        // CVSDatabase db = (CVSDatabase) pipe.getData("cvsDB");
        // CVSLogWriter writer = new CVSLogWriter(db);
        // writer.save(outputPath + "/out.dt");
    }
}
