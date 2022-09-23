package com.example.algo.recursive;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MPowerN {
	
	public static int pow(int m, int n){
		return (n>1) ? m*pow(m, n-1) : m;
	}
	
	@Test
	public void test() {
		assertEquals(pow(2,3), 8);
	}
}
