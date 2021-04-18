package com.example.collection;

import java.util.HashMap;
import java.util.Map;

import com.example.dto.Employee;

public class HashMapTest {

	public static void main(String args[]){
		Map<Employee, Integer> map = new HashMap();
		Employee e1 = new Employee("ashish");
		Employee e2 = new Employee("ashish");
		map.put(e1, 1);
		map.put(e2, 2);
		System.out.println("Map Size: " + map.size());
	}
}
