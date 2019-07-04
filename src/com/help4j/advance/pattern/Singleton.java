package com.help4j.advance.pattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class Singleton implements Serializable, Cloneable{

	public static void main(String[] args) throws CloneNotSupportedException, FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		Singleton obj = Singleton.getInstance();
		System.out.println("Singleton Object");
		System.out.println(obj);
		
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("resources/singleton.ser"));
		os.writeObject(obj);
		ObjectInputStream is = new ObjectInputStream(new FileInputStream("resources/singleton.ser"));
		Singleton deserObj = (Singleton) is.readObject();
		System.out.println("Deserialized Singleton Object");
		System.out.println(deserObj);
		
		Singleton cloneObj = (Singleton) obj.clone();
		System.out.println("Cloned Singleton Object");
		System.out.println(cloneObj);
		
		Class<Singleton> classObj = (Class<Singleton>) Class.forName("com.java.test.pattern.Singleton");
		System.out.println(classObj.newInstance());
		
	}
	private static final long serialVersionUID = 1L;

	private static Singleton instance = null;
	
	private static Object DUMMY_OBJECT = new Object();
	
	private Singleton(){
		/*To prevent object creation using reflection*/
		if(instance!=null){
			throw new InstantiationError( "Singleton Object is already created." );
		}
	}
	
	public static Singleton getInstance(){
		/*Double checked locking*/
		if(instance == null){
			synchronized (DUMMY_OBJECT) {
				if(instance == null){
					instance = new Singleton();
				}
			}
		}
		return instance;
	}
	
	public static void print(){
		System.out.println("I am a singleton class.");
	}
	
	/*To prevent object creation using deserialization*/
	private Object readResolve() throws ObjectStreamException{
		return instance;
	}
	/*To prevent object creation using cloning*/
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return instance;
	}
}
