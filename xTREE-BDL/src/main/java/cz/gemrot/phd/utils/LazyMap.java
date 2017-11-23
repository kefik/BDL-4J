package cz.gemrot.phd.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maps whose items are initialized on demand by {@link #newItem(Object)} method.
 * 
 * @author Jakub Gemrot
 */
public class LazyMap<KEY, ITEM> implements Map<KEY, ITEM>, Serializable {

    Map<KEY, ITEM> map = null;

    /**
     * Creates value for given key.
     * @param key
     * @return
     */
    protected ITEM newItem(KEY key) {
    	return null;
    }

    public LazyMap() {
        map = new HashMap<KEY, ITEM>();
    }

    public LazyMap(Map<KEY, ITEM> baseMap) {
        map = baseMap;
    }


    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public ITEM get(Object key) {
        ITEM item = map.get((KEY)key);
        if(item == null) {
        	synchronized(this) {
        		item = map.get((KEY)key);
        		if (item != null) return item;
	            item = newItem((KEY)key);
	            if(item != null) {
	                map.put((KEY)key, item);
	            }
        	}
        }
        return item;
    }

    @Override
    public ITEM put(KEY key, ITEM value) {
    	return map.put(key, value);
    }

    @Override
    public ITEM remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends KEY, ? extends ITEM> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<KEY> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<ITEM> values() {
        return map.values();
    }

    @Override
    public Set<Entry<KEY, ITEM>> entrySet() {
        return map.entrySet();
    }

}
