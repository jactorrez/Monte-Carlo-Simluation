/**
 * Javier Torrez
 * 4/19/17
 * Simulating a system to measure percolation potential
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	private int[][] siteGrid;  				// 2D array of N-by-N sites
	private WeightedQuickUnionUF nodeGrid;  // WQUF data structure of N-by-N nodes			// 
	private int n;							// Factor used to determine size of grid
	private int openSites;					// Counter for number of open sites
	private int topNode = 0;				// Top node to  decrease amount of necessary checks
	private int bottomNode = (n * n) + 1;	// Bottom node to decrease amount of necessary checks

	public Percolation(int n){ 
		validateInput(n);
		siteGrid = new int[n][n];
		nodeGrid = new WeightedQuickUnionUF(n * n + 2);
		this.n = n; 
		
		/**
		 * Initializing all sites to be blocked
		 */
	    for (int i = 0; i < siteGrid.length; i++) {
            for (int j = 0; j < siteGrid[i].length; j++) {
                siteGrid[i][j] = 0;
            }
        }
	}
	
	/**
	 * Validates a given input passed by checking
	 * if it's less than zero
	 * @param input value to validate
	 */
	private void validateInput(int input){
		if(input <= 0){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Validates a given site coordinate by checking
	 * if it exists
	 * @param row
	 * @param col
	 * @return
	 */
	public int validateSite(int row, int col){
		if (row > n || row < 1 || col > n || col < 1){
			throw new IndexOutOfBoundsException();
		}
		return siteGrid[row-1][col-1];
	}
	
	/**
	 * Maps a given site coordinate (row, col) to a location
	 * on the grid of nodes 
	 * @param row
	 * @param col
	 * @return node corresponding to the location on the site
	 * coordinate grid
	 */
	private int siteToPoint(int row, int col){
		int node = ((row * n) - n) + col;
		return node;
	}
	
	
	/**
	 * Connects given node in current coordinate to surrounding 
	 * open sites if possible 
	 * @param row
	 * @param col
	 */
	private void connectToSurrounding(int row, int col){
		int node = siteToPoint(row, col);
		
		boolean isInTopRow = row == 1;
		boolean isInBottomRow = row == n;
		
		// Checks if open nodes surrounding the current node exist
		boolean hasOpenTop = row > 1 ? (isOpen(row - 1, col) ? true : false) : false;
		boolean hasOpenLeft = col > 1 ? (isOpen(row, col - 1) ? true : false) : false;
		boolean hasOpenRight = col < n ? (isOpen(row, col + 1) ? true : false) : false;
		boolean hasOpenBottom = row < n ? (isOpen(row + 1, col) ? true : false) : false;
		
		// Given results from variables above, connects node via union 
		// method to surrounding open sites
		if(hasOpenTop){
			nodeGrid.union(node, node - n);
			
		} else if(!hasOpenTop && isInTopRow){
			nodeGrid.union(node, topNode);
		}
		
		if(hasOpenLeft){
			nodeGrid.union(node, node - 1);
		}
		
		if(hasOpenRight){
			nodeGrid.union(node, node + 1);
		}
		
		if(hasOpenBottom){
			nodeGrid.union(node,node + n);
		} else if(!hasOpenBottom && isInBottomRow){
			nodeGrid.union(node, bottomNode);
		}
	}
	
	/**	
	 * Opens a given site
	 * @param row row of node to open
	 * @param col column node to open
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
	 * @param row row of node to check
	 * @param col column of node to check
	 * @return
	 */
	public boolean isOpen(int row, int col){
		validateSite(row, col);
		return siteGrid[row-1][col-1] == 1;
	}
	
	/**
	 * Checks is a given site on the grid is full
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isFull(int row, int col){
		validateSite(row, col);
		int node = siteToPoint(row, col);
		boolean nodeIsFull = nodeGrid.connected(node, 0);
		
		return nodeIsFull;
	}
	
	/**
	 * Checks how many open sites exist on the current grid
	 * @return amount of open sites
	 */
	public int numberOfOpenSites(){
		return openSites;
	}
	
	/**
	 * Checks if the grid percolations
	 * @return
	 */
	
	public boolean percolates(){
		return nodeGrid.connected(bottomNode, topNode);
	}
	
	public static void main(String[] args){
		
		Percolation test = new Percolation(3);
		test.open(1, 3);
		test.open(2, 3);

        System.out.println(test.isFull(3, 1));
	}
}
