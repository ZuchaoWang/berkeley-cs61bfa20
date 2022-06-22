import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {
  private class MyHashMapNode {
    public K key;
    public V value;
    public MyHashMapNode(K k, V v) {
      key = k;
      value = v;
    }
  }

  private final int defaultInitialSize = 16;
  private final double defaultLoadfactor = 0.75;
  private final int resizeFactor = 2;

  List<MyHashMapNode>[] hashTable;
  double lf;
  int n;

  public MyHashMap() {
    initMyHashMap(defaultInitialSize, defaultLoadfactor);
  }
  public MyHashMap(int initialSize) {
    initMyHashMap(initialSize, defaultLoadfactor);
  }

  public MyHashMap(int initialSize, double loadFactor) {
    initMyHashMap(initialSize, loadFactor);
  }

  private void initMyHashMap(int initialSize, double loadFactor) {
    hashTable = (List<MyHashMapNode>[]) new List[initialSize];
    lf = loadFactor;
    clear();
  }

  @Override
  public Iterator<K> iterator() {
    Set<K> s = keySet();
    return s.iterator();
  }

  @Override
  public void clear() {
    Arrays.fill(hashTable, null);
    n = 0;
  }

  @Override
  public boolean containsKey(K key) {
    if (key == null) throw new IllegalArgumentException();
    int index = Math.floorMod(key.hashCode(), hashTable.length);
    List<MyHashMapNode> bucket = hashTable[index];
    if (bucket != null) {
      for (MyHashMapNode node: bucket) {
        if(key.equals(node.key)) return true;
      }
    }
    return false;
  }

  @Override
  public V get(K key) {
    if (key == null) throw new IllegalArgumentException();
    int index = Math.floorMod(key.hashCode(), hashTable.length);
    List<MyHashMapNode> bucket = hashTable[index];
    if (bucket != null) {
      for (MyHashMapNode node: bucket) {
        if(key.equals(node.key)) return node.value;
      }
    }
    return null;
  }

  @Override
  public int size() {
    return n;
  }

  @Override
  public void put(K key, V value) {
    if (key == null) throw new IllegalArgumentException();
    if (value == null) throw new IllegalArgumentException();
    int index = Math.floorMod(key.hashCode(), hashTable.length);
    List<MyHashMapNode> bucket = hashTable[index];
    if (bucket == null) {
      hashTable[index] = new LinkedList<MyHashMapNode>();
      bucket = hashTable[index];
    }
    for (MyHashMapNode node: bucket) {
      if(key.equals(node.key)) {
        node.value = value;
        return;
      }
    }
    bucket.add(new MyHashMapNode(key, value));
    n++;
    if (n > hashTable.length * lf) {
      resize(hashTable.length * resizeFactor);
    }
  }

  private void resize(int newSize) {
    List<MyHashMapNode>[] newHashTable = (List<MyHashMapNode>[]) new List[newSize];
    for(int i=0; i<hashTable.length; i++) {
      List<MyHashMapNode> bucket = hashTable[i];
      if (bucket != null) {
        for (MyHashMapNode node: bucket) {
          int newIndex = Math.floorMod(node.key.hashCode(), newSize);
          List<MyHashMapNode> newBucket = newHashTable[newIndex];
          if (newBucket == null) {
            newHashTable[newIndex] = new LinkedList<MyHashMapNode>();
            newBucket = newHashTable[newIndex];
          }
          newBucket.add(node);
        }
      }
    }
    hashTable = newHashTable;
  }

  @Override
  public Set<K> keySet() {
    Set<K> s = new HashSet<>();
    for(int i=0; i<hashTable.length; i++) {
      List<MyHashMapNode> bucket = hashTable[i];
      if (bucket != null) {
        for (MyHashMapNode node: bucket) {
          s.add(node.key);
        }
      }
    }
    return s;
  }

  @Override
  public V remove(K key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V remove(K key, V value) {
    throw new UnsupportedOperationException();
  }
  
}
