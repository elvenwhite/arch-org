package cvx;

import java.net.URL;

import junit.framework.TestCase;
import util.Constants;
import util.ImmutableCollection;
import cvx.cvsdata.CVSAuthor;
import cvx.cvsdata.CVSDatabase;
import cvx.cvsdata.CVSFile;
import framework.Application;
import framework.Pipe;
import framework.TargetSoftware;

public class CVSLogTest extends TestCase {

    protected void setUp() {
        CVSFile.clearAll();
        CVSAuthor.clearAll();
        URL resourcesURL = ClassLoader.getSystemResource("resources");
        System.setProperty("project-path", resourcesURL.getPath() + "\\");
    }

    // 제거해버렸음. 수정해야 할 테스트
    public void testTemplate() throws Throwable {

        Application application = new Application() {

            @Override
            public void analyze(Pipe pipe) {
                CVSDatabase db = (CVSDatabase) pipe.getData(Constants.KEY_CVS_DATABASE);
                assertNotNull(db);

                ImmutableCollection<CVSFile> files = db.getFiles();
                for (CVSFile file : files) {
                    assertTrue(file.isValid());
                }
            }

            @Override
            public void prepare(TargetSoftware target, Pipe pipe) {
                CVSLogParser log = CVSLogParserFactory
                        .createCVSLogParser(target);
                log.parseCVSLogFile();
                CVSDatabase db = CVSDatabase.instance();
                db.load();
                pipe.addData(Constants.KEY_CVS_DATABASE, db);
            }

            @Override
            public void setUp() {
            }

            @Override
            public void visualize(Pipe pipe) {
            }

        };

        application.execute(TargetSoftware.JUnit);
    }

    public void testDummy() {
        // TODO: 위의 테스트가 제거되면 지울 것
    }
}
