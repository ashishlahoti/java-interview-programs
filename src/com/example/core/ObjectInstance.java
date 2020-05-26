package com.example.core;

import java.lang.reflect.InvocationTargetException;

public class ObjectInstance {

	public static void main(String[] args) throws CloneNotSupportedException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ObjectInstance instance1 = new ObjectInstance();
		
		ObjectInstance instance2 = (ObjectInstance) instance1.clone();
		
		ObjectInstance instance3 = ObjectInstance.class.newInstance();
		
		ObjectInstance instance4 = ObjectInstance.class.getConstructor().newInstance();
		
		//ObjectInstance instance4 = ObjectInstance.class.getConstructor().newInstance();
	}
}
