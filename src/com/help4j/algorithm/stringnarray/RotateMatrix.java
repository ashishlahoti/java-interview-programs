package com.help4j.algorithm.stringnarray;

import java.util.Random;

public class RotateMatrix {

	public static void main(String[] args){
		int[][] matrix = new int[4][4];
		fillMatrix(matrix);
		print(matrix);
		rotateBy90(matrix);
		print(matrix);
	}
	
	public static void rotateBy90(int[][] matrix){
		for(int i=0; i < 1; i++){
			for(int j = 0; j < 2; j++){
				int temp = matrix[j][matrix.length-1];
				matrix[j][matrix.length-1] = matrix[i][j];
				
				int temp1 = matrix[matrix.length-1][matrix.length-1-j];
				matrix[matrix.length-1][matrix.length-1-j] = temp;
				
				int temp2 = matrix[matrix.length-1][j];
				matrix[matrix.length-1][j] = temp1;
				
				matrix[i][j] = temp2;
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
