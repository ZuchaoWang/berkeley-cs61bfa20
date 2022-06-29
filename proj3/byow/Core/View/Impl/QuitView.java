package byow.Core.View.Impl;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;

public class QuitView implements BaseView {
  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    System.exit(0);
    return null;
  }
}
