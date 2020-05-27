package com.example.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberRegex {

	public static void main(String[] args) {
		String Afghanistan = "+93 30 539-0605";
		String Australia = "+61 2 1255-3456";
		String China = "+86 20 1255-3456";
		String Germany = "+49 351 125-3456";
		String India = "+91 9876543210";
		String Indonesia = "+62 21 6539-0605";
		String Iran = "+98 515 539-0605";
		String Italy = "+39 06 5398-0605";
		String NewZealand = "+64 3 539-0605";
		String Philippines = "+63 35 539-0605";
		String Singapore = "+65 6396 0605";
		String Thailand = "+66 2 123 4567";
		String UK = "+44 141 222-3344";
		String USA = "+1 212 555-3456";
		String Vietnam = "+84 35 539-0605";

		// REGEX PATTERN - PHONE NUMBER - NO SPACE
		String TenDigitNumber = "9876543210";

		String regex1 = "^\\d{10}$";
		Pattern pattern1 = Pattern.compile(regex1);
		Matcher matcher1 = pattern1.matcher(TenDigitNumber);
		System.out.println(matcher1.matches());

		// REGEX PATTERN - PHONE NUMBER - TO VALIDATE SPACE OR HYPHEN
		String TenDigitNumberWithSpace = "987 654 3210";
		String TenDigitNumberWithHyphen = "987-654-3210";
		String TenDigitNumberWithSpaceAndHyphen = "987 654-3210";

		String regex2 = "^(\\d{3}[- ]?){2}\\d{4}$";
		Pattern pattern2 = Pattern.compile(regex2);
		System.out.println(pattern2.matcher(TenDigitNumber).matches());
		System.out.println(pattern2.matcher(TenDigitNumberWithSpace).matches());
		System.out.println(pattern2.matcher(TenDigitNumberWithHyphen).matches());
		System.out.println(pattern2.matcher(TenDigitNumberWithSpaceAndHyphen).matches());

		// REGEX PATTERN - PHONE NUMBER - TO VALIDATE SPACE OR HYPHEN OR PARENTHESES
		String TenDigitNumberWithSpaceAndParentheses = "(987) 654 3210";
		String TenDigitNumberWithHyphenAndParentheses = "(987)-654-3210";
		String TenDigitNumberWithSpaceAndHyphenAndParentheses = "(987) 654-3210";

		String regex3 = "^((\\(\\d{3}\\))|\\d{3})[- ]?\\d{3}[- ]?\\d{4}$";
		Pattern pattern3 = Pattern.compile(regex3);
		System.out.println(pattern3.matcher(TenDigitNumber).matches());
		System.out.println(pattern3.matcher(TenDigitNumberWithSpace).matches());
		System.out.println(pattern3.matcher(TenDigitNumberWithHyphen).matches());
		System.out.println(pattern3.matcher(TenDigitNumberWithSpaceAndHyphen).matches());
		System.out.println(pattern3.matcher(TenDigitNumberWithSpaceAndParentheses).matches());
		System.out.println(pattern3.matcher(TenDigitNumberWithHyphenAndParentheses).matches());
		System.out.println(pattern3.matcher(TenDigitNumberWithSpaceAndHyphenAndParentheses).matches());

		// REGEX PATTERN - PHONE NUMBER - TO VALIDATE SPACE OR HYPHEN OR PARENTHESES OR
		// INTERNATIONAL PREFIX
		String regex4 = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
		Pattern pattern4 = Pattern.compile(regex4);
		System.out.println(pattern4.matcher(TenDigitNumber).matches());
		System.out.println(pattern4.matcher(TenDigitNumberWithSpace).matches());
		System.out.println(pattern4.matcher(TenDigitNumberWithHyphen).matches());
		System.out.println(pattern4.matcher(TenDigitNumberWithSpaceAndHyphen).matches());
		System.out.println(pattern4.matcher(TenDigitNumberWithSpaceAndParentheses).matches());
		System.out.println(pattern4.matcher(TenDigitNumberWithHyphenAndParentheses).matches());
		System.out.println(pattern4.matcher(TenDigitNumberWithSpaceAndHyphenAndParentheses).matches());
		System.out.println(pattern4.matcher(Germany).matches());
		System.out.println(pattern4.matcher(India).matches());
		System.out.println(pattern4.matcher(Iran).matches());
		System.out.println(pattern4.matcher(UK).matches());
		System.out.println(pattern4.matcher(USA).matches());

		// REGEX PATTERN - PHONE NUMBER - TO VALIDATE MOST OF COUNTRY FORMATS
		String regex5 = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
		Pattern pattern5 = Pattern.compile(regex5);
		System.out.println(pattern5.matcher(TenDigitNumber).matches());
		System.out.println(pattern5.matcher(TenDigitNumberWithSpace).matches());
		System.out.println(pattern5.matcher(TenDigitNumberWithHyphen).matches());
		System.out.println(pattern5.matcher(TenDigitNumberWithSpaceAndHyphen).matches());
		System.out.println(pattern5.matcher(TenDigitNumberWithSpaceAndParentheses).matches());
		System.out.println(pattern5.matcher(TenDigitNumberWithHyphenAndParentheses).matches());
		System.out.println(pattern5.matcher(TenDigitNumberWithSpaceAndHyphenAndParentheses).matches());

		System.out.println(pattern5.matcher(Afghanistan).matches());
		System.out.println(pattern5.matcher(Australia).matches());
		System.out.println(pattern5.matcher(China).matches());
		System.out.println(pattern5.matcher(Germany).matches());
		System.out.println(pattern5.matcher(India).matches());
		System.out.println(pattern5.matcher(Indonesia).matches());
		System.out.println(pattern5.matcher(Iran).matches());
		System.out.println(pattern5.matcher(Italy).matches());
		System.out.println(pattern5.matcher(NewZealand).matches());
		System.out.println(pattern5.matcher(Philippines).matches());
		System.out.println(pattern5.matcher(Singapore).matches());
		System.out.println(pattern5.matcher(Thailand).matches());
		System.out.println(pattern5.matcher(UK).matches());
		System.out.println(pattern5.matcher(USA).matches());
		System.out.println(pattern5.matcher(Vietnam).matches());

		// REGEX PATTERN - COUNTRY SPECIFIC - SINGAPORE
		Pattern SingaporeRegexPattern = Pattern.compile("^(\\+\\d{2}( )?)?\\d{4}[- .]?\\d{4}$");
		System.out.println(SingaporeRegexPattern.matcher("+65 9876 4321").matches());
		System.out.println(SingaporeRegexPattern.matcher("+65 9876-4321").matches());

	}
}
