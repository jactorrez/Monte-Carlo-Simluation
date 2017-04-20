/*----------------------------------------------------------------
 *  Author:        Javier Torrez
 *  Written:       4/17/2017
 *  Last updated:  4/19/2017
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats [size] [trials]
 *  
 *  Calculates and logs the sample mean, standard deviation, and  
 *  confidence intervals of a given array containing p-thresholds
 *  based on performing T independent trials on a grid of size n-by-n
 *
 *  % java Percolation 100 200
 *  mean                    = 0.6669475
 *	stddev                  = 0.11775205263262094
 *  95% confidence interval = [0.666217665216461, 0.6676773347835391]
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.*;

public class PercolationStats {
	
	private int gridSize; 		// size of grid
	private int openSites; 		// value of amount of open sites
	private Percolation grid; 	// data structure for testing percolation
	private int trials;			// amount of independent trials to perform 
	private  double[] pValues;	// p-threshold values gathered throughout trials
	
	/**
	 * Class constructor that takes two value, n and trials 
	 * 
	 * @param n		  factor used to determine total size of grid (n x n)
	 * @param trials  number of independent trials to perform 
	 */
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
	
	/**
	 * Calculates the sample mean of all the p-threshold values
	 * in the pValues array
	 * 
	 * @return sample mean
	 */
	public double mean(){
		double mean = StdStats.mean(this.pValues);
		return mean;
	}
	
	/**
	 * Calculates standard deviation of all the p-threshold values
	 * 
	 * @return standard deviation
	 */
	
	public double stddev(){
		double stdDev = StdStats.stddev(this.pValues);
		return stdDev;
	}
	
	/**
	 * Calculates lower bound of confidence interval 
	 * 
	 * @return lower bound of confidence interval
	 */
	
	public double confidenceLo(){
		double calcLo = this.mean() - (1.96 * this.stddev())/Math.sqrt(this.trials);
		return calcLo;
	}
	
	/**
	 * Calculates upper bound of confidence interval
	 * 
	 * @return upper bound based on confidence interval
	 */
	
	public double confidenceHi(){
		double calcHi = this.mean() + (1.96 * this.stddev())/Math.sqrt(this.trials);
		return calcHi;
	}
	
	/**
	 * Performs T trials on grid of size n-by-n given 
	 * inputs from the command line, T and n
	 * 
	 * @param args arguments passed in via command line
	 */
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
