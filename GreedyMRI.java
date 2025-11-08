import java.util.*;
import static java.util.Comparator.comparingInt;

public final class GreedyMRI {
    private GreedyMRI() {}

    // Earliest-finish-time greedy
    public static List<IntervalPointModels.Interval> intervalScheduling(List<IntervalPointModels.Interval> intervals) {
        ArrayList<IntervalPointModels.Interval> A = new ArrayList<>(intervals);
        A.sort(comparingInt(iv -> iv.finish));
        ArrayList<IntervalPointModels.Interval> ans = new ArrayList<>();
        int lastFinish = Integer.MIN_VALUE;
        for (IntervalPointModels.Interval iv : A) {
            if (iv.start >= lastFinish) {
                ans.add(iv);
                lastFinish = iv.finish;
            }
        }
        return ans;
    }
}
