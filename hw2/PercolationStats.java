package hw2;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    public int[] percolationStatsList;
    public Percolation percolation;
    public int numberOfExperiements;

    public PercolationStats(int N, int T) {
        // perform T independent experiments on an N-by-N grid
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Illegal Argument");
        }
        numberOfExperiements = T;
        percolationStatsList = new int[T];
        for(int i = 0; i < T; i++) {
            percolation = new Percolation(N);
            percolationStatsList[i] = percolate(percolation);
        }
    }

    private int[] generateRandomBlockSiteIndex(Percolation percolation) {
        //returns row and col of the random blocked site
        ArrayList<Integer> blockedSites = percolation.blockedSites;
        int randomSiteIndex_Row = StdRandom.uniform(percolation.gridSize);
        int randomSiteIndex_Col = StdRandom.uniform(percolation.gridSize);
        int randomSiteIndex = percolation.findIndex(randomSiteIndex_Row,randomSiteIndex_Col);
        if (blockedSites.contains(randomSiteIndex)){
            int[] randomSiteCoordinates = {randomSiteIndex_Row, randomSiteIndex_Col};
            return randomSiteCoordinates;
        } else {
            return generateRandomBlockSiteIndex(percolation);
        }
    }

    private int percolate(Percolation percolation){
        while(!percolation.percolates()){
            int[] randomSiteIndex = generateRandomBlockSiteIndex(percolation);
            percolation.open(randomSiteIndex[0], randomSiteIndex[1]);
        }
        return percolation.numberOfOpenSites();
    }

    public double mean() {
        // sample mean of percolation threshold
        return StdStats.mean(percolationStatsList);
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return StdStats.stddev(percolationStatsList);
    }

    public double confidenceLow() {
        // low  endpoint of 95% confidence interval
        double average = this.mean();
        double stddev = Math.sqrt(this.stddev());
        return average - ((1.96*stddev)/(Math.sqrt(numberOfExperiements)));
    }

    public double confidenceHigh() {
        // high endpoint of 95% confidence interval
        double average = this.mean();
        double stddev = Math.sqrt(this.stddev());
        return average + ((1.96*stddev)/(Math.sqrt(numberOfExperiements)));
    }

    public static void main(String[] args){
        PercolationStats newPercolateStats = new PercolationStats(10, 20);
        System.out.println(newPercolateStats.percolationStatsList.length);
        System.out.println(newPercolateStats.mean());
        System.out.println(newPercolateStats.stddev());
        System.out.println(newPercolateStats.confidenceHigh());
        System.out.println(newPercolateStats.confidenceLow());


    }

}                       
