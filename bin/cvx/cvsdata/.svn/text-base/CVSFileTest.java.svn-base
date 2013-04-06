package cvx.cvsdata;

import junit.framework.TestCase;

public class CVSFileTest extends TestCase {
    private CVSFile file;

    protected void setUp() {
        CVSFile.clearAll();
        CVSAuthor.clearAll();
        file = CVSFile.getInstance("a/file1");
        assertNotNull(file);
    }

    public void testName() {
        assertEquals("a/file1", file.getName());
    }

    public void testValidity() {
        // 기본값이 true이군요.
        assertTrue(file.isValid());

        file.setTotalRevisions(3);
        assertFalse(file.isValid());
    }
}
