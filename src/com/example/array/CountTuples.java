package com.example.array;

import java.util.Arrays;
import java.util.HashSet;

/*
 * Given four arrays: W, X, Y, Z find the number of tuples (w, x, y, z) that
 * make W[w] + X[x] + Y[y] + Z[z] = 0
 * 
 * input W = [0, 2, -100] X = [-2,-1] Y = [-1, 2] Z = [ 1, 2, 1000]
 * 
 * output 2
 * 
 * explanation (1, 0, 0, 0) -> 2 - 2 - 1 + 1 = 0 
 * 			   (0, 1, 0, 1) -> 0 - 1 - 1 + 2 =
 * 0
 * 
 */
public class CountTuples {

	public static void main(String[] args) {
		int[] W = new int[] { 0, 2, -100 };
		int[] X = new int[] { -2, -1 };
		int[] Y = new int[] { -1, 2 };
		int[] Z = new int[] { 1, 2, 1000 };
		System.out.println(countTuples(W, X, Y, Z));
	}

	public static int countTuples(int[] W, int[] X, int[] Y, int[] Z) {
		Arrays.sort(W);
		Arrays.sort(X);
		Arrays.sort(Y);
		Arrays.sort(Z);

		int count = 0;
		HashSet<Integer> set = new HashSet<>();
		for (int k = 0; k < Y.length; k++) {
			for (int l = 0; l < Z.length; l++) {
				int sum = Y[k] + Z[l];
				set.add(0 - sum);
			}
		}
		//System.out.println(set);
		for (int i = 0; i < W.length; i++) {
			for (int j = 0; j < X.length; j++) {
				int sum = W[i] + X[j];
				if (set.contains(sum)) {
					System.out.println(sum + " " + W[i] + " " + X[j]);
					count++;
				}
			}
		}
		return count;
	}
}
