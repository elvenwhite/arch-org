package cvx.cvsdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import util.ImmutableCollection;

public class CVSFile implements Serializable{
	
	private static Map<String, CVSFile> instances = new HashMap<String, CVSFile>();
	
	/**
	 * @return the files iterator
	 */
	public static ImmutableCollection<CVSFile> instances() {
		return new ImmutableCollection<CVSFile>(instances.values());
	}
	
	/**
	 * @param name
	 * @return the cvs file of the name, null if there's none. 
	 */
	static CVSFile getCVSFile(String name) {
		return instances.get(name);
	}
	
	/**
	 * @param name
	 * @return a CVSFile instance || one in the pool
	 */
	public static CVSFile getInstance(String name) {
		if( instances.containsKey(name) ) {
			return instances.get(name);
		}
		else {
			CVSFile instance = new CVSFile(name, instances.values().size());
			instances.put(name, instance);
			return instance;
		}
	}

	public static void clearAll() {
		instances.clear();
	}	
		
	public static void regist(CVSFile file) {
		for (CVSRevision revision : file.revisions) {
			CVSAuthor.regist(revision.getAuthor());
		}

		CVSModule.regist(file.module);

		if (!instances.containsKey(file.name)) {
			instances.put(file.name, file);
		}
	}
		
	private String name;
	private int id;
	private CVSModule module;
	private Set<CVSRevision> revisions;
	private int totalRevisions;
		
	/**
	 * CONSTRUCTOR
	 * @param name
	 */
	private CVSFile(String name, int id) {
		this.name = name;
		this.id = id;
		module = CVSModule.getInstance(CVSModule.getSimpleModuleName(name));		
		//XXX: should be TreeSet for sorting revisions..
		revisions = new TreeSet<CVSRevision>();
		totalRevisions = 0;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	CVSModule getModule() {
		return module;
	}
	
	/**
	 * @param totalRevisions
	 */
	public void setTotalRevisions(int totalRevisions) {
		this.totalRevisions = totalRevisions;
	}
	
	public String toString() {
//		return name;
		return ""+id;
	}
		
	/**
	 * A file-revision information is lifed to the level of module.
	 * <Author, Revision> relation is made.
	 * @param rev
	 */
	public void addRevision(CVSRevision rev) {
		revisions.add(rev);
	}

	/**
	 * @return the revisions iterator
	 */
	ImmutableCollection<CVSRevision> getRevisions() {
		return new ImmutableCollection<CVSRevision>(revisions);
	}
		
	/**
	 * If this file is valid, totalRevisions is the same as the number of revisions.
	 * @return
	 */
	public boolean isValid() {
		return totalRevisions == revisions.size();
	}
}
