package cvx.cvsdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import util.ImmutableCollection;

public class CVSModule implements Serializable {

	private static final long serialVersionUID = -3463373290607081954L;
	
	public static final String MODULE_SEPARATOR = "/";
	
	static String getSimpleModuleName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf(MODULE_SEPARATOR));
	}
	
	private static Map<String, CVSModule> instances = new HashMap<String, CVSModule>();
	
	static ImmutableCollection<CVSModule> instances() {
		return new ImmutableCollection<CVSModule>(instances.values());
	}
	
	/**
	 * FACTORY METHOD
	 * @param name
	 * @return a CVSModule instance || one in the pool
	 */
	static CVSModule getInstance(String name) {
		if( instances.containsKey(name) ) 
			return instances.get(name);
		else {
			CVSModule instance = new CVSModule(name, instances.size());
			instances.put(name, instance);
			return instance;
		}
	}

	public static void regist(CVSModule module) {
		if (!instances.containsKey(module.name))
			instances.put(module.name, module);
	}

	public static void clearAll() {
		instances.clear();
	}
		
	private String name;
	private int id;
	
	/**
	 * CONSTRUCTOR
	 * @param fileName
	 */
	private CVSModule(String name, int id) {
		this.name = name; 
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return ""+id;
	}

    public String getName() {
        return name;
    }	
}
