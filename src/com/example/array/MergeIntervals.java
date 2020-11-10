package com.example.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
 * 
 * Input: intervals = [[1,4],[0,4]]
 * Output: [[0,4]]
 * Explanation: Intervals [1,4] and [0,4] are considered overlapping.
 * 
 */
public class MergeIntervals {
	
	public static void main(String[] args) {
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,3},{2,6},{8,10},{15,18}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,4},{4,5}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,2},{3,4},{5,6}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,3},{2,5},{4,6}})));
		System.out.println(Arrays.deepToString(merge(new int[0][0])));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,4},{0,4}})));
		System.out.println(Arrays.deepToString(merge(new int[][] {{1,4},{2,3}})));
	}

	public static int[][] merge(int[][] intervals) {
		if(intervals == null || intervals.length <= 1) {
			return intervals;
		}
		Arrays.sort(intervals, (i1, i2) -> Integer.compare(i1[0], i2[0]));
		List<int[]> results = new ArrayList<>();
		int start = intervals[0][0];
		int end = intervals[0][1];
		for (int i = 0; i < intervals.length; i++) {
			int startNext = intervals[i][0];
			int endNext = intervals[i][1];
			if(startNext <= end) {
				end = Math.max(end, endNext);
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
