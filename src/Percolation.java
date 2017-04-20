/*----------------------------------------------------------------
 *  Author:        Javier Torrez
 *  Written:       4/12/2017
 *  Last updated:  4/19/2017
 *
 *  Compilation:   javac-algs4 Percolation.java
 *  Execution:     java-algs4 Percolation
 *  
 *  Simulates system of sites capable of creating and testing
 *  the conditions for it to percolate 
 * 
 *----------------------------------------------------------------*/



import edu.princeton.cs.algs4.*;

public class Percolation {
	
	private int[][] siteGrid;  				// 2D array to simulate an N-by-N grid of sites
	private WeightedQuickUnionUF nodeGrid;  // quick-union data structure of N-by-N map of nodes		
	private WeightedQuickUnionUF backwashGrid; // grid used to avoid backwash
	private int n;							// factor used to determine size of grid
	private int openSites;					// counter to keep track of number of open sites
	private int topNode = 0;				// location of top node used to check percolation
	private int bottomNode;	// location of bottom used to check percolation
	
	/**
	 * Class constructor that takes one parameter, n
	 * @param n the factor used to determine the grid size (n x n)
	 */
	public Percolation(int n) { 
		validateInput(n);
		siteGrid = new int[n][n];
		bottomNode = (n * n) + 1;
		nodeGrid = new WeightedQuickUnionUF(n * n + 2);
		backwashGrid = new WeightedQuickUnionUF(n * n + 1);
		this.n = n; 
		
		 // initializing all sites on the grid to be blocked 
	    for (int i = 0; i < siteGrid.length; i++) {
            for (int j = 0; j < siteGrid[i].length; j++) {
                siteGrid[i][j] = 0;
            }
        }
	}
	
	/**
	 * Validates a single input value by checking
	 * if it's less than or equal to zero 
	 * 
	 * @param input value to validate
	 * @exception IllegalArgumentException If input is less than 
	 * 									   or equal to 0
	 */
	private void validateInput(int input){
		if(input <= 0){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Validates two inputs. Used for validation
	 * of locations on the grid by checking if they
	 * exist or are out of bounds
	 * 
	 * @param row Y-axis location of given site
	 * @param col X-axis location of given site
	 * @exception IndexOutOfBoundsException If point location is outside
	 * 										of the bounds of the grid
	 */
	private void validateSite(int row, int col){
		if (row > n || row < 1 || col > n || col < 1){
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * Determines the value of the node on the 1-dimensional QF grid based
	 * on coordinates given from the 2-dimensional grid of sites
	 * 
	 * @param row Y-axis location of given site
	 * @param col X-axis location of given site
	 * @return    the node mapped to the 1D grid based on the 
	 * 			  initial coordinate point
	 */
	private int siteToPoint(int row, int col){
		int node = ((row * n) - n) + col;
		return node;
	}
	
	/**
	 * Connects given site in corresponding to surrounding 
	 * open sites if possible
	 * 
	 * @param row Y-axis location of given site
	 * @param col X-axis location of given site
	 */
	private void connectToSurrounding(int row, int col){
		int node = siteToPoint(row, col);
		
		boolean isInTopRow = row == 1;
		boolean isInBottomRow = row == n;
		
		// checks if surrounding sites for the given point are open
		// to determine where connections via union can be made
		boolean hasOpenTop = row > 1 ? (isOpen(row - 1, col) ? true : false) : false;
		boolean hasOpenLeft = col > 1 ? (isOpen(row, col - 1) ? true : false) : false;
		boolean hasOpenRight = col < n ? (isOpen(row, col + 1) ? true : false) : false;
		boolean hasOpenBottom = row < n ? (isOpen(row + 1, col) ? true : false) : false;
		
		// given determined values from variables above, connects site
		// to surrounding open sites via union. If site is in the top or
		// bottom row, connects site to top-most or bottom-most node 
		
		if(hasOpenTop){
			nodeGrid.union(node, node - n);
			backwashGrid.union(node, node - n);
		} else if(isInTopRow){
			nodeGrid.union(node, topNode);
			backwashGrid.union(node, topNode);
		}
		
		if(hasOpenLeft){
			nodeGrid.union(node, node - 1);
			backwashGrid.union(node, node - 1);
		}
		
		if(hasOpenRight){
			nodeGrid.union(node, node + 1);
			backwashGrid.union(node, node + 1);
		}
		
		if(hasOpenBottom){
			nodeGrid.union(node,node + n);
			backwashGrid.union(node, node + n);
		} else if(isInBottomRow){
			nodeGrid.union(node, bottomNode);
		}
	}
	
	/**	
	 * Opens a given site
	 * 
	 * @param row Y-axis location of given site
	 * @param col X-axis location of given site
	 */
	public void open(int row, int col){
		validateSite(row, col);
	
		if(!isOpen(row, col)){
			siteGrid[row-1][col-1] = 1;	
			openSites++;
			connectToSurrounding(row, col);
		}
	}
	
	/**
	 * Checks if a given site is open
	 * 
	 * @param row Y-axis location of given site
	 * @param col X-axis location of given site
	 * @return
	 */
	public boolean isOpen(int row, int col){
		validateSite(row, col);
		return siteGrid[row-1][col-1] == 1;
	}
	
	/**
	 * Checks if a given site on the grid is full
	 * 
	 * @param row vertical location of given site location
	 * @param col horizontal location of given site location
	 * @return
	 */
	public boolean isFull(int row, int col){
		validateSite(row, col);
		int node = siteToPoint(row, col);
		boolean nodeIsFull = backwashGrid.connected(node, topNode);
		
		return nodeIsFull;
	}
	
	/**
	 * Checks how many open sites exist on the current grid
	 * 
	 * @return amount of open sites
	 */
	public int numberOfOpenSites(){
		return openSites;
	}
	
	/**
	 * Checks if the grid percolates
	 * 
	 * @return true or false, determined by seeing if top-most and
	 * 		   bottom-most sites on grid are connected 
	 */
	public boolean percolates(){
		return nodeGrid.connected(bottomNode, topNode);
	}
}
