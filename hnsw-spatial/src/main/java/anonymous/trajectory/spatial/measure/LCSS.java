package anonymous.trajectory.spatial.measure;

import anonymous.trajectory.spatial.entity.TraPoint;
import anonymous.trajectory.spatial.entity.Trajectory;
import com.github.jelmerk.knn.DistanceFunction;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author anonymous
 */
public class LCSS implements DistanceFunction<LinkedList<TraPoint>, Double>, Measure {
    public PointDist pointDist;
    public double threshold;
    public int delta;



    public LCSS(PointDist pointDist, double threshold, int delta) {
        this.pointDist = pointDist;
        this.threshold = threshold;
        this.delta = delta;
    }

    private double pointsDist(LinkedList<TraPoint> ps1, LinkedList<TraPoint> ps2) {
        int m = ps1.size(), n = ps2.size();
        int[][] dp = new int[m + 1][n + 1];
        dp[0][0] = 0;
        for (int i = 1; i <= m; i++) {
            dp[i][0] = 0;
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = 0;
        }

        Iterator<TraPoint> it1 = ps1.iterator();
        for (int i = 1; i <= m; i++) {
            TraPoint p1 = it1.next();
            Iterator<TraPoint> it2 = ps2.iterator();
            for (int j = 1; j <= n; j++) {
                TraPoint p2 = it2.next();
//                System.out.println("i " + i + "j " + j + "dis " + pointDist.calc(p1, p2));
                boolean common1 = (pointDist.calc(p1, p2) <= threshold);
                boolean common2 = Math.abs(i - j) <= delta;
                if (common1 && common2) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        double slcss = 1.0 * dp[m][n];

        return m + n - 2 * slcss; //lcss
//        return 1 - slcss / (m + n - slcss); //normalized lcss
    }

    @Override
    public double traDist(Trajectory t1, Trajectory t2) {
        return pointsDist(t1.vector(), t2.vector());
    }



    @Override
    public Double distance(LinkedList<TraPoint> u, LinkedList<TraPoint> v) {
        return pointsDist(u, v);
    }
}
