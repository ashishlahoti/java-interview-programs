package com.techgig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitonicSequence1 {
	
	public static int bitonic_sequence(int input1[]) {
		
		if(input1 == null || input1.length == 0){
			return 0;
		}
		int size = input1.length;
		if(size < 3){
			return size;
		}
		int[] input = removeSimilarSequence(input1);
		size = input.length;
		int[] reverseInput = new int[input.length];
		for(int j=0; j< size; j++){
			reverseInput[j] = input[size-j-1];
		}
		
		Map<Integer, Entry> increasingSeqMap = new HashMap<Integer, Entry>();
		Map<Integer, Entry> decreasingSeqMap = new HashMap<Integer, Entry>();
		
		for(int i=0; i< input.length; i++){
			getLongestSequenceAtMarker(input, i, increasingSeqMap, 0);
			getLongestSequenceAtMarker(reverseInput, i, decreasingSeqMap, 0);
		}
		System.out.println(increasingSeqMap);
		Map<Integer, Entry> decreasingSeqMap1 = new HashMap<Integer, Entry>();
		for (Map.Entry<Integer, Entry> entry : decreasingSeqMap.entrySet()){ 
			decreasingSeqMap1.put(size - 1 - entry.getKey(), entry.getValue());
		}
		System.out.println(decreasingSeqMap1);
		int longestSequence = 0;		
		for(int k=0; k < size ; k++){
			int sequence = 0;
			Entry inEntry = increasingSeqMap.get(k);
			Entry deEntry = decreasingSeqMap1.get(k);
			if(inEntry != null && deEntry!=null){
				sequence = inEntry.getSequenceCount() + deEntry.getSequenceCount();
				if(inEntry.isBoundryCondition() && deEntry.isBoundryCondition()){
					sequence = sequence - 1;
				}
			}else if(inEntry !=null){
				sequence = inEntry.getSequenceCount();
			}else if(deEntry !=null){
				sequence = deEntry.getSequenceCount();
			}
			longestSequence = (sequence > longestSequence) ? sequence : longestSequence;
		}
		return longestSequence;
	}
	
	private static void getLongestSequenceAtMarker(int[] input, int markerIndex, Map<Integer, Entry> map, int startIndex){
		int sequenceCount = 1;
		int currentMarker = startIndex;
		for(int i = startIndex; i < markerIndex; i++){
			int nextMarker = i+1;
			if(input[currentMarker] < input[nextMarker]){
				sequenceCount = sequenceCount + 1;
				currentMarker = nextMarker;
			}
		}
		boolean boundryCondition = false;
		if(markerIndex == currentMarker){
			boundryCondition = true;
		}
		if(map.get(markerIndex) != null){
			if(map.get(startIndex) != null && input[markerIndex] > input[currentMarker]){
				sequenceCount = sequenceCount + map.get(startIndex).getSequenceCount() - 1;
			}
			if(map.get(markerIndex).getSequenceCount() < sequenceCount){
				map.put(markerIndex, new Entry(sequenceCount, boundryCondition));
			}
		}else{
			if(map.get(startIndex) != null){
				sequenceCount = sequenceCount + map.get(startIndex).getSequenceCount() - 1;
			}
			map.put(markerIndex,  new Entry(sequenceCount, boundryCondition));
		}
		
		if(startIndex + 1 < markerIndex){
			getLongestSequenceAtMarker(input, markerIndex, map, startIndex + 1);
		}
	}
	
	private static int[] removeSimilarSequence(int[] input1){
		List<Integer> list = new ArrayList<Integer>();
		list.add(input1[0]);
		for(int i = 1; i< input1.length; i ++){
			if(input1[i] != input1[i-1]){
				list.add(input1[i]);
			}
		}
		int[] input = new int[list.size()];
		for(int j = 0; j < list.size(); j++){
			input[j] = list.get(j);
		}
		return input;
	}
	private static class Entry{
		private int sequenceCount;
		private boolean boundryCondition;
		
		public Entry(int sequenceCount, boolean boundryCondition) {
			this.sequenceCount = sequenceCount;
			this.boundryCondition = boundryCondition;
		}
		public int getSequenceCount() {
			return sequenceCount;
		}
		public boolean isBoundryCondition() {
			return boundryCondition;
		}
		
		@Override
		public String toString() {
			return this.sequenceCount + ":" + this.boundryCondition;
		}
	}
}
