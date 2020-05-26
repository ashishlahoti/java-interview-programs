package com.example.bits;

public class BasicBitsOperation {

	public static void main(String args[]){
		int num = 0;
		num  |= 1 << 1;
		//num = setBit(num, 4);
		//num = setBit(num, 5);
		//num = setBit(num, 1);
		//num = setBit(num, 10);
		//num = setBit(num, 2);
		System.out.println(isBitSet(num, 2));
		System.out.println(isExactlyOneBitSet(num));
		System.out.println(Integer.toBinaryString(num));
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
