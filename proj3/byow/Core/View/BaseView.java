package byow.Core.View;

import byow.Core.SharedState;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;

public interface BaseView {
  // each view ends by entering another view, the new view type is returned
  // if returned view type is null, then the whole game ends
  public ViewType interact(InputSource inputSource, SharedState sharedState, TERenderer terenderer);
}
