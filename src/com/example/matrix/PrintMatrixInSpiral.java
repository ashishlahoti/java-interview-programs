package com.example.matrix;

public class PrintMatrixInSpiral {

	public static void main(String[] args) {
		print(new int[][] { { 1, 2, 3, 4 }, 
							{ 5, 6, 7, 8 }, 
							{ 9, 10, 11, 12 },
							{ 13, 14, 15, 16}});
		
		print(new int[][] { { 1, 2, 3, 4, 5 }, 
			{ 6, 7, 8, 9, 10 }, 
			{ 11, 12, 13, 14, 15 },
			{ 16, 17, 18, 19, 20}});
	}

	public static void print(int[][] matrix) {
		if (matrix == null || matrix.length <= 0) {
			return;
		}

		int row = matrix.length;
		int col = matrix[0].length;

		for (int i = 0; i < row/2; i++) {			
			for(int j=0; j < col; j++) {
				if((i+j < col) && i<=j) {
					System.out.print(matrix[i][j] + " ");
				}			
			}
			
			for(int k = i+1; k < row-1-i; k++) {
				System.out.print(matrix[k][col-1-i] + " ");
			}
			
			for(int j=0; j < col; j++) {
				if((i+(col-1-j) < col) && i<= (col-1-j)) { 
					System.out.print(matrix[row-1-i][col-1-j] + " ");	
				}				
			}
			
			for(int k = i+1; k < row-1-i; k++) {
				System.out.print(matrix[row-1-i-k][i] + " ");
			}
		}
		System.out.println();
	}
}
