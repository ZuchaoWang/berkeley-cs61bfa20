public class ArrayDeque<T> implements Deque<T> {
  private static int MIN_ITEMS_LEN = 16;
  private static int ITEMS_CHANGE_FACTOR = 2;
  private static double ITEMS_MIN_OCCUPANCY = 0.25;

  private T[] items;
  private int front;
  private int sz;

  public ArrayDeque() {
    items = (T[]) new Object[MIN_ITEMS_LEN];
    front = 0;
    sz = 0;
  }

  public void addFirst(T item) {
    tryExpandItems();
    items[Math.floorMod(front - 1, items.length)] = item;
    front = Math.floorMod(front - 1, items.length);
    sz++;
  }

  public void addLast(T item) {
    tryExpandItems();
    items[Math.floorMod(front + sz, items.length)] = item;
    sz++;
  }

  public boolean isEmpty() {
    return sz == 0;
  }

  public int size() {
    return sz;
  }

  public void printDeque() {
    for (int i = 0; i < sz; i++) {
      System.out.print(items[Math.floorMod(front + i, items.length)]);
      if (i != sz - 1)
        System.out.print(" ");
    }
    System.out.print("\n");
  }

  public T removeFirst() {
    if (sz == 0)
      return null;

    T item = items[front];
    front++;
    sz -= 1;
    tryShrinkItems();
    return item;
  }

  public T removeLast() {
    if (sz == 0)
      return null;

    T item = items[Math.floorMod(front + sz - 1, items.length)];
    sz -= 1;
    tryShrinkItems();
    return item;
  }

  public T get(int index) {
    if (index > sz)
      return null;
    else
      return items[Math.floorMod(front + index, items.length)];
  }

  private void tryExpandItems() {
    if (items.length == sz) {
      T[] expandItems = (T[]) new Object[items.length * ITEMS_CHANGE_FACTOR];
      for (int i = 0; i < sz; i++) {
        expandItems[i] = items[Math.floorMod(front + i, items.length)];
      }
      items = expandItems;
      front = 0;
    }
  }

  private void tryShrinkItems() {
    if (items.length > MIN_ITEMS_LEN && sz < items.length * ITEMS_MIN_OCCUPANCY) {
      // cut to half
      T[] shrunkItems = (T[]) new Object[items.length / ITEMS_CHANGE_FACTOR];
      for (int i = 0; i < sz; i++) {
        shrunkItems[i] = items[Math.floorMod(front + i, items.length)];
      }
      items = shrunkItems;
      front = 0;
    }
  }
}
