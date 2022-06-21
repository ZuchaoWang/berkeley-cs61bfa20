import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {
  private class MayHashMapNode {
    public K key;
    public V value;
    public MayHashMapNode(K k, V v) {
      key = k;
      value = v;
    }
  }

  private final int defaultInitialSize = 16;
  private final double defaultLoadfactor = 0.75;
  private final int resizeFactor = 2;

  List<MayHashMapNode>[] hashTable;
  double lf;

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
    hashTable = (List<MayHashMapNode>[]) new Object[initialSize];
    lf = loadFactor;
    clear();
  }

  @Override
  public Iterator<K> iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void clear() {
    Arrays.fill(hashTable, null);
  }

  @Override
  public boolean containsKey(K key) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public V get(K key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void put(K key, V value) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Set<K> keySet() {
    // TODO Auto-generated method stub
    return null;
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
