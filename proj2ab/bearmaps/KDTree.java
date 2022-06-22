package bearmaps;

import java.util.List;

public class KDTree implements PointSet {
  private final static int DIR_ROOT = 0;
  private final static int DIR_X = 0;
  private final static int DIR_Y = 1;

  private class KDTreeNode {
    public Point point;
    public int direction; // 0 for x, 1 for y; not really needed, just for debugging
    public KDTreeNode left;
    public KDTreeNode right;

    public KDTreeNode(Point pt, int dir) {
      point = pt;
      direction = dir;
      left = null;
      right = null;
    }
  }

  KDTreeNode root;

  public KDTree(List<Point> points) {
    root = null;
    for (Point pt : points) {
      insert(pt);
    }
  }

  private void insert(Point pt) {
    if (pt == null)
      throw new IllegalArgumentException();
    root = insertAtNode(root, DIR_ROOT, pt);
  }

  private KDTreeNode insertAtNode(KDTreeNode curNode, int curDir, Point pt) {
    if (curNode == null) {
      return new KDTreeNode(pt, curDir);
    } else {
      int childIndex = chooseChild(curNode, curDir, pt);
      if (childIndex == 0) {
        curNode.left = insertAtNode(curNode.left, nextDir(curDir), pt);
      } else {
        curNode.right = insertAtNode(curNode.right, nextDir(curDir), pt);
      }
      return curNode;
    }
  }

  private int nextDir(int direction) {
    return 1 - direction;
  }

  private int chooseChild(KDTreeNode curNode, int curDir, Point pt) {
    // return 0 for left, 1 for right
    if (curDir == DIR_X)
      return pt.getX() <= curNode.point.getX() ? 0 : 1;
    else
      return pt.getY() <= curNode.point.getY() ? 0 : 1;
  }

  @Override
  public Point nearest(double x, double y) {
    Point curNearestPt = null,
        targetPt = new Point(x, y);
    return nearestAtNode(root, DIR_ROOT, targetPt, curNearestPt);
  }

  private Point nearestAtNode(KDTreeNode curNode, int curDir, Point pt, Point curNearestPt) {
    if (curNode == null)
      return curNearestPt;
    if (curNearestPt == null || Point.distance(curNode.point, pt) < Point.distance(curNearestPt, pt))
      curNearestPt = curNode.point;
    curNearestPt = nearestAtNode(curNode.left, nextDir(curDir), pt, curNearestPt);
    curNearestPt = nearestAtNode(curNode.right, nextDir(curDir), pt, curNearestPt);
    return curNearestPt;
  }
}
