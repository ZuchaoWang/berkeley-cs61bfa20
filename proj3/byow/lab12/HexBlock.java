package byow.lab12;

import java.util.Random;

import byow.TileEngine.TETile;

public class HexBlock {
  private static final int[] tessXSteps = new int[]{1, 0, -1, -1, 0, 1};
  private static final int[] tessYSteps = new int[]{-1, -2, -1, 1, 2, 1};

  // origin at bottom left corner of bounding box
  private int size;
  private int x;
  private int y;
  private int[] tileXOffsets;
  private int[] tileYOffsets;
  private TETile[] tiles;
  private TETile seedTile;
  private Random randGen;
  private int randColor;

  public HexBlock(int sz, int xx, int yy, TETile seed, Random rg, int rc) {
    size = sz;
    seedTile = seed;
    x = xx;
    y = yy;
    randGen = rg;
    randColor = rc;
    int n = numTiles(size);
    tileXOffsets = new int[n];
    tileYOffsets = new int[n];
    tiles = new TETile[n];
    calcTileOffsets();
    calcTiles();
  }

  public void fillWorld(TETile[][] world) {
    int n = numTiles(size);
    for (int i=0; i<n; i++) {
      world[x+tileXOffsets[i]][y+tileYOffsets[i]] = tiles[i];
    }
  }

  private void calcTileOffsets() {
    int tileIndex = 0;
    // from bottom to top, left to right
    for (int y = 0; y < boundingBoxHeight(size); y++) {
      for (int x = withinHexStartX(size, y); x < withinHexEndX(size, y); x++) {
        tileXOffsets[tileIndex] = x;
        tileYOffsets[tileIndex] = y;
        tileIndex++;
      }
    }
  }

  private void calcTiles() {
    int n = numTiles(size);
    for (int tileIndex=0; tileIndex<n; tileIndex++) {
      tiles[tileIndex] = TETile.colorVariant(seedTile, randColor, randColor, randColor, randGen);
    }
  }

  public static void tesselate(int sz, int n, int[] xs, int[] ys) {
    valiateSize(sz);
    valiateN(n);
    tesselateFromZero(sz, n, xs, ys);
    moveTesselation(sz, n, xs, ys);
  }

  private static void tesselateFromZero(int sz, int n, int[] xs, int[] ys) {
    int oftx = interHexOffsetX(sz),
      ofty = interHexOffsetY(sz),
      tileIndex = 0,
      layerIndex = 0,
      x = 0,
      y = 0;
    xs[tileIndex] = x;
    ys[tileIndex] = y;
    tileIndex++;
    layerIndex++;
    while(true) {
      x = 0;
      y = ofty * 2 * layerIndex;
      for (int edgeIndex=0; edgeIndex<6; edgeIndex++) {
        for (int i=0; i<layerIndex; i++) {
          if (tileIndex < n) {
            xs[tileIndex] = x;
            ys[tileIndex] = y;
            tileIndex++;
            x += oftx * tessXSteps[edgeIndex];
            y += ofty * tessYSteps[edgeIndex];
          } else {
            return;
          }
        }
      }
      layerIndex++;
    }
  }

  private static void moveTesselation(int sz, int n, int[] xs, int[] ys) {
    int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
    for (int i=0; i<n; i++) {
      if (xs[i] < minX) minX = xs[i];
      if (ys[i] < minY) minY = ys[i];
    }
    for (int i=0; i<n; i++) {
      xs[i] -= minX;
      ys[i] -= minY;
    }
  }

  public static int worldWidth(int sz, int n, int[] xs) {
    valiateSize(sz);
    valiateN(n);
    int maxX = Integer.MIN_VALUE;
    for (int i=0; i<n; i++) {
      if (xs[i] > maxX) maxX = xs[i];
    }
    return maxX + boundingBoxWidth(sz);
  }

  public static int worldHeight(int sz, int n, int[] ys) {
    valiateSize(sz);
    valiateN(n);
    int maxY = Integer.MIN_VALUE;
    for (int i=0; i<n; i++) {
      if (ys[i] > maxY) maxY = ys[i];
    }
    return maxY + boundingBoxHeight(sz);
  }

  private static void valiateSize(int sz) {
    if (sz < 2) throw new IllegalArgumentException();
  }

  private static void valiateN(int n) {
    if (n < 1) throw new IllegalArgumentException();
  }

  private static int boundingBoxWidth(int sz) {
    return 3 * sz - 2;
  }

  private static int boundingBoxHeight(int sz) {
    return 2 * sz;
  }

  private static int numTiles(int sz) {
    return 4 * sz * sz - 2 * sz;
  }

  private static int interHexOffsetX(int sz) {
    return 2 * sz - 1;
  }

  private static int interHexOffsetY(int sz) {
    return sz;
  }

  private static int withinHexStartX(int sz, int y) {
    if (y < sz)
      return sz - 1 - y;
    else
      return y - sz;
  }

  private static int withinHexEndX(int sz, int y) {
    if (y < sz)
      return 2 * sz - 1 + y;
    else
      return 4 * sz - 2 - y;
  }
}
