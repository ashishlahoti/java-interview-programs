package com.example.dto;

import java.io.Serializable;

public class Employee implements Serializable{

	public static void main(String[] args) throws InstantiationException, IllegalAccessException{
		Employee emp = new Employee();
		try {
			emp.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Class empCls = Class.forName("com.quark.test.dto.Employee");
			//empCls.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static final long serialVersionUID = 42L;

	public Employee(String name){
		this.name = name;
	}
	
	private Employee(){
		super();
	}
	
	private String name;
	
	private long id;
	
	private String department;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Employee){
			return ((Employee) obj).getName().equals(this.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getName().length();
	}
}
