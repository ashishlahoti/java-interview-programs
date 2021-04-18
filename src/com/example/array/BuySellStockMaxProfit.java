package com.example.array;

/**
 * Say you have an array for which the i(th) element is the price of a given stock
 * on day i.
 * 
 * If you were only permitted to complete at most one transaction (i.e., buy one
 * and sell one share of the stock), design an algorithm to find the maximum
 * profit.
 * 
 * Note that you cannot sell a stock before you buy one.
 */
public class BuySellStockMaxProfit {

	public static void main(String[] args) {
		System.out.println(maxProfit(new int[] { 7, 1, 5, 3, 6, 4 }));
		System.out.println(maxProfit(new int[] { 7, 6, 4, 3, 1 }));
		
		System.out.println(onePass(new int[] { 7, 1, 5, 3, 6, 4 }));
		System.out.println(onePass(new int[] { 7, 6, 4, 3, 1 }));
	}

	// Brute Force Time:O(N2) Space:O(1)
	public static int maxProfit(int[] prices) {
		int maxProfit = Integer.MIN_VALUE;
		for (int i = 0; i < prices.length; i++) {
			for (int j = i; j < prices.length; j++) {
				maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
			}
		}
		return maxProfit;
	}
	
	// Single Pass Time:O(N) Space: O(1)
	public static int onePass(int[] prices) {
		int minPrice = Integer.MAX_VALUE;
		int maxProfit = 0;		
		for (int i = 0; i < prices.length; i++) {
			minPrice = Math.min(prices[i], minPrice);
			maxProfit = Math.max(prices[i] - minPrice, maxProfit);
			
		}
		return maxProfit;
	}
}
