import java.io.*;
import java.util.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Bench {
    public static void main(String[] args) throws Exception {
        new File("data").mkdirs();
        benchGreedy();
        benchClosest();
        System.out.println("Wrote data/greedy_runtime.csv and data/dc_runtime.csv");
    }

    static void benchGreedy() throws Exception {
        int[] ns = {1000, 2000, 5000, 10000, 20000, 50000};
        ArrayList<double[]> rows = new ArrayList<>();
        for (int n : ns) {
            double best = Double.POSITIVE_INFINITY;
            for (int r = 0; r < 3; r++) {
                var acts = IntervalPointModels.synthIntervals(n, 24*60, 15, 120, 42L + r);
                long t0 = System.nanoTime();
                GreedyMRI.intervalScheduling(acts);
                long t1 = System.nanoTime();
                best = Math.min(best, (t1 - t0) / 1e9);
            }
            double nlogn = IntervalPointModels.nLog2n(n);
            rows.add(new double[]{n, best, nlogn});
        }
        double c = rows.get(rows.size()-1)[1] / rows.get(rows.size()-1)[2];
        try (var w = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("data/greedy_runtime.csv"), UTF_8))) {
            w.write("n,time_sec,c_nlogn\n");
            for (double[] r : rows) {
                double fit = c * r[2];
                w.write((int)r[0] + "," + r[1] + "," + fit + "\n");
            }
        }
    }

    static void benchClosest() throws Exception {
        int[] ns = {1000, 2000, 5000, 10000, 20000};
        ArrayList<double[]> rows = new ArrayList<>();
        for (int n : ns) {
            double best = Double.POSITIVE_INFINITY;
            for (int r = 0; r < 3; r++) {
                var pts = IntervalPointModels.uniformPoints(n, 2025L + r);
                long t0 = System.nanoTime();
                ClosestPair.closestPair(pts);
                long t1 = System.nanoTime();
                best = Math.min(best, (t1 - t0) / 1e9);
            }
            double nlogn = IntervalPointModels.nLog2n(n);
            rows.add(new double[]{n, best, nlogn});
        }
        double c = rows.get(rows.size()-1)[1] / rows.get(rows.size()-1)[2];
        try (var w = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("data/dc_runtime.csv"), UTF_8))) {
            w.write("n,time_sec,c_nlogn\n");
            for (double[] r : rows) {
                double fit = c * r[2];
                w.write((int)r[0] + "," + r[1] + "," + fit + "\n");
            }
        }
    }
}
