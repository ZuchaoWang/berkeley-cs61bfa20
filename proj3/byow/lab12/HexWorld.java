package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
  private static final long SEED = 2873123;
  private static final Random RANDOM = new Random(SEED);

  public static void main(String[] args) {
    // tesselation
    int size = 4, 
      n = 19;
    int xs[] = new int[n],
      ys[] = new int[n];
    HexBlock.tesselate(size, n, xs, ys);
    int worldWidth = HexBlock.worldWidth(size, n, xs),
      worldHeight = HexBlock.worldHeight(size, n, ys);

    // initialize the tile rendering engine
    TERenderer ter = new TERenderer();
    ter.initialize(worldWidth, worldHeight);

    // initialize tiles
    TETile[][] world = new TETile[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x += 1) {
        for (int y = 0; y < worldHeight; y += 1) {
            world[x][y] = Tileset.NOTHING;
        }
    }

    // initialize hex-blocks
    HexBlock[] hexs = new HexBlock[n];
    int randColor = 32;
    for (int i=0; i<n; i++) {
      hexs[i] = new HexBlock(size, xs[i], ys[i], randomTile(), RANDOM, randColor);
    }

    // fill world with hex-blocks
    for (int i=0; i<n; i++) {
      hexs[i].fillWorld(world);
    }

    // draws the world to the screen
    ter.renderFrame(world);
  }

  private static TETile randomTile() {
    int tileNum = RANDOM.nextInt(5);
    switch (tileNum) {
        case 0: return Tileset.MOUNTAIN;
        case 1: return Tileset.TREE;
        case 2: return Tileset.GRASS;
        case 3: return Tileset.FLOWER;
        case 4: return Tileset.SAND;
        default: return Tileset.NOTHING;
    }
  }
}
