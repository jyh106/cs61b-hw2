package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Percolation {
    public ArrayList<Integer> blockedSites = new ArrayList<>();
    public ArrayList<Object> openedSites = new ArrayList<>();
    public ArrayList<Object> fullSites = new ArrayList<>();

    public WeightedQuickUnionUF Grid;

    public int gridSize;
    private int totalNumberOfSites;
    private int virtualTopSite;
    private int virtualBottomSite;

    public Set<Integer> leftSideIndexes = new HashSet<>();
    public Set<Integer> rightSideIndexes = new HashSet<>();

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Illegal Argument");
        }
        totalNumberOfSites = N * N;
        gridSize = N;
        int totalNumberOfSitesAndTwoVirtualSites = totalNumberOfSites + 2;

        Grid = new WeightedQuickUnionUF(totalNumberOfSitesAndTwoVirtualSites);

        //assign the virtual top site for purpose of checking full & percolation == N*N
        virtualTopSite = N * N;

        //assign virtual bottom site
        virtualBottomSite = N * N + 1;

        //all sites are initially blocked
        for (int i = 0; i < totalNumberOfSites; i++) {
            blockedSites.add(Grid.find(i));
        }

        //finding the left and right side Indexes -- for finding the right neighbor purposes
        for(int i = 0; i < gridSize; i++) {
            int leftSideIndex = gridSize * i;
            int rightSideIndex = gridSize * (i + 1) - 1;
            leftSideIndexes.add(leftSideIndex);
            rightSideIndexes.add(rightSideIndex);
        }
    }

    public int findIndex(int row, int col) {
        if (row == 0) {
            return col;
        } else {
            return gridSize * row + col;
        }
    }

    private ArrayList<Integer> filterNeighborIndexes(int[] unfilteredNeighborIndexes) {
        //findNeighbor helper function: filters out invalid neighbors, e.g. negative numbers, numbers larger than totalSites
        ArrayList<Integer>neighborIndexes = new ArrayList<>();
        for (int index : unfilteredNeighborIndexes) {
            if (index > -1 && index < totalNumberOfSites) {
                neighborIndexes.add(index);
            }
        }

        return neighborIndexes;
    }

    private ArrayList<Integer> findNeighbors_LeftSide(int currentSiteIndex) {
        int rightNeighborIndex = currentSiteIndex + 1;
        int upperNeighborIndex = currentSiteIndex - gridSize;
        int bottomNeighborIndex = currentSiteIndex + gridSize;

        int[] unfilteredNeighborIndexes = {rightNeighborIndex, upperNeighborIndex, bottomNeighborIndex};

        return filterNeighborIndexes(unfilteredNeighborIndexes);
    }

    private ArrayList<Integer> findNeighbors_RightSide(int currentSiteIndex){
        int leftNeighborIndex = currentSiteIndex - 1;
        int upperNeighborIndex = currentSiteIndex - gridSize;
        int bottomNeighborIndex = currentSiteIndex + gridSize;

        int[] unfilteredNeighborIndexes = {leftNeighborIndex, upperNeighborIndex, bottomNeighborIndex};

        return filterNeighborIndexes(unfilteredNeighborIndexes);

    }

    public ArrayList<Integer> findNeighbors(int currentSiteIndex){
        if(rightSideIndexes.contains(currentSiteIndex)) {
            return findNeighbors_RightSide(currentSiteIndex);
        } else if(leftSideIndexes.contains(currentSiteIndex)) {
            return findNeighbors_LeftSide(currentSiteIndex);
        } else {
            int leftNeighborIndex = currentSiteIndex - 1;
            int rightNeighborIndex = currentSiteIndex + 1;
            int upperNeighborIndex = currentSiteIndex - gridSize;
            int bottomNeighborIndex = currentSiteIndex + gridSize;

            int[] unfilteredNeighborIndexes = {leftNeighborIndex, rightNeighborIndex, upperNeighborIndex, bottomNeighborIndex};

            return filterNeighborIndexes(unfilteredNeighborIndexes);
        }
    }

    public boolean anyFullNeighbors(ArrayList<Integer> neighbors) {
        for(int index : neighbors) {
            if(isFullHelper(index)) {
                return true;
            }
        }
        return false;
    }


    public void percolating(int currentSiteIndex, ArrayList<Integer> neighbors){
        //percolates to all connected open sites
        for (int neighbor : neighbors) {
            if(Grid.connected(neighbor, currentSiteIndex) && !isFullHelper(currentSiteIndex)){
                fullSites.add(Grid.find(neighbor));
            }
        }
    }

    private boolean isBottomSite(int siteIndex){
        return ((siteIndex >= (totalNumberOfSites - gridSize)) && (siteIndex < totalNumberOfSites));
    }

    public void open(int row, int col) {
       // open the site (row, col) if it is not open already
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }
        int currentSiteIndex = findIndex(row, col);

        ArrayList<Integer> neighbors = findNeighbors(currentSiteIndex);

        if (!isOpen(row, col)) { //if it is not open already
            //connect currentSite to its opened neighbors
            for (int neighbor : neighbors) {
                if(isOpenHelper(neighbor)) {
                    Grid.union(Grid.find(currentSiteIndex), Grid.find(neighbor));
                }
            }
            //if it is the top row or has full neighbors, then it is immediately full and percolates
            if (anyFullNeighbors(neighbors) || currentSiteIndex < gridSize){
                if (currentSiteIndex < gridSize) {
                    Grid.union(virtualTopSite, Grid.find(currentSiteIndex));
                }
                fullSites.add(Grid.find(currentSiteIndex));
                percolating(currentSiteIndex, neighbors);
            }
            //if it is the bottom site, then connect o the virtual bottom site
            if(isBottomSite(currentSiteIndex)){
                Grid.union(virtualBottomSite, Grid.find(currentSiteIndex));
            }

            blockedSites.remove((Object)Grid.find(currentSiteIndex));
            openedSites.add(currentSiteIndex);
        }
    }

    public boolean isOpenHelper(int currentSiteIndex) {
        return openedSites.contains(currentSiteIndex);
    }

    public boolean isOpen(int row, int col) {
        // is the site (row, col) open?
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }
        return isOpenHelper(findIndex(row, col));
    }

    public boolean isFullHelper(int currentSiteIndex) {
        return Grid.connected(currentSiteIndex, virtualTopSite);
    }

    public boolean isFull(int row, int col) {
        // is the site (row, col) full?
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }
        return isFullHelper(findIndex(row, col));
    }

    public int numberOfOpenSites() {
        // number of open sites
        return openedSites.size();
    }

    public boolean percolates() {
        // does the system percolate?
        return Grid.connected(virtualTopSite, virtualBottomSite);
    }

    public static void main(String[] args) {
        Percolation percolate = new Percolation(6);
        int index = percolate.findIndex(0,0);
        percolate.open(0,2);
        percolate.open(1,2);
        percolate.open(1,4);
        percolate.open(5,5);
        int blockedSize = percolate.blockedSites.size();
        int openedSize = percolate.openedSites.size();
        int fullSize = percolate.fullSites.size();
        System.out.print(percolate.Grid.connected(2, 11));

    }

}
