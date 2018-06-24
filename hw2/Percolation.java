package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Percolation {
    public Set<Integer> blockedSites = new HashSet<>();
    public Set<Integer> openedSites = new HashSet<>();
    public Set<Integer> fullSites = new HashSet<>();

    public WeightedQuickUnionUF newGrid;
    public int gridSize;
    public int totalNumberOfSites;

    public Set<Integer> leftSideIndexes = new HashSet<>();
    public Set<Integer> rightSideIndexes = new HashSet<>();

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Illegal Argument");
        }
        totalNumberOfSites = N * N;
        gridSize = N;
        newGrid = new WeightedQuickUnionUF(totalNumberOfSites);

        //all sites are initially blocked
        for (int i = 0; i < totalNumberOfSites; i++) {
            blockedSites.add(i);
        }

        //finding the left and right side Indexes -- for finding the right neighbor purposes
        for(int i = 0; i < gridSize; i++) {
            int leftSideIndex = gridSize * i;
            int rightSideIndex = gridSize * (i + 1) - 1;
            leftSideIndexes.add(leftSideIndex);
            rightSideIndexes.add(rightSideIndex);
        }
    }

    private int findIndex(int row, int col) {
        if (row == 0) {
            return col;
        } else {
            return gridSize * row + col;
        }
    }

    public ArrayList<Integer> filterNeighborIndexes(int[] unfilteredNeighborIndexes) {
        //filters out invalid neighbors, e.g. negative numbers, numbers larger than totalSites
        ArrayList<Integer>neighborIndexes = new ArrayList<>();
        for (int index : unfilteredNeighborIndexes) {
            if (index > -1 && index < totalNumberOfSites) {
                neighborIndexes.add(index);
            }
        }

        return neighborIndexes;
    }

    public ArrayList<Integer> findNeighbors_LeftSide(int currentSiteIndex) {
        int rightNeighborIndex = currentSiteIndex + 1;
        int upperNeighborIndex = currentSiteIndex - gridSize;
        int bottomNeighborIndex = currentSiteIndex + gridSize;

        int[] unfilteredNeighborIndexes = {rightNeighborIndex, upperNeighborIndex, bottomNeighborIndex};

        return filterNeighborIndexes(unfilteredNeighborIndexes);
    }

    public ArrayList<Integer> findNeighbors_RightSide(int currentSiteIndex){
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

    public boolean anyFullNeighbors(int currentSiteIndex) {
        ArrayList<Integer> currentSiteNeighbors = findNeighbors(currentSiteIndex);
        for(int index : currentSiteNeighbors) {
            if(index < gridSize && openedSites.contains(index) || fullSites.contains(index)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> openedNeighbors(int currentSiteIndex) {
        ArrayList<Integer> currentSiteNeighbors = findNeighbors(currentSiteIndex);

        ArrayList<Integer> openedNeighbors = new ArrayList<>();

        for(int index : currentSiteNeighbors) {
            if(isOpenHelper(index)) {
                openedNeighbors.add(index);
            }
        }
        return openedNeighbors;
    }

    public boolean openedNeighborsAllFull(ArrayList<Integer> openedNeighborIndexes){
        for (int openNeighbor : openedNeighborIndexes) {
            if(!fullSites.contains(openNeighbor)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> openedNotFullNeighborIndexes(ArrayList<Integer> openedNeighborIndexes) {
        ArrayList<Integer> openedNotFullNeighborIndexes = new ArrayList<>();
        for (int openNeighbor : openedNeighborIndexes) {
            if(!fullSites.contains(openNeighbor)) {
                openedNotFullNeighborIndexes.add(openNeighbor);
            }
        }
        return openedNotFullNeighborIndexes;
    }

    public void percolating(int currentSiteIndex){
        //percolates to all connected open sites
        ArrayList<Integer> openedNotFullNeighborIndexes = openedNotFullNeighborIndexes(openedNeighbors(currentSiteIndex));

        if (openedNotFullNeighborIndexes == null) {
            return;
        }
        if (!openedNeighborsAllFull(openedNotFullNeighborIndexes)) {
            for(int openedNeighbor : openedNotFullNeighborIndexes) {
                fullSites.add(openedNeighbor);
                percolating(openedNeighbor);
            }
        }
    }

    public void open(int row, int col) {
       // open the site (row, col) if it is not open already
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }

        int currentSiteIndex = findIndex(row, col);
        if (!openedSites.contains(currentSiteIndex)) {
            if (currentSiteIndex < gridSize || anyFullNeighbors(currentSiteIndex)) { //top row
                fullSites.add(currentSiteIndex);
                percolating(currentSiteIndex);
            }
            blockedSites.remove(currentSiteIndex);
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
        int currentSiteIndex = findIndex(row, col);
        return isOpenHelper(currentSiteIndex);
    }


    public boolean isFull(int row, int col) {
        // is the site (row, col) full?
        if (row < 0 || row > gridSize-1 || col < 0 || col > gridSize-1){
            throw new java.lang.IndexOutOfBoundsException("Index out of Bound");
        }
        int currentSiteIndex = findIndex(row, col);

        return fullSites.contains(currentSiteIndex);
    }

    public int numberOfOpenSites() {
        // number of open sites
        return openedSites.size();
    }

    public boolean percolates() {
        // does the system percolate?
        Set<Integer> lastRowIndexes = new HashSet<>();
        for (int i = gridSize*(gridSize-1); i < totalNumberOfSites; i++) {
            lastRowIndexes.add(i);
        }
        for(int index : lastRowIndexes) {
            if(fullSites.contains(index)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Percolation percolate = new Percolation(6);
        int index = percolate.findIndex(0,0);
        ArrayList<Integer> neighbors = percolate.findNeighbors(index);
        percolate.open(0,2);
        percolate.open(1,3);
        percolate.open(1,4);
        percolate.open(1,5);
        int blockedSize = percolate.blockedSites.size();
        int openedSize = percolate.openedSites.size();
        int fullSize = percolate.fullSites.size();

    }

}
