public class Palindrome {
  public Deque<Character> wordToDeque(String word) {
    Deque<Character> dq = new LinkedListDeque<Character>();
    if (word != null) {
      for (int i=0; i<word.length(); i++) {
        dq.addLast(word.charAt(i));
      }
    }
    return dq;
  }

  public boolean isPalindrome(String word) {
    Deque<Character> dq = wordToDeque(word);
    if (dq.size() < 2) return false;
    while (dq.size() >= 2) {
      char front = dq.removeFirst();
      char last = dq.removeLast();
      if (front != last) return false;
    }
    return true;
  }

  public boolean isPalindrome(String word, CharacterComparator cc) {
    Deque<Character> dq = wordToDeque(word);
    if (dq.size() < 2) return false;
    while (dq.size() >= 2) {
      char front = dq.removeFirst();
      char last = dq.removeLast();
      if (!cc.equalChars(front, last)) return false;
    }
    return true;
  }
}
