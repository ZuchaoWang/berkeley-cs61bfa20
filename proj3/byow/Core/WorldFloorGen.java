package byow.Core;

import java.util.Random;

public class WorldFloorGen {
  private static final int minRoomSize = 2;
  private static final int maxRoomSize = 6;
  private static final int minRoomMargin = 1;

  private static class BSPNode {
    public int roomLeft;
    public int roomRight;
    public int roomBottom;
    public int roomTop;
    public int hallwayPivotX;
    public int hallwayPivotY;
    public BSPNode leftChild;
    public BSPNode rightChild;

    public BSPNode(int l, int r, int b, int t) {
      roomLeft = l;
      roomRight = r;
      roomBottom = b;
      roomTop = t;
      hallwayPivotX = 0;
      hallwayPivotY = 0;
      leftChild = null;
      rightChild = null;
    }
  }

  // ref: https://gamedev.stackexchange.com/questions/82059/algorithm-for-procedureral-2d-map-with-connected-paths
  public static boolean[][] generateWorld(int worldWidth, int worldHeight, long seed) {
    Random randGen = new Random(seed);
    BSPNode root = splitWorld(1, worldWidth - 2, 1, worldHeight - 2, randGen);
    generateRooms(root, randGen);
    // generateHallways(root, randGen);
    return rasterizeWorld(root, worldWidth, worldHeight);
  }

  private static BSPNode splitWorld(int roomLeft, int roomRight, int roomBottom, int roomTop, Random randGen) {
    BSPNode parent = new BSPNode(roomLeft, roomRight, roomBottom, roomTop);
    boolean canSplitX = canSplit(roomRight - roomLeft + 1, randGen),
      canSplitY = canSplit(roomTop - roomBottom + 1, randGen);
    if (!canSplitX && !canSplitY) {
      return parent;
    } else if (canSplitX && canSplitY) {
      if (randGen.nextDouble() < 0.5) {
        return splitWorldAtX(parent, randGen);
      } else {
        return splitWorldAtY(parent, randGen);
      }
    } else if (canSplitX) {
      return splitWorldAtX(parent, randGen);
    } else {
      return splitWorldAtY(parent, randGen);
    }
  }

  private static boolean canSplit(int roomSize, Random randGen) {
    if (roomSize < 2 * minRoomSize + 2 * minRoomMargin + 1) {
      // if room too small, we cannot split
      return false;
    } else if (roomSize < 2 * maxRoomSize + 2 * minRoomMargin + 1) {
      // if room large enough, we can split or not split
      return randGen.nextDouble() < 0.5;
    } else {
      // if room is too large, we must split
      return true;
    }
  }

  private static BSPNode splitWorldAtX(BSPNode parent, Random randGen) {
    int splitMin = parent.roomLeft + minRoomSize + minRoomMargin,
      splitMax = parent.roomRight - minRoomSize - minRoomMargin,
      splitPos = splitMin + randGen.nextInt(splitMax - splitMin + 1);
    parent.leftChild = splitWorld(parent.roomLeft, splitPos - 1, parent.roomBottom, parent.roomTop, randGen);
    parent.rightChild = splitWorld(splitPos + 1, parent.roomRight, parent.roomBottom, parent.roomTop, randGen);
    return parent;
  }

  private static BSPNode splitWorldAtY(BSPNode parent, Random randGen) {
    int splitMin = parent.roomBottom + minRoomSize + minRoomMargin,
      splitMax = parent.roomTop - minRoomSize - minRoomMargin,
      splitPos = splitMin + randGen.nextInt(splitMax - splitMin + 1);
    parent.leftChild = splitWorld(parent.roomLeft, parent.roomRight, parent.roomBottom, splitPos - 1, randGen);
    parent.rightChild = splitWorld(parent.roomLeft, parent.roomRight, splitPos + 1, parent.roomTop, randGen);
    return parent;
  }

  private static void generateRooms(BSPNode parent, Random randGen) {
    if (parent.leftChild == null) { // leaf node
      // ensure room margin
      if (randGen.nextDouble() < 0.5) parent.roomLeft += minRoomMargin;
      else parent.roomRight -= minRoomMargin;
      if (randGen.nextDouble() < 0.5) parent.roomBottom += minRoomMargin;
      else parent.roomTop -= minRoomMargin;
      // continue to shrink the room randomly
      int roomWidth = minRoomSize + randGen.nextInt(Math.min(maxRoomSize, parent.roomRight - parent.roomLeft + 1) - minRoomSize + 1);
      parent.roomLeft += randGen.nextInt(parent.roomRight - parent.roomLeft + 1 - roomWidth + 1);
      parent.roomRight = parent.roomLeft + roomWidth - 1;
      int roomHeight = minRoomSize + randGen.nextInt(Math.min(maxRoomSize, parent.roomTop - parent.roomBottom + 1) - minRoomSize + 1);
      parent.roomBottom += randGen.nextInt(parent.roomTop - parent.roomBottom + 1 - roomHeight + 1);
      parent.roomTop = parent.roomBottom + roomHeight - 1;
    } else {
      // generate room for each children
      generateRooms(parent.leftChild, randGen);
      generateRooms(parent.rightChild, randGen);
      // refine parent bounding box
      parent.roomLeft = Math.min(parent.leftChild.roomLeft, parent.rightChild.roomLeft);
      parent.roomRight = Math.max(parent.leftChild.roomRight, parent.rightChild.roomRight);
      parent.roomBottom = Math.min(parent.leftChild.roomBottom, parent.rightChild.roomBottom);
      parent.roomTop = Math.max(parent.leftChild.roomTop, parent.rightChild.roomTop);
    }
  }

  private static void generateHallways(BSPNode parent, Random randGen) {

  }

  private static boolean[][] rasterizeWorld(BSPNode root, int worldWidth, int worldHeight) {
    boolean[][] world = new boolean[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        world[x][y] = false;
      }
    }
    rasterizeRoom(root, world);
    return world;
  }

  private static void rasterizeRoom(BSPNode parent, boolean[][] world) {
    if (parent.leftChild == null) {
      for (int x = parent.roomLeft; x <= parent.roomRight; x++) {
        for (int y = parent.roomBottom; y <= parent.roomTop; y++) {
          world[x][y] = true;
        }
      }
    } else {
      rasterizeRoom(parent.leftChild, world);
      rasterizeRoom(parent.rightChild, world);
    }
  }
}
