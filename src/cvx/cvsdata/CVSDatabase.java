package cvx.cvsdata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import util.ImmutableCollection;
import util.SortedSetMap;

/**
 *	Singleton CVS log database object
 * @author rmaker
 */
public class CVSDatabase {
	
	private static final double MAX_DISTANCE = 1000000;
	private static CVSDatabase instance = new CVSDatabase();
	
	public static CVSDatabase instance() { 
		return instance;
	}
	
	private CVSDatabase() {}
	
	//----------------------------------------------------------------------
	
	public ImmutableCollection<CVSFile> getFiles() {
		return CVSFile.instances();
	}
	
	public ImmutableCollection<CVSModule> getModules() {
		return CVSModule.instances();
	}
	
	public ImmutableCollection<CVSAuthor> getAuthors() {
		return CVSAuthor.instances();
	}
	
	//----------------------------------------------------------------------
	private Map<CVSRevision, CVSFile> revisionFiles = new HashMap<CVSRevision, CVSFile>();
	private Map<CVSRevision, CVSModule> revisionModules = new HashMap<CVSRevision, CVSModule>();
	
	private SortedSetMap<CVSAuthor, CVSRevision> revisionsByAuthor 
		= new SortedSetMap<CVSAuthor, CVSRevision>();
	
	private SortedSetMap<String, CVSRevision> revisionsByAuthorAndModule 
		= new  SortedSetMap<String, CVSRevision>();
	
	public void load() {
			
		for(CVSFile file: getFiles()) {
			
			CVSModule module = file.getModule();
			
			for(CVSRevision revision: file.getRevisions()) {
				
				CVSAuthor author = revision.getAuthor();
				
				revisionFiles.put(revision, file);
				revisionModules.put(revision, module);
				
				revisionsByAuthor.put(author, revision);
				revisionsByAuthorAndModule.put(getAuthorModuleKey(author,module), revision);
			}
		}
		
		System.out.println( getFiles().size()+" CVS files loaded." );
		System.out.println( getAuthors().size()+" authors have contributed." );
		
		System.out.println( getRevisionsByAuthor(CVSAuthor.getInstance("teicher")).size() +"teicher");
		System.out.println( getRevisionsByAuthor(CVSAuthor.getInstance("akiezun")).size() +"akiezun");


	}
	
	public static void clearAll() {
		
	}
	
	public CVSFile getRevisionFile(CVSRevision revision) {
		return revisionFiles.get(revision);
	}
	
	public CVSModule getRevisionModule(CVSRevision revision) {
		return revisionModules.get(revision);
	}
	
	public ImmutableCollection<CVSRevision> getRevisionsByAuthor(CVSAuthor author) {
		if( revisionsByAuthor.containsKey(author) )
			return revisionsByAuthor.get(author);
		else 
			return new ImmutableCollection<CVSRevision>(new HashSet<CVSRevision>());
	}
	
	public int numRevisionsByAuthor(CVSAuthor author) {
		return getRevisionsByAuthor(author).size();
	}
	
	public ImmutableCollection<CVSRevision> getRevisionsByAuthorAndModule(CVSAuthor author, CVSModule module) {
		String key = getAuthorModuleKey(author,module);
		if( revisionsByAuthorAndModule.containsKey(key) )
			return revisionsByAuthorAndModule.get(key);
		else 
			return new ImmutableCollection<CVSRevision>(new HashSet<CVSRevision>());
	}
	
	public int numRevisionsByAuthorAndModule(CVSAuthor author, CVSModule module) {
		return getRevisionsByAuthorAndModule(author,module).size();
	}
	
	private static String getAuthorModuleKey(CVSAuthor author, CVSModule module) {
		return author.toString()+module.toString();
	}



	public double calculateDistance(CVSAuthor a1, CVSAuthor a2, CVSModule m) {
		ImmutableCollection<CVSRevision> rev1 = getRevisionsByAuthorAndModule(a1,m);
		ImmutableCollection<CVSRevision> rev2 = getRevisionsByAuthorAndModule(a2,m);
		
		
		if (rev1.size()==0 ||rev2.size()==0) return MAX_DISTANCE;
		
		long[] date1 = new long[rev1.size()];
		long[] date2 = new long[rev2.size()];
		
		Iterator<CVSRevision> itr1 = rev1.iterator();
		Iterator<CVSRevision> itr2 = rev2.iterator();

		long MIN_DATE=250000; //should be found in context of project era
		int i = 0;
		for (i=0;i<rev1.size();i++) {
			CVSRevision rev = itr1.next();
			date1[i]= rev.getDateInMillis()/3600000 - MIN_DATE;
//			System.out.println(i+"/"+date1[i]);
		}
		for (i=0;i<rev2.size();i++) {
			CVSRevision rev = itr2.next();
			date2[i]= rev.getDateInMillis()/3600000-MIN_DATE;
//			System.out.println(i+"\\"+date2[i]);
		}
		
        double sum= 0.0;
        int cap = date1.length>=date2.length?date1.length:date2.length;
        int j=0;
        i=0;
        
        
        
        if (date1.length>=date2.length) {

	        for(i=0;i<cap;i++) {
	     
		           sum = sum + Math.pow((date1[i]-date2[j]),2.0);
		           j++;
		         if (j>=date2.length) j=0;
	        }
        } else if (date1.length<date2.length) {

        	j=0;
	        for(i=0;i<cap;i++) {

		           sum = sum + Math.pow((date1[j]-date2[i]),2.0);
		           j++;
		         if (j>=date1.length) j=0;
	        } 
        }
//        System.out.println(Math.sqrt(sum));
        
        return Math.sqrt(sum)/10000;		
	}


}
