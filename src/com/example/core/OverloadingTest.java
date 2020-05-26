package com.example.core;

import com.example.dto.Shape;
import com.example.dto.Triangle;

public class OverloadingTest {

	public void process(Shape shape){
		shape.draw();
	}
	
	public void process(Triangle triangle){
		triangle.draw();
	}
}
