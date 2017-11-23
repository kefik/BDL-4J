package cz.gemrot.phd.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map containing other maps. Whenever a map under some key is requested and does not exists,
 * the HashMapMap automatically creates new one.
 * <p><p>
 * The implementation is unsynchronized, created maps are synchronized (just iteration over the inner-map must
 * be synchronized by the user as described in Java(tm) documentation).
 * <p><p>
 * It's a lazy map, i.e., you have to provide a way for initiailization of the ITEM
 * 
 * @author Jimmy
 *
 * @param <PRIMARY_KEY>
 * @param <SECONDARY_KEY>
 * @param <ITEM>
 */
public class LazyMapMap<PRIMARY_KEY, SECONDARY_KEY, ITEM> extends HashMap<PRIMARY_KEY, Map<SECONDARY_KEY, ITEM>> {
	
	/**
	 * Auto-generated.
	 */
	private static final long serialVersionUID = -4541899270970246601L;
	int secondaryCapacity;
	
	public LazyMapMap() {
		secondaryCapacity = 16; 
	}
	
	public LazyMapMap(int primaryCapacity, int secondaryCapacity)
	{
		super(primaryCapacity);
		this.secondaryCapacity = secondaryCapacity;
	}
	
	protected ITEM newItem(PRIMARY_KEY primaryKey, SECONDARY_KEY secondaryKey) {
		return null;
	}

	/**
	 * The get method ensures that the requested map under primaryKey always exists!
	 * 
	 * @param primaryKey must be instance of PRIMARY_KEY
	 */
	@Override
	public Map<SECONDARY_KEY, ITEM> get(Object primaryKey) {
		Map<SECONDARY_KEY, ITEM> map = super.get(primaryKey);
		if (map != null) return map;
		map = new HashMap<SECONDARY_KEY, ITEM>(secondaryCapacity);
		super.put((PRIMARY_KEY)primaryKey, map);
		return map;
	}
	
	/**
	 * Returns an item under primary and secondary key if exists (otherwise it creates one and returns it).
	 * @param primaryKey
	 * @param secondaryKey
	 * @return
	 */
	public ITEM get(PRIMARY_KEY primaryKey, SECONDARY_KEY secondaryKey) {
		ITEM item = get(primaryKey).get(secondaryKey);
		if (item != null) return item;
		item = newItem(primaryKey, secondaryKey);
		put(primaryKey, secondaryKey, item);
		return item;		
	}
	
	/**
	 * Inserts an item under primary and then secondary key.
	 * @param primaryKey
	 * @param secondaryKey
	 * @param item
	 */
	public ITEM put(PRIMARY_KEY primaryKey, SECONDARY_KEY secondaryKey, ITEM item) {
		return get(primaryKey).put(secondaryKey, item);
	}
	
	/**
	 * Remove returns the removed item, if item was non-existent, it returns empty map. 
	 * @param primaryKey
	 * @return
	 */
	@Override
	public Map<SECONDARY_KEY, ITEM> remove(Object primaryKey) {
		Map<SECONDARY_KEY, ITEM> map = super.remove(primaryKey);
		if (map != null) return map;
		return new HashMap<SECONDARY_KEY, ITEM>(secondaryCapacity);
	}

	/**
	 * Removes an item from the map.
	 * @param primaryKey
	 * @param secondaryKey
	 * @return
	 */
	public ITEM remove(PRIMARY_KEY primaryKey, SECONDARY_KEY secondaryKey) {
		if (!containsKey(primaryKey)) return null;
		Map<SECONDARY_KEY, ITEM> map = get(primaryKey);
		ITEM result = map.remove(secondaryKey);
		if (map.size() == 0) {
			remove(primaryKey);
		}
		return result;
	}

}
