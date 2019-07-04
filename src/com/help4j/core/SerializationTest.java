package com.help4j.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.help4j.dto.Employee;

public class SerializationTest{

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		Employee e = new Employee("ashish");
		e.setId(1L);
		e.setName("ashish");
		SerializationTest.serialize(e);
		SerializationTest.deserialize();
	}
	
	private static void serialize(Employee e) throws IOException{
		FileOutputStream f = new FileOutputStream("resources/employee3.ser");
		try(ObjectOutputStream o = new ObjectOutputStream(f)){
			o.writeObject(e);
			o.flush();
		}
	}
	
	private static void deserialize() throws IOException, ClassNotFoundException{
		FileInputStream f = new FileInputStream("resources/employee3.ser");
		try(ObjectInputStream i = new ObjectInputStream(f)){
			Employee emp = (Employee) i.readObject();
			System.out.println(emp.getName());
			System.out.println(emp.getId());
			//System.out.println(emp.getDepartment());
		}
	}
}
