package cvx;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class CVSLogParser2 
extends CVSLogParser {
	
	int modsum=0;
	
	public CVSLogParser2(String filePath, String cvsRoot) {
		super(filePath, cvsRoot);
	}
	
	/* (non-Javadoc)
	 * @see cvs.CVSLog#toStandardFormat(java.lang.String, int, java.util.Vector)
	 */
	protected int toStandardFormat(String aLine, int mode, Vector<String> contents) {
		
		
		if( aLine.startsWith("RCS file") ) {
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
			if( aLine.startsWith("total revisions") ) {
				String aLine0 = aLine.split( ";" )[0].trim();
				
				String totalRevisions = aLine0.split("total revisions:")[1].trim();
				contents.add( toContentFormat(FILE_TOTAL_REVISIONS, totalRevisions) );
	        }
			else if( aLine.startsWith("revision") ) {
				String version = aLine.split("revision")[1].trim();
				contents.add( toContentFormat(VERSION, version) );
			}
			else if( aLine.startsWith("date") ) {
				String aLine0 = aLine.split( ";" )[0].trim();
				String aLine1 = aLine.split( ";" )[1].trim();
				String aLine2 = aLine.split( ";" )[2].trim(); //state
				
				String date = aLine0.split("date:")[1].trim();
				
				String yyyyMMdd = date.split(" ")[0].trim();
				String yyyy = yyyyMMdd.split("-")[0].trim();
				String MM = yyyyMMdd.split("-")[1].trim();
				String dd = yyyyMMdd.split("-")[2].trim();
				
				String hhmmss = date.split(" ")[1].trim();
				String hh = hhmmss.split(":")[0].trim();
				String mm = hhmmss.split(":")[1].trim();
				String ss = hhmmss.split(":")[2].trim();
				
				int p=0;
				int m=0;
				String mod = "0";
				
				if (aLine.split(";").length==4){
					String aLine3 = aLine.split( ";" )[3].trim(); //lines
					
					if (aLine3.contains("+")){
//						System.out.println(aLine3);
					String pmline = aLine3.split("lines:")[1].trim();
//					System.out.println(pmline);
					String ppline = pmline.substring(1).trim();
//					System.out.println(ppline);
					String[] pm=ppline.split("-");
					String pline=pm[0].trim();
					String mline=pm[1].trim();
					
					
					
					p = (int)Integer.valueOf(pline);
					m = (int)Integer.valueOf(mline);
					modsum=modsum+(p+m);
//					System.out.println("!+"+p+"-"+m);	
					}
				}
				mod = String.valueOf(p+m);
				
				date = yyyy+DATE_SEPARATOR+MM+DATE_SEPARATOR+dd+DATE_SEPARATOR
						+hh+DATE_SEPARATOR+mm+DATE_SEPARATOR+ss;
				contents.add( toContentFormat(DATE, date) );
				
				contents.add(toContentFormat(LINE,mod));
				
				String author = aLine1.split("author:")[1].trim();
				contents.add( toContentFormat(AUTHOR, author) );

			} 

			return VALID_MODE;
		}
		
		return INVALID_MODE;
	}

	@Override
	protected int toStandardFormat(String aLine, int mode, Vector<String> contents, FileWriter fw) throws IOException {
		if( aLine.startsWith("RCS file") ) {
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
			if( aLine.startsWith("total revisions") ) {
				String aLine0 = aLine.split( ";" )[0].trim();
				
				String totalRevisions = aLine0.split("total revisions:")[1].trim();
				contents.add( toContentFormat(FILE_TOTAL_REVISIONS, totalRevisions) );
	        }
			else if( aLine.startsWith("revision") ) {
				String version = aLine.split("revision")[1].trim();
				contents.add( toContentFormat(VERSION, version) );
			}
			else if( aLine.startsWith("date") ) {
				String aLine0 = aLine.split( ";" )[0].trim();
				String aLine1 = aLine.split( ";" )[1].trim();
				String aLine2 = aLine.split( ";" )[2].trim(); //state
				
				String date = aLine0.split("date:")[1].trim();
				
				String yyyyMMdd = date.split(" ")[0].trim();
				String yyyy = yyyyMMdd.split("-")[0].trim();
				String MM = yyyyMMdd.split("-")[1].trim();
				String dd = yyyyMMdd.split("-")[2].trim();
				
				String hhmmss = date.split(" ")[1].trim();
				String hh = hhmmss.split(":")[0].trim();
				String mm = hhmmss.split(":")[1].trim();
				String ss = hhmmss.split(":")[2].trim();
				
				int p=0;
				int m=0;
				String mod = "0";
				
				if (aLine.split(";").length==4){
					String aLine3 = aLine.split( ";" )[3].trim(); //lines
					
					if (aLine3.contains("+")){
//						System.out.println(aLine3);
					String pmline = aLine3.split("lines:")[1].trim();
//					System.out.println(pmline);
					String ppline = pmline.substring(1).trim();
//					System.out.println(ppline);
					String[] pm=ppline.split("-");
					String pline=pm[0].trim();
					String mline=pm[1].trim();
					
					
					
					p = (int)Integer.valueOf(pline);
					m = (int)Integer.valueOf(mline);
					modsum=modsum+(p+m);
//					System.out.println("!+"+p+"-"+m);	
					}
				}
				mod = String.valueOf(p+m);
//				if (!(p+m>1000)) {
//					fw.write(mod+"\n");
//				} else {
//					System.out.println(mod);
//				}
				
				date = yyyy+DATE_SEPARATOR+MM+DATE_SEPARATOR+dd+DATE_SEPARATOR
						+hh+DATE_SEPARATOR+mm+DATE_SEPARATOR+ss;
				contents.add( toContentFormat(DATE, date) );
				
				contents.add(toContentFormat(LINE,mod));
				
				String author = aLine1.split("author:")[1].trim();
				contents.add( toContentFormat(AUTHOR, author) );

			} 

			return VALID_MODE;
		}
		
		return INVALID_MODE;
	} 
}
