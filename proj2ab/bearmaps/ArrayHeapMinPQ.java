package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
  private class ArrayHeapMinPQNode {
    public T item;
    public double priority;

    public ArrayHeapMinPQNode(T _item, double _priority) {
      item = _item;
      priority = _priority;
    }
  }

  private ArrayList<ArrayHeapMinPQNode> items;
  private HashMap<T, Integer> itemIndex;

  public ArrayHeapMinPQ() {
    items = new ArrayList<ArrayHeapMinPQNode>();
    items.add(0, null);
    itemIndex = new HashMap<T, Integer>();
  }

  @Override
  public void add(T item, double priority) {
    if (item == null || contains(item))
      throw new IllegalArgumentException();
    int sz = size();
    ArrayHeapMinPQNode node = new ArrayHeapMinPQNode(item, priority);
    items.add(sz+1, node);
    itemIndex.put(item, sz+1);
    siftUp(sz+1);
  }

  @Override
  public boolean contains(T item) {
    if (item == null)
      return false;
    else
      return itemIndex.containsKey(item);
  }

  @Override
  public T getSmallest() {
    if (size() == 0) throw new NoSuchElementException();
    return items.get(1).item;
  }

  @Override
  public T removeSmallest() {
    int sz = size();
    if (sz == 0) throw new NoSuchElementException();
    else {
      ArrayHeapMinPQNode firstNode = items.get(1);
      swap(1, sz);
      items.remove(sz);
      itemIndex.remove(firstNode.item);
      siftDown(1);
      return firstNode.item;
    }
  }

  @Override
  public int size() {
    return items.size() - 1;
  }

  @Override
  public void changePriority(T item, double priority) {
    if (!contains(item)) throw new NoSuchElementException();
    // TODO Auto-generated method stub

  }

  private void siftUp(int k) {
    while (k > 1) {
      int p = parent(k);
      if (items.get(p).priority > items.get(k).priority) {
        swap(k, p);
        k = p;
      } else {
        break;
      }
    }
  }

  private void siftDown(int k) {
    int sz = size();
    while(leftChild(k) <= sz) {
      int l = leftChild(k),
        r = rightChild(k),
        m = l; // min of l and r
      if (r <= sz && items.get(r).priority < items.get(l).priority) {
        m = r;
      }
      if (items.get(m).priority < items.get(k).priority) {
        swap(k, m);
        k = m;
      } else {
        break;
      }
    }
  }

  private void swap(int k, int l) {
    if (k == l) return;
    ArrayHeapMinPQNode oldKNode = items.get(k),
        oldLNode = items.get(l);
    items.set(k, oldLNode);
    items.set(l, oldKNode);
    itemIndex.put(oldKNode.item, l);
    itemIndex.put(oldLNode.item, k);
  }

  private int leftChild(int k) {
    return k * 2;
  }

  private int rightChild(int k) {
    return k * 2 + 1;
  }

  private int parent(int k) {
    return k / 2;
  }
}
