package bearmaps;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTreeTest {
  private final double delta = 1e-6;

  @Test
  public void testSimpleCase() {
    Point p1 = new Point(1.1, 2.2);
    Point p2 = new Point(3.3, 4.4);
    Point p3 = new Point(-2.9, 4.2);
    List<Point> pts = List.of(p1, p2, p3);

    NaivePointSet nn1 = new NaivePointSet(pts);
    assertEquals(p2, nn1.nearest(3.0, 4.0));
    
    KDTree nn2 = new KDTree(pts);
    assertEquals(p2, nn2.nearest(3.0, 4.0));
  }

  @Test
  public void testSimpleCase2() {
    Point p1 = new Point(2, 3);
    Point p2 = new Point(4, 2);
    Point p3 = new Point(4, 5);
    Point p4 = new Point(3, 3);
    Point p5 = new Point(1, 5);
    Point p6 = new Point(4, 4);
    List<Point> pts = List.of(p1, p2, p3, p4, p5, p6);

    NaivePointSet nn1 = new NaivePointSet(pts);
    assertEquals(p5, nn1.nearest(0, 7));
    
    KDTree nn2 = new KDTree(pts);
    assertEquals(p5, nn2.nearest(0, 7));
  }

  @Test
  public void testRandomCase() {
    List<Point> pts = new ArrayList<Point>();
    
    int seed = 0;
    int N = 10000;
    int M = 10000;
    double R = 10000;
    Random rg = new Random(seed);
    for(int i=0; i<N; i++) {
      double x = rg.nextDouble() * R;
      double y = rg.nextDouble() * R;
      pts.add(new Point(x, y));
    }
    double[] targetXs = new double[M];
    double[] targetYs = new double[M];
    for(int i=0; i<M; i++) {
      targetXs[i] = rg.nextDouble() * R;
      targetYs[i] = rg.nextDouble() * R;
    }

    NaivePointSet nn1 = new NaivePointSet(pts);
    KDTree nn2 = new KDTree(pts);
    for(int i=0; i<M; i++) {
      double x = targetXs[i];
      double y = targetYs[i];
      Point targetPt = new Point(x, y);
      double dis1 = Point.distance(targetPt, nn1.nearest(x, y));
      double dis2 = Point.distance(targetPt, nn2.nearest(x, y));
      assertEquals(dis1, dis2, delta);
    }
  }

  @Test
  public void testTime() {
    List<Point> pts = new ArrayList<Point>();
    
    int seed = 0;
    int N = 1000000;
    int M = 1000;
    double R = 10000;
    Random rg = new Random(seed);
    for(int i=0; i<N; i++) {
      double x = rg.nextDouble() * R;
      double y = rg.nextDouble() * R;
      pts.add(new Point(x, y));
    }
    double[] targetXs = new double[M];
    double[] targetYs = new double[M];
    for(int i=0; i<M; i++) {
      targetXs[i] = rg.nextDouble() * R;
      targetYs[i] = rg.nextDouble() * R;
    }

    long tstart, tend;
    tstart = System.currentTimeMillis();
    NaivePointSet nn1 = new NaivePointSet(pts);
    tend = System.currentTimeMillis();
    System.out.println("Insertion of " + N + " points into NaivePointSet takes " + (tend - tstart)/1000.0 +  " seconds.");

    tstart = System.currentTimeMillis();
    KDTree nn2 = new KDTree(pts);
    tend = System.currentTimeMillis();
    System.out.println("Insertion of " + N + " points into KDTree takes " + (tend - tstart)/1000.0 +  " seconds.");

    tstart = System.currentTimeMillis();
    for(int i=0; i<M; i++) {
      double x = targetXs[i];
      double y = targetYs[i];
      nn1.nearest(x, y);
    }
    tend = System.currentTimeMillis();
    System.out.println("Nearest query repeated " + M + " times on NaivePointSet with " + N + " points takes " + (tend - tstart)/1000.0 +  " seconds.");

    tstart = System.currentTimeMillis();
    for(int i=0; i<M; i++) {
      double x = targetXs[i];
      double y = targetYs[i];
      nn2.nearest(x, y);
    }
    tend = System.currentTimeMillis();
    System.out.println("Nearest query repeated " + M + " times on KDTree with " + N + " points takes " + (tend - tstart)/1000.0 +  " seconds.");
  }
}
