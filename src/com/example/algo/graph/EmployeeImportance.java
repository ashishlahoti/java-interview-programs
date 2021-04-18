package com.example.algo.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class EmployeeImportance {
	
	public static void main(String[] args) {
		List<Employee> employees1 = Arrays.asList(new Employee(1, 2, 2), new Employee(2, 3));
		System.out.println(getImportance(employees1, 2));
		
		List<Employee> employees2 = Arrays.asList(new Employee(1, 5, 2, 3), new Employee(2, 3), new Employee(3, 3));
		System.out.println(getImportance(employees2, 1));
		
		List<Employee> employees3 = Arrays.asList(new Employee(1, 5, 2, 3), new Employee(2, 3, 4), new Employee(3, 3), new Employee(4, 1));
		System.out.println(getImportance(employees3, 1));
	}

	public static int getImportance(List<Employee> employees, int id) {

		Queue<Employee> queue = new LinkedList<>();
		
		//Create map to search employee by id
		Map<Integer, Employee> map = new HashMap<>();
        for (Employee employee : employees) {
            map.put(employee.id, employee);
        }
        
		Employee employee = map.get(id);
		int importance = 0;
		queue.add(employee);

		while (!queue.isEmpty()) {
			
			Employee emp = queue.remove();
			importance += emp.importance;

			List<Integer> subordinates = emp.subordinates;
			for (Integer subordinateId : subordinates) {
				Employee subordinate = map.get(subordinateId);
				queue.add(subordinate);
			}
		}

		return importance;
	}
}
