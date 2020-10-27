package com.example.string;

import java.util.Scanner;

public class PrintPattern {

	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		for (int i = 1; i <= n; i++) {
			for (int j = i; j <=n; j++) {
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
