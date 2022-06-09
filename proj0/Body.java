public class Body {
    public static double G = 6.67e-11;

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Body(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Body(Body b) {
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;
    }

    public double calcDistance(Body b) {
        double dx = b.xxPos - xxPos;
        double dy = b.yyPos - yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Body b) {
        if (this.equals(b)) {
            return 0;
        } else {
            double r = calcDistance(b);
            return G * mass * b.mass / r / r;
        }
    }

    public double calcForceExertedByX(Body b) {
        if (this.equals(b)) {
            return 0;
        } else {
            double dx = b.xxPos - xxPos;
            double r = calcDistance(b);
            double f = calcForceExertedBy(b);
            return f * dx / r;
        }
    }

    public double calcForceExertedByY(Body b) {
        if (this.equals(b)) {
            return 0;
        } else {
            double dy = b.yyPos - yyPos;
            double r = calcDistance(b);
            double f = calcForceExertedBy(b);
            return f * dy / r;
        }
    }

    public double calcNetForceExertedByX(Body[] bs) {
        double nf = 0;
        for(Body b: bs) {
            nf += calcForceExertedByX(b);
        }
        return nf;
    }

    public double calcNetForceExertedByY(Body[] bs) {
        double nf = 0;
        for(Body b: bs) {
            nf += calcForceExertedByY(b);
        }
        return nf;
    }

    public void update(double dt, double fX, double fY) {
        double xxAcc = fX / mass;
        double yyAcc = fY / mass;
        xxVel += xxAcc * dt;
        yyVel += yyAcc * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
