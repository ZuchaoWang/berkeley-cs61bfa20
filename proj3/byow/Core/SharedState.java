package byow.Core;

import java.io.File;
import java.util.Random;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;

public class SharedState {
  private static final String saveFileName = "save.txt";

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

  public TETile getAvatarCurrentTile() {
    return world[avatarX][avatarY];
  }

  public boolean canAvatarMove(int dx, int dy) {
    if (dx * dx + dy * dy != 1) throw new IllegalArgumentException();
    int nextAvatarX = avatarX + dx,
        nextAvatarY = avatarY + dy;
    return WorldUtils.canMoveTo(world, nextAvatarX, nextAvatarY);
  }

  public void moveAvatar(int dx, int dy) {
    if (canAvatarMove(dx, dy)) {
      avatarX += dx;
      avatarY += dy;
    }
  }

  public void clearWorld() {
    world = new TETile[worldWidth][worldHeight];
    for (int x = 0; x < worldWidth; x += 1) {
      for (int y = 0; y < worldHeight; y += 1) {
        world[x][y] = Tileset.NOTHING;
      }
    }
  }

  public void generateWorld(Random randGen) {
    WorldGen.generateWorld(world, randGen);
  }

  public void generateAvatarPos(Random randGen) {
    int[] avatarPos = WorldGen.generateAvatarPos(world, randGen);
    avatarX = avatarPos[0];
    avatarY = avatarPos[1];
  }

  public boolean canLoad() {
    File file = new File(saveFileName);
    return file.exists();
  }

  public void load() {
    In in = new In(saveFileName);
    avatarX = in.readInt();
    avatarY = in.readInt();
    in.readLine(); // new line at the end
    TETile.load(in, world);
  }

  public void save() {
    Out out = new Out(saveFileName);
    out.println(avatarX);
    out.println(avatarY);
    TETile.save(out, world);
  }
}
