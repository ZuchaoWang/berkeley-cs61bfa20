import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
  private class BSTMapNode {
    public K key;
    public V value;
    public BSTMapNode left;
    public BSTMapNode right;
    public BSTMapNode(K k, V v) {
      key = k;
      value = v;
      left = null;
      right = null;
    }
  }

  private BSTMapNode root;
  private int size;
  
  public BSTMap() {
    clear();
  }

  @Override
  public void clear() {
    root = null;
    size = 0;
  }

  @Override
  public boolean containsKey(K key) {
    if (key == null) throw new IllegalArgumentException();
    return containsKeyInNode(key, root);
  }

  private boolean containsKeyInNode(K key, BSTMapNode node) {
    if (node == null) return false;
    int cmp = key.compareTo(node.key);
    if (cmp == 0) return true;
    else if (cmp < 0) return containsKeyInNode(key, node.left);
    else return containsKeyInNode(key, node.right);
  }

  @Override
  public V get(K key) {
    if (key == null) throw new IllegalArgumentException();
    return getInNode(key, root);
  }

  private V getInNode(K key, BSTMapNode node) {
    if (node == null) return null;
    int cmp = key.compareTo(node.key);
    if (cmp == 0) return node.value;
    else if (cmp < 0) return getInNode(key, node.left);
    else return getInNode(key, node.right);
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public void put(K key, V value) {
    if (key == null) throw new IllegalArgumentException();
    if (value == null) throw new IllegalArgumentException();
    root = putInNode(key, value, root);
  }

  private BSTMapNode putInNode(K key, V value, BSTMapNode node) {
    if (node == null) {
      size++;
      return new BSTMapNode(key, value);
    }
    int cmp = key.compareTo(node.key);
    if (cmp == 0) {
      node.value = value;
      return node;
    } else if (cmp < 0) {
      node.left = putInNode(key, value, node.left);
      return node;
    } else {
      node.right = putInNode(key, value, node.right);
      return node;
    }
  }

  @Override
  public Set<K> keySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterator<K> iterator() {
    throw new UnsupportedOperationException();
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
