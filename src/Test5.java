import java.util.*;
import java.util.stream.*;

public class Test5 {
	public static void main(String[] args) {
		String s = "1-2-3-4-5";
		List<Integer> list = Arrays.stream(s.split("-")).map(Integer::parseInt).collect(Collectors.toList());
		System.out.println(list);
		
		
		String s1 = "[1-2-3-4-5], 2";
		String[] twopart = s1.split(", ");
		System.out.println(twopart[0].substring(1, twopart[0].length()-1));
		System.out.println(twopart[1]);
		List<Integer> list1 = Arrays.stream(s.split("-")).map(Integer::parseInt).collect(Collectors.toList());
		System.out.println(list);
			
	}
}
