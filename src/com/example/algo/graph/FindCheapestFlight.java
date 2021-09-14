package com.example.algo.graph;

import java.util.Arrays;

/**
 * Find cheapest flight from source to destination
 * Given the flights as edges(src, dest, price)
 * n = total vertex
 */
public class FindCheapestFlight {


    public static void main(String[] args) {
        int[][] flights = new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        System.out.println(findCheapestPrice(3, flights, 0, 2));
    }

    public static int findCheapestPrice(int n, int[][] flights, int src, int dst) {
        int[] cost = new int[n];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[src] = 0;

        for (int i = 0; i < n; i++) {
            for (int[] edge : flights) {
                int u = edge[0]; //src
                int v = edge[1]; //dest
                int w = edge[2]; //cost        
                cost[v] = Math.min(cost[v], cost[u] + w);
            }
        }
        return cost[dst] == Integer.MAX_VALUE ? -1 : cost[dst];
    }
}
