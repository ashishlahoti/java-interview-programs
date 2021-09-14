package com.example.amazon;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchSuggestions {

    public static void main(String[] args) {
        //System.out.println(SearchSuggestions.searchSuggestions(List.of("mobile", "mouse", "moneypot", "monitor", "mousepad"), "mouse"));
        //List<List<String>> result = SearchSuggestions.searchSuggestions(List.of("bags", "baggage", "banner", "box", "cloths"), "bags");
        List<List<String>> result = SearchSuggestions.searchSuggestions(List.of("abbS"), "abc");


        System.out.println(result);
        //result.stream().forEach(list -> System.out.println(list));


    }

    public static List<List<String>> searchSuggestions(List<String> repository, String customerQuery) {
        List<List<String>> results = new ArrayList<>();
        return searchSuggestions(results, repository, customerQuery, 2);

    }

    public static List<List<String>> searchSuggestions(List<List<String>> results, List<String> repository, String customerQuery, int endIndex) {
        if(endIndex > customerQuery.length()){
            return results;
        }
        String s = customerQuery.substring(0, endIndex);
        List<String> result = repository.stream().filter(name -> name.toUpperCase().startsWith(s.toUpperCase())).collect(Collectors.toList());
        results.add(result.stream().sorted().limit(3).collect(Collectors.toList()));
        return searchSuggestions(results, repository, customerQuery, endIndex+1);
    }
}
