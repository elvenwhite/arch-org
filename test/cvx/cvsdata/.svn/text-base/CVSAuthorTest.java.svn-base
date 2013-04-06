package cvx.cvsdata;

import junit.framework.TestCase;

public class CVSAuthorTest extends TestCase {
    private CVSAuthor author;

    protected void setUp() {
        CVSAuthor.clearAll();
        
        author = CVSAuthor.getInstance("author1");
        assertNotNull(author);
    }

    public void testId() {
        assertEquals(0, author.getId());

        CVSAuthor author2 = CVSAuthor.getInstance("author2");
        assertEquals(1, author2.getId());
    }

    public void testName() {
        assertEquals("author1", author.getName());
    }

    public void testToString() {
        assertEquals("author1", author.toString());
    }
}
