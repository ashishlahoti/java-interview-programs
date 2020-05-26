package com.example.core;

import com.example.dto.Employee;

public class HeapMemory {

	public static void main(String[] args) { // Line 1
        Employee emp = new Employee("ashish"); // Object is created in heap memory and stack memory contains the reference for it 
        emp.setId(1);
        emp.setName("ashish");
        print(emp);
        emp.setName("lahoti");
        print(emp);
    }
 
	private static void print(Employee emp){
		System.out.println(emp.getName());
		emp = null;
	}
}
