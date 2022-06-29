package byow.Core;

import byow.Core.Utils.lab6.UnionFind;

import java.util.Random;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WorldFloorGenTest {
  private static final int MINSIZE = 20;
  private static final int MAXSIZE = 100;

  @Test
  public void testRandomWorld() {
    int N = 100;
    for (int i = 0; i < N; i++) {
      Random randGen = new Random(i);
      boolean[][] world = generateWorld(randGen);
      ensureWorldConnected(world);
    }
  }

  private boolean[][] generateWorld(Random randGen) {
    int worldWidth = RandomUtils.uniform(randGen, MINSIZE, MAXSIZE + 1),
        worldHeight = RandomUtils.uniform(randGen, MINSIZE, MAXSIZE + 1);
    return WorldFloorGen.generateWorld(worldWidth, worldHeight, randGen);
  }

  private void ensureWorldConnected(boolean[][] world) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    UnionFind uf = new UnionFind(worldWidth * worldHeight);
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        if (world[x][y]) {
          int tileIndex = getTileIndex(x, y, worldWidth);
          if (x != worldWidth - 1 && world[x + 1][y]) {
            int rightTileIndex = getTileIndex(x + 1, y, worldWidth);
            uf.connect(tileIndex, rightTileIndex);
          }
          if (y != worldHeight - 1 && world[x][y + 1]) {
            int upTileIndex = getTileIndex(x, y + 1, worldWidth);
            uf.connect(tileIndex, upTileIndex);
          }
        }
      }
    }
    int compIndex = -1;
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        if (world[x][y]) {
          int tileIndex = getTileIndex(x, y, worldWidth);
          if (compIndex == -1) {
            compIndex = uf.find(tileIndex);
          } else {
            assertEquals(compIndex, uf.find(tileIndex));
          }
        }
      }
    }
  }

  private int getTileIndex(int x, int y, int worldWidth) {
    return y * worldWidth + x;
  }
}
