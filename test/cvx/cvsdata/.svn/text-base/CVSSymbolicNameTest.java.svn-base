package cvx.cvsdata;

import junit.framework.TestCase;

public class CVSSymbolicNameTest extends TestCase {

    private CVSSymbolicName symbol;

    private CVSRevision revision;

    protected void setUp() {
        revision = new CVSRevision("1.0.0.0", 2007, 1, 1, 1, 1, 1, "author",
                "description", "+1 0");
        symbol = new CVSSymbolicName("Version1", revision);

        assertNotNull(symbol);
    }

    public void testName() {
        assertEquals("Version1", symbol.getName());
    }

    public void testRevision() {
        assertEquals(revision, symbol.getRevision());
    }
}
