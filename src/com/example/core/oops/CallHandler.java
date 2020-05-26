package com.example.core.oops;

import java.util.ArrayList;

public class CallHandler {

	public static final int LEVELS = 3; 
	ArrayList<Employee>[] empList = new ArrayList[LEVELS];
}

class Employee{
	private int rank;
	
	public Employee(int rank){
		this.rank = rank;
	}
}

class Fresher extends Employee{
	public Fresher(){
		super(0);
	}
}

class TechnicalLead extends Employee{
	public TechnicalLead(){
		super(1);
	}
}

class ProgramManager extends Employee{
	public ProgramManager(){
		super(2);
	}
}