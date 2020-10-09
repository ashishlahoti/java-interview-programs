package com.example.matrix;

public class RotateMatrix {

	public static void main(String[] args){
		int[][] matrix = new int[5][5];
		fillMatrix(matrix);
		print(matrix);
		rotateBy90(matrix);
		print(matrix);
		System.out.println(Math.floor(matrix.length/2f));
		System.out.println(Math.ceil(matrix.length/2f));
	}
	
	public static void rotateBy90(int[][] matrix){
		for(int i=0; i < Math.floor(matrix.length/2f); i++){
			for(int j = 0; j < Math.ceil(matrix.length/2f); j++){
				int temp = matrix[i][j]; //00 //01
				int temp1 = matrix[j][matrix.length-1-i]; //02 //12
				int temp2 = matrix[matrix.length-1-i][matrix.length-1-j]; //22 //21
				int temp3 = matrix[matrix.length-1-j][i]; //20 //10
				
				matrix[i][j] = temp3;
				matrix[j][matrix.length-1-i] = temp;
				matrix[matrix.length-i-1][matrix.length-j-1] = temp1;
				matrix[matrix.length-1-j][i] = temp2;				
			}
		}
	}
	
	public static void fillMatrix(int[][] matrix){
		int count = 1;
		for(int i=0; i<matrix.length; i++){
			for(int j =0; j < matrix[i].length; j++){
				matrix[i][j] = count++;
			}
		}
	}
	
	public static void print(int[][] matrix){
		for(int[] row: matrix){
			for(int element: row){
				System.out.print(element + "\t");
			}
			System.out.println();
		}
		System.out.println("====================");
	}
}
