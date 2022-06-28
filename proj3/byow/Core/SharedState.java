package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class SharedState {
  public int worldWidth;
  public int worldHeight;
  public int hudHeight;
  public TETile[][] world;
  public int avatarX;
  public int avatarY;
  
  public SharedState(int w, int h, int hh) {
    worldWidth = w;
    worldHeight = h;
    hudHeight = hh;
    avatarX = 0;
    avatarY = 0;
    clearWorld();
  }

  public void clearWorld() {
    world = new TETile[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x += 1) {
        for (int y = 0; y < worldHeight; y += 1) {
            world[x][y] = Tileset.NOTHING;
        }
    }
  }

  public void generateWorld(long seed) {
    WorldGen.generateWorld(world, worldWidth, worldHeight, seed);
  }

  public void generateAvatarPos() {}

  public boolean canLoad() {
    return false;
  }

  public void load() {}

  public void save() {}
}
