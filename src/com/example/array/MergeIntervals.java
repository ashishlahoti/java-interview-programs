package com.example.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
	
	public static void main(String[] args) {
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,3},{2,6},{8,10},{15,18}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,4},{4,5}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,2},{3,4},{5,6}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,3},{2,5},{4,6}})));
	}

	public static int[][] merge(int[][] intervals) {

		List<int[]> results = new ArrayList<>();
		int start = intervals[0][0];
		int end = intervals[0][1];
		for (int i = 0; i < intervals.length; i++) {
			int startNext = intervals[i][0];
			int endNext = intervals[i][1];
			if(startNext <= end) {
				end = endNext;
			}else {
				results.add(new int[] { start, end });
				start = startNext;
				end = endNext;
			}
		}
		
		results.add(new int[] { start, end });
		return results.toArray(new int[results.size()][]);
	}
}
