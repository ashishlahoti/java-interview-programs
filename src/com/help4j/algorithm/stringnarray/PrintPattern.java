package com.help4j.algorithm.stringnarray;

import java.util.Scanner;

public class PrintPattern {

	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <=n; j++) {
				System.out.print(j);
				if(j<n){
					System.out.print(" ");
				}
			}
			if(i<n){
				System.out.println("");
			}
		}
	}
}
