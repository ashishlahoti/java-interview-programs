package com.example.algo.dynamicprogramming;

import java.util.Arrays;

/**
 * How many ways you can make a total amount for given coin denominations
 * Suppose coin denominations are [1, 2, 5] and total amount is 7. Following are 6 ways:-
 * 1 1 1 1 1 1 1
 * 1 1 1 1 1 2
 * 1 1 1 2 2
 * 1 2 2 2
 * 1 1 5
 * 2 5
 * 
 * solution[x] = solution[x] + solution[x - den]
 *    0 1 2 3 4 5 6 7
 *   |---------------
 * 1 |1 1 1 1 1 1 1 1
 * 2 |1 1 2 2 3 3 4 4
 * 5 |1 1 2 2 3 4 5 6
 */
public class CoinChanging {

	public static void main(String[] args) {

		int[] denominations = new int[] { 1, 2, 5 };
		int amount = 7;
		int result = solveCoinChange(denominations, amount);
		System.out.print("SolveCoinChange(" + Arrays.toString(denominations) + ", " + amount + ") = ");
		System.out.print(result);
	}

	static int solveCoinChange(int[] denominations, int amount) {
		int[] solution = new int[amount + 1];
		solution[0] = 1;

		for (int den : denominations) {
			for (int i = den; i < (amount + 1); ++i) {
				solution[i] += solution[i - den];
			}
		}
		System.out.println(Arrays.toString(solution));
		return solution[solution.length - 1];
	}

}
