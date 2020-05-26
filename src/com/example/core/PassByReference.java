package com.example.core;

import com.example.dto.Employee;

public class PassByReference {
	
	public static void main(String args[]){
		//Java copies and passes the reference by value, not the object. Thus, method manipulation will alter the objects, since the references point to the original objects
		Integer j = new Integer(15);
		String s = "ashish";
		int[] array = new int[]{1, 2};
		PassByReference r = new PassByReference();
		Employee emp = new Employee("ashish");
		r.callInt(j);
		r.callString(s);
		r.callArray(array);
		r.callObj(emp);
		System.out.println(j);
		System.out.println(s);
		System.out.println(array[0]);
		System.out.println(emp.getName());
	}
	
	private void callInt(Integer i){
		i++;
	}
	private void callString(String i){
		i = "lahoti";
	}
	private void callArray(int[] i){
		i[0] = 5;
	}
	private void callObj(Employee emp){
		emp.setName("lahoti");
	}
}
