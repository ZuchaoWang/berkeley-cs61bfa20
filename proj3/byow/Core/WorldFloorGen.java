package byow.Core;

import java.util.Random;

public class WorldFloorGen {
  private static final int minRoomSize = 2;
  private static final int maxRoomSize = 5;
  private static final double maxSplitSkip = 0.2;

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

  // ref:
  // https://gamedev.stackexchange.com/questions/82059/algorithm-for-procedureral-2d-map-with-connected-paths
  public static boolean[][] generateWorldFloor(int worldWidth, int worldHeight, long seed) {
    Random randGen = new Random(seed);
    BSPNode root = splitWorld(1, worldWidth - 1, 1, worldHeight - 1, randGen);
    generateRooms(root, randGen);
    generateHallways(root, randGen);
    return rasterizeWorld(root, worldWidth, worldHeight);
  }

  private static BSPNode splitWorld(int roomLeft, int roomRight, int roomBottom, int roomTop, Random randGen) {
    BSPNode parent = new BSPNode(roomLeft, roomRight, roomBottom, roomTop);
    int xSplitMin = parent.roomLeft + minRoomSize,
        xSplitMax = parent.roomRight - minRoomSize,
        ySplitMin = parent.roomBottom + minRoomSize,
        ySplitMax = parent.roomTop - minRoomSize;
    boolean canSplitX = (xSplitMin <= xSplitMax),
        canSplitY = (ySplitMin <= ySplitMax);
    if (!canSplitX && !canSplitY) {
      return parent;
    } else if (randGen.nextDouble() < maxSplitSkip) {
      return parent;
    } else if (canSplitX && canSplitY) {
      if (randGen.nextDouble() < 0.5) {
        return splitWorldAtX(parent, randGen, xSplitMin, xSplitMax);
      } else {
        return splitWorldAtY(parent, randGen, ySplitMin, ySplitMax);
      }
    } else if (canSplitX) {
      return splitWorldAtX(parent, randGen, xSplitMin, xSplitMax);
    } else {
      return splitWorldAtY(parent, randGen, ySplitMin, ySplitMax);
    }
  }

  private static BSPNode splitWorldAtX(BSPNode parent, Random randGen, int splitMin, int splitMax) {
    int splitPos = splitMin + randGen.nextInt(splitMax - splitMin + 1);
    parent.leftChild = splitWorld(parent.roomLeft, splitPos - 1, parent.roomBottom, parent.roomTop, randGen);
    parent.rightChild = splitWorld(splitPos + 1, parent.roomRight, parent.roomBottom, parent.roomTop, randGen);
    return parent;
  }

  private static BSPNode splitWorldAtY(BSPNode parent, Random randGen, int splitMin, int splitMax) {
    int splitPos = splitMin + randGen.nextInt(splitMax - splitMin + 1);
    parent.leftChild = splitWorld(parent.roomLeft, parent.roomRight, parent.roomBottom, splitPos - 1, randGen);
    parent.rightChild = splitWorld(parent.roomLeft, parent.roomRight, splitPos + 1, parent.roomTop, randGen);
    return parent;
  }

  private static void generateRooms(BSPNode parent, Random randGen) {
    if (parent.leftChild == null) {
      // leaf node, shrink the room randomly
      parent.roomLeft += randGen.nextInt(parent.roomRight - parent.roomLeft + 1 - minRoomSize + 1);
      int roomWidth = minRoomSize + randGen.nextInt(Math.min(maxRoomSize, parent.roomRight - parent.roomLeft + 1) - minRoomSize + 1);
      parent.roomRight = parent.roomLeft + roomWidth;
      parent.roomBottom += randGen.nextInt(parent.roomTop - parent.roomBottom + 1 - minRoomSize + 1);
      int roomHeight = minRoomSize + randGen.nextInt(Math.min(maxRoomSize, parent.roomTop - parent.roomBottom + 1) - minRoomSize + 1);
      parent.roomTop = parent.roomBottom + roomHeight;
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
        for (int y = parent.roomBottom; y < parent.roomTop; y++) {
          world[x][y] = true;
        }
      }
    } else {
      rasterizeRoom(parent.leftChild, world);
      rasterizeRoom(parent.rightChild, world);
    }
  }
}
