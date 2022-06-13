import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
  @Test
  public void testRandom() {
    ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();
    StudentArrayDeque<Integer> stu = new StudentArrayDeque<>();
    Integer solx, stux;
    String message = "";

    for (int i = 0; i < 200; i += 1) {
      double numberBetweenZeroAndOne = StdRandom.uniform();
      int choice = (int)(numberBetweenZeroAndOne * 4);
      assertEquals(message, sol.size(), stu.size());
      if (sol.size() == 0) choice = Math.min(choice, 1);
      switch(choice) {
        case 0:
          sol.addFirst(i);
          stu.addFirst(i);
          message += ("addFirst(" + i + ")\n");
          break;
        case 1:
          sol.addLast(i);
          stu.addLast(i);
          message += ("addLast(" + i + ")\n");
          break;
        case 2:
          solx = sol.removeFirst();
          stux = stu.removeFirst();
          message += "removeFirst()\n";
          assertEquals(message, solx, stux);
          break;
        case 3:
          solx = sol.removeLast();
          stux = stu.removeLast();
          message += "removeLast()\n";
          assertEquals(message, solx, stux);
          break;
        default:
          assertTrue(message, false);
      }
    }
  }
}
