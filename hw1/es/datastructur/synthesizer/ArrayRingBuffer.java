package es.datastructur.synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> implements BoundedQueue<T>  {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (isFull()) throw new RuntimeException("Ring Buffer overflow");
        rb[last] = x;
        last = Math.floorMod(last + 1, rb.length);
        fillCount++;
        return;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) throw new RuntimeException("Ring Buffer underflow");
        T front = rb[first];
        first = Math.floorMod(first + 1, rb.length);
        fillCount--;
        return front;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (isEmpty()) throw new RuntimeException("Ring Buffer underflow");
        return rb[first];
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    private class ArrayRingBufferIterator implements Iterator<T> {
        private int wizPos;
        public ArrayRingBufferIterator() { wizPos = 0; }
        public boolean hasNext() { return wizPos < fillCount; }
        public T next() {
            T returnItem = rb[Math.floorMod(first + wizPos, rb.length)];
            wizPos += 1;
            return returnItem;
        }
    }    

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) return false;
        ArrayRingBuffer<T> other = (ArrayRingBuffer<T>)o;
        if (other.fillCount() != this.fillCount()) return false;
        Iterator<T> it = this.iterator(),
            otherIt = other.iterator();
        while(it.hasNext()) {
            T x = it.next(),
                otherX = otherIt.next();
            if(!x.equals(otherX)) return false; // should use equals, not == or !=
        }
        return true;
    } 
}
