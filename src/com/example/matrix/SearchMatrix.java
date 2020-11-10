package com.example.matrix;

public class SearchMatrix {

	public static void main(String[] args) {
		System.out.println(searchMatrix(new int[][] { { 1, 3, 5, 7 }, { 10, 11, 16, 20 }, { 23, 30, 34, 60 } }, 20));
		System.out.println(searchMatrix(new int[0][0], 12));
		System.out.println(searchMatrix(new int[][] { { 1, 3 } }, 3));
		System.out.println(searchMatrix(new int[][] { { 1 }, { 3 } }, 3));
	}

	public static boolean searchMatrix(int[][] matrix, int target) {
		if (matrix == null || matrix.length <= 0) {
			return false;
		}

		int row = matrix.length;
		int col = matrix[0].length;

		for (int i = 0; i < row; i++) {
			if (i + 1 < row && matrix[i + 1][0] <= target) {
				continue;
			}
			return binarySearch(matrix[i], target, 0, col - 1);
		}

		return false;
	}

	public static boolean binarySearch(int[] nums, int target, int m, int n) {
		if (m > n) {
			return false;
		}
		int mid = (m + n) / 2;
		if (nums[mid] == target) {
			return true;
		} else if (target > nums[mid]) {
			return binarySearch(nums, target, mid + 1, n);
		} else {
			return binarySearch(nums, target, m, mid - 1);
		}
	}
}
