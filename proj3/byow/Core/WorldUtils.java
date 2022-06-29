package byow.Core;

import java.util.HashMap;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class WorldUtils {
  public static boolean canMoveTo(TETile[][] world, int x, int y) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    if (x < 0 || x >= worldWidth || y < 0 || y >= worldHeight) return false;
    if (world[x][y].character() == Tileset.NOTHING.character()) return false;
    if (world[x][y].character() == Tileset.WALL.character()) return false;
    return true;
  }

  public static HashMap<Character, Integer> countNeighbourTiles4(TETile[][] world, int x, int y) {
    int[] offsetXs = new int[] { 1, 0, -1, 0 },
        offsetYs = new int[] { 0, 1, 0, -1 };
    return countNeighbourTilesAny(world, x, y, offsetXs, offsetYs);
  }

  public static HashMap<Character, Integer> countNeighbourTiles8(TETile[][] world, int x, int y) {
    int[] offsetXs = new int[] { 1, 1, 0, -1, -1, -1, 0, 1 },
        offsetYs = new int[] { 0, 1, 1, 1, 0, -1, -1, -1 };
    return countNeighbourTilesAny(world, x, y, offsetXs, offsetYs);
  }

  private static HashMap<Character, Integer> countNeighbourTilesAny(TETile[][] world, int x, int y, int[] offsetXs,
      int[] offsetYs) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    HashMap<Character, Integer> counter = new HashMap<Character, Integer>();
    for (int i = 0; i < offsetXs.length; i++) {
      int nextX = x + offsetXs[i],
          nextY = y + offsetYs[i];
      if (nextX < 0 || nextX >= worldWidth || nextY < 0 || nextY >= worldHeight) {
        addTileCount(counter, Tileset.NOTHING.character());
      } else {
        addTileCount(counter, world[nextX][nextY].character());
      }
    }
    return counter;
  }

  private static void addTileCount(HashMap<Character, Integer> counter, char c) {
    counter.put(c, counter.getOrDefault(c, 0) + 1);
  }
}
