package byow.lab12;

import byow.TileEngine.TETile;

public class HexBlock {
    // origin at top left corner 
    private int size;
    private int x;
    private int y;
    private int numTiles;
    private int[] tileXOffsets;
    private int[] tileYOffsets;
    private TETile[] tiles;
    private TETile seedTile;

    public HexBlock(int sz, TETile seed) {
      size = sz;
      seedTile = seed;
      x = 0;
      y = 0;
      numTiles = calcNumTiles();
      tileXOffsets = new int[numTiles];
      tileYOffsets = new int[numTiles];
      tiles = new TETile[numTiles];
      fillTileOffsets();
    }

    public void fillWorld(TETile[][] world, int envX, int envY) {

    }

    private void fillTileOffsets() {
      int k = 0;
      for (int i=0; i<size; i++) {
        for (int j=-i; j<size+i; j++) {
          tileXOffsets[k] = j;
          tileYOffsets[k] = i;
          k++;
        }
      }
      for (int i=size; i<size*2; i++) {
      }
    }

    private int calcNumTiles() {
      return 4 * size * size - 2 * size;
    }

    public static int tessOffsetX(int sz) {
      validateSize(sz);
      return 2 * sz - 1;
    }

    public static int tessOffsetY(int sz) {
      validateSize(sz);
      return sz;
    }

    private static void validateSize(int s) {
      if (s < 2) throw new IllegalArgumentException();
    }
}
