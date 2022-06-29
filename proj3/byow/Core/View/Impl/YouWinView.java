package byow.Core.View.Impl;

import java.awt.Color;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

public class YouWinView implements BaseView {
  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    render(sharedState, terenderer);
    while (inputSource.possibleNextInput()) {
      char c = Character.toLowerCase(inputSource.getNextKey());
      ViewType nextViewType = handleNextChar(c, sharedState);
      if (nextViewType != ViewType.YOUWIN) return nextViewType;
    }
    return null;
  }

  private void render(SharedState sharedState, TERenderer terenderer) {
    StdDraw.clear(new Color(0, 0, 0));
    StdDraw.setPenColor(new Color(255, 255, 255));
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.75, "Youn Win!");
    StdDraw.text(sharedState.worldWidth/2, sharedState.worldHeight * 0.4, "Quit (Q)");
    StdDraw.show();
  }

  private ViewType handleNextChar(char c, SharedState sharedState) {
    ViewType nextViewType = ViewType.YOUWIN;
    if (c == 'q') nextViewType = handleQuit(sharedState);
    return nextViewType;
  }

  private ViewType handleQuit(SharedState sharedState) {
    return ViewType.QUIT;
  }
}
