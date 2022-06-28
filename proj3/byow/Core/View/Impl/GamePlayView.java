package byow.Core.View.Impl;

import java.awt.Color;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

public class GamePlayView implements BaseView {
  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    render(sharedState, terenderer);
    return null;
  }
  
  private void render(SharedState sharedState, TERenderer terenderer) {
    terenderer.renderFrame(sharedState.world);
    StdDraw.setPenColor(new Color(255, 255, 255));
    StdDraw.textLeft(0, sharedState.worldHeight + 0.5 * sharedState.hudHeight, "Game Play: Press A/S/D/W to Move");
    StdDraw.show();
  }
}
