package com.example.algo.dynamicprogramming;

import java.util.Arrays;

public class Knapsack {

	public static void main(String[] args) {
		System.out.println(knapsack(new int[] { 150, 300, 200 }, new int[] { 1, 4, 3 }, 4));
	}

	public static int knapsack(int[] value, int[] weight, int capacity) {

		int[][] backtrack = new int[value.length + 1][capacity + 1];

		/**
		 *            1     2      3      4 <= weight
		 * Value ===========================
		 *  ^     0    0     0      0      0
		 * 150  | 0   150   150    150    150
		 * 300  | 0   150   150    150    300
		 * 200  | 0   150   150    200    350
		 */
		for (int i = 1; i <= weight.length; i++) {
			for (int c = 1; c <= capacity; c++) {
				int val = value[i - 1];
				int w = weight[i - 1];
				// Math.max(when_not_choose_current_weight, when_choose_current_weight);
				backtrack[i][c] = Math.max(backtrack[i - 1][c], c<w ? 0 : val + backtrack[i - 1][c - w]);
			}
		}
		System.out.println(Arrays.deepToString(backtrack));
		return backtrack[value.length][capacity];
	}
}
