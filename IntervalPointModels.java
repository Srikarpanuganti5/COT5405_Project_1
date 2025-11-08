import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Shared data models and small utilities for the project:
 *  - Interval (start, finish) for MRI scheduling (greedy)
 *  - Point (x, y) for closest-pair (divide & conquer)
 *  - Synthetic data generators and small math helpers
 */
public final class IntervalPointModels {

    private IntervalPointModels() {
        // utility holder (no instances)
    }

    /* =========================
     * Interval model & helpers
     * ========================= */
    public static final class Interval {
        public final int start;
        public final int finish;

        public Interval(int start, int finish) {
            if (finish < start) {
                throw new IllegalArgumentException("finish < start: (" + start + ", " + finish + ")");
            }
            this.start = start;
            this.finish = finish;
        }

        @Override
        public String toString() {
            return "(" + start + "," + finish + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Interval)) return false;
            Interval that = (Interval) o;
            return start == that.start && finish == that.finish;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, finish);
        }
    }

    /**
     * Generate n synthetic intervals within a day of length {@code dayLen} (e.g., minutes).
     * Each interval length is uniformly sampled in [minLen, maxLen], inclusive.
     * Start times are sampled so that start + length <= dayLen.
     */
    public static List<Interval> synthIntervals(int n, int dayLen, int minLen, int maxLen, long seed) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        if (dayLen <= 0) throw new IllegalArgumentException("dayLen must be > 0");
        if (minLen <= 0 || maxLen < minLen) {
            throw new IllegalArgumentException("Require 0 < minLen <= maxLen");
        }
        ArrayList<Interval> list = new ArrayList<>(n);
        Random rng = new Random(seed);

        final int span = maxLen - minLen + 1; // >= 1
        for (int i = 0; i < n; i++) {
            int len = minLen + rng.nextInt(span);
            int maxStart = Math.max(0, dayLen - len);
            // if len > dayLen, clamp to whole day (degenerate interval at [0, dayLen])
            if (maxStart == 0 && len > dayLen) {
                len = dayLen;
            }
            int start = (maxStart > 0) ? rng.nextInt(maxStart + 1) : 0;
            list.add(new Interval(start, start + len));
        }
        return list;
    }

    /* ======================
     * Point model & helpers
     * ====================== */
    public static final class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            if (Double.isNaN(x) || Double.isNaN(y) || Double.isInfinite(x) || Double.isInfinite(y)) {
                throw new IllegalArgumentException("Point coordinates must be finite numbers");
            }
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Generate n points uniformly in the unit square [0,1] x [0,1].
     */
    public static List<Point> uniformPoints(int n, long seed) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        ArrayList<Point> pts = new ArrayList<>(n);
        Random rng = new Random(seed);
        for (int i = 0; i < n; i++) {
            pts.add(new Point(rng.nextDouble(), rng.nextDouble()));
        }
        return pts;
        // If you prefer Java 17+ SplittableRandom:
        // SplittableRandom rng = new SplittableRandom(seed);
        // pts.add(new Point(rng.nextDouble(), rng.nextDouble()));
    }

    /* ==============
     * Math helpers
     * ============== */

    /** Stable Euclidean norm for (dx, dy). */
    public static double hypot(double dx, double dy) {
        return Math.hypot(dx, dy);
    }

    /** n * log2(n), with protection for small n. */
    public static double nLog2n(int n) {
        int nn = Math.max(2, n);
        return nn * (Math.log(nn) / Math.log(2.0));
    }
}
