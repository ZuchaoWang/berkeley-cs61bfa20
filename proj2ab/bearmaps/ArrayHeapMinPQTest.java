package bearmaps;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArrayHeapMinPQTest {
  @Test
  public void testSimpleCase() {
    NaiveMinPQ<String> pq1 = new NaiveMinPQ<String>();
    performTestSimpleCase(pq1);
    ArrayHeapMinPQ<String> pq2 = new ArrayHeapMinPQ<String>();
    performTestSimpleCase(pq2);
  }

  private void performTestSimpleCase(ExtrinsicMinPQ<String> pq) {
    String[] strs = { "A", "B", "C" };
    double[] prios = { 2.0, 1.0, 3.0 };
    for (int i = 0; i < 3; i++) {
      pq.add(strs[i], prios[i]);
    }
    assertEquals("B", pq.getSmallest());
    assertEquals(3, pq.size());
    assertEquals(true, pq.contains("B"));

    pq.changePriority("B", 4.0);
    assertEquals(3, pq.size());
    assertEquals("A", pq.removeSmallest());
    assertEquals(false, pq.contains("A"));
    assertEquals(2, pq.size());
    assertEquals("C", pq.removeSmallest());
    assertEquals(1, pq.size());
    assertEquals("B", pq.removeSmallest());
    assertEquals(0, pq.size());
  }

  @Test
  public void testRandomCase() {
    List<String> curStrs = new ArrayList<String>();

    int seed = 0;
    int N = 10000;
    double R = 10000;

    Random rg = new Random(seed);
    NaiveMinPQ<String> pq1 = new NaiveMinPQ<String>();
    ArrayHeapMinPQ<String> pq2 = new ArrayHeapMinPQ<String>();

    int op;
    int rdInt;
    double rdDb;
    String curStr;

    for (int i = 0; i < 2 * N; i++) {
      if (i < N) { // first N rounds
        if (curStrs.size() > 0)
          op = rg.nextInt(8); // if not empty, add has 2 chances
        else
          op = 6; // if empty, must add
      } else { // second N rounds
        if (curStrs.size() > 0)
          op = rg.nextInt(6); // if not empty, add has no chance
        else
          break; // if empty, end test
      }
      switch (op) {
        case 0: // size
          assertEquals(pq1.size(), pq2.size());
          break;
        case 1: // getSmallest
          assertEquals(pq1.getSmallest(), pq2.getSmallest());
          break;
        case 2: // removeSmallest
          curStrs.remove(pq1.getSmallest());
          assertEquals(pq1.removeSmallest(), pq2.removeSmallest());
          break;
        case 3: // contains true
          rdInt = rg.nextInt(curStrs.size());
          curStr = curStrs.get(rdInt);
          assertEquals(pq1.contains(curStr), pq2.contains(curStr));
          break;
        case 4: // contains likely false
          rdInt = rg.nextInt(N * 2);
          curStr = "s" + rdInt;
          assertEquals(pq1.contains(curStr), pq2.contains(curStr));
          break;
        case 5: // changePriority
          rdInt = rg.nextInt(curStrs.size());
          curStr = curStrs.get(rdInt);
          rdDb = rg.nextDouble() * R;
          pq1.changePriority(curStr, rdDb);
          pq2.changePriority(curStr, rdDb);
          break;
        default: // add
          curStr = "s" + i;
          rdDb = rg.nextDouble() * R;
          curStrs.add(curStr);
          pq1.add(curStr, rdDb);
          pq2.add(curStr, rdDb);
      }
    }
  }

  @Test
  public void testTime() {
    int[] Ns = { 10000, 20000, 40000, 80000, 160000, 320000, 640000, 1280000, 2560000, 5120000, 10240000 };
    int maxN = Ns[Ns.length - 1];
    String[] strs = new String[maxN];
    double[] prios = new double[maxN];
    String[] containStrs = new String[maxN];

    int seed = 0;
    double R = 10000;
    Random rg = new Random(seed);
    for (int i = 0; i < maxN; i++) {
      strs[i] = "s" + i;
      prios[i] = rg.nextDouble() * R;
      containStrs[i] = "s" + rg.nextInt(maxN * 2);
    }

    ArrayHeapMinPQ<String> pq;
    long tstart, tend;
    for (int i = 0; i < Ns.length; i++) {
      pq = new ArrayHeapMinPQ<String>();
      tstart = System.currentTimeMillis();
      for (int j = 0; j < Ns[i]; j++) {
        pq.add(strs[j], prios[j]);
      }
      tend = System.currentTimeMillis();
      System.out
          .println(
              "Insertion of " + Ns[i] + " items into ArrayHeapMinPQ takes " + (tend - tstart) / 1000.0 + " seconds.");
    }
    
    for (int i = 0; i < Ns.length; i++) {
      pq = new ArrayHeapMinPQ<String>();
      for (int j = 0; j < Ns[i]; j++) {
        pq.add(strs[j], prios[j]);
      }
      tstart = System.currentTimeMillis();
      for (int j = 0; j < Ns[i]; j++) {
        pq.contains(containStrs[j]);
      }
      tend = System.currentTimeMillis();
      System.out
          .println("Contain query repeated " + Ns[i] + " times on ArrayHeapMinPQ with " + maxN + " items takes "
              + (tend - tstart) / 1000.0 + " seconds.");
    }
  }
}
