public class OffByN implements CharacterComparator {
  private int n;
  
  public OffByN(int nn) {
    n = nn;
  }

  @Override
  public boolean equalChars(char x, char y) {
    int diff = x - y;
    return diff == n || diff == -n;
  }
}
