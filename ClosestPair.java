import java.util.*;
import static java.util.Comparator.comparingDouble;

public final class ClosestPair {
    private ClosestPair() {}

    public static final class Result {
        public final double d;
        public final IntervalPointModels.Point a, b;
        public Result(double d, IntervalPointModels.Point a, IntervalPointModels.Point b) {
            this.d = d; this.a = a; this.b = b;
        }
    }

    public static Result closestPair(List<IntervalPointModels.Point> pts) {
        ArrayList<IntervalPointModels.Point> Px = new ArrayList<>(pts);
        ArrayList<IntervalPointModels.Point> Py = new ArrayList<>(pts);
        Px.sort(comparingDouble(p -> p.x));
        Py.sort(comparingDouble(p -> p.y));
        return closestRec(Px, Py);
    }

    private static Result closestRec(List<IntervalPointModels.Point> Px,
                                     List<IntervalPointModels.Point> Py) {
        int n = Px.size();
        if (n <= 3) return brute(Px);
        int mid = n / 2;
        double midx = Px.get(mid - 1).x;

        List<IntervalPointModels.Point> Lx = Px.subList(0, mid);
        List<IntervalPointModels.Point> Rx = Px.subList(mid, n);

        ArrayList<IntervalPointModels.Point> Ly = new ArrayList<>(mid);
        ArrayList<IntervalPointModels.Point> Ry = new ArrayList<>(n - mid);
        for (IntervalPointModels.Point p : Py) {
            if (p.x <= midx) Ly.add(p); else Ry.add(p);
        }

        Result left = closestRec(Lx, Ly);
        Result right = closestRec(Rx, Ry);
        Result best = (left.d < right.d) ? left : right;
        double d = best.d;

        ArrayList<IntervalPointModels.Point> strip = new ArrayList<>();
        for (IntervalPointModels.Point p : Py) {
            if (Math.abs(p.x - midx) < d) strip.add(p);
        }

        // Check up to next 7 in y-order
        int m = strip.size();
        for (int i = 0; i < m; i++) {
            IntervalPointModels.Point pi = strip.get(i);
            for (int j = i + 1; j < Math.min(i + 8, m); j++) {
                IntervalPointModels.Point pj = strip.get(j);
                double dij = IntervalPointModels.hypot(pi.x - pj.x, pi.y - pj.y);
                if (dij < best.d) best = new Result(dij, pi, pj);
            }
        }
        return best;
    }

    private static Result brute(List<IntervalPointModels.Point> P) {
        double best = Double.POSITIVE_INFINITY;
        IntervalPointModels.Point a = null, b = null;
        int m = P.size();
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                IntervalPointModels.Point p = P.get(i), q = P.get(j);
                double d = IntervalPointModels.hypot(p.x - q.x, p.y - q.y);
                if (d < best) { best = d; a = p; b = q; }
            }
        }
        if (a == null) return new Result(Double.POSITIVE_INFINITY, null, null);
        return new Result(best, a, b);
    }
}
