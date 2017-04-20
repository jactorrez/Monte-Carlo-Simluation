import edu.princeton.cs.algs4.*;

public class PercolationStats {
	
	private int gridSize;
	public double[] pValues;
	private int openSites; 
	private Percolation grid;
	private int trials;
	
	public PercolationStats(int n, int trials){
		
		if(n <= 0 || trials <= 0)
			throw new IllegalArgumentException();
		
		gridSize = n * n;
		pValues = new double[n];
		this.trials = trials;
			
		for(int i = 0; i < trials; i++){
			
			grid = new Percolation(n);
			openSites = 0; 
			
			while(!grid.percolates()){
				int randRow = StdRandom.uniform(1, n);
				int randCol = StdRandom.uniform(1, n);
				
				while(grid.isOpen(randRow, randCol)){
					randRow = StdRandom.uniform(1, n);
					randCol = StdRandom.uniform(1, n);
				} 
				
				grid.open(randRow, randCol);
			}
			openSites = grid.numberOfOpenSites();
			pValues[i] = (double) openSites/gridSize;
		}
	}
	
	public double mean(){
		double mean = StdStats.mean(this.pValues);
		return mean;
	}
	
	public double stddev(){
		double stdDev = StdStats.stddev(this.pValues);
		return stdDev;
	}
	
	public double confidenceLo(){
		double calcLo = this.mean() - (1.96 * this.stddev())/Math.sqrt(this.trials);
		return calcLo;
	}
	
	public double confidenceHi(){
		double calcHi = this.mean() + (1.96 * this.stddev())/Math.sqrt(this.trials);
		return calcHi;
	}
	
	public static void main(String[] args){
		int size = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		
		PercolationStats stats = new PercolationStats(size, trials);
		System.out.println("mean = " + stats.mean());
		System.out.println("stddev = " + stats.stddev());
		System.out.println("95% confidence interval = [" + stats.confidenceLo()
														 + ", " + stats.confidenceHi() + "]");
	}
}
