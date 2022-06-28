package byow.Core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class WorldGen {
  private static final int randColor = 32;

  public static void generateWorld(TETile[][] world, long seed) {
    Random randGen = new Random(seed);
    generateFloor(world, randGen);
    generateWall(world, randGen);
    generateLockedDoor(world, randGen);
  }

  private static void generateFloor(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
      worldHeight = world[0].length;
    boolean[][] worldFloor = WorldFloorGen.generateWorld(worldWidth, worldHeight, randGen);
    for (int x=0; x<worldWidth; x++) {
      for (int y=0; y<worldHeight; y++) {
        if (worldFloor[x][y]) world[x][y] = Tileset.FLOOR;
      }
    }
  }

  private static void generateWall(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
      worldHeight = world[0].length;
    for (int x=0; x<worldWidth; x++) {
      for (int y=0; y<worldHeight; y++) {
        if (world[x][y].character() == Tileset.NOTHING.character()) {
          HashMap<Character,Integer> count = countNeighbourTiles8(world, x, y);
          if (count.getOrDefault(Tileset.FLOOR.character(), 0) > 0) {
            world[x][y] = TETile.colorVariant(Tileset.WALL, randColor, randColor, randColor, randGen);
          }
        };
      }
    }
  }

  private static void generateLockedDoor(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
      worldHeight = world[0].length;
    ArrayList<Integer> candidateXs = new ArrayList<Integer>(),
      candidateYs = new ArrayList<Integer>();
    for (int x=0; x<worldWidth; x++) {
      for (int y=0; y<worldHeight; y++) {
        HashMap<Character,Integer> count = countNeighbourTiles4(world, x, y);
        if (count.getOrDefault(Tileset.FLOOR.character(), 0) == 1 &&
          count.getOrDefault(Tileset.NOTHING.character(), 0) == 1 &&
          count.getOrDefault(Tileset.WALL.character(), 0) == 2) {
            candidateXs.add(x);
            candidateYs.add(y);
          }
      }
    }
    int choice = randGen.nextInt(candidateXs.size());
    world[candidateXs.get(choice)][candidateYs.get(choice)] = Tileset.LOCKED_DOOR;
  }

  private static HashMap<Character,Integer> countNeighbourTiles4(TETile[][] world, int x, int y) {
    int worldWidth = world.length,
      worldHeight = world[0].length;
    HashMap<Character,Integer> counter = new HashMap<Character,Integer>();
    if (x == 0) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x-1][y].character());
    if (x == worldWidth-1) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x+1][y].character());
    if (y == 0) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x][y-1].character());
    if (y == worldHeight-1) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x][y+1].character());
    return counter;
  }

  private static HashMap<Character,Integer> countNeighbourTiles8(TETile[][] world, int x, int y) {
    int worldWidth = world.length,
      worldHeight = world[0].length;
    HashMap<Character,Integer> counter = countNeighbourTiles4(world, x, y);
    if (x == 0 || y == 0) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x-1][y-1].character());
    if (x == 0 || y == worldHeight-1) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x-1][y+1].character());
    if (x == worldWidth-1 || y == 0) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x+1][y-1].character());
    if (x == worldWidth-1 || y == worldHeight-1) addTileCount(counter, Tileset.NOTHING.character());
    else addTileCount(counter, world[x+1][y+1].character());
    return counter;
  }

  private static void addTileCount(HashMap<Character,Integer> counter, char c) {
    counter.put(c, counter.getOrDefault(c, 0) + 1);
  }
}
