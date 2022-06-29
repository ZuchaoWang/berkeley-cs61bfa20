package byow.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class WorldGen {
  private static final int randColor = 32;
  private static final int avatarCandidateCount = 3;
  private static final double encounterProbability = 0.03;

  public static void generateWorld(TETile[][] world, Random randGen) {
    generateFloor(world, randGen);
    generateWall(world, randGen);
    generateLockedDoor(world, randGen);
    generateEncounter(world, randGen);
  }

  public static int[] generateAvatarPos(TETile[][] world, Random randGen) {
    // gather all floor position and the only lockedDoor position
    ArrayList<Integer> floorXs = new ArrayList<Integer>(),
      floorYs = new ArrayList<Integer>();
    int lockedDoorX = 0,
      lockedDoorY = 0;
    int worldWidth = world.length,
      worldHeight = world[0].length;
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        if (world[x][y].character() == Tileset.FLOOR.character()) {
          floorXs.add(x);
          floorYs.add(y);
        }
        if (world[x][y].character() == Tileset.LOCKED_DOOR.character()) {
          lockedDoorX = x;
          lockedDoorY = y;
        }
      }
    }
    // choose {avatarCandiateCount} floor positions at random
    ArrayList<Integer> candidateIndices = new ArrayList<Integer>();
    for (int i=0; i<avatarCandidateCount; i++) {
      int candidateIndex = RandomUtils.uniform(randGen, floorXs.size());
      candidateIndices.add(candidateIndex);
    }
    // pick the floor position farthest away from lockedDoor
    int farthestCandidateIndex = -1,
      farthestCandidateDis2 = -1;
    for (int i=0; i<avatarCandidateCount; i++) {
      int candidateIndex = candidateIndices.get(i),
        x = floorXs.get(candidateIndex),
        y = floorYs.get(candidateIndex),
        dx = x - lockedDoorX,
        dy = y - lockedDoorY,
        dis2 = dx * dx + dy * dy;
      if (dis2 > farthestCandidateDis2) {
        farthestCandidateDis2 = dis2;
        farthestCandidateIndex = candidateIndex;
      }
    }
    // return the picked floor position as int[2]
    int[] pos = new int[2];
    pos[0] = floorXs.get(farthestCandidateIndex);
    pos[1] = floorYs.get(farthestCandidateIndex);
    return pos;
  }

  private static void generateFloor(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    boolean[][] worldFloor = WorldFloorGen.generateWorld(worldWidth, worldHeight, randGen);
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        if (worldFloor[x][y])
          world[x][y] = Tileset.FLOOR;
      }
    }
  }

  private static void generateWall(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        if (world[x][y].character() == Tileset.NOTHING.character()) {
          HashMap<Character, Integer> count = WorldUtils.countNeighbourTiles8(world, x, y);
          if (count.getOrDefault(Tileset.FLOOR.character(), 0) > 0) {
            world[x][y] = TETile.colorVariant(Tileset.WALL, randColor, randColor, randColor, randGen);
          }
        }
        ;
      }
    }
  }

  private static void generateLockedDoor(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    ArrayList<Integer> candidateXs = new ArrayList<Integer>(),
        candidateYs = new ArrayList<Integer>();
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        HashMap<Character, Integer> count = WorldUtils.countNeighbourTiles4(world, x, y);
        if (count.getOrDefault(Tileset.FLOOR.character(), 0) == 1 &&
            count.getOrDefault(Tileset.NOTHING.character(), 0) == 1 &&
            count.getOrDefault(Tileset.WALL.character(), 0) == 2) {
          candidateXs.add(x);
          candidateYs.add(y);
        }
      }
    }
    int choice = RandomUtils.uniform(randGen, candidateXs.size());
    world[candidateXs.get(choice)][candidateYs.get(choice)] = Tileset.LOCKED_DOOR;
  }

  private static void generateEncounter(TETile[][] world, Random randGen) {
    int worldWidth = world.length,
        worldHeight = world[0].length;
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        if (world[x][y].character() == Tileset.FLOOR.character()) {
          HashMap<Character, Integer> count = WorldUtils.countNeighbourTiles8(world, x, y);
          if (count.getOrDefault(Tileset.FLOWER.character(), 0) == 0) {
            if (RandomUtils.uniform(randGen) < encounterProbability) {
              world[x][y] = Tileset.FLOWER;
            }
          }
        }
      }
    }
  }
}
