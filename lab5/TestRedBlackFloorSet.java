import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestRedBlackFloorSet {
    @Test
    public void randomizedTest() {
        int N = 1000000;
        int L = 100000;
        double M = 5000;
        double delta = 0.000001;
        AListFloorSet as = new AListFloorSet();
        RedBlackFloorSet rs = new RedBlackFloorSet();
        for(int i=0; i<N; i++) {
            double x = StdRandom.uniform(-M, M);
            as.add(x);
            rs.add(x);
        }
        for(int i=0; i<L; i++) {
            double x = StdRandom.uniform(-M, M);
            assertEquals(as.floor(x), rs.floor(x), delta);
        }
    }
}
