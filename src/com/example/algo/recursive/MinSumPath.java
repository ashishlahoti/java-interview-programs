package com.example.algo.recursive;

public class MinSumPath {

    public static void main(String[] args) {
        System.out.println(minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}));
    }

    public static int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] memo = new int[m][n];
        return minPathSum(grid, 0, 0, memo);
    }

    public static int minPathSum(int[][] grid, int row, int column, int[][] memo) {
        int m = grid.length;
        int n = grid[0].length;
        if (row == m - 1 && column == n - 1) {
            return grid[row][column];
        } else if (row >= m || column >= n) {
            return Integer.MAX_VALUE;
        }

        if (memo[row][column] > 0) {
            return memo[row][column];
        }

        memo[row][column] = grid[row][column] + Math.min(minPathSum(grid, row + 1, column, memo), minPathSum(grid, row, column + 1, memo));
        return memo[row][column];
    }
}
