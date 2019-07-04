package com.help4j.algorithm.complex;

/* Java program to solve Rat in a Maze problem using
backtracking */

public class RatMaze2
{
	public void solveMaze(int[][] maze){
		int[][] sol = new int[][]{{0, 0 ,0 ,0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}};
		print(maze);
		solveMazeUtil(0, 0, sol, maze);
		print(sol);
	}

	public boolean solveMazeUtil(int x, int y, int[][] sol, int[][] maze){
		int N = maze.length;
		if(x == N-1 && y == N-1){
			sol[x][y] = 1;
			return true;
		}
		
		if(isSafe(x, y, maze)){
			sol[x][y] = 1;
			
			if(solveMazeUtil(x+1, y, sol, maze)){
				return true;
			}
			
			if(solveMazeUtil(x, y+1, sol, maze)){
				return true;
			}
			sol[x][y] = 0;
			return false;
		}
		return false;
	}
	
	boolean isSafe(int x, int y, int maze[][]){
		int N = maze.length;
		return x >= 0 && x < N && y>=0 && y < N && maze[x][y] ==1;
	}
	public void print(int[][] matrix){
		for (int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				System.out.print(" " + matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	public static void main(String args[]){
		RatMaze2 rat = new RatMaze2();
		int maze[][] = {{1, 0, 0, 0},
			{1, 1, 0, 1},
			{0, 1, 0, 0},
			{1, 1, 1, 1}
		};
		rat.solveMaze(maze);
	}
}
// This code is contributed by Abhishek Shankhadhar
