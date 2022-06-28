package byow.Core;

import java.util.Random;

public class WorldFloorGen {
  private static final int minRoomSize = 2;
  private static final int maxRoomSize = 6;
  private static final int minRoomInnerMargin = 1;
  private static final int minRoomInterMargin = 2;

  private static class BSPNode {
    public int roomLeft;
    public int roomRight;
    public int roomBottom;
    public int roomTop;
    public int hallwayPivotX;
    public int hallwayPivotY;
    public BSPNode leftChild;
    public BSPNode rightChild;
    public boolean isLeaf;

    public BSPNode(int l, int r, int b, int t) {
      roomLeft = l;
      roomRight = r;
      roomBottom = b;
      roomTop = t;
      hallwayPivotX = 0;
      hallwayPivotY = 0;
      leftChild = null;
      rightChild = null;
      isLeaf = true;
    }
  }

  // ref:
  // https://gamedev.stackexchange.com/questions/82059/algorithm-for-procedureral-2d-map-with-connected-paths
  public static boolean[][] generateWorld(int worldWidth, int worldHeight, Random randGen) {
    BSPNode root = splitWorld(1, worldWidth - 2, 1, worldHeight - 2, randGen);
    generateRooms(root, randGen);
    generateHallways(root, randGen);
    boolean[][] world = rasterizeInit(root, worldWidth, worldHeight);
    rasterizeRoom(root, world);
    rasterizeHallway(root, world);
    return world;
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
    if (roomSize < 2 * (minRoomSize + minRoomInnerMargin) + minRoomInterMargin) {
      // if room too small, we cannot split
      return false;
    } else if (roomSize < 2 * (maxRoomSize + minRoomInnerMargin) + minRoomInterMargin) {
      // if room large enough, we can split or not split
      return randGen.nextDouble() < 0.5;
    } else {
      // if room is too large, we must split
      return true;
    }
  }

  private static BSPNode splitWorldAtX(BSPNode parent, Random randGen) {
    int splitMin = parent.roomLeft + minRoomSize + minRoomInnerMargin,
        splitMax = parent.roomRight - minRoomSize - minRoomInnerMargin - minRoomInterMargin + 1,
        splitPos = splitMin + randGen.nextInt(splitMax - splitMin + 1);
    parent.isLeaf = false;
    parent.leftChild = splitWorld(parent.roomLeft, splitPos - 1, parent.roomBottom, parent.roomTop, randGen);
    parent.rightChild = splitWorld(splitPos + minRoomInterMargin, parent.roomRight, parent.roomBottom, parent.roomTop,
        randGen);
    return parent;
  }

  private static BSPNode splitWorldAtY(BSPNode parent, Random randGen) {
    int splitMin = parent.roomBottom + minRoomSize + minRoomInnerMargin,
        splitMax = parent.roomTop - minRoomSize - minRoomInnerMargin - minRoomInterMargin + 1,
        splitPos = splitMin + randGen.nextInt(splitMax - splitMin + 1);
    parent.isLeaf = false;
    parent.leftChild = splitWorld(parent.roomLeft, parent.roomRight, parent.roomBottom, splitPos - 1, randGen);
    parent.rightChild = splitWorld(parent.roomLeft, parent.roomRight, splitPos + minRoomInterMargin, parent.roomTop,
        randGen);
    return parent;
  }

