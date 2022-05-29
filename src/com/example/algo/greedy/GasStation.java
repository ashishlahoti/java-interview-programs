package com.example.algo.greedy;

/**
 * There are n gas stations along a circular route, where the amount of gas at the ith station is gas[i].
 * <p>
 * You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from the ith station to its next (i + 1)th station.
 * You begin the journey with an empty tank at one of the gas stations.
 * <p>
 * Given two integer arrays gas and cost, return the starting gas station's index if you can travel around the circuit once in the clockwise direction,
 * otherwise return -1. If there exists a solution, it is guaranteed to be unique
 */
public class GasStation {

    public static void main(String[] args) {
        System.out.println(greedyApproachBruteForce(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}));
        System.out.println(greedyApproachBruteForce(new int[]{4, 1, 1, 7, 3}, new int[]{1, 3, 3, 4, 5}));
        System.out.println(greedyApproachBruteForce(new int[]{2, 3, 4}, new int[]{3, 4, 3}));
        System.out.println("========");
        System.out.println(greedyApproachOneLoop(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}));
        System.out.println(greedyApproachOneLoop(new int[]{4, 1, 1, 7, 3}, new int[]{1, 3, 3, 4, 5}));
        System.out.println(greedyApproachOneLoop(new int[]{2, 3, 4}, new int[]{3, 4, 3}));
    }

    public static int greedyApproachBruteForce(int[] gas, int[] cost) {
        int n = gas.length;
        for (int i = 0; i < n; i++) {
            int tank = 0;
            for (int j = i; j < n + i; j++) {
                tank += gas[j % n] - cost[j % n];
                if (tank < 0) {
                    break;
                }
            }
            if (tank >= 0) {
                return i;
            }
        }
        return -1;
    }

    public static int greedyApproachOneLoop(int[] gas, int[] cost) {
        int tank = 0, total = 0, index = 0;
        for (int i = 0; i < gas.length; i++) {
            tank += gas[i] - cost[i];
            if (tank < 0) {
                tank = 0;
                index = i + 1;
            }
            total += gas[i] - cost[i];
        }
        return total < 0 ? -1 : index;
    }
}
