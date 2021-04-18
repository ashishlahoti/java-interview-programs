package com.example.matrix;

import java.util.Arrays;

/**
 * [sr,sc] is starting position to color the all associated blocks (4 directions) with newColor
 */
public class ColorFill {

	public static void main(String[] args) {
		System.out.println(Arrays.deepToString(floodFill(new int[][] {{ 1, 1, 1 }, 
																	  { 1, 1, 0 }, 
																	  { 1, 0, 1 }}, 1, 1, 2)));
		System.out.println(Arrays.deepToString(floodFill(new int[][] {{ 0, 0, 0 }, 
			  														  { 0, 1, 1 }}, 1, 1, 1)));

	}

	public static int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
		int oldColor = image[sr][sc];
		if(oldColor == newColor) {
			return image;
		}
		fill(image, sr, sc, oldColor, newColor);
		return image;
	}

	public static void fill(int[][] image, int i, int j, int oldColor, int newColor) {
		if (i < 0 || j < 0 || i >= image.length || j >= image[0].length || image[i][j] != oldColor) {
			return;
		}
		image[i][j] = newColor;
		fill(image, i - 1, j, oldColor, newColor);
		fill(image, i, j - 1, oldColor, newColor);
		fill(image, i + 1, j, oldColor, newColor);
		fill(image, i, j + 1, oldColor, newColor);
	}
}
