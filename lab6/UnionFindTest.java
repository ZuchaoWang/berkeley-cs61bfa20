import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UnionFindTest {

    private UnionFind uf;

    @Test
    public void testUnionFind() {
        uf = new UnionFind(7);
        uf.connect(0, 1);
        uf.connect(1, 2);
        uf.connect(0, 4);
        uf.connect(3, 5);
        assertTrue(uf.isConnected(2, 4));
        assertFalse(uf.isConnected(3, 0));
        uf.connect(4, 2);
        uf.connect(3, 6);
        assertFalse(uf.isConnected(3, 0));
        uf.connect(4, 6);
        assertTrue(uf.isConnected(3, 0));
    }
}

