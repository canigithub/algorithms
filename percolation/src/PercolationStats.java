import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by gerald on 5/25/16.
 */
public class PercolationStats {

    private double[] experiment;

    public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
    {

        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException("perco stats ctor N or T <= 0");
        }

        experiment = new double[T];

        for (int k = 0; k < T; ++k) {

            Percolation perco = new Percolation(N);
            int count = 0;

            while (!perco.percolates()) {

                int i = StdRandom.uniform(N) + 1;
                int j = StdRandom.uniform(N) + 1;

                if (!perco.isOpen(i, j)) {
                    ++count;
                    perco.open(i, j);
                }
            }

            experiment[k] = (double) count/N/N;
        }
    }


    public double mean()                      // sample mean of percolation threshold
    {
        double tot = 0.;

        for (int i = 0; i < experiment.length; ++i) {
            tot += experiment[i];
        }

        return tot/experiment.length;
    }


    public double stddev()                    // sample standard deviation of percolation threshold
    {
        double tot = 0., mean = this.mean();

        for (int i = 0; i < experiment.length; ++i) {
            tot += (experiment[i]-mean)*(experiment[i]-mean);
        }

        return Math.sqrt(tot/(experiment.length-1));
    }


    public double confidenceLo()              // low  endpoint of 95% confidence interval
    {
        double sqrt_t = Math.sqrt(experiment.length);
        return this.mean() - 1.96*this.stddev()/sqrt_t;
    }


    public double confidenceHi()              // high endpoint of 95% confidence interval
    {
        double sqrt_t = Math.sqrt(experiment.length);
        return this.mean() + 1.96*this.stddev()/sqrt_t;
    }


    public static void main(String[] args)    // test client (described below)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N,T);
        StdOut.println("mean = " + ps.mean());
        StdOut.println("stddev = " + ps.stddev());
        StdOut.println("95% confi interval = [" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
    }
}
