package byow.Core.View.Impl;

import byow.Core.SharedState;
import byow.Core.View.BaseView;
import byow.Core.View.ViewType;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;

public class SaveView implements BaseView {
  @Override
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer) {
    sharedState.save();
    return ViewType.EXIT;
  }
}
