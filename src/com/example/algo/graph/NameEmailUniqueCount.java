package com.example.algo.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NameEmailUniqueCount {

	public static void main(String[] args) {
		System.out.println(uniqueRecords(new String[][] { { "P1", "E1" }, { "P1", "E2" }, { "P2", "E1" }, { "P3", "E3" } }));
		System.out.println(uniqueRecords(new String[][] { { "P1", "E1" }, { "P2", "E2" }, { "P3", "E3" }, { "P4", "E4" } }));
	}

	public static int uniqueRecords(String[][] records) {
		Set<String> visitedNames = new HashSet<>();
		Set<String> visitedMails = new HashSet<>();

		int count = 1;
		visitedNames.add(records[0][0]);
		visitedMails.add(records[0][1]);

		for (int i = 1; i < records.length; i++) {
			String name = records[i][0];
			String email = records[i][1];

			if (!visitedNames.contains(name) && !visitedMails.contains(email)) {
				count++;
			}
			visitedNames.add(name);
			visitedMails.add(email);
		}

		return count;
	}

	public static int uniqueRecordsCount(String[][] records) {
		Map<String, List<String>> graph = new HashMap<>();

		for (int i = 0; i < records.length; i++) {
			String name = records[i][0];
			String email = records[i][1];
			if (graph.containsKey(name)) {
				graph.compute(name, (k, v) -> {
					v.add(email);
					return v;
				});
			} else {
				graph.put(name, new ArrayList<String>(Arrays.asList(email)));
			}
		}

		Map<String, List<String>> visited = new HashMap<>();
		int count = 0;
		for (String name : graph.keySet()) {
			List<String> emails = graph.get(name);
			if (visited.size() == 0) {
				count = 1;
			} else {
				for (String email : emails) {
					if (!visited.values().stream().anyMatch(list -> list.contains(email))) {
						count++;
					}
				}
			}
			visited.put(name, emails);
		}

		return count;
	}
}
