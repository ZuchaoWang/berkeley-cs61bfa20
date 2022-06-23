package bearmaps.proj2ab;

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
    ArrayHeapMinPQNode node = new ArrayHeapMinPQNode(item, priority);
    addNodeToTail(node);
    siftUp(size());
  }

  private void addNodeToTail(ArrayHeapMinPQNode node) {
    int sz = size();
    items.add(sz + 1, node);
    itemIndex.put(node.item, sz + 1);
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
    if (size() == 0)
      throw new NoSuchElementException();
    return items.get(1).item;
  }

  @Override
  public T removeSmallest() {
    int sz = size();
    if (sz == 0)
      throw new NoSuchElementException();
    else {
      swap(1, sz);
      ArrayHeapMinPQNode node = removeNodeFromTail();
      siftDown(1);
      return node.item;
    }
  }

  private ArrayHeapMinPQNode removeNodeFromTail() {
    ArrayHeapMinPQNode node = items.remove(size());
    itemIndex.remove(node.item);
    return node;
  }

  @Override
  public int size() {
    return items.size() - 1;
  }

  @Override
  public void changePriority(T item, double priority) {
    if (!contains(item))
      throw new NoSuchElementException();
    int index = itemIndex.get(item);
    ArrayHeapMinPQNode node = items.get(index);
    double oldPriority = node.priority;
    node.priority = priority;
    if (priority > oldPriority)
      siftDown(index);
    else if (priority < oldPriority)
      siftUp(index);
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
    while (leftChild(k) <= sz) {
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
    if (k == l)
      return;
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
