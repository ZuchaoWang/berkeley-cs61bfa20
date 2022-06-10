public class LinkedListDeque<T> implements Deque<T> {
  private static class LinkedListNode<T> {
    public T value;
    public LinkedListNode<T> prev;
    public LinkedListNode<T> next;

    public LinkedListNode(T val) {
      value = val;
      prev = null;
      next = null;
    }
  }

  private LinkedListNode<T> sentinel;
  private int sz;

  public LinkedListDeque() {
    sentinel = new LinkedListNode<T>(null);
    sentinel.prev = sentinel;
    sentinel.next = sentinel;
    sz = 0;
  }

  public void addFirst(T item) {
    LinkedListNode<T> before = sentinel;
    LinkedListNode<T> after = sentinel.next;
    LinkedListNode<T> current = new LinkedListNode<T>(item);
    before.next = current;
    current.prev = before;
    current.next = after;
    after.prev = current;
    sz += 1;
  }

  public void addLast(T item) {
    LinkedListNode<T> before = sentinel.prev;
    LinkedListNode<T> after = sentinel;
    LinkedListNode<T> current = new LinkedListNode<T>(item);
    before.next = current;
    current.prev = before;
    current.next = after;
    after.prev = current;
    sz += 1;
  }

  public boolean isEmpty() {
    return sz == 0;
  }

  public int size() {
    return sz;
  }

  public void printDeque() {
    LinkedListNode<T> current = sentinel;
    for (int i=0; i<sz; i++) {
      current = current.next;
      System.out.print(current.value);
      if (i != sz - 1) System.out.print(" ");
    }
    System.out.print("\n");
  }

  public T removeFirst() {
    if (sz == 0) return null;

    LinkedListNode<T> before = sentinel;
    LinkedListNode<T> current = sentinel.next;
    LinkedListNode<T> after = sentinel.next.next;
    before.next = after;
    after.prev = before;
    sz -= 1;
    return current.value;
  }

  public T removeLast() {
    if (sz == 0) return null;

    LinkedListNode<T> before = sentinel.prev.prev;
    LinkedListNode<T> current = sentinel.prev;
    LinkedListNode<T> after = sentinel;
    before.next = after;
    after.prev = before;
    sz -= 1;
    return current.value;
  }

  public T get(int index) {
    if (index > sz) return null;

    LinkedListNode<T> current = sentinel.next;
    while (index > 0) {
      current = current.next;
      index--;
    }
    return current.value;
  }

  public T getRecursive(int index) {
    if (index > sz) return null;
    return getRecursiveImpl(sentinel.next, index);
  }

  private T getRecursiveImpl(LinkedListNode<T> front, int index) {
    if (index == 0) return front.value;
    else return getRecursiveImpl(front.next, index-1);
  }
}
