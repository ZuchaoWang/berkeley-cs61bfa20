package byow.Core.View.Impl;

import java.awt.Color;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

public class EncounterView implements BaseView {
  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    render(sharedState, terenderer);
    while (inputSource.possibleNextInput()) {
      char c = Character.toLowerCase(inputSource.getNextKey());
      ViewType nextViewType = handleNextChar(c, sharedState);
      if (nextViewType != ViewType.ENCOUNTER) return nextViewType;
    }
    return null;
  }

  private void render(SharedState sharedState, TERenderer terenderer) {
    StdDraw.clear(new Color(0, 0, 0));
    StdDraw.setPenColor(new Color(255, 255, 255));
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.75, "Encounter: " + sharedState.getAvatarCurrentTile().description());
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.4, "Back to World Map (B)");
    StdDraw.show();
  }

  private ViewType handleNextChar(char c, SharedState sharedState) {
    ViewType nextViewType = ViewType.ENCOUNTER;
    if (c == 'b') nextViewType = handleBack(sharedState);
    return nextViewType;
  }

  private ViewType handleBack(SharedState sharedState) {
    return ViewType.GAMEPLAY;
  }
}
