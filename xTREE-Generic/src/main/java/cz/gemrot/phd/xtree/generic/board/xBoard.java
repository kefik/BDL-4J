package cz.gemrot.phd.xtree.generic.board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.gemrot.phd.utils.sockets.ConfigMap;
import cz.gemrot.phd.xtree.generic.iface.IxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint;

/**
 * Generic hierarchical BlackBoard to be used by the xTree.
 * 
 * May be used to pass arbitrary parameters and can be hierarchically composed. 
 * 
 * @author Jakub Gemrot
 */
public class xBoard {
	
	/**
	 * Parent board of this one, if any.
	 */
	private xBoard parent = null;
	
	/**
	 * Values stored within this board in an associative manner.
	 */
	private Map<String, Object> board;
	
	private Map<Object, Set<String>> support2Keys;
	
	private Map<String, Object> key2Support;
		
	public void setParent(xBoard parent) {
		this.parent = parent;
	}
	
	public Map<String, Object> getBoard() {
		if (board == null) this.board = new HashMap<String, Object>();
		return board;
	}
	
	public void set(String key, Object value) {
		if (board == null) board = new HashMap<String, Object>();
		board.put(key, value);
	}
	
	public void set(Entry<String, Object>... args) {
		if (board == null) board = new HashMap<String, Object>();
		for (Entry<String, Object> arg : args) {
			board.put(arg.getKey(), arg.getValue());
		}
	}
	
	public void set(ConfigMap userArgs) {
		if (userArgs == null || userArgs.size() == 0)  return;
		if (board == null) board = new HashMap<String, Object>();
		for (Entry<String, Object> arg : userArgs.entrySet()) {
			board.put(arg.getKey(), arg.getValue());
		}
	}
	
	public void deltaInt(String key, int delta) {
		set(key, getInt(key, 0) + delta);
	}
	
	public void incInt(String key) {
		deltaInt(key, 1);
	}
	
	public void decInt(String key) {
		deltaInt(key, -1);
	}
	
	public Object get(String key, Object defaultValue) {
		if (board == null) return defaultValue;
		if (board.containsKey(key)) return board.get(key);
		if (parent != null) return parent.get(key, defaultValue);
		return defaultValue;
	}
	
	public <T> T getTyped(String key, Class<T> type, T defaultValue) {
		if (board == null) return defaultValue;
		if (board.containsKey(key)) {
			Object value = board.get(key);
			if (value != null && type.isAssignableFrom(value.getClass())) return type.cast(value);
			if (parent != null) return parent.getTyped(key, type, defaultValue);
			return defaultValue;
		}
		return defaultValue;
	}
	
	public String getString(String key, String defaultValue) {
		return getTyped(key, String.class, defaultValue);
	}
	
	public int getInt(String key, int defaultValue) {
		return getTyped(key, Integer.class, defaultValue);
	}
	
	public double getDouble(String key, double defaultValue) {
		return getTyped(key, Double.class, defaultValue);
	}
	
	public float getFloat(String key, float defaultValue) {
		return getTyped(key, Float.class, defaultValue);
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		return getTyped(key, Boolean.class, defaultValue);
	}
	
	public IxTreePoint<?> getPoint(String key) {
		return getTyped(key, IxTreePoint.class, null);
	}
	
	public IxSignal getSignal(String key) {
		return getTyped(key, IxSignal.class, null);
	}

	public boolean has(String key) {
		if (board.containsKey(key)) return true;
		if (parent != null) return parent.has(key);
		return false;
	}
	
	public <T> boolean hasTyped(String key, Class<T> type) {
		Object value = board.get(key);
		if (value != null && type.isAssignableFrom(value.getClass())) return true;
		if (parent != null) return hasTyped(key, type);
		return false;
	}
	
	public void clear() {
		if (board != null) board.clear();
		if (support2Keys != null) support2Keys.clear();
		if (key2Support != null) key2Support.clear();
	}
	
	// =============
	// SUPPORT UTILS	
	// =============

	public void set(Object support, String key, Object value) {
		if (board == null) board = new HashMap<String, Object>();
		initSupports();
		
		board.put(key, value);
		
		Set<String> supportKeys = support2Keys.get(support);
		if (supportKeys == null) {
			supportKeys = new HashSet<String>();
			support2Keys.put(support, supportKeys);
		}
		
		key2Support.put(key, support);
		supportKeys.add(key);
	}
	
	public void set(Object support, Entry<String, Object>... args) {
		initSupports();
		
		Set<String> supportKeys = support2Keys.get(support);
		if (supportKeys == null) {
			supportKeys = new HashSet<String>();
			support2Keys.put(support, supportKeys);
		}
		
		for (Entry<String, Object> arg : args) {
			board.put(arg.getKey(), arg.getValue());
			key2Support.put(arg.getKey(), support);
			supportKeys.add(arg.getKey());
		}
	}
	
	public void wipeSupport(Object support) {
		if (support2Keys == null) return;
		Set<String> keys = support2Keys.remove(support);
		if (keys == null) return;
		for (String key : keys) {
			board.remove(key);			
			key2Support.remove(key);
		}		
	}	
	
	private void initSupports() {
		if (support2Keys != null) return;
		support2Keys = new HashMap<Object, Set<String>>();
		key2Support = new HashMap<String, Object>();
	}
	
}
