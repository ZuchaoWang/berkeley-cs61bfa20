/**
 * TODO: Fill in the add and floor methods.
 */
public class AListFloorSet implements Lab5FloorSet {
    AList<Double> items;

    public AListFloorSet() {
        items = new AList<>();
    }

    public void add(double x) {
        items.addLast(x);
    }

    public double floor(double x) {
        double res = Double.NEGATIVE_INFINITY;
        for(int i=0; i<items.size(); i++) {
            double val = items.get(i);
            if (val > res && val <= x) {
                res = val;
            }
        }
        return res;
    }
}
