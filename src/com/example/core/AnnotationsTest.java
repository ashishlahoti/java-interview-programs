package com.example.core;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationsTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		ArrayList list = new ArrayList();
		list.add(1);
		// printClassInfo(new MyClass());
		// printClassInfo(new ChildClass());

		// System.out.println(getAuthors(new MyClass()));
		validateProperties(new MyClass());
	}

	public static void validateProperties(Object object) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field: fields) {
			field.setAccessible(true);
			if(field.getAnnotatedType().isAnnotationPresent(NotNull.class)) {
				if(field.get(object) == null) {
					throw new NullPointerException("@NotNull field \"" + field.getName() + "\" can not be null");
				}
			}
		}
	}

	public static List<String> getAuthors(Object object) {
		Class<?> clazz = object.getClass();
		if (clazz.isAnnotationPresent(ClassInfo.class)) {
			Author[] authors = clazz.getAnnotationsByType(Author.class);
			return Arrays.stream(authors).map(author -> author.name()).collect(Collectors.toList());
		} else {
			System.out.println("Class Info is not available");
		}
		return null;
	}

	public static void printClassInfo(Object object) {
		Class<?> clazz = object.getClass();
		if (clazz.isAnnotationPresent(ClassInfo.class)) {
			ClassInfo classInfo = clazz.getAnnotation(ClassInfo.class);
			System.out.println("author: " + classInfo.author());
			System.out.println("date: " + classInfo.date());
			System.out.println("currentRevision: " + classInfo.currentRevision());
			System.out.println("lastModified: " + classInfo.lastModified());
			System.out.println("lastModifiedBy: " + classInfo.lastModifiedBy());
			System.out.println("reviewers: " + Arrays.toString(classInfo.reviewers()));
		} else {
			System.out.println("Class Info is not available");
		}
	}
}

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface ClassInfo {
	String author();

	String date();

	int currentRevision()

	default 1;

	String lastModified()

	default "N/A";

	String lastModifiedBy()

	default "N/A";

	// Note use of array
	String[] reviewers();
}

@ClassInfo(author = "John Doe", date = "3/17/2002",
		// Note array notation
		reviewers = { "Alice", "Bob", "Cindy" })
@Author(name = "John")
@Author(name = "Alice")
@Author(name = "Bob")
class MyClass {
	private @NotNull String key;
	private String value;
}

class ChildClass extends MyClass {

}

@Repeatable(Authors.class)
@interface Author {
	String name();
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Authors {
	Author[] value();
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@interface NotNull {

}
