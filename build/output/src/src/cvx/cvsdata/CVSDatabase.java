package cvx.cvsdata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import util.ImmutableCollection;
import util.SortedSetMap;

/**
 *	Singleton CVS log database object
 * @author rmaker
 */
public class CVSDatabase {
	
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
}
