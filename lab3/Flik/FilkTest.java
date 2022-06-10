import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilkTest {
    @Test
    public void testIsSameNumber() {
        assertEquals(false, Flik.isSameNumber(0, 1));
        assertEquals(true, Flik.isSameNumber(1, 1));
        assertEquals(true, Flik.isSameNumber(128, 128)); // this breaks !!!
    }
}
