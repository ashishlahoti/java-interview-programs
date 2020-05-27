package com.example.bits;

public class BasicBitsOperation {

	public static void main(String args[]){
		int num = 0;                  // 0
		num = setBit(num, 31);        // 10000000000000000000000000000000
		System.out.println(isExactlyOneBitSet(num));
		System.out.println(isBitSet(num, 31));
		num = setBit(num, 0);         // 10000000000000000000000000000001
		num = setBit(num, 1);         // 10000000000000000000000000000011
		num = setBit(num, 3);         // 10000000000000000000000000001011
		num = setBit(num, 5);         // 10000000000000000000000000101011
		System.out.println(Integer.toBinaryString(num));
		System.out.println(isBitSet(num, 2));
		System.out.println(isExactlyOneBitSet(num));
		
	}
	
	public static int toggleBit(int num, int index){
		return isBitSet(num, index) ? setBit(num, index) : unsetBit(num, index);
	}
	public static int setBit(int num, int index){
		return num | 1 << index;
	}
	
	public static int unsetBit(int num, int index){
		return num & ~ ( 1 << index);
	}
	
	public static boolean isBitSet(int num, int index){
		return (num & 1 << index)!=0;
	}
	
	public static boolean isExactlyOneBitSet(int num) {
		return (num & (num - 1)) == 0;
	}
	
}
