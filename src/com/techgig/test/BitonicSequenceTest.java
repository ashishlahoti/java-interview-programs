package com.techgig.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.techgig.BitonicSequence;

public class BitonicSequenceTest {

    @Test
	public void test1() {
		assertEquals(6, BitonicSequence.bitonic_sequence(new int[]{1, 11, 2, 10, 4, 5, 2, 1}));
	}
	
	/*@Test
	public void test2() {
		assertEquals(5, BitonicSequence.bitonic_sequence(new int[]{12, 11, 40, 5, 3, 1}));
	}
	
	@Test
	public void test3() {
		assertEquals(5, BitonicSequence.bitonic_sequence(new int[]{80, 60, 30, 40, 20, 10}));
	}
	
	@Test
	public void test4() {
		assertEquals(4, BitonicSequence.bitonic_sequence(new int[]{1, 2, 3, 1, 2, 3}));
	}
	
	@Test
	public void test5() {
		assertEquals(4, BitonicSequence.bitonic_sequence(new int[]{1, 2, 2, 3, 3, 1}));
	}
	
	@Test
	public void test6() {
		assertEquals(9, BitonicSequence.bitonic_sequence(new int[]{1, 2, 5, 1, 2, 4, 3, 5, 2, 6, 1, -1, 5, 4, 2, 1 }));
	}
	
	@Test
	public void test7() {
		assertEquals(6, BitonicSequence.bitonic_sequence(new int[]{1, 2, 3, 4, 5, 6 }));
	}
	
	@Test
	public void test8() {
		assertEquals(0, BitonicSequence.bitonic_sequence(new int[]{}));
	}
	
	@Test
	public void test9() {
		assertEquals(1, BitonicSequence.bitonic_sequence(new int[]{1}));
	}
	
	@Test
	public void test10() {
		assertEquals(2, BitonicSequence.bitonic_sequence(new int[]{1, 2}));
	}
	
	@Test
	public void test11() {
		assertEquals(3, BitonicSequence.bitonic_sequence(new int[]{1, 2, 3}));
	}
	
	@Test
	public void test12() {
		assertEquals(0, BitonicSequence.bitonic_sequence(null));
	}*/

}
