package com.example.core.fp;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamsWithMap {

    public static void main(String[] args) {
        StreamsWithMap streamsWithMap = new StreamsWithMap();
        List<String> fruits = List.of("Apple", "Orange", "Banana", "Apple", "Orange", "Kiwi", "Kiwi", "Grapes");

        System.out.println("countFruitsFromListOfString: " + streamsWithMap.countFruitsFromListOfString());
        System.out.println("createMapFromListOfFruitObject: " + streamsWithMap.createMapFromListOfFruitObject());
        streamsWithMap.iterateThroughMap();
        final Comparator<Map.Entry<String, Long>> valueComparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
        final Comparator<Map.Entry<String, Long>> keyComparator = Map.Entry.comparingByKey();

        List<String> result = fruits.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .sorted(valueComparator.thenComparing(keyComparator))
            .map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.toList());

        System.out.println(result);
    }

    private Map<String, Long> countFruitsFromListOfString() {
        List<String> fruits = List.of("Apple", "Orange", "Banana", "Apple", "Orange", "Kiwi", "Kiwi", "Grapes");
        Map<String, Long> count = fruits.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return count;
    }

    private Map<Long, String> createMapFromListOfFruitObject() {
        List<User> users = List.of(new User(1L, "John", 23),
            new User(2L, "Andrew", 42),
            new User(3L, "Peter", 29),
            new User(4L, "Christina", 30),
            new User(5L, "Ashley", 18));
        Map<Long, String> userMapByIdAndName = users.stream()
            .collect(Collectors.toMap(User::getId, User::getName));

        Map<Long, User> userMapByIdAndObj = users.stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));
        System.out.println("userMapByIdAndObj: " + userMapByIdAndObj);
        return userMapByIdAndName;
    }

    private void iterateThroughMap() {
        Map<String, Integer> fruitCountMap = Map.of("Orange", 2, "Kiwi", 3, "Grapes", 1, "Apple", 2, "Banana", 1);
        fruitCountMap.keySet().stream().forEach(System.out::println);
        fruitCountMap.values().stream().forEach(System.out::println);
        fruitCountMap.entrySet().stream().forEach(System.out::println);
        System.out.print("=====");
        fruitCountMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(System.out::println);
        System.out.print("=====");
        fruitCountMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
        System.out.print("=====");
        fruitCountMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(System.out::println);
        System.out.print("=====");
        fruitCountMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(System.out::println);
        System.out.print("=====");
        final Comparator<Map.Entry<String, Integer>> valueComparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
        final Comparator<Map.Entry<String, Integer>> keyComparator = Map.Entry.comparingByKey();
        List<String> sortResult = fruitCountMap.entrySet().stream()
            .sorted(valueComparator.thenComparing(keyComparator))
            .map(e -> e.getKey() + " " + e.getValue())
            .collect(Collectors.toList());
        System.out.print(sortResult);
        System.out.println("=====");
    }

    class User {
        Long id;
        String name;
        Integer age;

        User(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
        }
    }
}
