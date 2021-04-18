package com.example.algo.graph;

import java.util.Arrays;
import java.util.List;

public class Employee {

	public int id;
    public int importance;
    public List<Integer> subordinates;

    Employee(int id, int importance, Integer ...subordinates){
    	this.id = id;
    	this.importance = importance;
    	this.subordinates = Arrays.asList(subordinates);
    }
    
    @Override
    public String toString() {
    	
    	return "(" + this.id + ", " + this.importance + ", " + this.subordinates.toString() + ")";
    }
}
