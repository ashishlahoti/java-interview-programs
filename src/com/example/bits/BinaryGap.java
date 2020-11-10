package com.example.bits;

public class BinaryGap {

	public static void main(String[] args) {
		System.out.println(binaryGap(9)); //1001
		System.out.println(binaryGap(529)); //1000010001
		System.out.println(binaryGap(20)); //10100
		System.out.println(binaryGap(15)); //1111
		System.out.println(binaryGap(32)); //100000
	}
	
	public static int binaryGap(int n) {
		int lastMaxCount = Integer.MIN_VALUE;
		int count = 0;
		boolean gapCountStart = false;
		for(int i=0; i< 32; i++) {
			if((n & 1<<i) != 0) {
				lastMaxCount = Math.max(lastMaxCount, count);
				count = 0;
				gapCountStart = true;
			}else {
				if(gapCountStart) {
					count++;
				}				
			}
		}		
		return lastMaxCount;
	}
}
