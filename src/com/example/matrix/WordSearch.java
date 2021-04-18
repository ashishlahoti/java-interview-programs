package com.example.matrix;

/**
 * Given an m x n board and a word, find if the word exists in the grid. 
 * search all 4 directions and from any position
 */
public class WordSearch {

	public static void main(String[] args) {
		System.out.println(exist(
				new char[][] {{ 'A', 'B', 'C', 'E' }, 
							  { 'S', 'F', 'C', 'S' },
							  { 'A', 'D', 'E', 'E' }}, "ABCCED"));
		System.out.println(exist(
				new char[][] {{ 'A', 'B', 'C', 'E' },
							  { 'S', 'F', 'C', 'S' },
							  { 'A', 'D', 'E', 'E' } }, "ABCB"));
	}

	public static boolean exist(char[][] board, String word) {
		int m = board.length;
		int n = board[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (exist(board, i, j, word, 0))
					return true;
			}
		}
		return false;
	}

	public static boolean exist(char[][] board, int m, int n, String word, int index) {
		if (index == word.length())
			return true;
		if (m < 0 || n < 0 || m >= board.length || n >= board[0].length || board[m][n] != word.charAt(index)
				|| board[m][n] == '#')
			return false;
		board[m][n] = '#';
		boolean isExist = exist(board, m + 1, n, word, index + 1) || exist(board, m, n + 1, word, index + 1)
				|| exist(board, m - 1, n, word, index + 1) || exist(board, m, n - 1, word, index + 1);
		board[m][n] = word.charAt(index);
		return isExist;
	}
}
