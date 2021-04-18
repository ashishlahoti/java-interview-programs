package com.example.algo.backtrack;

/**
 * Find the number of islands in given MxN matrix
 * value 1 is island area and 0 is water area
 */
public class NumberOfIslands {

	public static void main(String[] args) {
		char[][] grid = new char[][] {{'1','1','1','1','0'},
		                              {'1','1','0','1','0'},
		                              {'1','1','0','0','0'},
		                              {'0','0','0','0','0'}};
		System.out.println(numIslands(grid));
		
		char[][] grid1 = new char[][] {{'1','1','0','0','0'},
									   {'1','1','0','0','0'},		                             
									   {'0','0','1','0','0'},
									   {'0','0','0','1','1'}};
		System.out.println(numIslands(grid1));
	}

	public static int numIslands(char[][] grid) {
		int m = grid.length;
		int n = grid[0].length;
		int count = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if(grid[i][j] == '1') {
					checkIsland(grid, i, j, m, n);
					count++;
				}
			}
		}
		
		return count;
	}
	
	public static void checkIsland(char[][] grid, int i, int j, int m, int n) {
		if(i<0 || j<0 || i>=m || j>=n || grid[i][j] == '0') return;
		grid[i][j] = '0';
		checkIsland(grid, i-1, j, m, n);
		checkIsland(grid, i, j-1, m, n);
		checkIsland(grid, i+1, j, m, n);
		checkIsland(grid, i, j+1, m, n);
	}

}
