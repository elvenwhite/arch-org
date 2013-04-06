package cvx;

import util.Constants;
import util.properties.PropertiesManager;
import cvx.cvsdata.CVSDatabase;
import framework.Analysis;
import framework.Pipe;
import framework.TargetSoftware;

public class CVSParserAnalysis implements Analysis {

    private static final String PK_TARGET_MODULE = "cvs.target.module";

    private static final String PK_INPUT_FILE = "cvs.input.file";

    private TargetSoftware target;

    private String inputFile;

    public void analyze(Pipe pipe) {
        readProperties();
        CVSLogParser parser = CVSLogParserFactory.createCVSLogParser(target);
        parser.parseCVSLogFile(inputFile);

        CVSDatabase db = CVSDatabase.instance();
        db.load();
        pipe.addData(Constants.KEY_CVS_DATABASE, db);
    }

    private void readProperties() {
        String targetSoftware = PropertiesManager.getValue(PropertiesManager
                .getDefaultGroup(), PK_TARGET_MODULE);

        target = null;
        if (targetSoftware == null || targetSoftware.equals("")) {
            return;
        }

        if (targetSoftware.equalsIgnoreCase("junit")) {
            target = TargetSoftware.JUnit;
        } else if (targetSoftware.equalsIgnoreCase("jhotdraw")) {
            target = TargetSoftware.JHotDraw;
        } else if (targetSoftware.equalsIgnoreCase("eclipse-jdt")) {
            target = TargetSoftware.EclipseJDT;
        }

        inputFile = PropertiesManager.getValue(PropertiesManager
                .getDefaultGroup(), PK_INPUT_FILE);
    }

}
