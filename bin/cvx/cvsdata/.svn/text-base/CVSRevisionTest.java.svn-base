package cvx.cvsdata;


import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class CVSRevisionTest extends TestCase {

	private CVSRevision revision;

	protected void setUp() {
		revision = new CVSRevision("1.0.0.0", 2007, 1, 1, 1, 1, 1, "author",
				"description", "+1 0");
		assertNotNull(revision);
	}

	public void testAuthor() {
		assertEquals(CVSAuthor.getInstance("author"), revision.getAuthor());
	}

	public void testVersion() {
		assertEquals("1.0.0.0", revision.getVersion());
	}

	public void testDescription() {
		assertEquals("description", revision.getDescription());
	}

	public void testDiff() {
		assertEquals("+1 0", revision.getDiff());
	}

	public void testDateInString() {
		// TODO 이 부분은 좀 고치면 좋겠습니다.
		assertEquals("2007/1/1 1:1:1", revision.getDateInString());

		revision = new CVSRevision("1.0.0.0", 2007, 12, 30, 11, 25, 35,
				"author", "ddd", "+2 0");
		assertEquals("2007/12/30 11:25:35", revision.getDateInString());
	}

	public void testDateInMillis() {
		Calendar date = new GregorianCalendar(2007, 1 - 1, 1, 1, 1, 1);
		assertEquals(date.getTimeInMillis(), revision.getDateInMillis());
	}

	public void testAfter() {
		CVSRevision compare = new CVSRevision("1.0.0.0", 2007, 1, 1, 1, 1, 1,
				"author", "", "");
		assertFalse(revision.after(compare));

		compare = new CVSRevision("1.1.0.0", 2007, 1, 1, 1, 1, 1, "author", "",
				"");
		assertFalse(revision.after(compare));

		compare = new CVSRevision("1.0.0.0", 2007, 2, 1, 1, 1, 1, "author", "",
				"");
		assertFalse(revision.after(compare));

		compare = new CVSRevision("1.0.0.0", 2006, 1, 1, 1, 1, 1, "author", "",
				"");
		assertTrue(revision.after(compare));
	}

	public void testBefore() {
		CVSRevision compare = new CVSRevision("1.0.0.0", 2007, 1, 1, 1, 1, 1,
				"author", "", "");
		assertFalse(compare.before(revision));

		compare = new CVSRevision("1.1.0.0", 2007, 1, 1, 1, 1, 1, "author", "",
				"");
		assertFalse(compare.before(revision));

		compare = new CVSRevision("1.0.0.0", 2007, 2, 1, 1, 1, 1, "author", "",
				"");
		assertFalse(compare.before(revision));

		compare = new CVSRevision("1.0.0.0", 2006, 1, 1, 1, 1, 1, "author", "",
				"");
		assertTrue(compare.before(revision));
	}

	public void testCompare() {
		CVSRevision compare = new CVSRevision("1.0.0.0", 2007, 1, 1, 1, 1, 1,
				"author", "", "");
		// TODO 여기는 0이 나와야 할 것 같습니다만..
		assertTrue(revision.compareTo(compare) < 0);

		compare = new CVSRevision("1.1.0.0", 2007, 1, 1, 1, 1, 1, "author", "",
				"");
		// TODO 여기는 ?
		assertTrue(revision.compareTo(compare) < 0);

		compare = new CVSRevision("1.0.0.0", 2007, 2, 1, 1, 1, 1, "author", "",
				"");
		assertTrue(revision.compareTo(compare) < 0);

		compare = new CVSRevision("1.0.0.0", 2006, 1, 1, 1, 1, 1, "author", "",
				"");
		assertTrue(revision.compareTo(compare) > 0);
	}
}
