package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class WorldGen {
  public static void generateWorld(TETile[][] world, long seed) {
    generateFloor(world, seed);
  }

  private static void generateFloor(TETile[][] world, long seed) {
    int worldWidth = world.length,
      worldHeight = world[0].length;
    boolean[][] worldFloor = WorldFloorGen.generateWorld(worldWidth, worldHeight, seed);
    for (int x=0; x<worldWidth; x++) {
      for (int y=0; y<worldHeight; y++) {
        if (worldFloor[x][y]) world[x][y] = Tileset.FLOOR;
      }
    }
  }
}
