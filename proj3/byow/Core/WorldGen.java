package byow.Core;

import byow.TileEngine.TETile;

public class WorldGen {
  public static void generateWorld(TETile[][] world, int worldWidth, int worldHeight, long seed) {
    boolean[][] worldFloor = WorldFloorGen.generateWorldFloor(worldWidth, worldHeight, seed);
  }
}
