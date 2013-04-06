package cvx.cvsdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import util.ImmutableCollection;

public class CVSAuthor implements Serializable {

	private static final long serialVersionUID = 723358533061375113L;
	
	private static Map<String, CVSAuthor> instances = new HashMap<String, CVSAuthor>();
	
	/**
	 * @return the authors iterator
	 */
	static ImmutableCollection<CVSAuthor> instances() {
		return new ImmutableCollection<CVSAuthor>(instances.values());
	}
	
	/**
	 * FACTORY METHOD
	 * @param name
	 * @return an CVSAuthor instance || one in the pool
	 */
	static CVSAuthor getInstance(String name) {
		if( instances.containsKey(name) ) 
			return instances.get(name);
		else {
			CVSAuthor instance = new CVSAuthor(name, instances.values().size());
			instances.put(name, instance);
			return instance;
		}
	}

	public static void regist(CVSAuthor author) {
		if (!instances.containsKey(author.name)) {
			instances.put(author.name, author);
		}
	}

	public static void clearAll() {
		instances.clear();		
	}
	
	private String name;
	private int id;	// id>0

	/**
	 * CONSTRUCTOR
	 * @param name
	 */
	private CVSAuthor(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
		
	public String toString() {
		return getName();
	}
}
