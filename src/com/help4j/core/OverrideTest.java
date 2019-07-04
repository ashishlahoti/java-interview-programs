package com.help4j.core;

import com.help4j.dto.Circle;
import com.help4j.dto.Shape;
import com.help4j.dto.Triangle;

public class OverrideTest {
	public static void main(String[] args){
		Shape s1 = new Shape();
		s1.draw();
		Shape s2 = new Triangle();
		s2.draw();
		Shape s3 = new Circle();
		s3.draw();
		/* Class cast exception 
		 Triangle t1 = (Triangle) new Shape(); 
		 t1.draw(); 
		*/
	}
}
