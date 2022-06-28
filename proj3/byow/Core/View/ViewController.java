package byow.Core.View;

import byow.Core.SharedState;
import byow.Core.View.Impl.GamePlayView;
import byow.Core.View.Impl.WelcomeView;
import byow.Core.View.Impl.WorldGenView;
import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;

public class ViewController {
  private BaseView currentView;

  public ViewController() {
    currentView = createView(ViewType.WELCOME);
  }

  public void interact(InputSource inputSource, SharedState sharedState, TERenderer terender) {
    while (true) {
      ViewType nextViewType = currentView.interact(inputSource, sharedState, terender);
      if (nextViewType == null)
        break;
      currentView = createView(nextViewType);
    }
  }

  private BaseView createView(ViewType type) {
    switch (type) {
      case WELCOME:
        return new WelcomeView();
      case ENCOUNTER:
      case GAMEPLAY:
        return new GamePlayView();
      case WORLDGEN:
        return new WorldGenView();
      case YOUWIN:
      case QUIT:
      default:
        return null;
    }
  }
}
