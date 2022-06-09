public class NBody {
  public static double readRadius(String fname) {
    In in = new In(fname);
    in.readInt();
    return in.readDouble();
  }

  public static Body[] readBodies(String fname) {
    In in = new In(fname);
    int n = in.readInt();
    in.readDouble();
    Body[] bs = new Body[n];
    for (int i = 0; i < n; i++) {
      double xP = in.readDouble();
      double yP = in.readDouble();
      double xV = in.readDouble();
      double yV = in.readDouble();
      double m = in.readDouble();
      String img = in.readString();
      bs[i] = new Body(xP, yP, xV, yV, m, img);
    }
    return bs;
  }

  public static void main(String[] args) {
    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String filename = args[2];
    double radius = readRadius(filename);
    Body[] bs = readBodies(filename);

    StdDraw.enableDoubleBuffering();
    StdDraw.setScale(-radius, radius);

    double t = 0;
    double[] xForces = new double[bs.length];
    double[] yForces = new double[bs.length];
    while (t < T) {
      for (int i = 0; i < bs.length; i++) {
        xForces[i] = bs[i].calcNetForceExertedByX(bs);
        yForces[i] = bs[i].calcNetForceExertedByY(bs);
      }
      for (int i = 0; i < bs.length; i++) {
        bs[i].update(dt, xForces[i], yForces[i]);
      }
      StdDraw.clear();
      StdDraw.picture(0, 0, "images/" + "starfield.jpg");
      for (Body b : bs) {
        b.draw();
      }
      StdDraw.show();
      StdDraw.pause(5);
      t += dt;
    }
    StdOut.printf("%d\n", bs.length);
    StdOut.printf("%.2e\n", radius);
    for (int i = 0; i < bs.length; i++) {
      StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
          bs[i].xxPos, bs[i].yyPos, bs[i].xxVel,
          bs[i].yyVel, bs[i].mass, bs[i].imgFileName);
    }
  }
}
