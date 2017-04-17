import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	private int[][] siteGrid; 
	private WeightedQuickUnionUF nodeGrid;
	private boolean[][] fullSites;
	private int n;
	private int openSites;
	private int topNode = 0;
	private int bottomNode = (n * n) + 1;

	public Percolation(int n){ 
		validateInput(n);
		siteGrid = new int[n][n];
		fullSites = new boolean[n][n];
		nodeGrid = new WeightedQuickUnionUF(n * n + 2);
		this.n = n; 
		
	    for (int i = 0; i < siteGrid.length; i++) {
            for (int j = 0; j < siteGrid[i].length; j++) {
                siteGrid[i][j] = 0;
            }
        }
	}
	
	private void validateInput(int input){
		if(input <= 0){
			throw new IllegalArgumentException();
		}
	}
	
	public int validateSite(int row, int col){
		return siteGrid[row-1][col-1];
	}
	
	private int siteToPoint(int row, int col){
		int node = ((row * n) - n) + col;
		return node;
	}
	
	private void connectToSurrounding(int row, int col){
		int node = siteToPoint(row, col);
		
		boolean isInTopRow = row == 1;
		boolean isInBottomRow = row == n;
		
		boolean hasOpenTop = row > 1 ? (isOpen(row - 1, col) ? true : false) : false;
		boolean hasOpenLeft = col > 1 ? (isOpen(row, col - 1) ? true : false) : false;
		boolean hasOpenRight = col < n ? (isOpen(row, col + 1) ? true : false) : false;
		boolean hasOpenBottom = row < n ? (isOpen(row + 1, col) ? true : false) : false;
		

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
		
		if(isFull(row, col)){
			fullSites[row-1][col-1] = true;		
		} else{
			fullSites[row-1][col-1] = false;
		}
	}
	
	
	public void open(int row, int col){
		validateSite(row, col);
	
		if(!isOpen(row, col)){
			siteGrid[row-1][col-1] = 1;	
			openSites++;
			connectToSurrounding(row, col);
		}
	}
	
	public boolean isOpen(int row, int col){
		validateSite(row, col);
		return siteGrid[row-1][col-1] == 1;
	}
	
	public boolean isFull(int row, int col){
		validateSite(row, col);
		int node = siteToPoint(row, col);
		boolean nodeIsFull = nodeGrid.connected(node, 0);
		
		return nodeIsFull;
	}
	
	public int numberOfOpenSites(){
		return openSites;
	}
	
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
