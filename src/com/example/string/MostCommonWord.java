package com.example.string;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Given a paragraph and a list of banned words, return the most frequent word
 * that is not in the list of banned words. It is guaranteed there is at least
 * one word that isn't banned, and that the answer is unique.
 * 
 * Words in the list of banned words are given in lowercase, and free of
 * punctuation. Words in the paragraph are not case sensitive. The answer is in
 * lowercase.
 * 
 * @author ashishkumarlahoti
 *
 */
public class MostCommonWord {

	public static void main(String[] args) {

		String paragraph = "Bob hit a ball, the hit BALL flew far after it was hit.";
		String banned[] = new String[] { "hit" };
		System.out.println(mostCommonWord(paragraph, banned));

		String paragraph1 = "a, a, a, a, b,b,b,c, c";
		String banned1[] = new String[] { "a" };
		System.out.println(mostCommonWord(paragraph1, banned1));

	}

	public static String mostCommonWord(String paragraph, String[] banned) {

		// 1). replace the punctuations with spaces, and put all letters in lower case
		String normalizeStr = paragraph.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase();
		System.out.println(normalizeStr);

		// 2). split the string into words (split by one or more whitespace)
		String[] words = normalizeStr.split("\\s+");

		// 3). convert the banned array to Set for easy lookup
		Set<String> bannedWords = new HashSet<>();
		for (String banWord : banned) {
			bannedWords.add(banWord);
		}

		// 4. count the occurrence of each word, excluding the banned words
		Map<String, Integer> wordCount = new HashMap<>();
		for (String word : words) {
			if (!bannedWords.contains(word)) {
				wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
			}
		}

		// 5). return the word with the highest count
		return Collections.max(wordCount.entrySet(), Map.Entry.comparingByValue()).getKey();
	}
}
