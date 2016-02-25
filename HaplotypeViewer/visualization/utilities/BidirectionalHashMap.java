package visualization.utilities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class BidirectionalHashMap<K1,K2>  {
	
	protected HashMap<K1,K2> left = new HashMap<K1, K2>();
	protected HashMap<K2,K1> right= new HashMap<K2, K1>();

	
	public void put(K1 k1, K2 k2) {
		left.put(k1,k2);
		right.put(k2,k1);
	}
	
	public void remove(Object key) {
		if (left.containsKey(key))
			right.remove(left.remove(key));
		else
			left.remove(right.remove(key));
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		Object ret = left.get(key);
		if (ret==null)
			ret = right.get(key);
		return (T)ret;
	}	
	
	public int size() {
		return left.size();
	}
	
	public Collection<K1> getLeftElements() {
		return Collections.unmodifiableSet(left.keySet());
	}
	public Collection<K2> getRightElements() {
		return Collections.unmodifiableSet(right.keySet());
	}

	public void clear() {
		left.clear();
		right.clear();
		
	}

	@SuppressWarnings("unchecked")
	public <T> T getRight(Object key) {
		Object ret = right.get(key);
		return (T)ret;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getLeft(Object key) {
		Object ret = left.get(key);
		return (T)ret;
	}
}
