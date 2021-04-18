package com.example.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * How many ways you can create an instance of an object 
 * 1. New Keyword 
 * 2. Cloning 
 * 3. Reflection
 * 4. Object Serialization and Deserialization
 * 
 * @author ashishkumarlahoti
 */
public class ObjectInstance {

	public static void main(String[] args)
			throws CloneNotSupportedException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException, ClassNotFoundException {
		// New Keyword
		ObjectInstance instance1 = new ObjectInstance();

		// Cloning
		ObjectInstance instance2 = (ObjectInstance) instance1.clone();

		// Reflection
		ObjectInstance instance3 = ObjectInstance.class.newInstance();
		ObjectInstance instance4 = ObjectInstance.class.getConstructor().newInstance();

		// Object Serialization and Deserialization
		serialize(instance1);
		ObjectInstance instance5 = deserialize();

	}

	private static void serialize(ObjectInstance objInstance) throws IOException {
		FileOutputStream f = new FileOutputStream("resources/objectinstance.ser");
		try (ObjectOutputStream outputStream = new ObjectOutputStream(f)) {
			outputStream.writeObject(objInstance);
			outputStream.flush();
		}
	}

	private static ObjectInstance deserialize() throws IOException, ClassNotFoundException {
		FileInputStream f = new FileInputStream("resources/objectinstance.ser");
		try (ObjectInputStream inputStream = new ObjectInputStream(f)) {
			return (ObjectInstance) inputStream.readObject();
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
