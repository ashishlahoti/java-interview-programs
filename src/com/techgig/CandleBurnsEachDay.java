package com.techgig;
/**
 * PROBLEM : 
 * you are given n candles with size of each candle present as an element in an integer array i.e a[i] represent length of ith candle. 
 * There is a tradition according to which you celebrate by lighting a candle every night. The candles are lit such that there are k 
 * candles lit on the kth day. For ex. 1 candle on day 1 , 2 candles on day 2 and so on. Each night a candle burns 1 
 * 
 * @author aklahoti
 */
public class CandleBurnsEachDay {
	
	public static void main(String[] args){
		System.out.println(CandleBurnsEachDay.getMaximumDays(3, new int[]{2, 2, 2}));
		System.out.println(CandleBurnsEachDay.getMaximumDays(4, new int[]{4, 5, 3, 2}));
		System.out.println(CandleBurnsEachDay.getMaximumDays(5, new int[]{0, 1, 3, 2, 1}));
	}

	public static int getMaximumDays(int n, int[] candles){
	    int day = 0;
		while(day >= 0){
			sortCandlesFromMaxToMinSize(candles);
			for(int i = 0; i < candles.length &&  i <= day; i++){
				if(candles[i] == 0){
					return day;
				}
				candles[i] = candles[i] - 1;
			}
			day++;
		}
		return day;
	}
	
	public static void sortCandlesFromMaxToMinSize(int[] input){
		for(int i = 0; i < input.length; i++){
			for(int j = i; j < input.length; j ++){
				if(input[i] < input[j]){
					int temp = input[j];
					input[j] = input[i];
					input[i] = temp;
				}
			}
		}
	}
}
