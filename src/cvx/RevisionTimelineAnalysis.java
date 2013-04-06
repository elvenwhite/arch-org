package cvx;

import java.util.Calendar;
import java.util.GregorianCalendar;

import util.ImmutableCollection;
import cvx.cvsdata.CVSAuthor;
import cvx.cvsdata.CVSDatabase;
import cvx.cvsdata.CVSRevision;
import framework.Pipe;

public class RevisionTimelineAnalysis {
	
	public void analyze(CVSDatabase db, Pipe pipe) {

		CVSRevision allRevisionsStart = null;
		CVSRevision allRevisionsEnd = null;
		
		for(CVSAuthor author: db.getAuthors()) {
			System.out.println( author );
			
			int num = 0;
			long sum = 0;			
			CVSRevision prevRevision = null;
			CVSRevision revisionsStart = null;
			CVSRevision revisionsEnd = null;
			
			ImmutableCollection<CVSRevision> revisions = db.getRevisionsByAuthor(author); 
			for(CVSRevision revision: revisions) {
				
				if( prevRevision == null || revision.after(prevRevision) ) {
					//System.out.println( "\t"+revision.getDateInString()+" ("+revision.getDateInMillis()+")" );
					prevRevision = revision;
					num++;
					sum += revision.getDateInMillis();
				}
				
				if( revisionsStart == null || revision.before(revisionsStart) )
					revisionsStart = revision;
				if( revisionsEnd == null || revision.after(revisionsEnd) )
					revisionsEnd = revision;
				
				if( allRevisionsStart == null || revision.before(allRevisionsStart) )
					allRevisionsStart = revision;
				if( allRevisionsEnd == null || revision.after(allRevisionsEnd) )
					allRevisionsEnd = revision;
			}
			
			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(sum/num);
			int yyyy = c.get(Calendar.YEAR);
			int MM = c.get(Calendar.MONTH)+1;
			int dd = c.get(Calendar.DAY_OF_MONTH);
			int hh = c.get(Calendar.HOUR_OF_DAY);
			int mm = c.get(Calendar.MINUTE);
			int ss = c.get(Calendar.SECOND);

			// TODO: CVSAuthor에서 Revision을 제거했음
			System.out.println( " @("+revisions.size()+","+num+")> "
									+yyyy+"/"+MM+"/"+dd+" "+hh+":"+mm+":"+ss
									+" ["+revisionsStart.getDateInString()
									+" ~ "+revisionsEnd.getDateInString()+"]" );
		}
		
		System.out.println( "DURATION> ["+allRevisionsStart.getDateInString()
								+" ~ "+allRevisionsEnd.getDateInString()+"]" );
	}
	
}
