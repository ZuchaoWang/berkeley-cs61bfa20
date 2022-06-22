package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {
  private List<Point> pts;

  public NaivePointSet(List<Point> points) {
    pts = points;
  }
  
  @Override
  public Point nearest(double x, double y) {
    Point curNearestPt = null,
      targetPt = new Point(x, y);
    double curMinDis2 = Double.MAX_VALUE;
    for (Point pt: pts) {
      double dis2 = Point.distance(pt, targetPt);
      if (dis2 < curMinDis2) {
        curMinDis2 = dis2;
        curNearestPt = pt;
      }
    }
    return curNearestPt;
  }
  
}
