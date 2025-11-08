import java.util.*;
public class Main {
    public static void main(String[] args) {
        // Greedy demo
        List<IntervalPointModels.Interval> intervals = List.of(
            new IntervalPointModels.Interval(1,4),
            new IntervalPointModels.Interval(3,5),
            new IntervalPointModels.Interval(0,6),
            new IntervalPointModels.Interval(5,7),
            new IntervalPointModels.Interval(3,9),
            new IntervalPointModels.Interval(5,9),
            new IntervalPointModels.Interval(6,10),
            new IntervalPointModels.Interval(8,11),
            new IntervalPointModels.Interval(8,12),
            new IntervalPointModels.Interval(2,14),
            new IntervalPointModels.Interval(12,16)
        );
        var chosen = GreedyMRI.intervalScheduling(intervals);
        System.out.println("Greedy chosen intervals: " + chosen);
        System.out.println("Count: " + chosen.size());

        // Closest-pair demo
        List<IntervalPointModels.Point> points = List.of(
            new IntervalPointModels.Point(0,0),
            new IntervalPointModels.Point(5,4),
            new IntervalPointModels.Point(3,1),
            new IntervalPointModels.Point(1,1),
            new IntervalPointModels.Point(2,2),
            new IntervalPointModels.Point(9,9)
        );
        var res = ClosestPair.closestPair(points);
        System.out.printf("Closest pair distance = %.4f\n", res.d);
    }
}
