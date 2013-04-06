package util;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableCollection<T> 
implements Iterable<T> {
	
	private Collection<T> collection;
	
	public ImmutableCollection(Collection<T> collection) {
		this.collection = collection;
	}
	
	public Iterator<T> iterator() {
		return collection.iterator();
	}
	
	public int size() {
		return collection.size();
	}
}
