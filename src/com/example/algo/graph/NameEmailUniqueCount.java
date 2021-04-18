package com.example.algo.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * You have a records of Users (name, email)
 * Two users with same name but different email is same user
 * Two users with same email but different name is same user
 * 
 * Find unique users from the given list
 */
public class NameEmailUniqueCount {

	public static void main(String[] args) {
		System.out.println(
				uniqueRecords(new String[][] { { "P1", "E1" }, { "P1", "E2" }, { "P2", "E1" }, { "P3", "E3" } }));
		System.out.println(
				uniqueRecords(new String[][] { { "P1", "E1" }, { "P2", "E2" }, { "P3", "E3" }, { "P4", "E4" } }));
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
}
