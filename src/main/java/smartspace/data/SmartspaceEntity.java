package smartspace.data;

public interface SmartspaceEntity<K> {

	// Returns the key of the Entity
	public K getKey();

	/**
	 * set a new key to an Entity
	 * 
	 * @param key: a new key to define
	 */
	public void setKey(K key);

}
