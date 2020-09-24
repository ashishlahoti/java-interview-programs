package com.example.dp;

public class UniquePaths {

	public static void main(String[] args) {
		/*
		System.out.println(uniquePaths(0, 0));
		System.out.println(uniquePaths(0, 4));
		System.out.println(uniquePaths(1, 1));
		System.out.println(uniquePaths(2, 3));
		System.out.println(uniquePaths(3, 2));
		System.out.println(uniquePaths(3, 3));
		System.out.println(uniquePaths(51, 9));
		*/
		//System.out.println(uniquePaths(3, 3));
		//System.out.println(uniquePaths1(3, 3));
		//System.out.println(uniquePaths2(3, 3));
		
		int[][] obstacleGrid = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
		System.out.println(uniquePathsWithObstacles(obstacleGrid));
		int[][] obstacleGrid1 = {{0, 1}, {1, 0}};
		System.out.println(uniquePathsWithObstacles(obstacleGrid1));
		int[][] obstacleGrid2 = {{0, 1}, {1, 1}, {0, 0}};
		System.out.println(uniquePathsWithObstacles(obstacleGrid2));
	}

	public static final int uniquePaths(int m, int n) {
		if (m <= 0 || n <= 0) {
			return 0;
		}
		int[][] memo = new int[m + 1][n + 1];
		return uniquePaths(0, 0, m, n, memo);
	}

	public static final int uniquePaths(int i, int j, int m, int n, int[][] memo) {
		if (memo[i][j] > 0) {
			return memo[i][j];
		}

		if (i == m - 1 || j == n - 1) {
			return 1;
		}
		memo[i][j] = uniquePaths(i, j + 1, m, n, memo) + uniquePaths(i + 1, j, m, n, memo);
		return memo[i][j];
	}

	public static int uniquePaths1(int m, int n) {
		int[][] dp = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				dp[i][j] = Math.max((dp[i - 1][j] + dp[i][j - 1]), 1);
			}
		}
		return dp[m][n];
	}
	
	public static int uniquePaths2(int m, int n) {
		int[][] dp = new int[m][n];
		if (m <= 0 || n <= 0) {
			return 0;
		}
		if(m == 1 || n == 1) {
			return 1;
		}
		dp[0][0] = 0;
		for(int i =1; i<m; i++) {
			dp[i][0] =1;
		}
		for(int j =1; j<n; j++) {
			dp[0][j] =1;
		}
		
		
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
			}
		}
		return dp[m-1][n-1];
	}
	
	public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        
		if(obstacleGrid[0][0] == 1) {
			return 0;
		}
		
		int R = obstacleGrid.length;
		int C = obstacleGrid[0].length;
		
		System.out.println("before");
		for(int i=0; i<R; i++) {
			for(int j=0; j<C; j++) {
				System.out.print(obstacleGrid[i][j] + " ");
			}
			System.out.println();
		}
		
		obstacleGrid[0][0] = 1;
		for(int i =1; i<R; i++) {
			obstacleGrid[i][0] = (obstacleGrid[i][0] == 1 || obstacleGrid[i-1][0] == 0) ? 0 : 1;
		}
		for(int j =1; j<C; j++) {
			obstacleGrid[0][j] = (obstacleGrid[0][j] == 1 || obstacleGrid[0][j-1] == 0)? 0 : 1;
		}
		
		for(int i=1; i<R; i++) {
			for(int j=1; j<C; j++) {
				obstacleGrid[i][j] = obstacleGrid[i][j] == 1 ? 0 : (obstacleGrid[i-1][j] + obstacleGrid[i][j-1]);
			}
		}
		
		System.out.println("after");
		for(int i=0; i<R; i++) {
			for(int j=0; j<C; j++) {
				System.out.print(obstacleGrid[i][j] + " ");
			}
			System.out.println();
		}
		
		return obstacleGrid[R-1][C-1];
    } 
}
