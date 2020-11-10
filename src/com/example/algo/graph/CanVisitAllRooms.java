package com.example.algo.graph;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Each index of an array is room number, each nested array values are keys of other rooms inside that room.
 * You go to a room, pick keys of other rooms, go inside those rooms, pick keys of other rooms from there, and so and so forth.
 * Find out if you can visit all the rooms given that you have key of 0th room initially to enter.
 */
public class CanVisitAllRooms {

	public static void main(String[] args) {
		System.out.println(canVisitAllRooms(getListFromArray(new Integer[][] {{1}, {2}, {3}, {}})));
		
		System.out.println(canVisitAllRooms(getListFromArray(new Integer[][] {{1, 3}, {3, 0, 1}, {2}, {0}})));
	}

	public static boolean canVisitAllRooms(List<List<Integer>> rooms) {
		System.out.println(rooms);
		Stack<Integer> stack = new Stack<>();
		boolean[] seen = new boolean[rooms.size()];
		
		stack.push(0); // we can enter first room, add key to first room
		while (!stack.isEmpty()) {
			int key = stack.pop(); // get key of first room
			if(!seen[key]) {
				seen[key] = true; // enter the first room
				for (int otherKey : rooms.get(key)) {
					stack.push(otherKey); // add all keys to stack from first room
				}				
			}
		}

		for (boolean entered : seen) {
			if (!entered) {
				return false;
			}
		}
		return true;
	}
	
	private static List<List<Integer>> getListFromArray(Integer[][] array) {
		return Arrays.stream(array)
        .map(Arrays::asList)
        .collect(Collectors.toList());
	}
}
