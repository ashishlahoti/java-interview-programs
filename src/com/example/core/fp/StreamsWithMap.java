package com.example.core.fp;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamsWithMap {

    List<User> users = List.of(new User(1L, "Andrew", 23),
        new User(2L, "Billy", 42),
        new User(3L, "David", 29),
        new User(4L, "Charlie", 30),
        new User(5L, "Andrew", 18),
        new User(6L, "Charlie", 19));

    public static void main(String[] args) {
        StreamsWithMap streamsWithMap = new StreamsWithMap();

        streamsWithMap.createMapFromListOfObject();
        //streamsWithMap.iterateThroughMap();
        //streamsWithMap.sortMap();
    }

    private void createMapFromListOfObject() {
        IntStream.range(0, users.size())
            .boxed()
            .collect(Collectors.toMap(Function.identity(), users::get));

        Map<Long, String> idNameMap = users.stream()
            .collect(Collectors.toMap(User::getId, User::getName));
        System.out.println("idNameMap: " + idNameMap);

        Map<Long, User> idUserMap = users.stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));
        System.out.println("idUserMap: " + idUserMap);

        Map<String, Integer> idValueMap = users.stream()
            .collect(Collectors.toMap(User::getName, User::getAge, (existing, replacement) -> existing, TreeMap::new));
        System.out.println("idValueMap: " + idValueMap);

        Map<String, List<User>> groupByName = users.stream()
            .collect(Collectors.groupingBy(User::getName));
        System.out.println("groupByName: " + groupByName);

        Map<String, Integer> groupCounting = users.stream()
            .collect(Collectors.groupingBy(User::getName, Collectors.summingInt(User::getAge)));
        System.out.println("groupCounting: " + groupCounting);
    }

    private void iterateThroughMap() {
        users.stream()
            .collect(Collectors.toMap(User::getId, User::getName)).keySet().stream().forEach(System.out::println);

        users.stream()
            .collect(Collectors.toMap(User::getId, User::getName)).values().stream().forEach(System.out::println);

        users.stream()
            .collect(Collectors.toMap(User::getId, User::getName)).entrySet().stream().forEach(System.out::println);
    }

    private void sortMap() {

        users.stream()
            .collect(Collectors.toMap(User::getName, User::getAge, (o1, o2) -> o1))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(System.out::println);

        System.out.print("=====");

        Map<String, Integer> sortByKeyReverse = users.stream()
            .collect(Collectors.toMap(User::getName, User::getAge, (o1, o2) -> o1))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
        System.out.println("sorted by key reverse map: " + sortByKeyReverse);

        users.stream()
            .collect(Collectors.toMap(User::getName, User::getAge, (o1, o2) -> o1))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
        System.out.println("=====");

        Map<String, Integer> sortByValueReverse = users.stream()
            .collect(Collectors.toMap(User::getName, User::getAge, (o1, o2) -> o1))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
        System.out.println("sorted by value reverse map: " + sortByValueReverse);

        Comparator<Map.Entry<Long, String>> valueComparator = Map.Entry.comparingByValue();
        Comparator<Map.Entry<Long, String>> keyComparator = Map.Entry.comparingByKey(Comparator.reverseOrder());

        Map<Long, String> sortByKeyThenValue = users.stream()
            .collect(Collectors.toMap(User::getId, User::getName))
            .entrySet()
            .stream()
            .sorted(valueComparator.thenComparing(keyComparator))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
        System.out.println("sorted by value then key map: " + sortByKeyThenValue);

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
