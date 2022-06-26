package byow.lab12;

import org.junit.Test;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static org.junit.Assert.assertEquals;

import java.util.Random;

public class HexBlockTest {
  private static final long SEED = 2873123;
  private static final Random RANDOM = new Random(SEED);

  @Test
  public void testSingleBlock() {
    int worldWidth = 10, worldHeight = 10;
    TETile[][] world = new TETile[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x += 1) {
      for (int y = 0; y < worldHeight; y += 1) {
        world[x][y] = Tileset.NOTHING;
      }
    }
    HexBlock hex = new HexBlock(3, 0, 0, Tileset.WALL, RANDOM, 0);
    hex.fillWorld(world);

    boolean[][] answer = new boolean[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x += 1) {
      for (int y = 0; y < worldHeight; y += 1) {
        answer[x][y] = false;
      }
    }
    answer[2][0] = true;
    answer[3][0] = true;
    answer[4][0] = true;
    answer[1][1] = true;
    answer[2][1] = true;
    answer[3][1] = true;
    answer[4][1] = true;
    answer[5][1] = true;
    answer[0][2] = true;
    answer[1][2] = true;
    answer[2][2] = true;
    answer[3][2] = true;
    answer[4][2] = true;
    answer[5][2] = true;
    answer[6][2] = true;
    answer[0][3] = true;
    answer[1][3] = true;
    answer[2][3] = true;
    answer[3][3] = true;
    answer[4][3] = true;
    answer[5][3] = true;
    answer[6][3] = true;
    answer[1][4] = true;
    answer[2][4] = true;
    answer[3][4] = true;
    answer[4][4] = true;
    answer[5][4] = true;
    answer[2][5] = true;
    answer[3][5] = true;
    answer[4][5] = true;

    for (int x = 0; x < worldWidth; x += 1) {
      for (int y = 0; y < worldHeight; y += 1) {
        assertEquals(answer[x][y], world[x][y] != Tileset.NOTHING);
      }
    }
  }
}
