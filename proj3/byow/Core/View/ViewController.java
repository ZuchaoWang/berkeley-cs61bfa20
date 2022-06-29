package byow.Core.View;

import byow.Core.SharedState;
import byow.Core.View.Impl.EncounterView;
import byow.Core.View.Impl.ExitView;
import byow.Core.View.Impl.GamePlayView;
import byow.Core.View.Impl.SaveView;
import byow.Core.View.Impl.WelcomeView;
import byow.Core.View.Impl.WorldGenView;
import byow.Core.View.Impl.YouWinView;
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
        return new EncounterView();
      case GAMEPLAY:
        return new GamePlayView();
      case WORLDGEN:
        return new WorldGenView();
      case YOUWIN:
        return new YouWinView();
      case SAVE:
        return new SaveView();
      case EXIT:
        return new ExitView();
      default:
        return null;
    }
  }
}
