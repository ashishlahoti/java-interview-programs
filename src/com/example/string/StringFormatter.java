package com.example.string;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class StringFormatter {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		// 1. STRING FORMAT
		
		// String template
		String[] fruits = new String[] { "Mango", "Grapes", "Banana", "Kiwi" };
		System.out.println(String.format("My favourite fruits are %1$s, %2$s, %3$s and %4$s", fruits));
		// Reverse String template
		System.out.println(String.format("My favourite fruits are %4$s, %3$s, %2$s and %1$s", fruits));
		// Line Break
		System.out.println(String.format("My favourite fruits are:- %n %1$s %n %2$s %n %3$s %n %4$s", fruits));
		

		// 2. NUMBER FORMAT
		
		System.out.println(Math.PI);

		System.out.println(String.format("%.1f", Math.PI));
		System.out.println(String.format("%.5f", Math.PI));
		System.out.println(String.format("%.10f", Math.PI));

		System.out.println(String.format("%10f", Math.PI));
		System.out.println(String.format("%20f", Math.PI));
		System.out.println(String.format("%30f", Math.PI));

		System.out.println(String.format("%+20.5f", Math.PI));
		System.out.println(String.format("%020.5f", Math.PI));
		System.out.println(String.format("%(20.5f", Math.PI));

		// -ve Floating Numbers
		System.out.println(String.format("%+20.5f", -Math.PI));
		System.out.println(String.format("%020.5f", -Math.PI));
		System.out.println(String.format("%(20.5f", -Math.PI));

		// Locale
		System.out.println(String.format(Locale.FRANCE, "%f", Math.PI));
		System.out.println(String.format(Locale.FRANCE, "%f", -Math.PI));

		// Comma Separated
		System.out.println(String.format("%+d", 1234567890));
		System.out.println(String.format("%,d", 1234567890));
		System.out.println(String.format("%,20d", 1234567890));
		System.out.println(String.format(Locale.UK, "%+d", 1234567890));

		// 3. DATE FORMAT
		
		LocalDateTime date = LocalDateTime.parse("1986-08-22T05:45:00");

		System.out.println(String.format("My Birth Date is %1$te-%1$tm-%1$tY %1tT", date));
		System.out.println(String.format("I was born on %1$teth %1tB, %1$tY", date));

		LocalDate date1 = LocalDate.parse("2020-12-25");

		System.out.println(String.format("%1$tY%1$tm%1$te", date1));
		System.out.println(String.format("Address:-%nUnit 505,%n32 Cross Street", null));
		
		// 4. PRINT FORMAT
		
		String print = "Hello World !!!";
		System.out.println(String.format("Printing - %s", print));
		System.out.printf("Printing - %s", print);
		System.out.format("Printing - %s", print);

	}
}
