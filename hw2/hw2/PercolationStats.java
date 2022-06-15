package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
  private double mean;
  private double stddev;
  private double confidenceLow;
  private double confidenceHigh;

  public PercolationStats(int N, int T, PercolationFactory pf) {
    // perform T independent experiments on an N-by-N grid
    // input checking
    if (N <= 0) throw new java.lang.IllegalArgumentException("N");
    if (T <= 0) throw new java.lang.IllegalArgumentException("T");

    // experiment
    double[] estimates = new double[T];
    for (int i=0; i<T; i++) {
      Percolation p = pf.make(N);
      while(!p.percolates()) {
        int row = StdRandom.uniform(N);
        int col = StdRandom.uniform(N);
        p.open(row, col);
      }
      estimates[i] = (double)p.numberOfOpenSites() / N / N;
    }

    // stat
    mean = StdStats.mean(estimates);
    if (T > 1) {
      stddev = StdStats.stddev(estimates);
      confidenceLow = mean - 1.96 * stddev / Math.sqrt((double)T);
      confidenceHigh = mean + 1.96 * stddev / Math.sqrt((double)T);
    } else {
      stddev = Double.NaN;
      confidenceLow = Double.NaN;
      confidenceHigh = Double.NaN;
    }
  }

  public double mean() {
    // sample mean of percolation threshold
    return mean;
  }

  public double stddev() {
    // sample standard deviation of percolation threshold
    return stddev;
  }

  public double confidenceLow() {
    // low endpoint of 95% confidence interval
    return confidenceLow;
  }

  public double confidenceHigh() {
    // high endpoint of 95% confidence interval
    return confidenceHigh;
  }

  public static void main(String[] args) {
    int N = 100;
    int T = 100;
    PercolationFactory pf = new PercolationFactory();
    PercolationStats ps = new PercolationStats(N, T, pf);
    System.out.println("N = " + N);
    System.out.println("T = " + T);
    System.out.println("mean = " + ps.mean);
    System.out.println("stddev = " + ps.stddev);
    System.out.println("confidenceLow = " + ps.confidenceLow);
    System.out.println("confidenceHigh = " + ps.confidenceHigh);
  }
}
