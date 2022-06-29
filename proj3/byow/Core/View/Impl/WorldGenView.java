package byow.Core.View.Impl;

import java.awt.Color;
import java.util.Random;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

public class WorldGenView implements BaseView {
  private String seedStr;

  public WorldGenView() {
    seedStr = "";
  }

  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    render(sharedState, terenderer);
    while (inputSource.possibleNextInput()) {
      char c = Character.toLowerCase(inputSource.getNextKey());
      ViewType nextViewType = handleNextChar(c, sharedState);
      if (nextViewType != ViewType.WORLDGEN) return nextViewType;
      render(sharedState, terenderer);
    }
    return null;
  }
  
  private void render(SharedState sharedState, TERenderer terenderer) {
    StdDraw.clear(new Color(0, 0, 0));
    StdDraw.setPenColor(new Color(255, 255, 255));
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.75, "Genereate New Game");
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.4, "Enter Seed, End with S");
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.4-1, seedStr);
    StdDraw.show();
  }

  private ViewType handleNextChar(char c, SharedState sharedState) {
    ViewType nextViewType = ViewType.WORLDGEN;
    if (c == 's') nextViewType = handleSeedEnd(sharedState);
    else if (c > '0' && c <= '9') nextViewType = handleSeedDigit(c);
    return nextViewType;
  }

  private ViewType handleSeedEnd(SharedState sharedState) {
    Random randGen = new Random(getSeedLong());
    sharedState.generateWorld(randGen);
    sharedState.generateAvatarPos(randGen);
    return ViewType.GAMEPLAY;
  }

  private ViewType handleSeedDigit(char c) {
    seedStr += c;
    return ViewType.WORLDGEN;
  }

  private long getSeedLong() {
    return Long.parseLong(seedStr, 10);
  }
}
