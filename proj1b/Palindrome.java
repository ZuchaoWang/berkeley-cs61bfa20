public class Palindrome {
  public Deque<Character> wordToDeque(String word) {
    Deque<Character> dq = new LinkedListDeque<Character>();
    for (int i=0; i<word.length(); i++) {
      dq.addLast(word.charAt(i));
    }
    return dq;
  }

  public boolean isPalindrome(String word) {
    return true;
  }
}
