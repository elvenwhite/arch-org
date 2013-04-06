package cvx.cvsdata;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CVSRevision 
implements Comparable, Serializable {
	
	private static final long serialVersionUID = -673698251728432416L;
	
	private String version;
	private Calendar date;
	private String description;
	private String diff;		 
	private CVSAuthor author;
	
	public CVSRevision(String version,
			int yyyy, int MM, int dd, int hh, int mm, int ss, 
			String authorName, String description, String diff) {
		this.version = version;
		date = new GregorianCalendar( yyyy, MM-1, dd, hh, mm, ss );
		author = CVSAuthor.getInstance(authorName);
		this.description = description;
		this.diff = diff;
	}

	/**
	 * @return the version
	 */
	String getVersion() {
		return version;
	}		
	
	/**
	 * @return the revision date in milli seconds
	 */
	public long getDateInMillis() {
		return date.getTimeInMillis();
	}
	
	/**
	 * @return the revision date in [yyyy/MM/dd hh:mm:ss] format
	 */
	public String getDateInString() {
		int yyyy = date.get(Calendar.YEAR);
		int MM = date.get(Calendar.MONTH)+1;
		int dd = date.get(Calendar.DAY_OF_MONTH);
		int hh = date.get(Calendar.HOUR_OF_DAY);
		int mm = date.get(Calendar.MINUTE);
		int ss = date.get(Calendar.SECOND);
		
		return yyyy+"/"+MM+"/"+dd+" "+hh+":"+mm+":"+ss;
	}

	/**
	 * @return the author
	 */
	CVSAuthor getAuthor() {
		return author;
	}
	
	public boolean after(CVSRevision rev) {
		return date.after( rev.date );
	}
	
	public boolean before(CVSRevision rev) {
		return date.before( rev.date );
	}
	
	public int compareTo(Object o) {
		if( o instanceof CVSRevision ) {
			CVSRevision rev = (CVSRevision)o;
			return after(rev) ? 1 : -1;
		}
		else
			return 0;
	}

	/**
	 * @return the description
	 */
	String getDescription() {
		return description;
	}

	/**
	 * @return the diff
	 */
	String getDiff() {
		return diff;
	}
}
