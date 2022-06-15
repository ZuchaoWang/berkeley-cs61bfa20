package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private int gridSize;
  private int openCount;
  private int topId;
  private int bottomId;
  private boolean[] openMatrix;
  private WeightedQuickUnionUF uf1; // only top row connected to pseudo cell
  private WeightedQuickUnionUF uf2; // both top/bottom rows connected to pseudo cell

  public Percolation(int N) {
    // create N-by-N grid, with all sites initially blocked
    if (N <= 0)
      throw new java.lang.IllegalArgumentException("N");

    gridSize = N;
    openCount = 0;
    topId = gridSize * gridSize;
    bottomId = gridSize * gridSize + 1;
    openMatrix = new boolean[gridSize * gridSize];
    for (int i = 0; i < gridSize * gridSize; i++)
      openMatrix[i] = false;
    uf1 = new WeightedQuickUnionUF(gridSize * gridSize + 1); // does not include pseudo-bottom cell
    uf2 = new WeightedQuickUnionUF(gridSize * gridSize + 2);
  }

  public void open(int row, int col) {
    // open the site (row, col) if it is not open already
    int id = rowColToId(row, col);
    if (!openMatrix[id]) {
      openMatrix[id] = true;
      openCount++;
      // connect to top
      if (row == 0) {
        uf1.union(id, topId);
        uf2.union(id, topId);
      } else {
        int aboveId = rowColToId(row-1, col);
        if (openMatrix[aboveId]) {
          uf1.union(id, aboveId);
          uf2.union(id, aboveId);
        }
      }
      // connect to bottom
      if (row == gridSize-1) {
        // no union in uf1
        // this is the only difference between uf1 and uf2
        uf2.union(id, bottomId);
      } else {
        int belowId = rowColToId(row+1, col);
        if (openMatrix[belowId]) {
          uf1.union(id, belowId);
          uf2.union(id, belowId);
        }
      }
      // connect to left
      if (col != 0) {
        int leftId = rowColToId(row, col-1);
        if (openMatrix[leftId]) {
          uf1.union(id, leftId);
          uf2.union(id, leftId);
        }
      }
      // connect to right
      if (col != gridSize-1) {
        int rightId = rowColToId(row, col+1);
        if (openMatrix[rightId]) {
          uf1.union(id, rightId);
          uf2.union(id, rightId);
        }
      }
    }
  }

  public boolean isOpen(int row, int col) {
    // is the site (row, col) open?
    int id = rowColToId(row, col);
    return openMatrix[id];
  }  

  public boolean isFull(int row, int col) {
    // is the site (row, col) full?
    int id = rowColToId(row, col);
    return uf1.connected(topId, id);
  } 

  public int numberOfOpenSites() {
    return openCount;
  }          

  public boolean percolates() {
    // does the system percolate?
    return uf2.connected(topId, bottomId);
  }             
  // public static void main(String[] args)   // use for unit testing (not required, but keep this here for the autograder)

  private int rowColToId(int row, int col) {
    if (row < 0 || row >= gridSize) {
      throw new java.lang.IndexOutOfBoundsException("row");
    }
    if (col < 0 || col >= gridSize) {
      throw new java.lang.IndexOutOfBoundsException("column");
    }
    return row * gridSize + col;
  }
}
