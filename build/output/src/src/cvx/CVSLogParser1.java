package cvx;

import java.io.FileWriter;
import java.util.Vector;

/**
 *
 * @author rmaker
 */
public class CVSLogParser1 
extends CVSLogParser {
	
	public CVSLogParser1(String filePath, String cvsRoot) {
		super(filePath, cvsRoot);
	}
	
	/* (non-Javadoc)
	 * @see cvs.CVSLog#toStandardFormat(java.lang.String, int, java.util.Vector)
	 */
	protected int toStandardFormat(String aLine, int mode, Vector<String> contents) {
		
		if( aLine.startsWith("Rcs file") ) {
			if( aLine.indexOf("/Attic/") >= 0 ) 
				return INVALID_MODE;
			else if( aLine.indexOf("/test/") >= 0 || aLine.indexOf("/tests/") >= 0 ) 
				return INVALID_MODE;
			
			int beginIndex = aLine.indexOf( getCVSRoot() )+getCVSRoot().length();
			int endIndex = aLine.indexOf( ".java" );
			if( beginIndex > 0 && endIndex > beginIndex ) {
				String file = aLine.substring( beginIndex, endIndex );
				//file = file.replaceAll( "/", "." );
				contents.add( toContentFormat(FILE, file) );
				return VALID_MODE;
			}
		}
		else if( isValidMode(mode) ) {
			if( aLine.startsWith("Total revisions") ) {
				String totalRevisions = aLine.split("Total revisions :")[1].trim();
				contents.add( toContentFormat(FILE_TOTAL_REVISIONS, totalRevisions) );
	        }
			else if( aLine.startsWith("Revision") ) {
				String version = aLine.split("Revision :")[1].trim();
				contents.add( toContentFormat(VERSION, version) );
			}
			else if( aLine.startsWith("Date") ) {
				String date = aLine.split("Date :")[1].trim();
				
				String yyyyMMdd = date.split(" ")[0].trim();
				String yyyy = yyyyMMdd.split("/")[0].trim();
				String MM = yyyyMMdd.split("/")[1].trim();
				String dd = yyyyMMdd.split("/")[2].trim();
				
				String hhmmss = date.split(" ")[1].trim();
				String hh = hhmmss.split(":")[0].trim();
				String mm = hhmmss.split(":")[1].trim();
				String ss = hhmmss.split(":")[2].trim();
								
				date = yyyy+DATE_SEPARATOR+MM+DATE_SEPARATOR+dd+DATE_SEPARATOR
						+hh+DATE_SEPARATOR+mm+DATE_SEPARATOR+ss;
				contents.add( toContentFormat(DATE, date) );
			}
			else if( aLine.startsWith("Author") ) {
				String author = aLine.split("Author :")[1].trim();
				contents.add( toContentFormat(AUTHOR, author) );
			}
			return VALID_MODE;
		}
		
		return INVALID_MODE;
	}

	@Override
	protected int toStandardFormat(String line2, int mode, Vector<String> contents, FileWriter fw) {
		// TODO Auto-generated method stub
		return this.toStandardFormat(line2, mode, contents);
	}	
}
