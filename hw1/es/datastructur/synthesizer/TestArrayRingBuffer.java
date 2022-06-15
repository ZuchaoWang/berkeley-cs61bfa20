package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void testEnqueueAndDequeue() {
        double delta = 1e-6;
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(4);
        assertTrue(arb.isEmpty());
        arb.enqueue(9.3);    // 9.3
        arb.enqueue(15.1);   // 9.3  15.1
        arb.enqueue(31.2);  // 9.3  15.1  31.2
        assertFalse(arb.isFull());
        assertEquals(3, arb.fillCount());
        arb.enqueue(-3.1);     // 9.3  15.1  31.2  -3.1
        assertTrue(arb.isFull());
        assertEquals(9.3, (double)arb.dequeue(), delta);   // 15.1 31.2  -3.1
        assertEquals(3, arb.fillCount());
        assertEquals(15.1, (double)arb.peek(), delta);     // 15.1 31.2  -3.1
        assertEquals(3, arb.fillCount());
        arb.enqueue(6.2);
        assertEquals(15.1, (double)arb.dequeue(), delta); 
    }

    @Test
    public void testIterator() {
        ArrayRingBuffer<Double> arb = new ArrayRingBuffer<Double>(4);
        double[] al1 = new double[]{9.3, 15.1, 31.2};
        for (double x: al1) {
            arb.enqueue(x);
        }
        double[] al2 = new double[al1.length];
        int pos = 0;
        for (double x: arb) {
            al2[pos] = x;
            pos++;
        }
        double delta = 1e-6;
        for (int i=0; i<al1.length; i++) {
            assertEquals(al1[i], al2[i], delta);
        }
    }

    @Test
    public void testEquals() {
        ArrayRingBuffer<Double> arb1 = new ArrayRingBuffer<Double>(4);
        arb1.enqueue(9.3);
        arb1.enqueue(15.1);
        arb1.enqueue(31.2);
        ArrayRingBuffer<Double> arb2 = new ArrayRingBuffer<Double>(4);
        arb2.enqueue(-3.1);
        arb2.enqueue(9.3);
        arb2.enqueue(15.1);
        arb2.enqueue(31.2);
        assertNotEquals(arb1, arb2);
        arb2.dequeue();
        assertEquals(arb1, arb2);
    }
}
