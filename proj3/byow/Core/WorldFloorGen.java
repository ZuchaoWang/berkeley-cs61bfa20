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

    public int roomWidth() {
      return roomRight - roomLeft + 1;
    }

    public int roomHeight() {
      return roomTop - roomBottom + 1;
    }

    public void refineBoundingBox() {
      if (!isLeaf) {
        roomLeft = Math.min(leftChild.roomLeft, rightChild.roomLeft);
        roomRight = Math.max(leftChild.roomRight, rightChild.roomRight);
        roomBottom = Math.min(leftChild.roomBottom, rightChild.roomBottom);
        roomTop = Math.max(leftChild.roomTop, rightChild.roomTop);
      }
    }

    public boolean containsPoint(int x, int y) {
      return x >= roomLeft && x <= roomRight
          && y >= roomBottom && y <= roomTop;
    }
  }

  // ref:
  // https://gamedev.stackexchange.com/questions/82059/algorithm-for-procedureral-2d-map-with-connected-paths
  public static boolean[][] generateWorld(int worldWidth, int worldHeight, Random randGen) {
    BSPNode root = partitionWorld(1, worldWidth - 2, 1, worldHeight - 2, randGen);
    shrinkRooms(root, randGen);
    generateHallwayPivots(root, randGen);
    boolean[][] world = rasterizeInit(root, worldWidth, worldHeight);
    rasterizeRoom(root, world);
    rasterizeHallway(root, world);
    return world;
  }

  private static BSPNode partitionWorld(int roomLeft, int roomRight, int roomBottom, int roomTop, Random randGen) {
    // split world recursively into BSPNodes, must be at least of size = minRoomSize
    // + minRoomInnerMargin
    // sibling BSPNodes must be separated at x or y direction for a distance =
    // minRoomInterMargin
    BSPNode parent = new BSPNode(roomLeft, roomRight, roomBottom, roomTop);
    boolean canPartitionX = canPartition(parent.roomWidth(), randGen),
        canPartitionY = canPartition(parent.roomHeight(), randGen);
    if (!canPartitionX && !canPartitionY) {
      return parent;
    } else if (canPartitionX && canPartitionY) {
      if (RandomUtils.bernoulli(randGen)) {
        return partitionWorldAtX(parent, randGen);
      } else {
        return partitionWorldAtY(parent, randGen);
      }
    } else if (canPartitionX) {
      return partitionWorldAtX(parent, randGen);
    } else {
      return partitionWorldAtY(parent, randGen);
    }
  }

  private static boolean canPartition(int roomSize, Random randGen) {
    if (roomSize < 2 * (minRoomSize + minRoomInnerMargin) + minRoomInterMargin) {
      // if room too small, we cannot split
      return false;
    } else if (roomSize < 2 * (maxRoomSize + minRoomInnerMargin) + minRoomInterMargin) {
      // if room large enough, we can split or not split
      return RandomUtils.bernoulli(randGen);
    } else {
      // if room is too large, we must split
      return true;
    }
  }

  private static BSPNode partitionWorldAtX(BSPNode parent, Random randGen) {
    int splitPos = sampleSplitPos(parent.roomLeft, parent.roomRight, randGen);
    parent.isLeaf = false;
    parent.leftChild = partitionWorld(parent.roomLeft, splitPos - 1, parent.roomBottom, parent.roomTop, randGen);
    parent.rightChild = partitionWorld(splitPos + minRoomInterMargin, parent.roomRight, parent.roomBottom, parent.roomTop,
        randGen);
    return parent;
  }

  private static BSPNode partitionWorldAtY(BSPNode parent, Random randGen) {
    int splitPos = sampleSplitPos(parent.roomBottom, parent.roomTop, randGen);
    parent.isLeaf = false;
    parent.leftChild = partitionWorld(parent.roomLeft, parent.roomRight, parent.roomBottom, splitPos - 1, randGen);
    parent.rightChild = partitionWorld(parent.roomLeft, parent.roomRight, splitPos + minRoomInterMargin, parent.roomTop,
        randGen);
    return parent;
  }

  private static int sampleSplitPos(int start, int end, Random randGen) {
    int splitMin = start + minRoomSize + minRoomInnerMargin,
        splitMax = end - minRoomSize - minRoomInnerMargin - minRoomInterMargin + 1,
        splitPos = RandomUtils.uniform(randGen, splitMin, splitMax + 1);
    return splitPos;
  }

  private static void shrinkRooms(BSPNode parent, Random randGen) {
    if (parent.isLeaf) { // leaf node
      // ensure minRoomInnerMargin by first shrink it, this is mainly to dislocate
      // room
      if (RandomUtils.bernoulli(randGen))
        parent.roomLeft += minRoomInnerMargin;
      else
        parent.roomRight -= minRoomInnerMargin;
      if (RandomUtils.bernoulli(randGen))
        parent.roomBottom += minRoomInnerMargin;
      else
        parent.roomTop -= minRoomInnerMargin;
      // continue to shrink the room randomly, but ensure its size is in [minRoomSize,
      // maxRoomSize]
      int nextRoomWidth = RandomUtils.uniform(randGen, minRoomSize, Math.min(maxRoomSize, parent.roomWidth()) + 1);
      parent.roomLeft += RandomUtils.uniform(randGen, parent.roomWidth() - nextRoomWidth + 1);
      parent.roomRight = parent.roomLeft + nextRoomWidth - 1;
      int nextRoomHeight = RandomUtils.uniform(randGen, minRoomSize, Math.min(maxRoomSize, parent.roomHeight()) + 1);
      parent.roomBottom += RandomUtils.uniform(randGen, parent.roomHeight() - nextRoomHeight + 1);
      parent.roomTop = parent.roomBottom + nextRoomHeight - 1;
    } else { // for non leaf
      // generate room for each children
      shrinkRooms(parent.leftChild, randGen);
      shrinkRooms(parent.rightChild, randGen);
      // refine parent bounding box
      parent.refineBoundingBox();
    }
  }

  private static void generateHallwayPivots(BSPNode parent, Random randGen) {
    if (parent.isLeaf) {
      return; // do nothing for leaf
    } else {
      generateHallwayPivots(parent.leftChild, randGen);
      generateHallwayPivots(parent.rightChild, randGen);
      // now find the connection point of leftChild and rightChild
      int minPivotX = Math.max(parent.leftChild.roomLeft, parent.rightChild.roomLeft),
          maxPivotX = Math.min(parent.leftChild.roomRight, parent.rightChild.roomRight),
          minPivotY = Math.max(parent.leftChild.roomBottom, parent.rightChild.roomBottom),
          maxPivotY = Math.min(parent.leftChild.roomTop, parent.rightChild.roomTop);
      boolean xFaceToFace = minPivotX <= maxPivotX,
          yFaceToFace = minPivotY <= maxPivotY;
      if (xFaceToFace) { // then cannot be yFaceToFace
        parent.hallwayPivotX = RandomUtils.uniform(randGen, minPivotX, maxPivotX + 1);
        parent.hallwayPivotY = (minPivotY + maxPivotY) / 2;
      } else if (yFaceToFace) { // then cannot be xFaceToFace
        parent.hallwayPivotX = (minPivotX + maxPivotX) / 2;
        parent.hallwayPivotY = RandomUtils.uniform(randGen, minPivotY, maxPivotY + 1);
      } else { // neither xFaceToFace nor yFaceToFace
        if (RandomUtils.bernoulli(randGen)) {
          parent.hallwayPivotX = RandomUtils.uniform(randGen, parent.leftChild.roomLeft,
              parent.leftChild.roomRight + 1);
          parent.hallwayPivotY = RandomUtils.uniform(randGen, parent.rightChild.roomBottom,
              parent.rightChild.roomTop + 1);
        } else {
          parent.hallwayPivotX = RandomUtils.uniform(randGen, parent.rightChild.roomLeft,
              parent.rightChild.roomRight + 1);
          parent.hallwayPivotY = RandomUtils.uniform(randGen, parent.leftChild.roomBottom,
              parent.leftChild.roomTop + 1);
        }
      }
    }
  }

  private static boolean[][] rasterizeInit(BSPNode root, int worldWidth, int worldHeight) {
    // create an empty world
    boolean[][] world = new boolean[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x++) {
      for (int y = 0; y < worldHeight; y++) {
        world[x][y] = false;
      }
    }
    return world;
  }

  private static void rasterizeRoom(BSPNode parent, boolean[][] world) {
    // add leaf rooms to the world
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
    // add hallways in non-leaf node to the world
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
    // find direction for extensiokn of hallway pivot point
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
    // extend pivot point along the direction, until it connects with BSPNode
    // current
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
    return world[hallwayX][hallwayY] && current.containsPoint(hallwayX, hallwayY);
  }
}
