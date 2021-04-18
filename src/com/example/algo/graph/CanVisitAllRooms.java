package com.example.algo.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
		Queue<Integer> queue = new LinkedList<>();
		boolean[] visited = new boolean[rooms.size()];
		
		queue.offer(0); // we can enter first room, add key to first room
		while (!queue.isEmpty()) {
			int key = queue.remove(); // get key of first room
			if(!visited[key]) {
				visited[key] = true; // enter the first room
				for (int otherKey : rooms.get(key)) {
					queue.offer(otherKey); // add all keys to stack from first room
				}				
			}
		}

		for (boolean entered : visited) {
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
