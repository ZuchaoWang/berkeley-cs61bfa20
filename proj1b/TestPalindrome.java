import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("noon"));
        assertFalse(palindrome.isPalindrome("cat"));
        assertFalse(palindrome.isPalindrome("a"));
        assertFalse(palindrome.isPalindrome(null));

        OffByOne of1 = new OffByOne();
        assertFalse(palindrome.isPalindrome("noon", of1));
        assertTrue(palindrome.isPalindrome("flake", of1));

        OffByN ofn = new OffByN(2);
        assertFalse(palindrome.isPalindrome("noon", ofn));
        assertFalse(palindrome.isPalindrome("flake", ofn));
        assertTrue(palindrome.isPalindrome("fland", ofn));
    }
}