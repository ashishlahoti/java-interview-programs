package com.techgig;

public class BitonicSequence {
	
	public static int bitonic_sequence(int[] input1) {
		int result = 0;
		if (input1 != null && input1.length > 0) {
			int count = 0;
			for (int i = 0; i < input1.length; i++) {
				count = count(input1[i], input1, i + 1, true, 1);
				if (result < count) {
					result = count;
				}
			}
		}
		return result;
	}
	
	public static int count(int value, int[] values, int index, boolean up, int count) {
		int result = count;
		int temp = 0;
		
		if(up) {
			for(int i = index; i < values.length ; i++) {
				if(value < values[i]) {
					temp = count(values[i], values, i + 1, true, count + 1);
				} else if(value > values[i]) {
					temp = count(values[i], values, i + 1, false, count + 1);
				}
				
				if(result <  temp) {
					result = temp;
				}
			}
		} else {
			for(int i = index; i < values.length ; i++) {
				if(value > values[i]) {
					temp = count(values[i], values, i + 1, false, count + 1);
				}
				
				if(result <  temp) {
					result = temp;
				}
			}
		}
		return result;
	}

}
