package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Percolation {

    private class Grid {
        /* Grid Data Structure
            grid = {{0, 1, 2, 3, 4, 5,..N},{N+1, N+2.....}, {...}, {...}}
         */
        public int totalSites;
        ArrayList<Object> sites = new ArrayList<>();

        public Grid(int N) {
            totalSites = N * N;

            for (int i = 0; i < N; i++) {
                sites.add(new ArrayList<Integer>());
            }
            this.assignIndex(N);
        }

        public void assignIndex(int N) {
            int index = 0;
            for (Object row : this.sites) {
                if (index >= this.totalSites) {
                    System.out.print(index + " is larger than gridSize " + this.totalSites);
                }
                for (int i = 0; i < N; i++) {
                    ((ArrayList) row).add(index);
                    index++;
                }
            }
        }
    }

    public ArrayList<Integer> blockedSites = new ArrayList<>();
    public ArrayList<Integer> openedSites = new ArrayList<>();
    public ArrayList<Integer> fullSites = new ArrayList<>();
    public Grid newGrid;
    public int gridSize;
    public int totalNumberOfSites;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Illegal Argument");
        }
        newGrid = new Grid(N);
        gridSize = N;
        totalNumberOfSites = N * N;

        //all sites are initially blocked
        for (int i = 0; i < totalNumberOfSites; i++) {
            blockedSites.add(i);
        }
    }

    private int findIndex(int row, int col) {
        return (int)((ArrayList)this.newGrid.sites.get(row)).get(col);
    }

    public void open(int row, int col) {
       // open the site (row, col) if it is not open already
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }

        int siteIndex = findIndex(row, col);
        if (!openedSites.contains(siteIndex)) {
            if (siteIndex < gridSize || anyFullNeighbors(siteIndex)) { //top row
                fullSites.add(siteIndex);
            }
            blockedSites.remove(siteIndex);
            openedSites.add(siteIndex);
        }
    }

    public boolean isOpen(int row, int col) {
        // is the site (row, col) open?
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }
        int siteIndex = findIndex(row, col);
        return openedSites.contains(siteIndex);
    }


    public ArrayList<Integer> findNeighbors(int currentSiteIndex){
        int leftNeighborIndex = currentSiteIndex - 1;
        int rightNeighborIndex = currentSiteIndex + 1;
        int upperNeighborIndex = currentSiteIndex - gridSize;
        int bottomNeighborIndex = currentSiteIndex + gridSize;

        int[] unfilteredNeighborIndexes = {leftNeighborIndex, rightNeighborIndex, upperNeighborIndex, bottomNeighborIndex};

        ArrayList<Integer>neighborIndexes = new ArrayList<>();

        for (int index : unfilteredNeighborIndexes) {
            if (index > -1 && index < totalNumberOfSites) {
                neighborIndexes.add(index);
            }
        }

        return neighborIndexes;

    }

    public boolean anyFullNeighbors(int currentSiteIndex) {
        ArrayList<Integer> currentSiteNeighbors = findNeighbors(currentSiteIndex);
        for(int index : currentSiteNeighbors) {
            if(index < gridSize) {
                return true;
            } else if (fullSites.contains(index)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        // is the site (row, col) full?
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }
        int siteIndex = findIndex(row, col);

        return fullSites.contains(siteIndex);
    }

    public int numberOfOpenSites() {
        // number of open sites
        return openedSites.size();
    }

    public boolean percolates() {
        // does the system percolate?
        //TODO complete this method
        return false;
    }

    public static void main(String[] args) {
        Percolation percolate = new Percolation(6);
        int index = percolate.findIndex(5,5);
        ArrayList<Integer> neighbors = percolate.findNeighbors(index);
        percolate.open(0,0);
        percolate.open(1,0);
        int blockedSize = percolate.blockedSites.size();
        int openedSize = percolate.openedSites.size();
        int fullSize = percolate.fullSites.size();

    }

}
