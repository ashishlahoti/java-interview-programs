package com.example.algo.backtrack;

import java.util.Arrays;

public class SolveSudoku {

	public static void main(String[] args) {
		int[][] board = { 
				{ 0, 1, 2, 7, 5, 3, 6, 4, 9 }, 
				{ 9, 4, 0, 6, 8, 2, 1, 7, 5 }, 
				{ 6, 7, 5, 4, 9, 1, 2, 8, 3 },
				{ 1, 5, 4, 2, 3, 7, 8, 9, 6 }, 
				{ 3, 6, 9, 8, 4, 5, 7, 2, 1 }, 
				{ 2, 8, 7, 1, 6, 9, 5, 3, 4 },
				{ 5, 2, 1, 9, 7, 4, 3, 6, 8 }, 
				{ 4, 3, 8, 5, 2, 6, 9, 1, 7 }, 
				{ 7, 9, 6, 3, 1, 8, 4, 5, 0 }};

		solveSudoku(board, 0, 0);
		for(int[] row: board) {
			System.out.println(Arrays.toString(row));
		}

	}

	public static boolean solveSudoku(int[][] board, int row, int col) {
		if (row == board.length - 1 && col == board.length) {
			return true;
		}
		if (col == 9) {
			row++;
			col = 0;
		}
		if (board[row][col] == 0) {
			for (int k = 1; k <= 9; k++) {
				if (isValidSudoku(board, row, col, k)) {
					board[row][col] = k;
					if (solveSudoku(board, row, col + 1)) {
						return true;
					}
				}
				board[row][col] = 0; // reverse the backtrack
			}
			return false; // board[row][col] is not valid for all values 1 to 9, return false;
		} else {
			return solveSudoku(board, row, col + 1);
		}
	}

	public static boolean isValidSudoku(int[][] board, int row, int col, int num) {
		for (int i = 0; i < board.length; i++) {
			//row check
			if (board[row][i] != 0 && board[row][i] == num) {
				return false;
			}
			//col check
			if (board[i][col] != 0 && board[i][col] == num) {
				return false;
			}
		}
		// 3x3 block check
		row = (row/3)*3;
		col = (col/3)*3;
		for(int i=row; i<row+3; i++) {
			for(int j=col; j<col+3; j++) {
				if (board[row][i] != 0 && board[row][i] == num) {
					return false;
				}
			}
		}
		return true;
	}
}
