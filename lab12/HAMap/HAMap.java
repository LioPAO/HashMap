import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 *  Hash-based Map
 */
public class HAMap<K, V> implements Iterable<K> {

    /**
     * Represents a key-value pair.
     */
    private class Entry {
        K key;
        V value;

        Entry(K k, V v) {
            key = k;
            value = v;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 1.5;

    private ArrayList<ArrayList<Entry>> buckets;
    private HashSet<K> keySet;
    private int numBuckets;
    private int numEntries;
    private final double loadFactor;
    private double currentLoadFactor = (double)numEntries/(double)numBuckets ;

    /**
     * @return a set of the keys contained in this map.
     */
    public HashSet<K> keySet() {
        return keySet;
    }

    /**
     * @return the number of entries in this map.
     */
    public int size() {
        return numEntries;
    }

    /**
     * @return the number of buckets in this map.
     */
    public int getNumBuckets() {
        return numBuckets;
    }

    /*
     ***************************
     * DO NOT MODIFY CODE ABOVE
     ***************************
     */


    /*
     ***** HELPER METHODS START *****
     */

    
	// INCLUDE your helper methods in EACH of your submissions that use them

    //Computes the code for a given key
    private int getHashIndex(K key, int capacity){
        int code = Math.floorMod(key.hashCode(),capacity);
        return code;
    }

    //resizes the bucket when loadfactor is exceeded
    private void resize(int newCapacity){
        ArrayList<ArrayList<Entry>> newBucket = new ArrayList<>(); // Creates new Bucket with new capacity (double the old)
        for (int i=0; i<newCapacity; i++){
            newBucket.add(new ArrayList<Entry>());
        }

        Iterator<K> it = this.iterator(); //Uses iterator "it" to iterate through all keys found in HAMap (keySet)
        while (it.hasNext()){
            int newIndex = getHashIndex(it.next(), newCapacity);  // Computes new index for key based on new capacity
            newBucket.get(newIndex).add(new Entry(it.next(), getValue(it.next()))); // adds the key and corresponding value in a new Arraylist
        }
        buckets = newBucket;    //Updates pointer
        numBuckets = newCapacity;   // Updates size of HAMap
    }

    //Changes the value of a key to a new value
    private void setNewValue (K key, V value){

        if (!containsKey(key)) return;
        ArrayList<Entry> arrayListWhichStoresKey =  getArrayListWhichStoresKey(key);

        int sizeOfArrayListWhereKeyIsStored = arrayListWhichStoresKey.size(); //size Of ArrayList Where Key Is Stored

        for(int i=0; i<sizeOfArrayListWhereKeyIsStored; i++ ){ //Loops through the arrayList where key is stored and checks if the keys match.

            if(arrayListWhichStoresKey.get(i).key.equals(key)){
                arrayListWhichStoresKey.get(i).value = value; // Sets value to new value
            }
        }
    }
    // returns the array list which stores a given key
    public ArrayList<Entry> getArrayListWhichStoresKey (K key){
        int index = getHashIndex(key,numBuckets); // gets index of key
        ArrayList<Entry> arrayListWhichStoresKey = buckets.get(index); //Reference to arrayList which stores the key
        return arrayListWhichStoresKey;
    }



    /*
     ***** HELPER METHODS END *****
     */


    // LAB EXERCISE 12.2 CONSTRUCTORS

    public HAMap(int initialCapacity, double loadFactor) {
        buckets = new ArrayList<ArrayList<Entry>>();
        for (int i=0; i<initialCapacity;i++){
            buckets.add(new ArrayList<Entry>());
        }
        keySet = new HashSet<>();
        numBuckets = initialCapacity;
        numEntries = 0;
        this.loadFactor = loadFactor;
    }

    public HAMap() {
        this(DEFAULT_CAPACITY,DEFAULT_LOAD_FACTOR);
    }

    public HAMap(int initialCapacity) {
        this(initialCapacity,DEFAULT_LOAD_FACTOR);
    }


    // LAB EXERCISE 12.3 CLEAR

    /**
     * Removes all of the entries from this map.
     */
    public void clear() {
        for (ArrayList<Entry> buck: buckets) {
            buck = new ArrayList<Entry>();
        }
        numEntries = 0;
            keySet = new HashSet<>();
    }


    // LAB EXERCISE 12.4 CONTAINS KEY and ITERATOR

    /**
     * @param key to be checked
     * @return true iff this map contains an entry with the specified key
     */
    public boolean containsKey(K key) {
		return keySet.contains(key);
    }

    /**
     * @return an Iterator that iterates over the stored keys
     */
    @Override
    public Iterator<K> iterator() {
		return keySet.iterator();
    }

    // EXERCISE 12.1 GET

    /**
     * @param key of the value to be returned
     * @return the value to which the specified key is mapped
     *         null if this map contains no entries of the key
     */
    public V getValue(K key) {
        if (this.containsKey(key)) {
            ArrayList<Entry> arrayListWhichStoresKey = getArrayListWhichStoresKey(key); //Reference to arrayList which stores Key
            for (int i = 0; i < arrayListWhichStoresKey.size(); i++) {
                if (arrayListWhichStoresKey.get(i).key.equals(key)) {
                    V value = (V) arrayListWhichStoresKey.get(i).value;
                    return value;
                }
            }
        }
		return null;
    }

    // EXERCISE 12.2 PUT

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained an entry with that key, the old value is replaced.
     * The key is not null.
     * @param key of the entry to be added
     * @param value of the entry to be added
     */
    public void put(K key, V value) {
        if (currentLoadFactor>loadFactor) { resize(numBuckets*2);}
        if (containsKey(key)){
            setNewValue(key,value);
        }

        int index = getHashIndex(key,numBuckets);
        buckets.get(index).add(new Entry(key,value));
        numEntries++;
        keySet.add(key);
        

    }
	
    // EXERCISE 12.3 REMOVE

    /**
     * Removes the entry for the specified key only if it is
     * currently mapped to the specified value.
     * @param key of the entry to be removed
     * @param value of the entry to be removed
     * @return the value if entry found,
     *         null otherwise
     */
    public V remove(K key, V value) {
        if (!containsKey(key)){ return null;}
            V removedValue = null;
            ArrayList<Entry> arrayListWhichStoresKey = getArrayListWhichStoresKey(key);

            for (int i = 0; i < arrayListWhichStoresKey.size(); i++) {

                if (arrayListWhichStoresKey.get(i).key.equals(key) && arrayListWhichStoresKey.get(i).value.equals(value)) {
                    removedValue = arrayListWhichStoresKey.get(i).value;
                    arrayListWhichStoresKey.remove(i);
                    numEntries--;
                    break;
                }
            }
            return removedValue;
    }

}
