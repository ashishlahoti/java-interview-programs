package com.help4j.core;

import com.help4j.dto.Shape;
import com.help4j.dto.Triangle;

public class OverloadingTest {

	public void process(Shape shape){
		shape.draw();
	}
	
	public void process(Triangle triangle){
		triangle.draw();
	}
}
