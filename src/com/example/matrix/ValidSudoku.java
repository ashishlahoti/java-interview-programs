package com.example.matrix;

import java.util.HashSet;
import java.util.Set;

public class ValidSudoku {

	public static void main(String[] args) {

		char[][] sudoku = { { '5', '3', '.', '.', '7', '.', '.', '.', '.' },
							{ '6', '.', '.', '1', '9', '5', '.', '.', '.' }, 
							{ '.', '9', '8', '.', '.', '.', '.', '6', '.' },
							{ '8', '.', '.', '.', '6', '.', '.', '.', '3' }, 
							{ '4', '.', '.', '8', '.', '3', '.', '.', '1' },
							{ '7', '.', '.', '.', '2', '.', '.', '.', '6' }, 
							{ '.', '6', '.', '.', '.', '.', '2', '8', '.' },
							{ '.', '.', '.', '4', '1', '9', '.', '.', '5' }, 
							{ '.', '.', '.', '.', '8', '.', '.', '7', '9' } };
		System.out.println(isValidSudoku(sudoku));
	}

	public static boolean isValidSudoku(char[][] board) {
		int R = board.length;
		int C = board[0].length;
		
		if(R != 9 && C != 9) {
			return false;
		}
		
		for(int row=0; row< R; row++) {
			Set<Character> rowSet = new HashSet<>();
			Set<Character> colSet = new HashSet<>();
			for(int col=0; col< C; col++) {
				char rowVal = board[row][col];
				if(rowVal != '.') {
					if(!rowSet.add(rowVal)) {
						return false;
					}					
				}
				char colVal = board[col][row];
				if(colVal != '.') {
					if(!colSet.add(colVal)) {
						return false;
					}					
				}
				if(row%3 == 0 && col%3 == 0) {
					Set<Character> boxSet = new HashSet<>();
					for(int i = row; i< row+3; i++) {
						for(int j = col; j< col+3; j++) {
							char boxVal = board[i][j];
							if(boxVal != '.') {
								if(!boxSet.add(boxVal)) {
									return false;
								}					
							}
						}
					}
				}
			}
		}
		
		return true;

	}

}
