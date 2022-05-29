package com.example.algo.greedy;

import java.util.Arrays;

/**
 * There is a car with capacity empty seats. The vehicle only drives east (i.e., it cannot turn around and drive west).
 * <p>
 * You are given the integer capacity and an array trips where trip[i] = [numPassengersi, fromi, toi] indicates that
 * the ith trip has numPassengersi passengers and the locations to pick them up and drop them off are fromi and toi respectively.
 * The locations are given as the number of kilometers due east from the car's initial location.
 * <p>
 * Return true if it is possible to pick up and drop off all passengers for all the given trips, or false otherwise.
 */
public class CarPooling {

    public static void main(String[] args) {
        System.out.println(carPoolingOneLoop(new int[][]{{2, 1, 5}, {3, 3, 7}}, 4));
        System.out.println(carPoolingOneLoop(new int[][]{{2, 1, 5}, {3, 3, 7}}, 5));
    }

    public static boolean carPoolingBruteForce(int[][] trips, int capacity) {
        int[] count = new int[1001];
        // Keep the count of passenger at every km
        for (int[] t : trips) {
            for (int i = t[1]; i <= t[2]; i++) {
                count[i] += t[0];
            }
        }
        // find if at any point, count of passenger exceeded the capacity
        return Arrays.stream(count).max().getAsInt() <= capacity;
    }

    public static boolean carPoolingOneLoop(int[][] trips, int capacity) {
        int[] count = new int[1001];
        for (int[] t : trips) {
            count[t[1]] += t[0]; //2
            count[t[2]] -= t[0];
        }
        for (int c : count) {
            capacity -= c;
            if (capacity < 0) {
                return false;
            }
        }
        return true;
    }
}
