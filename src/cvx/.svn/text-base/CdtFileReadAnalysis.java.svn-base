package cvx;

import java.io.IOException;

import util.Constants;
import util.properties.PropertiesManager;
import cvx.cvsdata.CVSDatabase;
import framework.Analysis;
import framework.Pipe;

public class CdtFileReadAnalysis implements Analysis {

    private static final String PK_INPUT_FILE = "cdt.input.file";

    private String filePath;

    public void analyze(Pipe pipe) {
        readProperties();

        CVSDataFileReader reader = new CVSDataFileReader(filePath);
        try {
            reader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CVSDatabase db = CVSDatabase.instance();
        db.load();
        pipe.addData(Constants.KEY_CVS_DATABASE, db);
    }

    private void readProperties() {
        this.filePath = PropertiesManager.getValue(PropertiesManager
                .getDefaultGroup(), PK_INPUT_FILE);
    }

}
