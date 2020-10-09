package com.example.core;

/**
 * Usage of Math.pow() function
 * 
 * @author ashishkumarlahoti
 */
public class MathPow {

	public static void main(String[] args) {
		double result = (int) Math.pow(2, 3);
		int intResult = (int) Math.pow(2, 3);
		double doubleResult = Math.pow(2.5, 5.2);
		double zeroPowerResult = Math.pow(2, 0);
		double sameResult = Math.pow(2, 1);
		double nanResult = Math.pow(2, Double.NaN);
		
		System.out.println(result);
		System.out.println(intResult);
		System.out.println(doubleResult);
		System.out.println(zeroPowerResult);
		System.out.println(sameResult);
		System.out.println(nanResult);
	}
}
