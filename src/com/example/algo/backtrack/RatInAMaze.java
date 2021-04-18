package com.example.algo.backtrack;

/**
 * Find the number of unique paths a rat can complete the NxN maze
 * Maze block value 1 is path is clear and 0 is path is blocked
 * Rat can only move forward or downwards.
 */
public class RatInAMaze {

	public static void main(String[] args) {
		int[][] ratMaze = new int[][] {{1, 1, 1}, 
									   {1, 1, 0},
									   {1, 1, 1}};
		System.out.println(uniquePath(ratMaze, 0, 0));
		
		
		ratMaze = new int[][]{{1, 0, 0, 0}, 
						      {1, 1, 0, 1},
						      {0, 1, 0, 0},
						      {1, 1, 1, 0}};
		System.out.println(uniquePath(ratMaze, 0, 0));
	}
	
	public static int uniquePath(int[][] ratMaze, int m, int n) {
		int R = ratMaze.length;
		int C = ratMaze[0].length;
		
		if(m >= R || n >=R || ratMaze[m][n] == 0) {
			return 0;
		}
		if(m == R-1 && n == C-1) {
			return 1;
		}
		
		return uniquePath(ratMaze, m+1, n) + uniquePath(ratMaze, m, n+1);
	}
	
}
