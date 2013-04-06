package cvx;

import util.Constants;
import cvx.cvsdata.CVSDatabase;
import framework.Analysis;
import framework.Pipe;

public abstract class CVSAnalysis implements Analysis {
    public abstract void analyze(CVSDatabase db, Pipe pipe);

    public void analyze(Pipe pipe) {
        CVSDatabase db = (CVSDatabase) pipe.getData(Constants.KEY_CVS_DATABASE);
        if (db == null)
            return;
        
        analyze(db, pipe);
    }
}