  private static void generateRooms(BSPNode parent, Random randGen) {
    if (parent.isLeaf) { // leaf node
      // ensure room margin
      if (randGen.nextDouble() < 0.5)
        parent.roomLeft += minRoomInnerMargin;
      else
        parent.roomRight -= minRoomInnerMargin;
      if (randGen.nextDouble() < 0.5)
        parent.roomBottom += minRoomInnerMargin;
      else
        parent.roomTop -= minRoomInnerMargin;
      // continue to shrink the room randomly
      int roomWidth = minRoomSize
          + randGen.nextInt(Math.min(maxRoomSize, parent.roomRight - parent.roomLeft + 1) - minRoomSize + 1);
      parent.roomLeft += randGen.nextInt(parent.roomRight - parent.roomLeft + 1 - roomWidth + 1);
      parent.roomRight = parent.roomLeft + roomWidth - 1;
      int roomHeight = minRoomSize
          + randGen.nextInt(Math.min(maxRoomSize, parent.roomTop - parent.roomBottom + 1) - minRoomSize + 1);
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
    if (parent.isLeaf) {
      return; // do nothing for leaf
    } else {
      generateHallways(parent.leftChild, randGen);
      generateHallways(parent.rightChild, randGen);
      // now find the connection point of leftChild and rightChild
      int minPivotX = Math.max(parent.leftChild.roomLeft, parent.rightChild.roomLeft),
          maxPivotX = Math.min(parent.leftChild.roomRight, parent.rightChild.roomRight),
          minPivotY = Math.max(parent.leftChild.roomBottom, parent.rightChild.roomBottom),
          maxPivotY = Math.min(parent.leftChild.roomTop, parent.rightChild.roomTop);
      boolean xFaceToFace = minPivotX <= maxPivotX,
          yFaceToFace = minPivotY <= maxPivotY;
      if (xFaceToFace) { // then cannot be yFaceToFace
        parent.hallwayPivotX = minPivotX + randGen.nextInt(maxPivotX - minPivotX + 1);
        parent.hallwayPivotY = (minPivotY + maxPivotY) / 2;
      } else if (yFaceToFace) { // then cannot be xFaceToFace
        parent.hallwayPivotX = (minPivotX + maxPivotX) / 2;
        parent.hallwayPivotY = minPivotY + randGen.nextInt(maxPivotY - minPivotY + 1);
      } else { // neither xFaceToFace nor yFaceToFace
        if (randGen.nextDouble() < 0.5) {
          parent.hallwayPivotX = parent.leftChild.roomLeft
              + randGen.nextInt(parent.leftChild.roomRight - parent.leftChild.roomLeft + 1);
          parent.hallwayPivotY = parent.rightChild.roomBottom
              + randGen.nextInt(parent.rightChild.roomTop - parent.rightChild.roomBottom + 1);
        } else {
          parent.hallwayPivotX = parent.rightChild.roomLeft
              + randGen.nextInt(parent.rightChild.roomRight - parent.rightChild.roomLeft + 1);
          parent.hallwayPivotY = parent.leftChild.roomBottom
              + randGen.nextInt(parent.leftChild.roomTop - parent.leftChild.roomBottom + 1);
        }
      }
    }
  }

  private static boolean[][] rasterizeInit(BSPNode root, int worldWidth, int worldHeight) {
    boolean[][] world = new boolean[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        world[x][y] = false;
      }
    }
    return world;
  }

  private static void rasterizeRoom(BSPNode parent, boolean[][] world) {
    if (parent.isLeaf) {
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

  private static void rasterizeHallway(BSPNode parent, boolean[][] world) {
    if (parent.isLeaf) {
      return;
    } else {
      rasterizeHallway(parent.leftChild, world);
      rasterizeHallway(parent.rightChild, world);
      // connect leftChild and rightChild
      world[parent.hallwayPivotX][parent.hallwayPivotY] = true;
      rasterizeHallwayConnection(parent, parent.leftChild, world);
      rasterizeHallwayConnection(parent, parent.rightChild, world);
    }
  }

  private static void rasterizeHallwayConnection(BSPNode parent, BSPNode current, boolean[][] world) {
    int xdir = 0;
    if (parent.hallwayPivotX > current.roomRight)
      xdir = -1;
    else if (parent.hallwayPivotX < current.roomLeft)
      xdir = 1;
    int ydir = 0;
    if (parent.hallwayPivotY > current.roomTop)
      ydir = -1;
    else if (parent.hallwayPivotY < current.roomBottom)
      ydir = 1;
    int hallwayX = parent.hallwayPivotX,
        hallwayY = parent.hallwayPivotY;
    while (!isRasterizeHallwayConnected(hallwayX + xdir, hallwayY + ydir, current, world) // front
        && !isRasterizeHallwayConnected(hallwayX - ydir, hallwayY + xdir, current, world) // 90 degree coutner clockwise
        && !isRasterizeHallwayConnected(hallwayX + ydir, hallwayY - xdir, current, world) // 90 degree clockwise
    ) {
      world[hallwayX + xdir][hallwayY + ydir] = true;
      hallwayX += xdir;
      hallwayY += ydir;
    }
  }

  private static boolean isRasterizeHallwayConnected(int hallwayX, int hallwayY, BSPNode current, boolean[][] world) {
    return world[hallwayX][hallwayY]
        && hallwayX >= current.roomLeft && hallwayX <= current.roomRight
        && hallwayY >= current.roomBottom && hallwayY <= current.roomTop;
  }
}
