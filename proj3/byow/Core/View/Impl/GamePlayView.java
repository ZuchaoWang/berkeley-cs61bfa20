package byow.Core.View.Impl;

import java.awt.Color;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

public class GamePlayView implements BaseView {
  // let's ignore mouse event, because we cannot use current inputSource implementation with getNextKey being blocking

  private boolean lastKeyIsColon;

  public GamePlayView() {
    lastKeyIsColon = false;
  }

  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    render(sharedState, terenderer);
    while (inputSource.possibleNextInput()) {
      char c = Character.toLowerCase(inputSource.getNextKey());
      ViewType nextViewType = handleNextChar(c, sharedState);
      if (nextViewType != ViewType.GAMEPLAY) return nextViewType;
      render(sharedState, terenderer);
    }
    return null;
  }
  
  private ViewType handleNextChar(char c, SharedState sharedState) {
    ViewType nextViewType = ViewType.GAMEPLAY;
    if (c == 'a') nextViewType = moveAvatar(-1, 0, sharedState);
    else if (c == 's') nextViewType = moveAvatar(0, -1, sharedState);
    else if (c == 'd') nextViewType = moveAvatar(1, 0, sharedState);
    else if (c == 'w') nextViewType = moveAvatar(0, 1, sharedState);
    else if (c == ':') nextViewType = handleColon(sharedState);
    else if (c == 'q') nextViewType = handleQuit(sharedState);
    return nextViewType;
  }

  private ViewType moveAvatar(int dx, int dy, SharedState sharedState) {
    lastKeyIsColon = false;
    if (sharedState.canAvatarMove(dx, dy)) {
      sharedState.moveAvatar(dx, dy);
      TETile curTile = sharedState.getAvatarCurrentTile();
      if (curTile.character() == Tileset.FLOOR.character()) return ViewType.GAMEPLAY;
      else if (curTile.character() == Tileset.LOCKED_DOOR.character()) return ViewType.YOUWIN;
      else if (curTile.character() == Tileset.FLOWER.character()) return ViewType.ENCOUNTER;
    }
    return ViewType.GAMEPLAY;
  }

  private ViewType handleColon(SharedState sharedState) {
    lastKeyIsColon = true;
    return ViewType.GAMEPLAY;
  }

  private ViewType handleQuit(SharedState sharedState) {
    if (lastKeyIsColon) {
      lastKeyIsColon = false;
      return ViewType.SAVE;
    } else {
      return ViewType.GAMEPLAY;
    }
  }

  private void render(SharedState sharedState, TERenderer terenderer) {
    terenderer.renderFrame(sharedState.world);
    Tileset.AVATAR.draw(sharedState.avatarX, sharedState.avatarY);
    StdDraw.setPenColor(new Color(255, 255, 255));
    StdDraw.textLeft(0.5, sharedState.worldHeight + 0.5 * sharedState.hudHeight, "World Map: A/S/D/W to Move, :Q to Quit");
    StdDraw.textRight(sharedState.worldWidth - 0.5, sharedState.worldHeight + 0.5 * sharedState.hudHeight, "Avatar at " + sharedState.getAvatarCurrentTile().description());
    StdDraw.show();
  }
}
