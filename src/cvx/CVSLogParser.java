package cvx;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import cvx.cvsdata.CVSFile;
import cvx.cvsdata.CVSRevision;

/**
 *
 * @author rmaker
 */
public abstract class CVSLogParser {
		
	private static final String SEPARATOR = "@";
	protected static final String FILE = "F";
	protected static final String FILE_TOTAL_REVISIONS = "FTR";
	protected static final String VERSION = "V";
	protected static final String DATE = "D";
	protected static final String AUTHOR = "A";
	protected static final String LINE = "L";
	
	protected static final String DIRECTORY_SEPARATOR = "/";
	//protected static final String HOUR_PREFIX = "1";
	protected static final String DATE_SEPARATOR = "/";
	
	protected static final int STARTUP_MODE = 0;
	protected static final int VALID_MODE = 1;
	protected static final int INVALID_MODE = -1;
	
	protected static boolean isValidMode(int mode) {
		return mode == VALID_MODE;
	}
	
	protected static String toContentFormat(String var, String val) {
		return var+SEPARATOR+val;
	}
	
	protected static String toTwoDigitFormat(String dateItem) {
		int len = dateItem.length();
		if( len == 2 ) return dateItem;
		else if( len == 1 ) return "0"+dateItem;
		else return "00";
	}
	
	private String filePath;
	private String cvsRoot;
	
	/**
	 * CONSTRUCTOR
	 * @param filePath
	 * @param cvsRoot
	 */
	protected CVSLogParser(String filePath, String cvsRoot) {
		this.filePath = filePath;
		this.cvsRoot = cvsRoot;
	}
	
	/**
	 * @return the cvs root
	 */
	public String getCVSRoot() {
		return cvsRoot;
	}	

    public void parseCVSLogFile(String filePath) {
        this.filePath = filePath;
        parseCVSLogFile();
    }
    
	/**
	 * TEMPLATE METHOD
	 * A cvs log is analyzed line by line, and interesting information is structured.
	 */
	public void parseCVSLogFile() {
		
		try {
			BufferedReader r = new BufferedReader(new FileReader(filePath));
			FileWriter fw = new FileWriter("outputs.txt"); 
			Vector<String> contents = new Vector<String>();
			int numLines = 0;
			int mode = STARTUP_MODE;
			while( true ) {
				String aLine = r.readLine();
				if( aLine == null ) break;

				numLines++;
				
				aLine = aLine.replaceAll( "\'", "" );
				
				mode = toStandardFormat(aLine, mode, contents,fw);
			}
			
			fw.flush();
			CVSFile file = null;
			int totalRevisions = 0;
			String version = "";
			int yyyy = 0;
			int MM = 0;
			int dd = 0;
			int hh = 0;
			int mm = 0;
			int ss = 0;
			int avgM = 0;
			String author = "";
			for( int i=0; i<contents.size(); i++ ) {
				String aLine = contents.get(i);
				//System.out.println(aLine);
				String tag = aLine.split(SEPARATOR)[0];
				String info = aLine.split(SEPARATOR)[1];
				
				if( tag.equals(FILE) ) {
					String name = info;
					file = CVSFile.getInstance(name);
				}
				else if( tag.equals(FILE_TOTAL_REVISIONS) ) {
					totalRevisions = Integer.valueOf(info);
					file.setTotalRevisions(totalRevisions);
				}
				else if( tag.equals(VERSION) ) {
					version = info;
				}
				else if( tag.equals(DATE) ) {
					yyyy = Integer.valueOf(info.split(DATE_SEPARATOR)[0]);
					MM = Integer.valueOf(info.split(DATE_SEPARATOR)[1]);
					dd = Integer.valueOf(info.split(DATE_SEPARATOR)[2]);
					hh = Integer.valueOf(info.split(DATE_SEPARATOR)[3]);
					mm = Integer.valueOf(info.split(DATE_SEPARATOR)[4]);
					ss = Integer.valueOf(info.split(DATE_SEPARATOR)[5]);
				}
				else if( tag.equals(AUTHOR) ) {
					author = info;
					// TODO: 마지막 인자 두 개 채울 것
					CVSRevision revision = new CVSRevision(version, yyyy, MM, dd, hh, mm, ss, author, "", "");
					file.addRevision(revision);
				}
			}
			
			System.out.println( numLines+" lines analyzed." );
			
		}
		catch( FileNotFoundException fnfe ) {
			System.err.println( "NO SUCH FILE: "+filePath );
			fnfe.printStackTrace();
		}
		catch( IOException ioe ) {
			System.err.println( "IMPOSSIBLE TO READ: "+filePath );
			ioe.printStackTrace();
		}
	}
	
	protected abstract int toStandardFormat(String line2, int mode, Vector<String> contents, FileWriter fw) throws IOException;

	/**
	 * HOOK METHOD
	 * [Standard Format] 
	 * FILE - dir/dir/dir/file
	 * DATE - yyyy/MM/dd/hh/mm/ss (yyyy must be 4 digit, the others are 1~2 digit.)
	 * @param aLine
	 * @param mode
	 * @param contents
	 * @return a new mode produced by [aLine]
	 */
	protected abstract int toStandardFormat(String aLine, int mode, Vector<String> contents);
	
}
